package com.yl.databindingdemo.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 图片工具
 * Created by yangle on 2017/7/21.
 */

public class ImageUtils {

    /**
     * 加载图片
     * 无需手动调用此方法
     *
     * @param view ImageView
     * @param url  图片地址
     */
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }
}
