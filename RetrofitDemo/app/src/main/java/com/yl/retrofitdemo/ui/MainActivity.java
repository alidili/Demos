package com.yl.retrofitdemo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yl.retrofitdemo.Constant;
import com.yl.retrofitdemo.R;
import com.yl.retrofitdemo.bean.PostInfo;
import com.yl.retrofitdemo.impl.RetrofitService;
import com.yl.retrofitdemo.manager.SendMessageManager;
import com.yl.retrofitdemo.utils.RetrofitUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 主页
 * Created by yangle on 2017/6/19.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */

public class MainActivity extends AppCompatActivity {

    private SendMessageManager sendMessageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        sendMessageManager = SendMessageManager.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.btn_base_request, R.id.btn_rx_request, R.id.btn_encap_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_base_request:
                // 基本使用
                baseRequest();
                break;

            case R.id.btn_rx_request:
                // Rx方式使用
                rxRequest();
                break;

            case R.id.btn_encap_request:
                // 封装使用
                encapRequest();
                break;

            default:
                break;
        }
    }

    /**
     * 基本使用
     */
    private void baseRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitUtils.getOkHttpClient()) // 打印请求参数
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<PostInfo> call = service.getPostInfo("yuantong", "11111111111");
        call.enqueue(new Callback<PostInfo>() {
            @Override
            public void onResponse(Call<PostInfo> call, Response<PostInfo> response) {
                Log.i("http返回：", response.body().toString());
                Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostInfo> call, Throwable t) {

            }
        });
    }

    /**
     * Rx方式使用
     */
    private void rxRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .client(RetrofitUtils.getOkHttpClient()) // 打印请求参数
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Observable<PostInfo> observable = service.getPostInfoRx("yuantong", "11111111111");
        observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .subscribe(new Observer<PostInfo>() {  // 订阅
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull PostInfo postInfo) {
                        Log.i("http返回：", postInfo.toString());
                        Toast.makeText(MainActivity.this, postInfo.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 封装使用
     */
    private void encapRequest() {
        sendMessageManager.getPostInfo("yuantong", "11111111111");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PostInfo postInfo) {
        Log.i("接收消息：", postInfo.toString());
        Toast.makeText(MainActivity.this, postInfo.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
