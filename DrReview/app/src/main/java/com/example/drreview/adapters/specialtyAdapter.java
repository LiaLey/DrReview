package com.example.drreview.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drreview.Doctors;
import com.example.drreview.R;
import com.example.drreview.items.SpecialtyItems;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

//Adapter class required for the layout inflation
//Binds the data to the views being recycled
public class specialtyAdapter extends FirebaseRecyclerAdapter<SpecialtyItems, specialtyAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options - the configured options(query) from the database
     */

    //constructor method
    public specialtyAdapter(@NonNull FirebaseRecyclerOptions<SpecialtyItems> options) {
        super(options);
    }

    //Binds the data being defined in the myViewHolder class
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull SpecialtyItems model) {

        //get the name of the specialty and set it to text
        holder.specialty.setText(model.getSpecialty());

        //get the key of the current hospital based on position
        //get the key of the current specialty based on position
        String clickedSpecialty = getRef(position).getKey();
        String clickedHosp = getRef(position).getParent().getParent().getKey();

        //listen for click event of the recycler items
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //Direct the user to the doctor activity based on which specialty clicked
                Intent intent = new Intent(v.getContext(), Doctors.class);

                //pass the key of the clicked hospital to the next activity
                //pass the key of the clicked specialty to the next activity
                intent.putExtra("clicked_specialty", clickedSpecialty);
                intent.putExtra("clicked_hospital", clickedHosp);

                //start the next activity
                v.getContext().startActivity(intent);
            }
        });

    }

    //Inflates the layout to be seen
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflates the layout, the layout for the recycler view is the specialty_items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specialty_items, parent, false);
        return new myViewHolder(view);
    }

    //myViewHolder class, holds the views ready for recycling
    //In this case, the specialty text needs to be recycled
    class myViewHolder extends RecyclerView.ViewHolder {

        TextView specialty;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            specialty = itemView.findViewById(R.id.specialtytxt);
        }
    }
}
