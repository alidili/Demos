package com.yl.databindingdemo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.bean.User;
import com.yl.databindingdemo.databinding.ActivityBaseUseBinding;

/**
 * 基本使用
 * Created by yangle on 2017/7/7.
 */

public class BaseUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ActivityBaseUseBinding是根据布局名称自动生成的
        // 代替原来的setContentView(R.layout.activity_base_use)方法
        ActivityBaseUseBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_base_use);
        final User user = new User("容华", "谢后");
        // set方法是根据data标签下的variable名称自动生成的
        binding.setUser(user);

        //  不会更新
        binding.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setFirstName("空谷");
                user.setLastName("幽兰");
            }
        });
    }
}
