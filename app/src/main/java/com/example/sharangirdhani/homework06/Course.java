package com.example.sharangirdhani.homework06;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by sharangirdhani on 11/4/17.
 */

public class Course extends RealmObject {
    @PrimaryKey
    private String          title;
    @Required
    private Integer          day;
    @Required
    private Integer          time_h;
    @Required
    private Integer          time_m;
    @Required
    private String          ampm;
    @Required
    private Integer          credit_hour;
    @Required
    private String          semester;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getTime_h() {
        return time_h;
    }

    public void setTime_h(Integer time_h) {
        this.time_h = time_h;
    }

    public Integer getTime_m() {
        return time_m;
    }

    public void setTime_m(Integer time_m) {
        this.time_m = time_m;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public Integer getCredit_hour() {
        return credit_hour;
    }

    public void setCredit_hour(Integer credit_hour) {
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
