package com.yangle.ashmem

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.ParcelFileDescriptor

/**
 * 共享内存发起传输端
 * Created by yangle on 2025/12/22.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */
class ShmService : Service() {

    private val native = NativeShm()
    private var client: IShmService? = null

    override fun onBind(p0: Intent?): IBinder? {
        return object : IShmService.Stub() {
            override fun startTransfer(callback: IShmCallback?) {
                // 创建共享内存
                val fd = native.createShm()
                // 将文件描述符回调给接收端
                val pfd = ParcelFileDescriptor.fromFd(fd)
                callback?.onShmReady(pfd)
                // 发送文件
                sendFile("test.jpg", fd)
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