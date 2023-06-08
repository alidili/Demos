package tech.yangle.retrofitcache

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import tech.yangle.retrofitcache.databinding.ActivityMainBinding
import java.io.File


/**
 * Retrofit增加接口缓存功能
 * <p>
 * Created by YangLe on 2023/6/8.
 * Website：http://www.yangle.tech
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
    }

    private fun initData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(getOkHttpClient())
            .build()

        binding.btnRequest.setOnClickListener {
            val service = retrofit.create(RetrofitService::class.java)
            val call = service.request("https://www.baidu.com")
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val result = response.body()?.string() ?: ""
                    binding.tvResult.text = result
                    Log.i("http返回：", result)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }
            })
        }
    }

    /**
     * 获取OkHttpClient
     *
     * @return OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        // 定制OkHttp
        val httpClientBuilder = OkHttpClient.Builder()
        // 添加响应数据缓存拦截器
        httpClientBuilder.addInterceptor(CacheInterceptor(this, "key"))
        return httpClientBuilder.build()
    }

    /**
     * 缓存数据拦截器
     *
     * @param mContext Context
     * @param key      秘钥
     */
    private class CacheInterceptor(
        private val mContext: Context,
        private val key: String
    ) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            val cacheKey = HttpUtils.getCacheKey(request)
            val cacheFile = File(HttpUtils.getCacheFile(mContext), cacheKey)

            // 缓存时间1小时
            val cacheEnable = (System.currentTimeMillis() - cacheFile.lastModified()) < 3600000L
            if (cacheEnable && cacheFile.exists() && cacheFile.length() > 0) {
                Log.i(
                    "CacheInterceptor",
                    "[intercept] 缓存模式 url:${HttpUtils.getRequestUrl(request)} " +
                            "过期时间:${HttpUtils.dateTimeToString(cacheFile.lastModified() + 3600000L)}"
                )
                val cache = SecurityUtils.decryptContent(cacheFile.readText(), key)
                if (cache.isNotEmpty() && cache.startsWith("{") && cache.endsWith("}")) {
                    return okhttp3.Response.Builder()
                        .code(200)
                        .body(cache.toResponseBody())
                        .request(request)
                        .message("from disk cache")
                        .protocol(Protocol.HTTP_2)
                        .build()
                }
            }
            val response = chain.proceed(request)
            val responseBody = response.body ?: return response
            val data = responseBody.bytes()
            val dataString = String(data)
            // 写入缓存
            if (response.code == 200) {
                // Json数据写入缓存
                cacheFile.writeText(SecurityUtils.encryptContent(dataString, key))
            } else {
                cacheFile.writeText("")
            }
            return response.newBuilder()
                .body(data.toResponseBody(responseBody.contentType()))
                .build()
        }
    }
}