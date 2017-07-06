package com.yl.mvpdemo.presenter;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yl.mvpdemo.bean.ExpressInfo;
import com.yl.mvpdemo.manager.DataManager;
import com.yl.mvpdemo.view.ExpressView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取快递信息逻辑处理
 * Created by yangle on 2017/6/27.
 */

public class ExpressPresenter extends BasePresenter {

    private ExpressView expressView;
    private DataManager dataManager;

    public ExpressPresenter(ExpressView expressView, LifecycleProvider<ActivityEvent> provider) {
        super(provider);
        this.expressView = expressView;
        dataManager = DataManager.getInstance();
    }

    /**
     * 获取快递信息
     *
     * @param type   快递类型
     * @param postid 快递单号
     */
    public void getExpressInfo(String type, String postid) {
        expressView.showProgressDialog();

        dataManager.getExpressInfo(type, postid)
                .subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                .compose(getProvider().<ExpressInfo>bindUntilEvent(ActivityEvent.DESTROY)) // onDestroy取消订阅
                .subscribe(new DefaultObserver<ExpressInfo>() {  // 订阅
                    @Override
                    public void onNext(@NonNull ExpressInfo expressInfo) {
                        expressView.updateView(expressInfo);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        expressView.showError(e.getMessage());
                        expressView.hideProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        expressView.hideProgressDialog();
                    }
                });
    }
}
