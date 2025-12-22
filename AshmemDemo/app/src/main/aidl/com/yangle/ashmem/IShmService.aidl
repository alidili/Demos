// ISharedMemoryService.aidl
package com.yangle.ashmem;

import com.yangle.ashmem.IShmCallback;

// Declare any non-default types here with import statements

interface IShmService {
    /**
     * 开始传输
     *
     * @params callback 回调
     */
    void startTransfer(in IShmCallback callback);
}