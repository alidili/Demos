package com.yl.mvvmdemo.manager;

import com.yl.mvvmdemo.bean.ExpressInfo;
import com.yl.mvvmdemo.net.RetrofitHelper;
import com.yl.mvvmdemo.net.RetrofitService;

import io.reactivex.Observable;

/**
 * 数据处理
 * Created by yangle on 2017/7/26.
 */

public class DataManager {

    private static DataManager dataManager;
    private RetrofitService retrofitService;

    public static DataManager getInstance() {
        return dataManager == null ? dataManager = new DataManager() : dataManager;
    }

    private DataManager() {
        retrofitService = RetrofitHelper.getInstance().getRetrofitService();
    }

    /**
     * 获取快递信息
     *
     * @param type   快递类型
     * @param postid 快递单号
     * @return Observable<ExpressInfo>
     */
    public Observable<ExpressInfo> getExpressInfo(String type, String postid) {
        return retrofitService.getExpressInfoRx(type, postid);
    }
}
