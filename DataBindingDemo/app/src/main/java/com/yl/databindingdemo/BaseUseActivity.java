package com.yl.databindingdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yl.databindingdemo.bean.StudentInfo;
import com.yl.databindingdemo.databinding.ActivityBaseUseBinding;

/**
 * 基本使用
 * Created by yangle on 2017/7/7.
 */

public class BaseUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBaseUseBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_base_use);
        StudentInfo studentInfo = new StudentInfo("小明", 12);
        binding.setStudentInfo(studentInfo);
    }
}
