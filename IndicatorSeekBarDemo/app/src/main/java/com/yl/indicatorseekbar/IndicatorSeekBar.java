package com.yl.indicatorseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatSeekBar;

/**
 * 主页
 * <p>
 * Created by yangle on 2018/11/30.
 * Website：http://www.yangle.tech
 */

public class IndicatorSeekBar extends AppCompatSeekBar {

    // 画笔
    private Paint mPaint;
    // 进度文字位置信息
    private Rect mProgressTextRect = new Rect();
    // 进度监听
    private OnIndicatorSeekBarChangeListener mIndicatorSeekBarChangeListener;

    public IndicatorSeekBar(Context context) {
        this(context, null);
    }

    public IndicatorSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarStyle);
    }

    public IndicatorSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new TextPaint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#00574B"));
        mPaint.setTextSize(sp2px(16));
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String progressText = getProgress() + "%";
        mPaint.getTextBounds(progressText, 0, progressText.length(), mProgressTextRect);

        int thumbWidth = dp2px(50);
        // 进度百分比
        float progressRatio = (float) getProgress() / getMax();
        // thumb偏移量
        float thumbOffset = (thumbWidth - mProgressTextRect.width()) / 2 - thumbWidth * progressRatio;
        float thumbX = getWidth() * progressRatio + thumbOffset;
        float thumbY = getHeight() / 2f + mProgressTextRect.height() / 2f;
        canvas.drawText(progressText, thumbX, thumbY, mPaint);

        if (mIndicatorSeekBarChangeListener != null) {
            mIndicatorSeekBarChangeListener.onProgressChanged(this, getProgress(), getWidth(),
                    thumbWidth, progressRatio);
        }
    }

    /**
     * 设置进度监听
     *
     * @param listener OnIndicatorSeekBarChangeListener
     */
    public void setOnSeekBarChangeListener(OnIndicatorSeekBarChangeListener listener) {
        this.mIndicatorSeekBarChangeListener = listener;
    }

    /**
     * 进度监听
     */
    public interface OnIndicatorSeekBarChangeListener {
        /**
         * 进度监听回调
         *
         * @param seekBar       SeekBar
         * @param progress      进度
         * @param width         SeekBa宽度
         * @param thumbWidth    thumb宽度
         * @param progressRatio 百分比
         */
        public void onProgressChanged(SeekBar seekBar, int progress, int width, int thumbWidth,
                                      float progressRatio);
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param sp sp值
     * @return px值
     */
    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}
