//
// Created by test on 2018/1/22.
//
#include "com_yl_ndkdemo_MainActivity.h"

JNIEXPORT jstring JNICALL Java_com_yl_ndkdemo_MainActivity_stringFromJNI
        (JNIEnv *env, jobject) {
    return env->NewStringUTF("Hello from C++");
}
