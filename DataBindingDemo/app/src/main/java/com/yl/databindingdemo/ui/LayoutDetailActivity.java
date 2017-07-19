package com.yl.databindingdemo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.bean.User;
import com.yl.databindingdemo.databinding.ActivityLayoutDetailBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 布局详情
 * Created by yangle on 2017/7/19.
 */

public class LayoutDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLayoutDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_layout_detail);
        User user = new User("ronghua", "xiehou");
        binding.setUser(user);

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(new User("空谷", "幽兰"));
        binding.setUserList(userList);
        binding.setPosition(1);
    }
}
