package com.yl.mvpdemo.net;

import com.yl.mvpdemo.Constant;
import com.yl.mvpdemo.bean.ExpressInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

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
     * @return Observable<ExpressInfo>
     */
    @GET(Constant.UrlOrigin.get_express_info)
    Observable<ExpressInfo> getExpressInfoRx(@Query("type") String type, @Query("postid") String postid);
}
