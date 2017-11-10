package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;


public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorRecyclerViewHolder> {

    private ArrayList<Instructor> instructorsList;
    private Context context;
    private IInstructorAdapter iInstructorAdapter;

    public InstructorAdapter(ArrayList<Instructor> instructorsList, Context context, IInstructorAdapter iInstructorAdapter) {
        this.instructorsList = instructorsList;
        this.context = context;
        this.iInstructorAdapter = iInstructorAdapter;
    }

    @Override
    public InstructorRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_row,parent,false);
        InstructorRecyclerViewHolder instructorRecyclerViewHolder = new InstructorRecyclerViewHolder(view);
        return instructorRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final InstructorRecyclerViewHolder holder, final int position) {
        final Instructor instructor = instructorsList.get(position);
        if(instructor.isValid()){
            holder.imgViewInstructoIcon.setImageURI(Uri.parse(instructor.getUri()));
            holder.txtInstructorName.setText(instructor.getFirstName()+" "+instructor.getLastName());
            holder.rbInstructor.setChecked(instructor.isChecked());
            holder.rbInstructor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!(instructor.isChecked())){
                        for(Instructor ins:instructorsList){
                            ins.setChecked(false);
                        }

                        instructor.setChecked(true);
                        holder.rbInstructor.setChecked(true);
                        iInstructorAdapter.sendInstructorInformation(position);

                        //Update Recycler View
                        notifyDataSetChanged();
                    }
                }
            });
        }
        else{
            iInstructorAdapter.makeTextMessageVisible();
        }
    }

    @Override
    public int getItemCount() {
        return instructorsList.size();
    }

    public class InstructorRecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgViewInstructoIcon;
        private TextView txtInstructorName;
        private RadioButton rbInstructor;
        public InstructorRecyclerViewHolder(View itemView) {
            super(itemView);

            rbInstructor = (RadioButton) itemView.findViewById(R.id.radioButtonInstructor);
            imgViewInstructoIcon = (ImageView) itemView.findViewById(R.id.imageViewInstructorIcon);
            txtInstructorName = (TextView) itemView.findViewById(R.id.textViewInsName);
        }
    }

    interface IInstructorAdapter
    {
        void sendInstructorInformation(int position);

        void makeTextMessageVisible();
    }
}