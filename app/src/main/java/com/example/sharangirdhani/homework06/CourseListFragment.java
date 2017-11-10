package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CourseListFragment extends Fragment implements CourseListAdapter.ICourseListAdapter{

    private OnFragmentInteractionListener mListener;
    View view;
    private ArrayList<Course> courseList;
    CourseListAdapter courseListAdapter;
    LinearLayoutManager layoutManager;
    ImageButton addCourseButton;
    TextView txtViewMessage;

    public CourseListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_course_list, container, false);
        courseList = (ArrayList<Course>) mListener.getCourseList();
        txtViewMessage = (TextView) view.findViewById(R.id.textViewMessage);
        addCourseButton = (ImageButton) view.findViewById(R.id.btnAddCourse);
        loadRecyclerView(courseList);

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToCreateCoursePage();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.updateTitleMain();
    }

    public void loadRecyclerView(ArrayList<Course> courseList) {
        if(courseList==null) {
            txtViewMessage.setVisibility(View.VISIBLE);
            return;
        }
        txtViewMessage.setVisibility(View.INVISIBLE);
        setAdapterAndNotify();
    }

    public void setAdapterAndNotify(){
        courseListAdapter = new CourseListAdapter(courseList, getContext(), CourseListFragment.this);
        RecyclerView recyclerViewCourse = ((RecyclerView) view.findViewById(R.id.courseRecycleView));
        recyclerViewCourse.setAdapter(courseListAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCourse.setLayoutManager(layoutManager);
        courseListAdapter.notifyDataSetChanged();
    }

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
    public void removeCourse(String title) {
        mListener.removeCourse(title);
    }

    @Override
    public void goToDisplayFragment(String title) {
        mListener.goTODisplayFragmentMain(title);
    }

    @Override
    public void displayMessage() {
        txtViewMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public Instructor fetchInstructor(long id) {
       return mListener.fetchInstructorDB(id);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void updateTitleMain();
        void goToCreateCoursePage();
        List<Course> getCourseList();
        List<Instructor> fetchInstructorsForUser();
        void removeCourse(String title);
        void goTODisplayFragmentMain(String title);
        Instructor fetchInstructorDB(long id);
    }
}
