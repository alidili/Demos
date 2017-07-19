package com.yl.databindingdemo.handler;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.yl.databindingdemo.task.Task;

/**
 * Created by test on 2017/7/19.
 */

public class EventHandler {

    public void onClickFriend(View view) {
        Toast.makeText(view.getContext(), "onClickFriend", Toast.LENGTH_LONG).show();
    }

    public void onTaskClick(Task task) {
        task.run();
    }

    public void onTaskClick(View view, Task task) {
        Toast.makeText(view.getContext(), "onTaskClick", Toast.LENGTH_LONG).show();
        task.run();
    }

    public void onCompletedChanged(Task task, boolean completed) {
        if (completed) {
            task.run();
        }
    }
}
