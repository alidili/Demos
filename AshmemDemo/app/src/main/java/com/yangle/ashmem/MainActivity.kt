package com.yangle.ashmem

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.ParcelFileDescriptor
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

/**
 * 共享内存传输数据
 * Created by yangle on 2025/12/15.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */
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