package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    View view;
    EditText edtUsername;
    EditText edtPassword;
    String username, password;
    Menu myMenu;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        edtUsername = (EditText) view.findViewById(R.id.edtUsername);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        username = "";
        password = "";

        setHasOptionsMenu(true);

        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                if(username.equals("") || username.isEmpty()){
                    Toast.makeText(getContext(),"Please enter the Username",Toast.LENGTH_LONG).show();
                }
                else if(password.equals("") || password.isEmpty()){
                    Toast.makeText(getContext(),"Please enter the Password",Toast.LENGTH_LONG).show();
                }
                else{
                    User user = mListener.viewOne(username);
                    if (user != null) {
                        if(user.getPassword().equals(password)) {
                            Toast.makeText(getContext(),"Login Successful",Toast.LENGTH_LONG).show();
                            edtPassword.setText("");
                            edtUsername.setText("");
                            mListener.setIsLoggedIn(user);
                            mListener.goToCourseListPage();
                        }
                        else {
                            Toast.makeText(getContext(),"Wrong Password",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(),"Username not found in the database.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        view.findViewById(R.id.tvSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSignUpPage();
            }
        });
        return view;
    }

    void resetScreen() {
        edtUsername.setText("");
        edtPassword.setText("");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.actions, menu);
        myMenu = menu;
//        mListener.menuManagement(menu);
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

    @Override
    public void onResume() {
        super.onResume();
        resetScreen();
        mListener.updateTitleMain();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        User viewOne(String userName);
        void goToSignUpPage();
        void goToCourseListPage();
        void updateTitleMain();
        void setIsLoggedIn(User user);
        void menuManagement(Menu menu);
    }
}
