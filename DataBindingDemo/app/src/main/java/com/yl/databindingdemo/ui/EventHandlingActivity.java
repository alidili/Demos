package com.yl.databindingdemo.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.databinding.ActivityEventHandlingBinding;
import com.yl.databindingdemo.handler.EventHandler;
import com.yl.databindingdemo.task.Task;

/**
 * 事件处理
 * Created by yangle on 2017/7/19.
 */

public class EventHandlingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEventHandlingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_event_handling);

        binding.setHandler(new EventHandler());
        binding.setTask(new Task());
    }
}
