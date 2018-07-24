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
        PluginManager.getInstance(base).init();
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
