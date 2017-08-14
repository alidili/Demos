package com.yl.uiautomatordemo;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import junit.framework.TestCase;

/**
 * Created by yangle on 2017/8/14.
 */

public class UiTest extends TestCase {

    public void testA() throws Exception {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);
        Context context = instrumentation.getContext();

        // 如果当前为锁屏状态，解锁屏幕
        if (!uiDevice.isScreenOn()) {
            uiDevice.wakeUp();
            uiDevice.swipe(100, 100, 500, 500, 10);
        }

        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.yang.testdemo");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        UiObject seven = uiDevice.findObject(new UiSelector().resourceId("com.yang.testdemo:id/btn_text_choice_question"));
        seven.click();
        uiDevice.pressBack();
    }
}
