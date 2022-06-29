package com.example.drreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drreview.adapters.userReviewAdapter;
import com.example.drreview.items.UserReviewItem;
import com.example.drreview.items.Users;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private Button signOutButton;
    FirebaseUser user;
    DatabaseReference ref;
    private String userID;
    private TextView username, email;
    private RecyclerView recyclerView;
    private userReviewAdapter mainAdapter;
    private ImageView gotoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        //on create, run these method
        signOut();
        gotToMain();

        //get the current user, check if logged in
        user = FirebaseAuth.getInstance().getCurrentUser();
        //references to the user in the database
        ref = FirebaseDatabase.getInstance().getReference("Users");
        //get the user id of the current user
        userID = user.getUid();

        //get the username and the email
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);

        //set the information in the card
        setInfo();

        ////get the user review list RecyclerView
        recyclerView = (RecyclerView)findViewById(R.id.userreviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set the options for the RecyclerView, query the data from Firebase
        //configure the adapter
        FirebaseRecyclerOptions<UserReviewItem> options =
                new FirebaseRecyclerOptions.Builder<UserReviewItem>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("reviews"), UserReviewItem.class)
                        .build();

        mainAdapter = new userReviewAdapter(options);
        recyclerView.setAdapter(mainAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start listening for data
        // monitor changes to the Firebase query.
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Stop listening for data
        // monitor changes to the Firebase query.
        mainAdapter.stopListening();
    }

    //set the information of the current user profile
    protected void setInfo(){

        //reference to the user in the database
        //get the specific user with the keys(userID) received
        ref.child(userID).addValueEventListener(new ValueEventListener() {

            //set the username and email text to show the correct info
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get the user info and store the data
                Users userProfile = snapshot.getValue(Users.class);

                //if the data is not null
                if (userProfile != null){

                    //get the correct username and email
                    String name = userProfile.getUsername();
                    String userEmail = userProfile.getEmail();

                    //set the username and email
                    username.setText(name);
                    email.setText(userEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //show message if error occurs
                Toast.makeText(Profile.this, "Something Went Wrong", Toast.LENGTH_LONG).show();

            }
        });
    }

    //sign out method
    protected void signOut(){

        // get the sign out button
        signOutButton = findViewById(R.id.signout_button);

        //listen for a click event
        signOutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //signs user out
                FirebaseAuth.getInstance().signOut();
                //redirects user to main page
                Intent intent = new Intent(Profile.this, MainActivity.class);
                //Clear activity back stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //shows message on successful sign out
                Toast.makeText(Profile.this, "User Signed Out", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    ////Go to the main page
    private void gotToMain(){

        //get the image of the home button
        gotoMain = (ImageView) findViewById(R.id.home);

        //listen for a click event
        gotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Redirect the user to the main page once clicked
                Intent intent = new Intent(Profile.this, MainActivity.class);
                //Clear the back stack of the activities
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
