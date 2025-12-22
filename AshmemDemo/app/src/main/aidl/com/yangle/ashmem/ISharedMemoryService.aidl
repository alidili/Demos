// ISharedMemoryService.aidl
package com.yangle.ashmem;

// Declare any non-default types here with import statements

interface ISharedMemoryService {
    /**
     * 开始传输，发送文件描述符
     *
     * @params fd 文件描述符
     */
    int startTransfer(int fd);
}