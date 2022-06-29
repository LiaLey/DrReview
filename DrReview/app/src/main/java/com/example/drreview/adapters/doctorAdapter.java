package com.example.drreview.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drreview.items.DoctorItems;
import com.example.drreview.DrInfo;
import com.example.drreview.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


//Adapter class required for the layout inflation
//Binds the data to the views being recycled
public class doctorAdapter extends FirebaseRecyclerAdapter<DoctorItems, doctorAdapter.myViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options - the configured options(query) from the database
     */

    //constructor method
    public doctorAdapter(@NonNull FirebaseRecyclerOptions<DoctorItems> options) {
        super(options);
    }

    //Binds the data being defined in the myViewHolder class
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull DoctorItems model) {

        //get the name of the doctor and set it to text
        holder.doctor.setText(model.getDoctor());

        //get the key of the current hospital based on position
        //get the key of the current specialty based on position
        //get the key of the current doctor based on position
        String clickedDoctor = getRef(position).getKey();
        String clickedSpecialty = getRef(position).getParent().getParent().getKey();
        String clickedHosp = getRef(position).getParent().getParent().getParent().getParent().getKey();

        //listen for click event of the recycler items
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //Direct the user to the doctorInfo activity based on which specialty clicked
                Intent intent = new Intent(v.getContext(), DrInfo.class);

                //pass the key of the clicked hospital to the next activity
                //pass the key of the clicked specialty to the next activity
                intent.putExtra("clicked_specialty", clickedSpecialty);
                intent.putExtra("clicked_hospital", clickedHosp);
                intent.putExtra("clicked_doctor", clickedDoctor);

                //start the next activity
                v.getContext().startActivity(intent);
            }
        });
    }

    //Inflates the layout to be seen
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflates the layout, the layout for the recycler view is the doctor_items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_items, parent, false);
        return new myViewHolder(view);

    }

    //myViewHolder class, holds the views ready for recycling
    //In this case, the doctor text needs to be recycled
    class myViewHolder extends RecyclerView.ViewHolder{

        TextView doctor;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            doctor = itemView.findViewById(R.id.doctortxt);
        }
    }
}
