package tech.yangle.retrofitcache

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Retrofit Api
 * Created by YangLe on 2023/6/8.
 */
interface RetrofitService {

    /**
     * 接口请求测试
     *
     * @param url 请求链接
     * @return Call<ResponseBody>
     */
    @GET
    fun request(@Url url: String): Call<ResponseBody>
}