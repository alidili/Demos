package com.yl.mvvmdemo.viewmodel;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * ViewModel基类
 * Created by yangle on 2017/7/26.
 */

public class BaseViewModel {

    private LifecycleProvider<ActivityEvent> provider;

    public BaseViewModel(LifecycleProvider<ActivityEvent> provider) {
        this.provider = provider;
    }

    public LifecycleProvider<ActivityEvent> getProvider() {
        return provider;
    }
}
