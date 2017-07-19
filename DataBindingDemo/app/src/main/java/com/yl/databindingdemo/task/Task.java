package com.yl.databindingdemo.task;

import android.util.Log;

/**
 * Created by test on 2017/7/19.
 */

public class Task implements Runnable {

    private static final String TAG = "Task";

    @Override
    public void run() {
        Log.i(TAG, "Task running");
    }
}
