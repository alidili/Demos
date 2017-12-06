package com.yl.countdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 主页
 * <p>
 * Created by yangle on 2017/12/5.
 * Website：http://www.yangle.tech
 */

public class MainActivity extends AppCompatActivity {

    private CountdownView countdownView;
    private Button btnStart;
    private int time = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        countdownView = findViewById(R.id.view_countdown);
        btnStart = findViewById(R.id.btn_start);

        // 设置倒计时时长
        countdownView.setCountdown(time);
        // 设置倒计时改变监听
        countdownView.setOnCountdownListener(new CountdownView.OnCountdownListener() {
            @Override
            public void countdown(int time) {
                MainActivity.this.time = time;
            }
        });

        // 开始倒计时监听
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                time--;
                                countdownView.setCountdown(time);
                                if (time == 0) {
                                    timer.cancel();
                                }
                            }
                        });
                    }
                }, 1000, 1000);
            }
        });
    }
}
