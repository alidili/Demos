package tech.yangle.retrofitcache

import android.content.Context
import okhttp3.Request
import okio.Buffer
import tech.yangle.retrofitcache.SecurityUtils.getMD5
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 接口请求工具
 * Created by YangLe on 2023/6/8.
 */
object HttpUtils {

    /**
     * 获取接口数据缓存key
     *
     * @param request Request
     * @return 接口数据缓存key
     */
    fun getCacheKey(request: Request): String {
        val key = "${getRequestUrl(request)}?${getPostParams(request)}"
        return getMD5(key)
    }

    /**
     * 获取POST请求Url
     *
     * @param request Request
     * @return 请求Url
     */
    fun getRequestUrl(request: Request): String {
        return request.url.toString()
    }

    /**
     * 获取POST请求参数
     *
     * @param request Request
     * @return 请求参数
     */
    private fun getPostParams(request: Request): String {
        val method: String = request.method
        if ("POST" == method) {
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            return buffer.readString(Charset.defaultCharset())
        }
        return ""
    }

    /**
     * 获取缓存目录
     *
     * @param context Context
     * @return 缓存目录File
     */
    fun getCacheFile(context: Context): File {
        val cachePath = context.externalCacheDir?.absolutePath + "/http"
        val cacheFile = File(cachePath)
        if (!cacheFile.exists()) {
            cacheFile.mkdirs()
        }
        return cacheFile
    }

    /**
     * 时间格式化
     *
     * @param time   时间戳
     * @param format 格式
     * @param locale 地区
     * @return 格式化后的时间
     */
    fun dateTimeToString(
        time: Long,
        format: String = "yyyy-MM-dd HH:mm",
        locale: Locale = Locale.CHINESE
    ): String {
        val simpleDateFormat = SimpleDateFormat(format, locale)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT+8")
        val date = Date(time)
        return simpleDateFormat.format(date)
    }
}