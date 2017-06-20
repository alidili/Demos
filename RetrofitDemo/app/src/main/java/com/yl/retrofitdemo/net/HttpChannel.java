package com.yl.retrofitdemo.net;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.yl.retrofitdemo.Constant;
import com.yl.retrofitdemo.utils.RetrofitUtils;
import com.yl.retrofitdemo.bean.BaseBean;
import com.yl.retrofitdemo.impl.RetrofitService;
import com.yl.retrofitdemo.manager.ReceiveMessageManager;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Http通道
 * Created by yangle on 2017/6/19.
 */

public class HttpChannel {

    private static HttpChannel httpChannel;
    private RetrofitService retrofitService;

    public static HttpChannel getInstance() {
        return httpChannel == null ? httpChannel = new HttpChannel() : httpChannel;
    }

    private HttpChannel() {
        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())) // json解析
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava
                .client(RetrofitUtils.getOkHttpClient()) // 打印请求日志
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    /**
     * 发送消息
     *
     * @param observable Observable<? extends BaseBean>
     * @param urlOrigin  请求地址
     */
    public void sendMessage(Observable<? extends BaseBean> observable, final String urlOrigin) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        Log.i("http返回：", baseBean.toString() + "");
                        ReceiveMessageManager.getInstance().dispatchMessage(baseBean, urlOrigin);
                    }
                });
    }

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }
}
