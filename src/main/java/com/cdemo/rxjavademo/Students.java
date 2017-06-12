package com.cdemo.rxjavademo;

/**
 * Created by yangdi on 2017/6/7.
 */

public class Students {

    private String name;
    //  课程
    private Course[] course;

    public Students(String name, Course[] course) {
        this.name = name;
        this.course = course;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(Course[] course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public Course[] getCourse() {
        return course;
    }
}
