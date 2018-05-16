package com.yl.ndklibrary;

/**
 * NDKLibrary
 * <p>
 * Created by yangle on 2018/5/16.
 * Website：http://www.yangle.tech
 */

public class NDKLibrary {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();
}
