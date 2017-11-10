package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;


public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseRecyclerViewHolder> {
    private List<Course> coursesList;
    private Context context;
    private ICourseListAdapter iCourseListAdapter;

    public CourseListAdapter(List<Course> coursesList, Context context, ICourseListAdapter iCourseListAdapter) {
        this.coursesList = coursesList;
        this.context = context;
        this.iCourseListAdapter = iCourseListAdapter;
    }

    @Override
    public CourseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.course_row,parent,false);
        CourseRecyclerViewHolder courseRecyclerViewHolder = new CourseRecyclerViewHolder(view);
        return courseRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(CourseListAdapter.CourseRecyclerViewHolder holder, int position) {
        final Course course = coursesList.get(position);
        holder.txtCourseTitle.setText(course.getTitle());
        long id = course.getInstructorID();

        Instructor instructor = iCourseListAdapter.fetchInstructor(id);

        holder.txtInstructorName.setText(instructor.getFirstName()+instructor.getLastName());
        holder.txtTime.setText((course.getDay()+" "+course.getTime_h()+
                                                ":"+ course.getTime_m()+course.getAmpm()));
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(instructor.getUri()));
        } catch (IOException e) {}
        holder.icon.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCourseListAdapter.goToDisplayFragment(course.getTitle());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                            {
                                int size = coursesList.size();
                                coursesList.remove(course);
                                size--;
                                if(size==0){
                                    iCourseListAdapter.displayMessage();
                                }
                                iCourseListAdapter.removeCourse(course.getTitle());
                                notifyDataSetChanged();
//                                iCourseListAdapter.removeCourse(course.getTitle());
                            }

                            break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you?").setPositiveButton("Yes", dialogClickListener)
                        .setTitle("Delete the Course from the list?")
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public class CourseRecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageButton icon;
        private TextView txtInstructorName;
        private TextView txtCourseTitle;
        private TextView txtTime;

        public CourseRecyclerViewHolder(View itemView) {
            super(itemView);
            txtInstructorName = (TextView) itemView.findViewById(R.id.textViewEmail);
            txtCourseTitle = (TextView) itemView.findViewById(R.id.textViewName);
            txtTime = (TextView) itemView.findViewById(R.id.textViewWebsite);
            icon = (ImageButton) itemView.findViewById(R.id.imageViewInstructor);
        }
    }

    interface ICourseListAdapter{
        void removeCourse(String title);
        void goToDisplayFragment(String title);
        void displayMessage();
        Instructor fetchInstructor(long id);
    }
}
