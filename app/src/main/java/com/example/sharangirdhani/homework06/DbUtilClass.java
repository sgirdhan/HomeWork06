package com.example.sharangirdhani.homework06;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class DbUtilClass {
    private Realm realm;
    User currentUser;

    public DbUtilClass(Context context){
        realm.init(context);
        realm = Realm.getDefaultInstance();
//        currentUser = user;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void removeInstructorMain(long id) {
        realm.beginTransaction();
        Instructor ins = realm.where(Instructor.class).equalTo("id", id).findFirst();

        ins.deleteFromRealm();
        realm.commitTransaction();
    }

    public void removeCourse(String title) {
        realm.beginTransaction();
        Course course = realm.where(Course.class).equalTo("title", title).findFirst();
        course.deleteFromRealm();
        realm.commitTransaction();
    }

    public User viewOne(String userName) {
        return realm.where(User.class).equalTo("username", userName).findFirst();
    }

    public void addInstructor(Instructor instructor) {
        Number maxValue = realm.where(Instructor.class).max("id");
        long pk = (maxValue != null) ? (long)maxValue + 1 : 0;
        realm.beginTransaction();
        instructor.setId(pk);
        currentUser.getInstructors().add(instructor);
        realm.copyToRealmOrUpdate(instructor);
        realm.copyToRealmOrUpdate(currentUser);
        realm.commitTransaction();
    }

    public void addUser(User user) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public Instructor fetchInstructorDB(long id){
        return realm.where(Instructor.class).equalTo("id", id).findFirst();
    }

    public void setCheckedFalseDB(){
        List<Instructor> instructorsList = fetchInstructorsForUser();
        realm.beginTransaction();
        for(Instructor ins :instructorsList){
            ins.setChecked(false);
            realm.copyToRealmOrUpdate(ins);
        }
        realm.commitTransaction();
    }

    public List<Course> getCourseList() {
        boolean flag = false;
        List<Course> courseList = new ArrayList<>();
        List<Instructor> instructorList = fetchInstructorsForUser();
        for(Instructor insCourse:instructorList){
            if(insCourse.getCourses().size()!=0){
                flag = true;
                for(Course course:insCourse.getCourses()){
                    courseList.add(course);
                }
            }
        }
        if(flag){
            return courseList;
        }
        return null;
    }

    public List<Instructor> fetchInstructorsForUser(){
        List<Instructor> instructorList = new ArrayList<>();
        if(currentUser.getInstructors().size()>0) {
            for (Instructor ins : currentUser.getInstructors()) {
                instructorList.add(ins);
            }
        }
        return instructorList;
    }

    public Course fetchCourseDB(String title){
        return realm.where(Course.class).equalTo("title", title).findFirst();

    }

    public void updateInstructor(Instructor instructor){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(instructor);
        realm.commitTransaction();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        realm.close();
    }
}
