package com.yl.databindingdemo.utils;

/**
 * Created by yangle on 2017/7/19.
 */

public class StringUtils {

    public static String capitalize(String word) {
        if (word.length() > 0) {
            return word.toUpperCase();
        }
        return word;
    }
}
