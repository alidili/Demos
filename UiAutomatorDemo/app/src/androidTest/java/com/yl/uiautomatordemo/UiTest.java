package com.yl.uiautomatordemo;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import junit.framework.TestCase;

/**
 * 测试用例
 * Created by yangle on 2017/8/14.
 * <p>
 * Website：http://www.yangle.tech
 * GitHub：https://github.com/alidili
 * CSDN：http://blog.csdn.net/kong_gu_you_lan
 * JianShu：http://www.jianshu.com/u/34ece31cd6eb
 */

public class UiTest extends TestCase {

    /**
     * 测试CollapsingToolbarLayout
     * 被测Demo下载地址：https://github.com/alidili/DesignSupportDemo
     *
     * @throws UiObjectNotFoundException
     */
    public void testA() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);
        // 获取上下文
        Context context = instrumentation.getContext();

        // 启动测试App
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.yang.designsupportdemo");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // 打开CollapsingToolbarLayout
        String resourceId = "com.yang.designsupportdemo:id/CollapsingToolbarLayout";
        UiObject collapsingToolbarLayout = uiDevice.findObject(new UiSelector().resourceId(resourceId));
        collapsingToolbarLayout.click();

        for (int i = 0; i < 5; i++) {
            // 向上移动
            uiDevice.swipe(uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayHeight(),
                    uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayHeight() / 2, 10);

            // 向下移动
            uiDevice.swipe(uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayHeight() / 2,
                    uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayHeight(), 10);
        }

        // 点击应用返回按钮
        UiObject back = uiDevice.findObject(new UiSelector().description("Navigate up"));
        back.click();

        // 点击设备返回按钮
        uiDevice.pressBack();
    }

    /**
     * 滑动界面，打开About phone选项
     * 测试环境为标准Android 7.1.1版本，不同设备控件查找方式会有不同
     *
     * @throws UiObjectNotFoundException
     */
    public void testB() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);
        // 获取上下文
        Context context = instrumentation.getContext();

        // 点击Settings按钮
        UiObject uiObject = uiDevice.findObject(new UiSelector().description("Settings"));
        uiObject.click();

        // 滑动列表到最后，点击About phone选项
        UiScrollable settings = new UiScrollable(new UiSelector().className("android.support.v7.widget.RecyclerView"));
        UiObject about = settings.getChildByText(new UiSelector().className("android.widget.LinearLayout"), "About phone");
        about.click();

        // 点击设备返回按钮
        uiDevice.pressBack();
        uiDevice.pressBack();
    }
}
