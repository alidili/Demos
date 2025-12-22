package com.yangle.ashmem

/**
 * JNI共享内存接口封装
 * Created by yangle on 2025/12/19.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */
class NativeShm {

    companion object {
        init {
            System.loadLibrary("native_shm")
        }
    }

    /**
     * 创建共享内存
     *
     * @return 文件描述符
     */
    external fun createShm(): Int

    /**
     * 写入数据
     *
     * @param data 数据
     * @param len  数据长度
     */
    external fun write(data: ByteArray, len: Int): Int

    /**
     * 读取数据
     *
     * @param fd 文件描述符
     * @param out 数据
     */
    external fun read(fd: Int, out: ByteArray): Int

    /**
     * 数据传输结束
     */
    external fun sendEof()

    /**
     * 销毁共享内存
     *
     * @param fd 文件描述符
     */
    external fun destroy(fd: Int)
}