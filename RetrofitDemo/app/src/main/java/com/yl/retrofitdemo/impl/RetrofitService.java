package com.yl.retrofitdemo.impl;

import com.yl.retrofitdemo.Constant;
import com.yl.retrofitdemo.bean.PostInfo;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 请求参数接口
 * Created by yangle on 2017/6/19.
 */

public interface RetrofitService {

    /**
     * 获取快递信息
     *
     * @param type   快递类型
     * @param postid 快递单号
     * @return Call<PostInfo>
     */
    @GET(Constant.UrlOrigin.get_post_info)
    Call<PostInfo> getPostInfo(@Query("type") String type, @Query("postid") String postid);

    /**
     * 获取快递信息
     * Rx方式
     *
     * @param type   快递类型
     * @param postid 快递单号
     * @return Observable<PostInfo>
     */
    @GET(Constant.UrlOrigin.get_post_info)
    Observable<PostInfo> getPostInfoRx(@Query("type") String type, @Query("postid") String postid);
}
