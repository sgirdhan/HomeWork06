package com.example.sharangirdhani.homework06;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        CourseListFragment.OnFragmentInteractionListener,
        CreateCourseFragment.OnFragmentInteractionListener,
        AddInstructorFragment.OnFragmentInteractionListener {
    Realm realm;
    LoginFragment lf;
    RegisterFragment rf;
    CourseListFragment clf;
    AddInstructorFragment af;
    CreateCourseFragment ccf;

    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentUser = null;
        realm.init(this);
        realm = Realm.getDefaultInstance();
        lf = new LoginFragment();
        rf = new RegisterFragment();
        clf = new CourseListFragment();
        af = new AddInstructorFragment();
        ccf = new CreateCourseFragment();

        updateTitleMain();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainLayout, lf)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public User viewOne(String userName) {
        return realm.where(User.class).equalTo("username", userName).findFirst();
    }

    @Override
    public void goToSignUpPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, rf)
                .addToBackStack(null)
                .commit();
        updateTitleSignup();
    }

    public void goToLogInPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, lf)
                .commit();
        updateTitleMain();
    }

    @Override
    public void goToCourseListPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, clf)
                .commit();
        updateTitleMain();
    }

    public void goToAddInstructorPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, af)
                .addToBackStack(null)
                .commit();
        updateTitleAddInstructor();
    }

    public void goToCreateCoursePage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, ccf)
                .addToBackStack(null)
                .commit();
        updateTitleCreateCourse();
    }

    @Override
    public void updateTitleSignup() {
        getSupportActionBar().setTitle("Register");
    }

    @Override
    public void setIsLoggedIn(User user) {
        currentUser = user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @Override
    public void updateTitleMain() {
        getSupportActionBar().setTitle("Course Manager");
    }

    public void updateTitleAddInstructor() {
        getSupportActionBar().setTitle("Add Instructor");
    }

    public void updateTitleCreateCourse() {
        getSupportActionBar().setTitle("Create Course");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:{
                if(isLoggedIn()) {
                    goToCourseListPage();
                }
                else {
                    goToLogInPage();
                }
                return false;
            }
            case R.id.instructor:{
                return false;
            }
            case R.id.add_instructor:{
                if(isLoggedIn()) {
                    goToAddInstructorPage();
                }
                else {
                    goToLogInPage();
                }
                break;
            }
            case R.id.logout:
            {
                currentUser = null;
                goToLogInPage();
                break;
            }
            case R.id.exit:
            {
                this.finishAffinity();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addUser(User user) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
        Toast.makeText(this, "User successfully added.",Toast.LENGTH_LONG).show();
    }

    @Override
    public void addInstructor(Instructor instructor) {
        Number maxValue = realm.where(Instructor.class).max("id");
        int pk = (maxValue != null) ? (int)maxValue + 1 : 0;
        realm.beginTransaction();
        instructor.setId(pk);
        currentUser.getInstructors().add(instructor);
        realm.copyToRealmOrUpdate(instructor);
        realm.copyToRealmOrUpdate(currentUser);
        realm.commitTransaction();
        Toast.makeText(this, "Instructor successfully added.",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isUniqueUsername(String username) {
        if(viewOne(username) == null)
            return true;
        else {
            return false;
        }
    }
}
