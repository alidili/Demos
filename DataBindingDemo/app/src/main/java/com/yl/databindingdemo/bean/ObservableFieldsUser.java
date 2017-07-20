package com.yl.databindingdemo.bean;

import android.databinding.ObservableField;

/**
 * Created by yangle on 2017/7/20.
 */

public class ObservableFieldsUser {

    public ObservableField<String> firstName = new ObservableField<>();
    public ObservableField<String> lastName = new ObservableField<>();

    public ObservableFieldsUser(String firstName, String lastName) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
    }
}
