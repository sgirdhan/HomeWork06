package com.example.sharangirdhani.homework06;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class Course extends RealmObject {
    @PrimaryKey
    private String title;
    private String day;
    private int time_h;
    private int time_m;
    private String ampm;
    private int credit_hour;
    private String semester;
    private long instructorID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;

        Course course = (Course) o;
        return getTitle().equals(course.getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }

    public long getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(long instructorID) {
        this.instructorID = instructorID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getTime_h() {
        return time_h;
    }

    public void setTime_h(int time_h) {
        this.time_h = time_h;
    }

    public int getTime_m() {
        return time_m;
    }

    public void setTime_m(int time_m) {
        this.time_m = time_m;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public int getCredit_hour() {
        return credit_hour;
    }

    public void setCredit_hour(int credit_hour) {
        this.credit_hour = credit_hour;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Course{" +
                "title='" + title + '\'' +
                ", day=" + day +
                ", time_h=" + time_h +
                ", time_m=" + time_m +
                ", ampm='" + ampm + '\'' +
                ", credit_hour=" + credit_hour +
                ", semester='" + semester + '\'' +
                '}';
    }
}
