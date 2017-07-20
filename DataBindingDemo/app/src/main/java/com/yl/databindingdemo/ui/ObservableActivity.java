package com.yl.databindingdemo.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yl.databindingdemo.R;
import com.yl.databindingdemo.bean.ObservableFieldsUser;
import com.yl.databindingdemo.bean.ObservableObjectsUser;
import com.yl.databindingdemo.databinding.ActivityObservableBinding;

/**
 * 同步更新
 * Created by yangle on 2017/7/20.
 */

public class ObservableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityObservableBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_observable);

        // Observable Objects
        final ObservableObjectsUser user = new ObservableObjectsUser("容华", "谢后");

        // ObservableFields
        //final ObservableFieldsUser user = new ObservableFieldsUser("容华", "谢后");

        // Observable Collections
        //final ObservableArrayMap<String, String> user = new ObservableArrayMap<>();
        //user.put("firstName", "容华");
        //user.put("lastName", "谢后");

        binding.setUser(user);

        binding.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Observable Objects
                user.setFirstName("空谷");
                user.setLastName("幽兰");

                // ObservableFields
                //user.firstName.set("空谷");
                //user.lastName.set("幽兰");

                // Observable Collections
                //user.put("firstName", "空谷");
                //user.put("lastName", "幽兰");
            }
        });
    }
}
