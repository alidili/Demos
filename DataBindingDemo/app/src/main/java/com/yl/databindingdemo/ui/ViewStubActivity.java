package com.yl.databindingdemo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.bean.User;
import com.yl.databindingdemo.databinding.ActivityViewStubBinding;
import com.yl.databindingdemo.databinding.LayoutIncludeBinding;

/**
 * ViewStub
 * Created by yangle on 2017/7/21.
 */

public class ViewStubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityViewStubBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_stub);

        binding.viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutIncludeBinding viewStubBinding = DataBindingUtil.bind(inflated);
                User user = new User("容华", "谢后");
                viewStubBinding.setUser(user);
            }
        });

        binding.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.viewStub.isInflated()) {
                    binding.viewStub.getViewStub().inflate();
                }
            }
        });
    }
}
