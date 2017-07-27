package com.yl.mvvmdemo.net;

import com.yl.mvvmdemo.Constant;
import com.yl.mvvmdemo.bean.ExpressInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 请求参数接口
 * Created by yangle on 2017/7/26.
 */

public interface RetrofitService {

    /**
     * 获取快递信息
     * Rx方式
     *
     * @param type   快递类型
     * @param postid 快递单号
     * @return Observable<ExpressInfo>
     */
    @GET(Constant.UrlOrigin.get_express_info)
    Observable<ExpressInfo> getExpressInfoRx(@Query("type") String type, @Query("postid") String postid);
}
