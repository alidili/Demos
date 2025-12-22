#include <jni.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <unistd.h>
#include <cstring>
#include <semaphore.h>
#include <android/log.h>
#include <linux/ashmem.h>
#include <sys/ioctl.h>
#include <cerrno>
#include "shared_block.h"

#define LOG_TAG "native_shm"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

/**
 * 当前进程mmap后得到的共享内存指针
 */
static SharedBlock *g_block = nullptr;

/**
 * 创建共享内存
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_yangle_ashmem_NativeShm_createShm(JNIEnv *, jobject) {
    // 创建共享内存区域
    int fd = open("/dev/ashmem", O_RDWR);
    if (fd < 0) {
        LOGE("ashmem_create_region failed");
        return -1;
    }
    if (ioctl(fd, ASHMEM_SET_NAME, "shared_memory") != 0) {
        LOGE("ASHMEM_SET_NAME failed: %s", strerror(errno));
        close(fd);
        return -1;
    }
    if (ioctl(fd, ASHMEM_SET_SIZE, sizeof(SharedBlock)) != 0) {
        LOGE("ASHMEM_SET_SIZE failed: %s", strerror(errno));
        close(fd);
        return -1;
    }

    // 映射地址空间
    void *addr = mmap(nullptr, sizeof(SharedBlock), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    if (addr == MAP_FAILED) {
        LOGE("mmap failed");
        close(fd);
        return -1;
    }

    // 共享内存空间用SharedBlock结构化
    g_block = reinterpret_cast<SharedBlock *>(addr);
    memset(g_block, 0, sizeof(SharedBlock));

    // 初始化进程间信号量
    // 初始可写
    sem_init(&g_block->sem_empty, 1, 1);
    // 初始不可读
    sem_init(&g_block->sem_full, 1, 0);
    // 互斥锁
    sem_init(&g_block->sem_mutex, 1, 1);

    g_block->state = SHM_STATE_IDLE;
    LOGI("createShm success, fd=%d", fd);
    return fd;
}

/**
 * 写入数据
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_yangle_ashmem_NativeShm_write(JNIEnv *env, jobject, jbyteArray data, jint len) {
    if (!g_block) {
        LOGE("write: g_block is null");
        return -1;
    }

    // P(empty)，如果Consumer还没读完，上一次写会阻塞在这里
    sem_wait(&g_block->sem_empty);
    // 进入临界区, 保护state、data_len、data的一致性
    sem_wait(&g_block->sem_mutex);

    jbyte *src = env->GetByteArrayElements(data, nullptr);
    memcpy(g_block->data, src, len);
    g_block->data_len = len;
    g_block->state = SHM_STATE_DATA;
    env->ReleaseByteArrayElements(data, src, 0);

    // 离开临界区
    sem_post(&g_block->sem_mutex);

    // V(full)，通知 Consumer 可以读取
    sem_post(&g_block->sem_full);
    return len;
}

/**
 * 读取数据
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_yangle_ashmem_NativeShm_read(JNIEnv* env, jobject, jint fd, jbyteArray out) {
    if (!g_block) {
        void *addr = mmap(nullptr, sizeof(SharedBlock), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
        if (addr == MAP_FAILED) {
            LOGE("client mmap failed");
            return -1;
        }
        g_block = reinterpret_cast<SharedBlock *>(addr);
    }

    // P(full)，等待 Producer 写入
    sem_wait(&g_block->sem_full);
    sem_wait(&g_block->sem_mutex);

    // 识别 EOF
    if (g_block->state == SHM_STATE_EOF) {
        sem_post(&g_block->sem_mutex);
        sem_post(&g_block->sem_empty);
        LOGI("receive EOF");
        return -1;
    }

    int len = g_block->data_len;
    env->SetByteArrayRegion(out, 0, len, reinterpret_cast<jbyte*>(g_block->data));

    // 信号量可写
    sem_post(&g_block->sem_mutex);
    sem_post(&g_block->sem_empty);
    return len;
}

/**
 * 数据传输结束
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_yangle_ashmem_NativeShm_sendEof(JNIEnv*, jobject) {
    if (!g_block) return;
    sem_wait(&g_block->sem_empty);
    sem_wait(&g_block->sem_mutex);

    g_block->state = SHM_STATE_EOF;
    g_block->data_len = 0;

    sem_post(&g_block->sem_mutex);
    sem_post(&g_block->sem_full);
    LOGI("send EOF");
}

/**
 * 销毁共享内存
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_yangle_ashmem_NativeShm_destroy(JNIEnv*, jobject, jint fd) {
    if (g_block) {
        munmap(g_block, sizeof(SharedBlock));
        g_block = nullptr;
    }
    close(fd);
    LOGI("destroy shm fd=%d", fd);
}
