package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.IOException;

import io.realm.RealmList;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment {

    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtUserName;
    private EditText edtPassword;
    private ImageButton icon;
    private Button register;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    private User user;

    private Uri photoURI;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        edtFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        edtLastName = (EditText) view.findViewById(R.id.editTextLastName);
        edtUserName = (EditText) view.findViewById(R.id.editTextUserName);
        edtPassword = (EditText) view.findViewById(R.id.editTextPassword);
        icon = (ImageButton) view.findViewById(R.id.imagebuttonAvatar);
        register = (Button) view.findViewById(R.id.buttonRegister);
        photoURI = null;
        initializeFields();

        setHasOptionsMenu(true);

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeFields();

                if(!validation()){
                    Toast.makeText(getContext(),"Please provide/correct all the inputs",Toast.LENGTH_LONG).show();
                    return;
                } else if(!mListener.isUniqueUsername(userName)) {
                    Toast.makeText(getContext(),"Username already exists",Toast.LENGTH_LONG).show();
                    return;
                }
                else{

                    setUserAttributes();
                    edtFirstName.setText("");
                    edtLastName.setText("");
                    edtUserName.setText("");
                    edtPassword.setText("");
                    photoURI = null;
                    icon.setImageResource(R.drawable.default_cam);
                    edtFirstName.requestFocus();
                    mListener.goToCourseListPage();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.updateTitleSignup();
    }

    private void setUserAttributes(){
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setUsername(userName);
        user.setUri(String.valueOf(photoURI));
        user.setInstructors(new RealmList<Instructor>());
        mListener.addUser(user);
        Toast.makeText(getContext(), "User successfully registered.",Toast.LENGTH_LONG).show();
        mListener.setIsLoggedIn(user);
    }

    private void initializeFields(){
        firstName = edtFirstName.getText().toString();
        lastName = edtLastName.getText().toString();
        userName = edtUserName.getText().toString();
        password = edtPassword.getText().toString();
        user = new User();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.getItem(0).setEnabled(false);
        menu.getItem(1).setEnabled(false);
        menu.getItem(2).setEnabled(false);
        menu.getItem(3).setEnabled(false);
//        mListener.menuManagement(menu);
    }

    boolean validation(){
        if(firstName.trim().equals("") ||
                firstName.isEmpty() || lastName.trim().equals("") || lastName.isEmpty() ||
                userName.trim().equals("") || userName.isEmpty() || password.trim().equals("")
                || password.isEmpty() || password.length()<8 || photoURI==null){
            return false;
        }
        return true;
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
        void addUser(User user);
        boolean isUniqueUsername(String username);
        void goToCourseListPage();
        void setIsLoggedIn(User user);
        void updateTitleSignup();
        void menuManagement(Menu menu);
    }
}