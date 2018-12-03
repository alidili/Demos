package com.yl.indicatorseekbar;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 主页
 * <p>
 * Created by yangle on 2018/11/30.
 * Website：http://www.yangle.tech
 */

public class MainActivity extends AppCompatActivity {

    private TextView tvIndicator;
    private IndicatorSeekBar indicatorSeekBar;
    private Paint mPaint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvIndicator = findViewById(R.id.tv_indicator);
        indicatorSeekBar = findViewById(R.id.indicator_seek_bar);

        initData();
    }

    private void initData() {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvIndicator.getLayoutParams();
        indicatorSeekBar.setOnSeekBarChangeListener(new IndicatorSeekBar.OnIndicatorSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, float indicatorOffset) {
                String indicatorText = progress + "%";
                tvIndicator.setText(indicatorText);
                params.leftMargin = (int) indicatorOffset;
                tvIndicator.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }
}
