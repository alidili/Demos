package com.yl.host;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.didi.virtualapk.PluginManager;

/**
 * 宿主
 * <p>
 * Created by yangle on 2018/7/24.
 * Website：http://www.yangle.tech
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start_plugin_activity).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (PluginManager.getInstance(this).getLoadedPlugin("com.yl.plugin") == null) {
            Toast.makeText(this, "Plugin is not loaded!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.setClassName("com.yl.plugin", "com.yl.plugin.PluginActivity");
            startActivity(intent);
        }
    }
}
