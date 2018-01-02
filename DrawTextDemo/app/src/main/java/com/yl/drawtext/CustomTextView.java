package com.yl.drawtext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * TextView
 * Created by yangle on 2017/12/26.
 */

public class CustomTextView extends View {

    // 画笔
    private Paint paint;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setTextSize(sp2px(50));
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // 将坐标原点移到控件中心
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // x轴
        canvas.drawLine(-getWidth() / 2, 0, getWidth() / 2, 0, paint);
        // y轴
        canvas.drawLine(0, -getHeight() / 2, 0, getHeight() / 2, paint);

        // 绘制坐标
        drawCoordinate(canvas);

        // 绘制居中文字
        //drawCenterText(canvas);

        // 绘制多行居中文字（方式1）
        //drawCenterMultiText1(canvas);

        // 绘制多行居中文字（方式2）
        //drawCenterMultiText2(canvas);
    }

    /**
     * 绘制坐标
     *
     * @param canvas 画布
     */
    private void drawCoordinate(Canvas canvas) {
        // 绘制文字
        canvas.drawText("YangLe", 0, 0, paint);

        // top-红
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        paint.setColor(Color.RED);
        canvas.drawLine(-getWidth() / 2, top, getWidth() / 2, top, paint);

        // ascent-黄
        float ascent = fontMetrics.ascent;
        paint.setColor(Color.parseColor("#ffc90e"));
        canvas.drawLine(-getWidth() / 2, ascent, getWidth() / 2, ascent, paint);

        // descent-绿
        float descent = fontMetrics.descent;
        paint.setColor(Color.GREEN);
        canvas.drawLine(-getWidth() / 2, descent, getWidth() / 2, descent, paint);

        // bottom-蓝
        float bottom = fontMetrics.bottom;
        paint.setColor(Color.BLUE);
        canvas.drawLine(-getWidth() / 2, bottom, getWidth() / 2, bottom, paint);
    }

    /**
     * 绘制居中文字
     *
     * @param canvas 画布
     */
    private void drawCenterText(Canvas canvas) {
        // 文字宽
        float textWidth = paint.measureText("YangLe");
        // 文字baseline在y轴方向的位置
        float baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2;
        canvas.drawText("YangLe", -textWidth / 2, baseLineY, paint);
    }

    /**
     * 绘制多行居中文字（方式1）
     *
     * @param canvas 画布
     */
    private void drawCenterMultiText1(Canvas canvas) {
        String text = "ABC";

        // 画笔
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.GRAY);

        // 设置宽度超过50dp时换行
        StaticLayout staticLayout = new StaticLayout(text, textPaint, dp2px(50),
                Layout.Alignment.ALIGN_CENTER, 1, 0, false);
        canvas.save();
        canvas.translate(-staticLayout.getWidth() / 2, -staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制多行居中文字（方式2）
     *
     * @param canvas 画布
     */
    private void drawCenterMultiText2(Canvas canvas) {
        String[] texts = {"A", "B", "C"};
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = Math.abs(fontMetrics.top);
        float ascent = Math.abs(fontMetrics.ascent);
        float descent = fontMetrics.descent;
        float bottom = fontMetrics.bottom;
        // 行数
        int textLines = texts.length;
        // 文字高度
        float textHeight = top + bottom;
        // 文字总高度
        float textTotalHeight = textHeight * textLines;
        // 基数
        float basePosition = (textLines - 1) / 2f;

        for (int i = 0; i < textLines; i++) {
            // 文字宽度
            float textWidth = paint.measureText(texts[i]);
            // 文字baseline在y轴方向的位置
            float baselineY;

            if (i < basePosition) { // 上
                baselineY = -(textTotalHeight / 2 - textHeight * i - top);

            } else if (i > basePosition) { // 下
                baselineY = textTotalHeight / 2 - textHeight * (textLines - i - 1) - bottom;

            } else { // 中
                baselineY = (ascent - descent) / 2;
            }

            canvas.drawText(texts[i], -textWidth / 2, baselineY, paint);
        }
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param sp sp值
     * @return px值
     */
    public int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getContext().getResources().getDisplayMetrics());
    }
}
