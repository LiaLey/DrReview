package com.example.drreview.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drreview.R;
import com.example.drreview.items.ReviewItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

//Adapter class required for the layout inflation
//Binds the data to the views being recycled
public class reviewAdapter extends FirebaseRecyclerAdapter<ReviewItem, reviewAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options -- the configured options(query) from the database
     */

    //constructor method
    public reviewAdapter(@NonNull FirebaseRecyclerOptions<ReviewItem> options) {
        super(options);
    }

    //Binds the data being defined in the myViewHolder class
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ReviewItem model) {

        //get the name of the user and the reviews and set it to text
        holder.user.setText(model.getUser());
        holder.reviews.setText(model.getReviews());

    }

    //Inflates the layout to be seen
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflates the layout, the layout for the recycler view is the review_items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_items, parent, false);
        return new myViewHolder(view);
    }

    //myViewHolder class, holds the views ready for recycling
    //In this case, the user and the review text needs to be recycled
    class myViewHolder extends RecyclerView.ViewHolder{

        TextView user, reviews;

        public myViewHolder(@NonNull View itemView){

            super(itemView);

            user = (TextView) itemView.findViewById(R.id.usertxt);
            reviews = (TextView)  itemView.findViewById(R.id.reviewstxt);
        }
    }
}
