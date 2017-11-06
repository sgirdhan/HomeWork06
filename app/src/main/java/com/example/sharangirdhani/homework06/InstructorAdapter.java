package com.example.sharangirdhani.homework06;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;



public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorRecyclerViewHolder> {

    ArrayList<Instructor> instructorsList;
    Context context;
    IInstructorAdapter iInstructorAdapter;
    private RadioButton lastCheckedRB = null;
    Realm realm;

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

        holder.imgViewInstructoIcon.setImageResource(R.drawable.dimage);
        holder.txtInstructorName.setText(instructor.getFirstName());
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
            txtInstructorName = (TextView) itemView.findViewById(R.id.textViewInstructorName);
        }
    }

    interface IInstructorAdapter
    {
        void sendInstructorInformation(int position);
//        void removeDataFromFirstList(Music music);
//        void refreshUpperList();

    }
}