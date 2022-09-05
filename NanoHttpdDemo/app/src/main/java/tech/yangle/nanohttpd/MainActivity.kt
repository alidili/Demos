package tech.yangle.nanohttpd

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.leo618.zip.IZipCallback
import com.leo618.zip.ZipManager
import tech.yangle.nanohttpd.FileUtils.copyAssetAndWrite
import tech.yangle.nanohttpd.FileUtils.getExternalFilesDir
import tech.yangle.nanohttpd.databinding.ActivityMainBinding
import java.io.File

/**
 * NanoHttpd本地服务Demo
 * <p>
 * Created by YangLe on 2022/9/2.
 * Website：http://www.yangle.tech
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mLocalServer = LocalServer.getInstance()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // WebView配置
        val settings = binding.wvContent.settings
        settings.javaScriptEnabled = true

        // 启动服务
        mLocalServer.setResourcePath(getExternalFilesDir(this, "alidili"))
        mLocalServer.startServer()

        // 拷贝网页文件到本地目录
        val webZipFile = File(getExternalFilesDir(this, ""), "alidili.zip")
        val webFile = File(getExternalFilesDir(this, "alidili"))
        if (webFile.listFiles()?.size ?: 0 > 0) {
            binding.wvContent.loadUrl("http://127.0.0.1:8888/index.html")
        } else {
            val result = copyAssetAndWrite(this, webFile.parent!!, webZipFile.name)
            if (!result) {
                Toast.makeText(this, "文件拷贝失败", Toast.LENGTH_SHORT).show()
            }
            // 解压文件
            ZipManager.unzip(webZipFile.absolutePath, getExternalFilesDir(this, ""),
                object : IZipCallback {
                    override fun onStart() {

                    }

                    override fun onProgress(percentDone: Int) {

                    }

                    override fun onFinish(success: Boolean) {
                        binding.wvContent.loadUrl("http://127.0.0.1:8888/index.html")
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocalServer.stopServer()
    }
}