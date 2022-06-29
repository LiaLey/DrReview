package com.example.drreview.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drreview.items.HospitalItems;
import com.example.drreview.R;
import com.example.drreview.Specialty;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


//Adapter class required for the layout inflation
//Binds the data to the views being recycled
public class hospitalAdapter extends FirebaseRecyclerAdapter<HospitalItems, hospitalAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options - the configured options(query) from the database
     */

    //constructor method
    public hospitalAdapter(@NonNull FirebaseRecyclerOptions<HospitalItems> options) {
        super(options);
    }

    //Binds the data being defined in the myViewHolder class
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull HospitalItems model) {

        //get the name of the hospital and set it to text
        holder.hosp.setText(model.getHosp());

        //get the key of the current hospital based on position
        String clickedHosp = getRef(position).getKey();

        //listen for click event of the recycler items
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //Direct the user to the specialty activity based on which hospital clicked
                Intent intent = new Intent(v.getContext(), Specialty.class);

                //pass the key of the clicked hospital to the next activity
                intent.putExtra("clicked_hosp", clickedHosp);

                //start the activity
                v.getContext().startActivity(intent);

            }
        });
    }

    //Inflates the layout to be seen
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflates the layout, the layout for the recycler view is the hospital_items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_items, parent, false);
        return new myViewHolder(view);
    }

    //myViewHolder class, holds the views ready for recycling
    //In this case, the hospital text needs to be recycled
    class myViewHolder extends RecyclerView.ViewHolder{

        TextView hosp;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            hosp = (TextView) itemView.findViewById(R.id.hospitaltxt);
        }
    }
}
