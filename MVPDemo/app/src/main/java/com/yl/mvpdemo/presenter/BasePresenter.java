package com.yl.mvpdemo.presenter;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Presenter基类
 * Created by yangle on 2017/6/28.
 */

public class BasePresenter {

    private LifecycleProvider<ActivityEvent> provider;

    public BasePresenter(LifecycleProvider<ActivityEvent> provider) {
        this.provider = provider;
    }

    public LifecycleProvider<ActivityEvent> getProvider() {
        return provider;
    }
}
