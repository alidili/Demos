package com.yl.mpandroidchartdemo.utils;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.yl.mpandroidchartdemo.PopupWindowAdapter;
import com.yl.mpandroidchartdemo.R;

import java.util.List;

/**
 * 弹框提示
 * Created by yangle on 2016/11/25.
 */
public class ShowUtils {

    private static View view;
    private static PopupWindow popupWindow;
    private static PopupWindowAdapter popupWindowAdapter;

    /**
     * popupWindow提示框消失方法
     */
    public static void popupWindowDismiss() {
        if (isPopupWindowShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            popupWindowAdapter = null;
        }
    }

    /**
     * popupWindow提示框是否正在运行
     *
     * @return popupWindow是否正在运行
     */
    public static boolean isPopupWindowShowing() {
        return popupWindow != null && popupWindow.isShowing();
    }


    /**
     * 创建PopupWindow选择提示框
     *
     * @param activity   当前Activity
     * @param targetView 与提示框关联的控件
     * @param width      提示框宽
     * @param height     提示框高
     */
    private static void createPopupWindow(Activity activity, View targetView,
                                          int width, int height) {
        popupWindowDismiss();

        //通过布局注入器，注入布局给View对象
        if (view != null) {
            view = null;
        }
        view = activity.getLayoutInflater().inflate(R.layout.layout_popup_window, null);
        //通过view和宽·高，构造popupWindow
        popupWindow = new PopupWindow(view, width, height);
        popupWindow.setFocusable(false);
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置window视图的位置
        popupWindow.showAsDropDown(targetView);
    }

    /**
     * PopupWindow选择提示框
     *
     * @param activity   当前Activity
     * @param targetView 与提示框关联的控件
     * @param width      提示框宽
     * @param height     提示框高
     * @param list       提示框内容列表
     * @param itemClick  列表点击事件
     */
    public static void showPopupWindow(Activity activity, View targetView, int width, int height,
                                       List<String> list, AdapterView.OnItemClickListener itemClick) {
        createPopupWindow(activity, targetView, DensityUtils.dp2px(activity, width),
                DensityUtils.dp2px(activity, height));

        ListView lvOptions = (ListView) view.findViewById(R.id.lv_options);
        popupWindowAdapter = new PopupWindowAdapter(activity, list);
        lvOptions.setAdapter(popupWindowAdapter);
        lvOptions.setOnItemClickListener(itemClick);
    }

    /**
     * 更新提示框
     *
     * @param selectedPosition 选中位置
     */
    public static void updatePopupWindow(int selectedPosition) {
        if (popupWindowAdapter != null) {
            popupWindowAdapter.setSelectedPosition(selectedPosition);
            popupWindowAdapter.notifyDataSetChanged();
        }
    }
}
