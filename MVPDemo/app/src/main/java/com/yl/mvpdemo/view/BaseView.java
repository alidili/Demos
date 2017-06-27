package com.yl.mvpdemo.view;

/**
 * View基类
 * Created by yangle on 2017/6/27.
 */

public interface BaseView {

    /**
     * 显示Loading
     */
    void showProgressDialog();

    /**
     * 隐藏Loading
     */
    void hideProgressDialog();

    /**
     * 显示错误信息
     *
     * @param msg 错误信息
     */
    void showError(String msg);
}
