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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Uri photoURI;
    private int counterImage=0;
    private boolean imageSelected;

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
        imageSelected = false;
        counterImage=20;
        initializeFields();

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {

                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        photoURI = FileProvider.getUriForFile(getContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
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
                    if(!imageSelected){
                        photoURI = Uri.parse("android.resource://com.example.sharangirdhani.homework06/drawable/dimage");
                    }
                    setUserAttributes();
                }
            }
        });
        return view;
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
        mListener.goToCourseListPage();

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

    boolean validation(){
        if(firstName.trim().equals("") ||
                firstName.isEmpty() || lastName.trim().equals("") || lastName.isEmpty() ||
                userName.trim().equals("") || userName.isEmpty() || password.trim().equals("")
                || password.isEmpty() || password.length()<8){
            return false;
        }
        return true;
    }

    /*==============================================================================================
    Image related functions Begin
    ==============================================================================================*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            icon.setImageBitmap(bitmap);
            imageSelected = true;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = "JPEG_user"+ counterImage + "_";
        counterImage++;
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    /*==============================================================================================
    Image related functions End
    ==============================================================================================*/

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void addUser(User user);
        boolean isUniqueUsername(String username);
        void goToCourseListPage();
        void setIsLoggedIn(User user);
    }
}