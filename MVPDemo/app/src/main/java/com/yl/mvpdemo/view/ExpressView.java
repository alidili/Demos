package com.yl.mvpdemo.view;

import com.yl.mvpdemo.bean.ExpressInfo;

/**
 * Created by yangle on 2017/6/27.
 */

public interface ExpressView extends BaseView {

    /**
     * 更新UI
     *
     * @param expressInfo 快递信息
     */
    void updateView(ExpressInfo expressInfo);
}
