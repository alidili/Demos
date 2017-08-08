package com.yl.mvvmdemo.viewmodel;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yl.mvvmdemo.bean.ExpressInfo;
import com.yl.mvvmdemo.databinding.ActivityMainBinding;
import com.yl.mvvmdemo.manager.DataManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * ViewModel层
 * Created by yangle on 2017/7/26.
 */

public class ExpressViewModel extends BaseViewModel {

    private Context context;
    public ExpressInfo expressInfo;
    private DataManager dataManager;

    // 是否显示Loading
    public final ObservableBoolean isShowLoading = new ObservableBoolean();
    public final ObservableField<String> errorMessage = new ObservableField<>();

    public ExpressViewModel(LifecycleProvider<ActivityEvent> provider, Context context,
                            ActivityMainBinding binding) {
        super(provider);
        this.context = context;
        expressInfo = new ExpressInfo();
        binding.setExpressViewModel(this);
        dataManager = DataManager.getInstance();
    }

    /**
     * 获取快递信息
     *
     * @param type   快递类型
     * @param postid 快递单号
     */
    public void getExpressInfo(String type, String postid) {
        isShowLoading.set(true);

        dataManager.getExpressInfo(type, postid)
                .subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .compose(getProvider().<ExpressInfo>bindUntilEvent(ActivityEvent.DESTROY)) // onDestroy取消订阅
                .subscribe(new DefaultObserver<ExpressInfo>() {  // 订阅
                    @Override
                    public void onNext(@NonNull ExpressInfo expressInfo) {
                        ExpressViewModel.this.expressInfo.setExpressInfo(expressInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        errorMessage.set(e.getMessage());
                        isShowLoading.set(false);
                    }

                    @Override
                    public void onComplete() {
                        isShowLoading.set(false);
                    }
                });
    }
}
