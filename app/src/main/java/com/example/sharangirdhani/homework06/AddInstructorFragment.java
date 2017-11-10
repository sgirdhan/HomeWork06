package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmList;


public class AddInstructorFragment extends Fragment {
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private EditText edtWebsite;
    private ImageButton icon;
    private Button btnAdd;
    private Button btnReset;

    private String firstName;
    private String lastName;
    private String email;
    private String website;
    private Instructor instructor;
    private Uri photoURI;

    private AddInstructorFragment.OnFragmentInteractionListener mListener;

    public AddInstructorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_instructor, container, false);
        edtFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        edtLastName = (EditText) view.findViewById(R.id.editTextLastName);
        edtEmail = (EditText) view.findViewById(R.id.editTextEmail);
        edtWebsite = (EditText) view.findViewById(R.id.editTextWebsite);
        icon = (ImageButton) view.findViewById(R.id.imagebuttonAvatar);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        photoURI = null;
        initializeFields();

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                photoURI = r.getUri();
                                icon.setImageURI(null);
                                icon.setImageURI(r.getUri());
                            }
                        }).show(getActivity().getSupportFragmentManager());

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeFields();
                if(!validation()){
                    Toast.makeText(getContext(),"Please provide/correct all the inputs",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(!isEmailValid(email)) {
                    Toast.makeText(getContext(),"Invalid Email",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    setInstructorAttributes();
                    edtFirstName.setText("");
                    edtLastName.setText("");
                    edtEmail.setText("");
                    edtWebsite.setText("");
                    icon.setImageResource(R.drawable.default_cam);
                    //setfocus
                    edtFirstName.requestFocus();
                    Toast.makeText(getContext(),"Instructor has been successfully added",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

        return view;
    }

    private void resetFields() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                    {
                        edtFirstName.setText("");
                        edtLastName.setText("");
                        edtEmail.setText("");
                        edtWebsite.setText("");
                        icon.setImageResource(R.drawable.default_cam);
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
    private void setInstructorAttributes(){
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);
        instructor.setEmail(email);
        instructor.setUri(String.valueOf(photoURI));
        instructor.setPersonalWebsite(website);
        instructor.setChecked(false);
        instructor.setCourses(new RealmList<Course>());
        mListener.addInstructor(instructor);
    }
    private void initializeFields(){
        firstName = edtFirstName.getText().toString();
        lastName = edtLastName.getText().toString();
        email = edtEmail.getText().toString();
        website = edtWebsite.getText().toString();
        instructor = new Instructor();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    boolean validation(){
        if(firstName.trim().equals("") ||
                firstName.isEmpty() || lastName.trim().equals("") || lastName.isEmpty() ||
                email.trim().equals("") || email.isEmpty() || website.trim().equals("")
                || website.isEmpty() || photoURI==null){
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.updateTitleAddInstructor();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragment.OnFragmentInteractionListener) {
            mListener = (AddInstructorFragment.OnFragmentInteractionListener) context;
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
        void addInstructor(Instructor instructor);
        void goToInstructorListPage();
        void updateTitleAddInstructor();
    }
}