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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import io.realm.Realm;



public class InstructorListAdapter extends RecyclerView.Adapter<InstructorListAdapter.InstructorListRecyclerViewHolder> {
    private List<Instructor> insList;
    private Context context;
    private IInstructorListAdapter iInstructorAdapter;
    Realm realm;

    public InstructorListAdapter(List<Instructor> insList, Context context, IInstructorListAdapter iInstructorAdapter) {
        this.insList = insList;
        this.context = context;
        this.iInstructorAdapter = iInstructorAdapter;
    }

    @Override
    public InstructorListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.instructor_row,parent,false);
        InstructorListRecyclerViewHolder instructorRecyclerViewHolder = new InstructorListRecyclerViewHolder(view);
        return instructorRecyclerViewHolder;
    }


    public void onBindViewHolder(InstructorListRecyclerViewHolder holder, int position) {
        final Instructor instructor = insList.get(position);

        realm.init(context);
        realm = Realm.getDefaultInstance();
        holder.txtInstructorName.setText(instructor.getFirstName()+" "+instructor.getLastName());
        holder.txtEmail.setText(instructor.getEmail());
        holder.txtWebsite.setText(instructor.getPersonalWebsite());

        final long ins = instructor.getId();
        try{
        holder.icon.setImageURI(Uri.parse(instructor.getUri()));
        } catch(NullPointerException e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iInstructorAdapter.goToDisplayInstructorFragment(instructor.getId());
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
                                int size = insList.size();
                                if(!(instructor.getCourses().size()>0)){
                                    //remove from local list
                                    insList.remove(instructor);

                                    realm.beginTransaction();
                                    instructor.setCourses(null);
                                    realm.copyToRealmOrUpdate(instructor);
                                    realm.commitTransaction();
                                    realm.close();

                                    //Database updation to match the local list
                                    iInstructorAdapter.removeInstructor(ins);
                                    size--;
                                    if (!(size > 0)) {
                                        iInstructorAdapter.makeWarningMessageVisible();
                                    }

                                    //refresh list on deletion
                                    notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(context," There are existing courses associated with the professor and you can not delete. ",Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }

                            break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you?").setPositiveButton("Yes", dialogClickListener)
                        .setTitle("Delete the Instructor from the list?")
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return insList.size();
    }

    public class InstructorListRecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView txtInstructorName;
        private TextView txtEmail;
        private TextView txtWebsite;

        public InstructorListRecyclerViewHolder(View itemView) {
            super(itemView);
            txtInstructorName = (TextView) itemView.findViewById(R.id.textViewName);
            txtEmail = (TextView) itemView.findViewById(R.id.textViewEmail);
            txtWebsite = (TextView) itemView.findViewById(R.id.textViewWebsite);
            icon = (ImageView) itemView.findViewById(R.id.imageViewInstructor);
        }
    }

    interface IInstructorListAdapter{

        void goToDisplayInstructorFragment(long id);
        void removeInstructor(long id);
        void makeWarningMessageVisible();
    }

}
