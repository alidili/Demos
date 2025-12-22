cmake_minimum_required(VERSION 3.18.1)

# 定义工程名
project(native_shm)

# 编译成一个共享库libnative_shm.so
add_library(
        native_shm
        SHARED
        native_shm.cpp
)

# 查找Android系统日志库（__android_log_print）
find_library(
        log-lib
        log
)

# 链接系统库
target_link_libraries(
        native_shm
        ${log-lib}
)