package com.yl.retrofitdemo.manager;

import com.yl.retrofitdemo.Constant;
import com.yl.retrofitdemo.bean.BaseBean;
import com.yl.retrofitdemo.bean.PostInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * 消息接收管理
 * Created by yangle on 2017/6/19.
 */

public class ReceiveMessageManager {

    private static ReceiveMessageManager manager;

    public static ReceiveMessageManager getInstance() {
        return manager == null ? manager = new ReceiveMessageManager() : manager;
    }

    private ReceiveMessageManager() {
    }

    /**
     * 分发消息
     *
     * @param baseBean Bean基类
     * @param url      请求地址
     */
    public void dispatchMessage(BaseBean baseBean, String url) {
        switch (url) {
            case Constant.UrlOrigin.get_post_info:
                PostInfo postInfo = (PostInfo) baseBean;
                EventBus.getDefault().post(postInfo);
                break;

            default:
                break;
        }
    }
}
