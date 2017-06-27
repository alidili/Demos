package com.yl.mvpdemo.impl;

import com.yl.mvpdemo.Constant;
import com.yl.mvpdemo.bean.PostInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 请求参数接口
 * Created by yangle on 2017/6/26.
 */

public interface RetrofitService {

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
