package com.example.sharangirdhani.homework06;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.List;
import io.realm.Realm;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        CourseListFragment.OnFragmentInteractionListener,
        CreateCourseFragment.OnFragmentInteractionListener,
        AddInstructorFragment.OnFragmentInteractionListener,
        DisplayCourseFragment.OnFragmentInteractionListener,
        DisplayInstructorFragment.OnFragmentInteractionListener,
        InstructorListFragment.OnFragmentInteractionListener{

    private Realm realm;
    Menu myMenu;
    private List<Long> instructorsIDList;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private CourseListFragment courseListFragment;
    private AddInstructorFragment addInsFragment;
    private CreateCourseFragment createCourseFragment;
    private DisplayCourseFragment displayCourseFragment;
    private DisplayInstructorFragment displayInstructorFragment;
    private InstructorListFragment instructorListFragment;
    private User currentUser;
    DbUtilClass dbUtilClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentUser = null;
        dbUtilClass = new DbUtilClass(MainActivity.this);

        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        courseListFragment = new CourseListFragment();
        addInsFragment = new AddInstructorFragment();
        createCourseFragment = new CreateCourseFragment();
        instructorListFragment = new InstructorListFragment();

        updateTitleMain();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainLayout, loginFragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void removeInstructorMain(long id) {

        dbUtilClass.removeInstructorMain(id);
    }

    @Override
    public void goTODisplayInsFragment(long id) {
        displayInstructorFragment = new DisplayInstructorFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("instructor_id", id);
        displayInstructorFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, displayInstructorFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void updateTitleInsDisplay() {
        getSupportActionBar().setTitle("Instructor Details");
    }

    @Override
    public User viewOne(String userName) {
        return dbUtilClass.viewOne(userName);
    }

    @Override
    public void goToSignUpPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, registerFragment)
                .addToBackStack("signup")
                .commit();
    }

    public void goToLogInPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, loginFragment)
                .addToBackStack("login")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            String fragmentTag = getTopFragmentTag();
            if(fragmentTag!=null){
                if(fragmentTag.equals("login")){
                    clearStack();
                    currentUser = null;
                }
                if(fragmentTag.equals("signup")){
                    currentUser = null;
                }
            }
        }
        else {
            super.onBackPressed();
        }
    }

    public String getTopFragmentTag() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return fragmentTag;
    }

    @Override
    public void goToCourseListPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, courseListFragment)
                .addToBackStack("course_list")
                .commit();
    }

    @Override
    public void updateTitleInsList() {
        getSupportActionBar().setTitle("Instructor Manager");
    }

    public void goToAddInstructorPage() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, new AddInstructorFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToCreateCoursePage() {
        createCourseFragment.setInstructorsList(fetchInstructorsForUser());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, createCourseFragment)
                .addToBackStack("create_course")
                .commit();
    }

    @Override
    public List<Instructor> fetchInstructorsForUser(){
        return dbUtilClass.fetchInstructorsForUser();
    }

    @Override
    public void removeCourse(String title) {
        dbUtilClass.removeCourse(title);
    }

    @Override
    public void goTODisplayFragmentMain(String title) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        displayCourseFragment = new DisplayCourseFragment();
        displayCourseFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, displayCourseFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public Instructor fetchInstructorDB(long id) {
        return dbUtilClass.fetchInstructorDB(id);
    }

    @Override
    public void updateTitleCurseDetail() {
        getSupportActionBar().setTitle("Course Details");
    }

    @Override
    public Course getCourse(String title) {
        return dbUtilClass.fetchCourseDB(title);
    }


    @Override
    public void updateTitleSignup() {
        getSupportActionBar().setTitle("Register");
    }

    @Override
    public void setIsLoggedIn(User user) {
        currentUser = user;
        dbUtilClass.setCurrentUser(currentUser);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @Override
    public void updateTitleMain() {
        getSupportActionBar().setTitle("Course Manager");
    }

    @Override
    public void setCheckedFalseDB(){
        dbUtilClass.setCheckedFalseDB();
    }

    @Override
    public void updateInstructor(Instructor instructor) {
        dbUtilClass.updateInstructor(instructor);
    }

    @Override
    public List<Course> getCourseList() {
        return dbUtilClass.getCourseList();
    }

    @Override
    public void updateTitleAddInstructor() {
        getSupportActionBar().setTitle("Add Instructor");
    }

    @Override
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        myMenu = menu;
//        menuManagement(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:{

                goToCourseListPage();
                return false;
            }
            case R.id.instructor:{

                goToInstructoristPage();

                return false;
            }
            case R.id.add_instructor:{
                goToAddInstructorPage();

                break;
            }
            case R.id.logout:
            {
                clearStack();
                currentUser = null;
                goToLogInPage();
                break;
            }
            case R.id.exit:
            {
                currentUser = null;
                this.finishAffinity();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearStack(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            int size = getSupportFragmentManager().getBackStackEntryCount();
            for(int i=size;i>0;i--){
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        getSupportFragmentManager().popBackStack(null, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void menuManagement(Menu menu){
        for(int i =0;i<4;i++){
            menu.getItem(i).setEnabled(isLoggedIn());
        }
    }

    private void goToInstructoristPage() {
        instructorListFragment.setInstructorList(fetchInstructorsForUser());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, instructorListFragment)
                .addToBackStack("instructorList")
                .commit();
    }

    @Override
    public void addUser(User user) {
        dbUtilClass.addUser(user);
    }

    @Override
    public void addInstructor(Instructor instructor) {
        dbUtilClass.addInstructor(instructor);
    }

    @Override
    public void goToInstructorListPage() {
        goToInstructoristPage();
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
