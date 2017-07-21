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

                    case R.id.btn_layout_detail: // 布局详情
                        intent = new Intent(MainActivity.this, LayoutDetailActivity.class);
                        break;

                    case R.id.btn_observable: // 动态更新
                        intent = new Intent(MainActivity.this, ObservableActivity.class);
                        break;

                    case R.id.btn_double_binding: // 双向绑定
                        intent = new Intent(MainActivity.this, DoubleBindingActivity.class);
                        break;

                    case R.id.btn_event_handling: // 事件处理
                        intent = new Intent(MainActivity.this, EventHandlingActivity.class);
                        break;

                    case R.id.btn_recycler_view: // RecyclerView
                        intent = new Intent(MainActivity.this, RecyclerViewActivity.class);
                        break;

                    case R.id.btn_multi_recycler_view: // 多布局RecyclerView
                        intent = new Intent(MainActivity.this, MultiRecyclerViewActivity.class);
                        break;

                    case R.id.btn_custom_attribute: // 自定义属性
                        intent = new Intent(MainActivity.this, CustomAttributeActivity.class);
                        break;

                    case R.id.btn_view_stub: // ViewStub
                        intent = new Intent(MainActivity.this, ViewStubActivity.class);
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
