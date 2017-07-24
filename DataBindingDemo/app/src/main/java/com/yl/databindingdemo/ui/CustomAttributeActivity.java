package com.yl.databindingdemo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.databinding.ActivityCustomAttributeBinding;

/**
 * 自定义属性
 * Created by yangle on 2017/7/21.
 */

public class CustomAttributeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCustomAttributeBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_custom_attribute);
        binding.setUrl("http://i7.qhmsg.com/t01b48a6f15bf0cf5c1.jpg");
    }
}
