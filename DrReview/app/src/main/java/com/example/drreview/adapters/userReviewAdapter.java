package com.example.drreview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drreview.R;
import com.example.drreview.items.UserReviewItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

//Adapter class required for the layout inflation
//Binds the data to the views being recycled
public class userReviewAdapter extends FirebaseRecyclerAdapter<UserReviewItem, userReviewAdapter.myViewHolder> {



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options - the configured options(query) from the database
     */

    //constructor method
    public userReviewAdapter(@NonNull FirebaseRecyclerOptions<UserReviewItem> options) {
        super(options);
    }

    //Binds the data being defined in the myViewHolder class
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull UserReviewItem model) {

        //get the name of the doctor and the reviews and set it to text
        holder.doctor.setText(model.getDoctor());
        holder.reviews.setText(model.getReviews());
    }

    //Inflates the layout to be seen
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflates the layout, the layout for the recycler view is the user_review_items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_review_items, parent, false);
        return new userReviewAdapter.myViewHolder(view);
    }

    //myViewHolder class, holds the views ready for recycling
    //In this case, the doctor and the review text needs to be recycled
    class myViewHolder extends RecyclerView.ViewHolder{

        TextView doctor, reviews;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            doctor = (TextView) itemView.findViewById(R.id.doctortxt);
            reviews = (TextView)  itemView.findViewById(R.id.reviewstxt);

        }
    };
}
