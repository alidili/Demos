package com.yl.rxlifecycledemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页
 * Created by yangle on 2017/7/4.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_components, R.id.btn_navi, R.id.btn_lifecycle})
    public void onViewClicked(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.btn_components:
                intent = new Intent(this, RxLifecycleComponentsActivity.class);
                break;

            case R.id.btn_navi:
                intent = new Intent(this, RxLifecycleNaviActivity.class);
                break;

            case R.id.btn_lifecycle:
                intent = new Intent(this, RxLifecycleActivity.class);

            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
