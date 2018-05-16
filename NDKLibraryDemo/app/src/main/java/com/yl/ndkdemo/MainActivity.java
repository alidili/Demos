package com.yl.ndkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yl.ndklibrary.NDKLibrary;

/**
 * 主页
 * <p>
 * Created by yangle on 2018/5/16.
 * Website：http://www.yangle.tech
 */

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.sample_text);
        tv.setText(NDKLibrary.stringFromJNI());
    }

    public native String stringFromJNI();
}
