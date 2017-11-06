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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmList;

import static android.app.Activity.RESULT_OK;


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

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Uri photoURI;
    private int counterImage=0;
    private boolean imageSelected;

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
        imageSelected = false;
        counterImage=50;
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
                    if(!imageSelected){
                        photoURI = Uri.parse("android.resource://com.example.sharangirdhani.homework06/drawable/dimage");
                    }
                    setInstructorAttributes();
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
        edtFirstName.setText("");
        edtLastName.setText("");
        edtEmail.setText("");
        edtWebsite.setText("");
        icon.setImageResource(R.drawable.default_cam);
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
        mListener.goToCourseListPage();

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
                || website.isEmpty() || photoURI.toString().trim().isEmpty() || photoURI.toString().trim().equals("")){
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

        String imageFileName = "JPEG_instructor"+ counterImage + "_";
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
        void addInstructor(Instructor instructor);
        void goToCourseListPage();
    }
}