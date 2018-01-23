package com.yl.ndkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * 主页
 * <p>
 * Created by yangle on 2018/1/22.
 * Website：http://www.yangle.tech
 */

public class MainActivity extends AppCompatActivity {

    // 加载native-lib，不加lib前缀
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 将获取的字符串显示在TextView上
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    /**
     * native-lib中的原生方法
     */
    public native String stringFromJNI();
}
