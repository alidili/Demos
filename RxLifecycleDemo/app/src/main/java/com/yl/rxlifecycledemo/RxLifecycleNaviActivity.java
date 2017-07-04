package com.yl.rxlifecycledemo;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.navi2.component.NaviActivity;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.navi.NaviLifecycle;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * rxlifecycle-navi
 * Created by yangle on 2017/7/4.
 */

public class RxLifecycleNaviActivity extends NaviActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;

    private final LifecycleProvider<ActivityEvent> provider
            = NaviLifecycle.createActivityLifecycleProvider(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxlifecycle_navi);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                // 假装耗时操作
                e.onNext("我处理完了，你显示吧");
            }
        });
        observable.compose(provider.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        Toast.makeText(RxLifecycleNaviActivity.this, s, Toast.LENGTH_SHORT).show();
                        tvContent.setText("我显示了");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                // 假装耗时操作
                e.onNext("我处理完了，你显示吧");
            }
        });
        observable.compose(provider.<String>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        Toast.makeText(RxLifecycleNaviActivity.this, s, Toast.LENGTH_SHORT).show();
                        tvContent.setText("我显示了");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
