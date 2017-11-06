package com.example.sharangirdhani.homework06;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by sharangirdhani on 11/4/17.
 */

public class User extends RealmObject {

    @PrimaryKey
    private String          username;
    @Required
    private String          password;
    @Required
    private String          firstName;
    @Required
    private String          lastName;
    @Required
    private String          uri;

    public RealmList<Instructor> instructors;

    public RealmList<Instructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(RealmList<Instructor> instructors) {
        this.instructors = instructors;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
