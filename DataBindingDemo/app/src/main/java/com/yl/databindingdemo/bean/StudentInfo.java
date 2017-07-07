package com.yl.databindingdemo.bean;

/**
 * 学生信息
 * Created by yangle on 2017/7/7.
 */

public class StudentInfo {

    private String name;
    private int age;

    public StudentInfo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
