package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class InstructorListFragment extends Fragment implements InstructorListAdapter.IInstructorListAdapter{

    private OnFragmentInteractionListener mListener;
    List<Instructor> instructorsList;
    InstructorListAdapter instructorListAdapter;
    LinearLayoutManager layoutManager;
    View view;
    TextView txtViewMesage;
    List<Long> instructorIDList;
    RecyclerView recyclerViewInstructors;
    Realm realm;
    Instructor instructor;
    public InstructorListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_instructor_list, container, false);
        txtViewMesage = (TextView) view.findViewById(R.id.textViewMessage);
        recyclerViewInstructors = (RecyclerView) view.findViewById(R.id.recyclerViewInstructors);

        realm.init(getContext());
        realm = Realm.getDefaultInstance();
        instructor = new Instructor();
        if(instructorsList.size()!=0){
            setAdapterAndNotify(view);
        }
        if(instructorsList.size()>0){
            recyclerViewInstructors.setVisibility(View.VISIBLE);
            txtViewMesage.setVisibility(View.INVISIBLE);
            setAdapterAndNotify(view);
        }
        else{
            recyclerViewInstructors.setVisibility(View.INVISIBLE);
            txtViewMesage.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        instructorsList = mListener.fetchInstructorsForUser();

        if(instructorsList.size()>0){
            setAdapterAndNotify(view);
            txtViewMesage.setVisibility(View.INVISIBLE);
            recyclerViewInstructors.setVisibility(View.VISIBLE);
        }
        else{
            txtViewMesage.setVisibility(View.VISIBLE);
            recyclerViewInstructors.setVisibility(View.INVISIBLE);
        }
        mListener.updateTitleInsList();
    }

    private void populateList(){
        instructorIDList = (List<Long>) getArguments().getSerializable("current_user");

        for(long id:instructorIDList){
            instructor = mListener.fetchInstructorDB(id);
            instructorsList.add(instructor);
        }
    }

    public void setAdapterAndNotify(View view){
        instructorListAdapter = new InstructorListAdapter(instructorsList, getContext(), InstructorListFragment.this);
        RecyclerView recyclerViewInstructors = ((RecyclerView) view.findViewById(R.id.recyclerViewInstructors));
        recyclerViewInstructors.setAdapter(instructorListAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewInstructors.setLayoutManager(layoutManager);
        instructorListAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setInstructorList(List<Instructor> insList){
        instructorsList = insList;
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
    public void goToDisplayInstructorFragment(long id) {
        //go to display fragment
        mListener.goTODisplayInsFragment(id);
    }

    @Override
    public void removeInstructor(long id) {
        mListener.removeInstructorMain(id);
    }

    @Override
    public void makeWarningMessageVisible() {
        txtViewMesage.setVisibility(View.VISIBLE);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void removeInstructorMain(long id);
        void goTODisplayInsFragment(long id);
        void updateTitleInsList();
        List<Instructor> fetchInstructorsForUser();
        Instructor fetchInstructorDB(long id);
    }
}
