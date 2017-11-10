package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;


public class CreateCourseFragment extends Fragment implements InstructorAdapter.IInstructorAdapter{

    private EditText edtTitle;
    private Spinner spinnerDay;
    private Spinner spinnerTime;
    private EditText edtHours;
    private EditText edtMinutes;
    private Spinner spinnerSemester;
    private Button btnReset;
    private Button btnCreate;
    private RadioGroup rgGroupCredit;
    private TextView txtEmptyMessage;

    private RecyclerView recyclerViewInstructor;

    private View view;

    Realm realm;

    private Course course;
    private String title;
    private String day;
    private String time;
    private int hours;
    private int minutes;
    private String semester;
    private int creditHour;
    private int postitionOfInstructor;
    private Instructor instructor;
    List<Instructor> instructorsList;
    InstructorAdapter instructorAdapter;
    LinearLayoutManager layoutManager;
    private OnFragmentInteractionListener mListener;

    public CreateCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_course, container, false);
        initialize(view);
        reset(view);

        //RecyclerView
        if(instructorsList.size()!=0){
            setAdapterAndNotify(view);
        }

        //Spinner for Day
        ArrayAdapter<CharSequence> adapterDay =
                ArrayAdapter.createFromResource(getContext(),R.array.day,
                        android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapterDay);
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner for time
        ArrayAdapter<CharSequence> adapterTime =
                ArrayAdapter.createFromResource(getContext(),R.array.time,
                        android.R.layout.simple_spinner_item);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapterTime);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner for semester
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getContext(),R.array.semester,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(adapter);
        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //RadioGroup
        rgGroupCredit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.radioButton1:{
                        creditHour = 1;
                        break;
                    }
                    case R.id.radioButton2:{
                        creditHour = 2;
                        break;
                    }
                    case R.id.radioButton3:{
                        creditHour = 3;
                        break;
                    }
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFields();
                setCourseObject();

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                            {
                                reset(view);
                            }

                            break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to reset?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        return view;
    }

    private void reset(View view) {
        edtTitle.setText("");
        edtHours.setText("");
        edtMinutes.setText("");
        spinnerSemester.setSelection(0);
        spinnerDay.setSelection(0);
        spinnerTime.setSelection(0);
        rgGroupCredit.clearCheck();

        mListener.setCheckedFalseDB();
        if(instructorsList.size()!=0){
            setAdapterAndNotify(view);
        }
    }

    public void setAdapterAndNotify(View view){
        instructorAdapter = new InstructorAdapter((ArrayList<Instructor>) instructorsList, getContext(), CreateCourseFragment.this);
        RecyclerView recyclerViewInstructor = ((RecyclerView) view.findViewById(R.id.recyclerViewInstructor));
        recyclerViewInstructor.setAdapter(instructorAdapter);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewInstructor.setLayoutManager(layoutManager);
        instructorAdapter.notifyDataSetChanged();
    }

    private void setCourseObject() {
        if(!isTitleValid()){
            Toast.makeText(getContext(),"Title is not valid",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isCourseUnique()){
            Toast.makeText(getContext(),"Course title is not unique",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isRecyclerButtonSelected()){
            Toast.makeText(getContext(),"Please select an instructor",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isDayValid()){
            Toast.makeText(getContext(),"Please select Day",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isTimeValid()){
            Toast.makeText(getContext(),"Time is not valid",Toast.LENGTH_LONG).show();
            return;
        }


        hours= Integer.parseInt(edtHours.getText().toString().trim());
        if(hours<10){
            hours = Integer.parseInt("0"+edtHours.getText().toString().trim());
        }
        minutes = Integer.parseInt(edtMinutes.getText().toString().trim());

        if(!isRadioButtonClicked()){
            Toast.makeText(getContext(),"Please select the Credit hours",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isSemesterValid()){
            Toast.makeText(getContext(),"Please select Semester value",Toast.LENGTH_LONG).show();
            return;
        }

        instructor = instructorsList.get(postitionOfInstructor);
        course.setInstructorID(instructor.getId());
        course.setTime_h(hours);
        course.setTitle(title);
        course.setTime_m(minutes);
        course.setCredit_hour(creditHour);
        course.setAmpm(time);


        if(day.equals("Monday")){
            day="Mon";
        }
        else if(day.equals("Tuesday")){
            day="Tue";
        }
        else if(day.equals("Wednesday")){
            day="Wed";
        }
        else if(day.equals("Thursday")){
            day="Thu";
        }
        else if(day.equals("Friday")){
            day="Fri";
        }
        course.setDay(day);
        course.setSemester(semester);

        realm.beginTransaction();
        instructor.getCourses().add(course);
        realm.copyToRealmOrUpdate(instructor);
        realm.commitTransaction();

        mListener.goToCourseListPage();
    }

    private boolean isRadioButtonClicked(){
        if(creditHour==0){
            return false;
        }
        return true;
    }
        @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

    private boolean isSemesterValid(){
        if(semester==null || semester.equals("Select Semester")){
            return false;
        }
        return true;
    }

    private boolean isTitleValid(){
        if(title == null || title.isEmpty() || title.trim().equals("")){
            return false;
        }
        return true;
    }
    private boolean isCourseUnique() {
        List<Course> courseList = mListener.getCourseList();
        if (courseList == null) {
            return true;
        } else {
            for (Course course : courseList) {
                if (title.equals(course.getTitle())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isDayValid(){
        if(day==null || day.equals("Select")){
            return false;
        }
        return true;
    }

    private boolean isTimeValid(){
        if(edtHours.getText().toString().isEmpty()
                || edtHours.getText().toString().trim().equals("")
                || edtMinutes.getText().toString().trim().equals("")
                || edtMinutes.getText().toString().isEmpty()){

            return false;
        }
        return true;
    }

    private boolean isRecyclerButtonSelected(){
        if(!(postitionOfInstructor>=0)){
            return false;
        }
        return true;
    }

    private void initialize(View view){
        semester = null;
        time = null;
        day = null;
        postitionOfInstructor = -1;
        course = new Course();

        Realm.init(getContext());
        realm = Realm.getDefaultInstance();

        mListener.setCheckedFalseDB();
        edtTitle = (EditText) view.findViewById(R.id.editTextTitle);
        edtHours = (EditText) view.findViewById(R.id.editTextHours);
        edtMinutes = (EditText) view.findViewById(R.id.editTextMinutes);
        rgGroupCredit = (RadioGroup) view.findViewById(R.id.radioGroup);
        rgGroupCredit.clearCheck();
        btnCreate = (Button) view.findViewById(R.id.buttonCreate);
        btnReset = (Button) view.findViewById(R.id.buttonReset);
        txtEmptyMessage = (TextView) view.findViewById(R.id.textViewMessage);
        recyclerViewInstructor = ((RecyclerView) view.findViewById(R.id.recyclerViewInstructor));

        if(instructorsList.size()==0){
            recyclerViewInstructor.setVisibility(View.INVISIBLE);
            txtEmptyMessage.setVisibility(View.VISIBLE);
            btnCreate.setEnabled(false);
        }
        else{
            recyclerViewInstructor.setVisibility(View.VISIBLE);
            txtEmptyMessage.setVisibility(View.INVISIBLE);
            btnCreate.setEnabled(true);
        }

        edtHours.setFilters(new InputFilter[]{new InputFilterMinMax("1", "12")});
        edtMinutes.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});

        initializeSpinners(view);
    }

    private void initializeSpinners(View view){
        spinnerDay = (Spinner) view.findViewById(R.id.spinnerDay);
        spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
        spinnerSemester = (Spinner) view.findViewById(R.id.spinnerSemester);
        spinnerDay.setSelection(0);
        spinnerTime.setSelection(0);
        spinnerSemester.setSelection(0);
    }

    private void setFields(){
        title = edtTitle.getText().toString().trim();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        instructorsList = mListener.fetchInstructorsForUser();
        if(instructorsList.size()>0){
            setAdapterAndNotify(view);
            recyclerViewInstructor.setVisibility(View.VISIBLE);
            txtEmptyMessage.setVisibility(View.INVISIBLE);
            btnCreate.setEnabled(true);
        }
        else{
            btnCreate.setEnabled(false);
        }

        edtTitle.setText("");
        edtHours.setText("");
        edtMinutes.setText("");
        spinnerSemester.setSelection(0);
        spinnerDay.setSelection(0);
        spinnerTime.setSelection(0);
        rgGroupCredit.clearCheck();
        mListener.setCheckedFalseDB();
        mListener.updateTitleCreateCourse();
    }

    public void setInstructorsList(List<Instructor> ins) {
        instructorsList = ins;
    }

    @Override
    public void sendInstructorInformation(int position) {
        postitionOfInstructor = position;
    }

    @Override
    public void makeTextMessageVisible() {
        txtEmptyMessage.setVisibility(View.VISIBLE);
        recyclerViewInstructor.setVisibility(View.INVISIBLE);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void goToCourseListPage();
        List<Course> getCourseList();
        void updateTitleCreateCourse();
        void setCheckedFalseDB();
        void updateInstructor(Instructor instructor);
        List<Instructor> fetchInstructorsForUser();
    }
}