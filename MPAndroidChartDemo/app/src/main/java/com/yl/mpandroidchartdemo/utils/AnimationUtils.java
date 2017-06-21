package com.yl.mpandroidchartdemo.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * 动画
 * Created by yangle on 2016/11/26.
 */
public class AnimationUtils {

    /**
     * 开始播放模式选择动画
     *
     * @param view       目标View
     * @param isSelected 是否选择
     */
    public static void startModeSelectAnimation(View view, boolean isSelected) {
        RotateAnimation rotate;
        if (isSelected) {
            rotate = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            rotate = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        rotate.setFillAfter(true);
        rotate.setDuration(200);
        view.startAnimation(rotate);
    }
}
