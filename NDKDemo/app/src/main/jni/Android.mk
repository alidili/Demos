# 当前路径
LOCAL_PATH := $(call my-dir)

# 清除LOCAL_XXX变量
include $(CLEAR_VARS)

# 原生库名称
LOCAL_MODULE := native-lib

# 原生代码文件
LOCAL_SRC_FILES =: native-lib.cpp

# 编译动态库
include $(BUILD_SHARED_LIBRARY)