package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayCourseFragment extends Fragment {

    private TextView txtCourseTitle;
    private TextView txtInsTitle;
    private TextView txtSchedule;
    private TextView txtSemester;
    private String title;
    private Course course;
    private long instructorID;

    private OnFragmentInteractionListener mListener;

    public DisplayCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_course, container, false);
        //Initialise variables
        instructorID = 0;
        course = new Course();

        //retrive the title for course
        title = getArguments().getString("title");

        //Get Course from the database
        course = mListener.getCourse(title);

        //Fetch Instructor's details from the database
        instructorID = course.getInstructorID();
        Instructor instructor = mListener.fetchInstructorDB(instructorID);
        String instructorName = instructor.getFirstName()+""+instructor.getLastName();

        //Initialise the fields
        txtCourseTitle = (TextView) view.findViewById(R.id.textViewEmail);
        txtSchedule = (TextView) view.findViewById(R.id.textViewInsWebsite);
        txtInsTitle = (TextView) view.findViewById(R.id.textViewInsName);
        txtSemester = (TextView) view.findViewById(R.id.textViewSemester);
        txtInsTitle = (TextView) view.findViewById(R.id.textViewInsName);

        //set fields
        txtCourseTitle.setText(title);
        txtSemester.setText(course.getSemester());
        txtSchedule.setText((course.getDay()+" "+course.getTime_h()+
                ":"+ course.getTime_m()+course.getAmpm()));
        txtSemester.setText(course.getSemester());
        txtInsTitle.setText(instructorName);

        return view;
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
        mListener.updateTitleCurseDetail();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void updateTitleCurseDetail();

        Course getCourse(String title);

        Instructor fetchInstructorDB(long instructorID);
    }
}
