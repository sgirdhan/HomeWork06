package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sharangirdhani on 11/4/17.
 */

public class RecycleViewCourseAdapter extends RecyclerView.Adapter<RecycleViewCourseAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Course> courseArrayList;

    public RecycleViewCourseAdapter(Context mContext, ArrayList<Course> courseArrayList) {
        this.mContext = mContext;
        this.courseArrayList = courseArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ImageView image ;
        public TextView instructor;
        public TextView timing;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.courseTitle);
            image = (ImageView) itemView.findViewById(R.id.instruct_image);
            instructor = (TextView) itemView.findViewById(R.id.instruct_name);
            timing = (TextView) itemView.findViewById(R.id.courseTime);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.course_row,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courseArrayList.get(position);
        holder.title.setText(course.getTitle());
        String time_in_format = course.getDay().toString().substring(0,3) +" " + course.getTime_h()+ ":"+course.getTime_m() + " " + course.getAmpm();
        holder.timing.setText(time_in_format);
        holder.instructor.setText("Pending");
    }

    @Override
    public int getItemCount() {
        return courseArrayList.size();
    }
}
