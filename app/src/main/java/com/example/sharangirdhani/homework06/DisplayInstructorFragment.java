package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import io.realm.Realm;

public class DisplayInstructorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView imgIcon;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtWebsite;
    private Realm realm;
    private long instructorId;
    public DisplayInstructorFragment() {
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
        View view = inflater.inflate(R.layout.fragment_display_instructor, container, false);

        //Initialise realm
        realm.init(getContext());
        realm = Realm.getDefaultInstance();

        instructorId = getArguments().getLong("instructor_id");
        final Instructor instructor = mListener.fetchInstructorDB(instructorId);
        //set fields
        txtName = (TextView) view.findViewById(R.id.textViewEmail);
        txtEmail= (TextView) view.findViewById(R.id.textViewInsName);
        txtWebsite = (TextView) view.findViewById(R.id.textViewInsWebsite);
        imgIcon = (ImageView) view.findViewById(R.id.imageViewInsIcon);


        //set Fields
        if (instructor != null) {
            txtName.setText(instructor.getFirstName() + "" + instructor.getLastName());
            txtEmail.setText(instructor.getEmail());
            txtWebsite.setText(instructor.getPersonalWebsite());
        }

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(instructor.getUri()));
        } catch (IOException e) {}

        imgIcon.setImageBitmap(bitmap);

        //Clickable URL
        txtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = instructor.getPersonalWebsite();
                if (!urlString.startsWith("http://") && !urlString.startsWith("https://"))
                    urlString = "http://" + urlString;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.updateTitleInsDisplay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void updateTitleInsDisplay();


        Instructor fetchInstructorDB(long instructorId);
    }
}
