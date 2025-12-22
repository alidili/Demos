// IShmCallback.aidl
package com.yangle.ashmem;

// Declare any non-default types here with import statements

interface IShmCallback {
   /**
     * 共享内存初始化完成
     *
     * @params pfd 文件描述符
     */
    void onShmReady(in ParcelFileDescriptor pfd);
}