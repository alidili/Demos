## Android基于共享内存实现跨进程大文件传输

[![](https://img.shields.io/badge/APK%20download-14.2M-green.svg)](https://github.com/alidili/Demos/raw/master/AshmemDemo/AshmemDemo.apk)

[《Android基于共享内存实现跨进程大文件传输》](https://juejin.cn/post/7587264707284877322)

# 0.写在前面

在实际的项目开发中，为了更好的性能和结构，我们经常会把一个项目应用分成多个进程或者多个组件，当不同进程的业务之间需要通信时，通常会选择AIDL、Bundle或者广播的方式进行通信，这些通信手段有一个共同的特点，就是只支持简单、小容量、不频繁的数据传输，当我们的应用存在这样的业务场景，比如视频流跨进程传输、大文件跨进程传输等需要复杂数据、频繁数据交换的功能时，就不能使用上述的通信手段了。

为了解决这个问题，可以选择共享内存的方式进行通信，在设备的内存中开辟一个固定容量的空间，使用生产者-消费者模型，生产者在内存中写入数据，通过PV操作通知消费者进行读取，消费者读取完成后再通知生产者进行写入。

# 1.实现

## 1.1 数据结构

首先定义下共享内存中的数据结构，首先定义3个信号量：

- sem_empty: 定义是否允许写，生产者写前 P(empty)，消费者读后 V(empty)

- sem_full: 定义是否允许读，消费者读前 P(full)，生产者写后 V(full)

- sem_mutex: 定义读写互斥锁，保护 state/data_len/data 的互斥

接下来定义数据传输的状态，分别是 IDLE（空闲状态）、DATA（传输数据状态）、EOF（文件传输结束状态），然后定义每次传输数据的有效长度 data_len 和数据块 data属性。

```
/**
 * 数据传输结构
 */
struct SharedBlock {
    // 是否允许写（Producer）
    sem_t sem_empty;
    // 是否允许读（Consumer）
    sem_t sem_full;
    // 读写互斥锁
    sem_t sem_mutex;
    // 当前状态
    uint32_t state;
    // data 中有效数据长度
    uint32_t data_len;
    // 数据
    uint8_t data[SHM_DATA_SIZE];
};

/**
 * 共享内存状态机
 *
 * IDLE : 空闲状态
 * DATA : 有有效数据
 * EOF  : 文件传输结束
 */
enum ShmState : uint32_t {
    SHM_STATE_IDLE = 0,
    SHM_STATE_DATA = 1,
    SHM_STATE_EOF = 2,
};
```

## 1.2 创建共享内存

通过 open("/dev/ashmem", O_RDWR) 方法创建共享内存，得到文件描述符，fd 可通过 AIDL 传递给其他进程，在其他进程通过 fd 映射相同的内存区域进行操作。创建完成后通过 ioctl 方法设置共享内存的名称和大小，再使用 mmap 对内存的地址空间进行映射，然后通过 reinterpret_cast 对内存空间进行结构化。

接下来进行信号量初始化，初始状态为可写、不可读、可进入临界区状态，数据传输状态初始为空闲状态，到这里共享内存就创建完成了，继续往下看共享内存的数据是如何进行读写的。

```
/**
 * 创建共享内存
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_yangle_ashmem_NativeShm_createShm(JNIEnv *, jobject) {
    // 创建共享内存区域
    int fd = open("/dev/ashmem", O_RDWR);
    if (fd < 0) {
        LOGE("ashmem create region failed");
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
```

## 1.3 写入数据

首先通过 sem_wait（P 操作）方法判断是否可写，以及临界区是否加锁，然后复制数据到共享内存中，通过 sem_post（V 操作）释放临界区锁，通知消费者可以读取数据。

```
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
```

## 1.4 读取数据

读取数据是在另一进程进行的，通过使用 AIDL 传递过来的 fd 可以映射与生产者进程相同的内存区域，然后再将内存区域结构化成 SharedBlock，可以拿到内存中的信号量标志和数据。

首先判断是否读取，如果生产者还在写入，会停在 sem_wait(&g_block->sem_full) 进行等待，然后判断是否传输完成，再进行数据读取，最后再释放临界区锁，通知生产者可以写入数据。

```
/**
 * 读取数据
 */
extern "C"
JNIEXPORT jint JNICALL
Java_com_yangle_ashmem_NativeShm_read(JNIEnv* env, jobject, jint fd, jbyteArray out) {
    if (!g_block) {
        void *addr = mmap(nullptr, sizeof(SharedBlock), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
        if (addr == MAP_FAILED) {
            LOGE("client mmap failed, fd=%d", fd);
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
```

## 1.5 传输结束

数据传输完成后，发送结束标志，先判断是否读取完成，然后把传输状态修改为 SHM_STATE_EOF 结束，再释放临界区锁，通知消费者可以读取数据，消费者读取传输状态为结束，到此一轮数据传输完成。

```
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
```

## 1.6 销毁共享内存

当不再传输数据后，也就是消费者收到 SHM_STATE_EOF 状态之后，可以通过 AIDL 通知生产者对共享内存进行销毁。

```
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
```

# 2.测试

到这里共享内存传输的基本功能就完成了，写个例子来测试下，定义一个 ShmService（android:process=":shm" ） 进程作为数据发送方，MainActivity 作为数据接收方，先看下项目结构：

![项目结构](https://upload-images.jianshu.io/upload_images/3270074-227545941f24222b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

流程如下：

- 1.MainActivity 与 ShmService 进行服务绑定，绑定成功后调用 AIDL 的 startTransfer 方法通知 ShmService 开始传输

- 2.ShmService 收到通知后开始创建共享内存，然后将 fd 通过 AIDL 的 onShmReady 回调方法传递给 MainActivity

- 3.ShmService 开始发送文件，MainActivity 开始读取文件

- 4.ShmService 发送文件完成后，通过共享内存信号量通知 MainActivity 结束

- 5.MainActivity 读取到 SHM_STATE_EOF 状态后，通过 AIDL 的 endTransfer 方法通知 ShmService

- 6.ShmService 收到结束通知后，销毁已创建的共享内存

**MainActivity 如下：**

```
class MainActivity : AppCompatActivity() {

    private val native = NativeShm()
    private lateinit var service: IShmService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(Intent(this, ShmService::class.java), conn, BIND_AUTO_CREATE)
    }

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            service = IShmService.Stub.asInterface(binder)
            // 通知服务端开始发送文件
            service.startTransfer(object : IShmCallback.Stub() {
                override fun onShmReady(pfd: ParcelFileDescriptor) {
                    startReceive(pfd.fd)
                }
            })
        }

        override fun onServiceDisconnected(name: ComponentName) {}
    }

    /**
     * 接收文件
     *
     * @param fd 文件描述符
     */
    private fun startReceive(fd: Int) {
        Thread {
            val out = File(getExternalFilesDir(""), "test.jpg")
            if (out.exists()) {
                out.delete()
            }
            val buf = ByteArray(64 * 1024)

            FileOutputStream(out).use { fos ->
                while (true) {
                    val len = native.read(fd, buf)
                    if (len < 0) {
                        service.endTransfer()
                        break
                    }
                    fos.write(buf, 0, len)
                }
            }
        }.start()
    }
}
```

**ShmService 如下：**

```
class ShmService : Service() {

    private val native = NativeShm()
    private var client: IShmService? = null
    private var mFd: Int? = null

    override fun onBind(p0: Intent?): IBinder? {
        return object : IShmService.Stub() {
            override fun startTransfer(callback: IShmCallback?) {
                // 创建共享内存
                mFd = native.createShm()
                // 将文件描述符回调给接收端
                val pfd = ParcelFileDescriptor.fromFd(mFd!!)
                callback?.onShmReady(pfd)
                // 发送文件
                sendFile("test.jpg", mFd!!)
            }

            override fun endTransfer() {
                if (mFd != null) {
                    native.destroy(mFd!!)
                }
            }
        }.also {
            client = it
        }
    }

    /**
     * 发送文件
     *
     * @param fileName 文件名
     * @param fd       文件描述符
     */
    private fun sendFile(fileName: String, fd: Int) {
        Thread {
            val buffer = ByteArray(64 * 1024)
            try {
                assets.open(fileName).use { input ->
                    while (true) {
                        val len = input.read(buffer)
                        if (len <= 0) break
                        native.write(buffer, len)
                    }
                }
                native.sendEof()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
```

**AIDL 如下：**

```
interface IShmService {
    /**
     * 开始传输
     *
     * @params callback 回调
     */
    void startTransfer(in IShmCallback callback);

    /**
     * 结束传输
     */
    void endTransfer();
}

interface IShmCallback {
   /**
     * 共享内存初始化完成
     *
     * @params pfd 文件描述符
     */
    void onShmReady(in ParcelFileDescriptor pfd);
}
```

**注意在 onShmReady 方法中需要传递 ParcelFileDescriptor 类型，不能直接传递 int 类型的 fd，ParcelFileDescriptor 内部会自动实现 Binder 的自动映射。**

- Binder 会把 fd 复制到目标进程，目标进程拿到的 ParcelFileDescriptor 对象里有一个新的 fd。

- 系统会在底层做 fd 映射和引用计数，保证两个进程都可以安全访问同一个底层资源。

- 它不仅能封装 ashmem fd，也能封装普通文件、socket、pipe 等。

# 3.PV操作

在数据传输中使用PV信号量来控制生产者-消费者的读写操作，在这里再梳理下流程：

**写数据**

- 1.sem_wait(sem_empty) — 等待缓冲区可写

- 2.sem_wait(sem_mutex) — 进入临界区

- 3.memcpy 数据到共享内存，设置 data_len、state = DATA

- 4.sem_post(sem_mutex) — 离开临界区

- 5.sem_post(sem_full) — 通知消费者有数据可读

**读数据**

- 6.sem_wait(sem_full) — 等待数据到达

- 7.sem_wait(sem_mutex) — 进入临界区，检查 state

- 8.若 state == EOF 则 sem_post(sem_mutex) 并 sem_post(sem_empty)，返回 EOF，否则拷贝数据到用户缓冲

- 9.sem_post(sem_mutex) — 离开临界区

- 10.sem_post(sem_empty) — 通知写数据端可以写下一个分片

# 4.写在最后

GitHub地址：https://github.com/alidili/Demos/tree/master/AshmemDemo

到这里，Android消息推送SSE方案就介绍完了，如有问题可以给我留言评论或者在GitHub中提交Issues，谢谢！

## License

```
Copyright (C) 2025 YangLe

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.