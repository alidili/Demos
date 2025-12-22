#pragma once

#include <semaphore.h>
#include <cstdint>

#define SHM_DATA_SIZE (1024 * 1024)

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