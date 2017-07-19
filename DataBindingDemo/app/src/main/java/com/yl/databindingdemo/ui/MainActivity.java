package com.yl.databindingdemo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.databinding.ActivityMainBinding;

/**
 * 主页
 * Created by yangle on 2017/7/7.
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
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                switch (v.getId()) {
                    case R.id.btn_base_use: // 基本使用
                        intent = new Intent(MainActivity.this, BaseUseActivity.class);
                        break;

                    case R.id.btn_event_handling: // 事件处理
                        intent = new Intent(MainActivity.this, EventHandlingActivity.class);
                        break;

                    default:
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
