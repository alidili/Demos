package com.yl.host;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.didi.virtualapk.PluginManager;

import java.io.File;

/**
 * Application
 * <p>
 * Created by yangle on 2018/7/24.
 * Website：http://www.yangle.tech
 */

public class VirtualAPKHostApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 初始化VirtualAPK
        PluginManager.getInstance(base).init();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 加载存储根目录的插件apk，实际项目中按需保存
        String pluginPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/plugin.apk");
        File plugin = new File(pluginPath);
        if (plugin.exists()) {
            try {
                PluginManager.getInstance(this).loadPlugin(plugin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
