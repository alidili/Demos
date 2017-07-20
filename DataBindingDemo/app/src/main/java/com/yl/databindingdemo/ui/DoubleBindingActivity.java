package com.yl.databindingdemo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.bean.ObservableObjectsUser;
import com.yl.databindingdemo.databinding.ActivityDoubleBindingBinding;

/**
 * 双向绑定
 * Created by yangle on 2017/7/20.
 */

public class DoubleBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDoubleBindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_double_binding);
        binding.setUser(new ObservableObjectsUser());
    }
}
