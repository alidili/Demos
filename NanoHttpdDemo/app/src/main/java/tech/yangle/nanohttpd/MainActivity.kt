package tech.yangle.nanohttpd

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.yangle.nanohttpd.databinding.ActivityMainBinding

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

        // 启动服务
        mLocalServer.setResourcePath(getExternalFilesDir("alidili")?.absolutePath ?: "")
        mLocalServer.startServer()

        // WebView配置
        val settings = binding.wvContent.settings
        settings.javaScriptEnabled = true
        binding.wvContent.loadUrl("http://127.0.0.1:8888/index.html")
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocalServer.stopServer()
    }
}