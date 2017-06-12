package com.cdemo.rxjavademo;

/**
 * Created by yangdi on 2017/6/7.
 */

public class Course {

    private String time;
    private String name;

    public Course(String name) {
        this.name = name;
    }

    public Course(String time, String name) {
        this.time = time;
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }
}
