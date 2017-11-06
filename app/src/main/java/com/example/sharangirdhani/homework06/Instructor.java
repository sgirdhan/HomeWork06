package com.example.sharangirdhani.homework06;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by sharangirdhani on 11/4/17.
 */

public class Instructor extends RealmObject {

    @PrimaryKey
    private Integer         id;
    @Required
    private String          firstName;
    @Required
    private String          lastName;
    @Required
    private String          uri;
    @Required
    private String          email;
    @Required
    private String          personalWebsite;
    @Ignore
    public Boolean          isChecked;

    RealmList<Course> courses;

    public RealmList<Course> getCourses() {
        return courses;
    }

    public void setCourses(RealmList<Course> courses) {
        this.courses = courses;
    }

    public String getPersonalWebsite() {
        return personalWebsite;
    }

    public void setPersonalWebsite(String personalWebsite) {
        this.personalWebsite = personalWebsite;
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", uri='" + uri + '\'' +
                ", email='" + email + '\'' +
                ", course size='" + courses.size() + '\'' +
                ", personalWebsite='" + personalWebsite + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
