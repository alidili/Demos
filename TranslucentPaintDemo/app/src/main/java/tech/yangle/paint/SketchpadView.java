package tech.yangle.paint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 半透明画笔，笔迹叠加效果
 * <p>
 * Created by yangle on 2020/4/16.
 * Website：http://www.yangle.tech
 */
public class SketchpadView extends View {

    private Paint mPaint;
    private Path mPath;
    private float mLastX;
    private float mLastY;
    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;

    public SketchpadView(Context context) {
        this(context, null);
    }

    public SketchpadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SketchpadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 抗锯齿、防抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        // 画笔模式为描边
        mPaint.setStyle(Paint.Style.STROKE);
        // 拐角为圆角
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        // 两端为圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        // 画笔宽度
        mPaint.setStrokeWidth(50);
        // 画笔颜色
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        // 画笔透明度，先设置颜色，再设置透明度0-255
        mPaint.setAlpha(80);
        // 笔迹路径
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 双缓存机制
        mBufferBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        }
        // ACTION_MOVE时，将笔迹临时绘制在画布上
        canvas.drawPath(mPath, mPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                mLastX = x;
                mLastY = y;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                // ACTION_UP时，将当前一笔的笔迹，绘制到缓存画布上
                mBufferCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                invalidate();
                break;
        }
        return true;
    }
}

