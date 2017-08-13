package com.yl.uiautomatordemo;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import junit.framework.TestCase;

/**
 * Created by yangle on 2017/8/13.
 */

public class UiTest extends TestCase {

    public void testDemo() throws UiObjectNotFoundException {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        uiDevice.pressHome();
        UiObject ofo = uiDevice.findObject(new UiSelector().description("ofo共享单车"));

        ofo.clickAndWaitForNewWindow();

        UiObject seven = uiDevice.findObject(new UiSelector().resourceId("so.ofo.labofo:id/panel_about_begin"));
        seven.click();
        uiDevice.pressBack();

        UiObject seven1 = uiDevice.findObject(new UiSelector().resourceId("so.ofo.labofo:id/panel_about_begin"));
        seven1.click();
        uiDevice.pressBack();

        UiObject seven2 = uiDevice.findObject(new UiSelector().resourceId("so.ofo.labofo:id/panel_about_begin"));
        seven2.click();
        uiDevice.pressBack();

        UiObject seven3 =uiDevice.findObject(new UiSelector().resourceId("so.ofo.labofo:id/panel_about_begin"));
        seven3.click();
        uiDevice.pressBack();
    }

}
