package com.example.drreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drreview.adapters.reviewAdapter;
import com.example.drreview.items.ReviewItem;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrInfo extends AppCompatActivity{

    private RecyclerView recyclerView;
    private reviewAdapter mainAdapter;
    private ImageView addButton;
    private String receivedSpecialty, receivedHospital, receivedDoctor;
    DatabaseReference ref;  //Firebase database reference
    FirebaseUser currentUser;  //user profile
    private TextView doctor, qualifications, hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dr_info);

        //get the current user, check if logged in
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //get the doctor, qualification and hospital text
        doctor =findViewById(R.id.drname);
        qualifications = findViewById(R.id.qualifications);
        hospital =findViewById(R.id.hospital);

        //get the keys of the hospital, specialty and doctor clicked
        receivedSpecialty = getIntent().getExtras().get("clicked_specialty").toString();
        receivedHospital = getIntent().getExtras().get("clicked_hospital").toString();
        receivedDoctor = getIntent().getExtras().get("clicked_doctor").toString();

        //set the info onto the card
        setInfo();

        ////get the review list RecyclerView
        recyclerView = (RecyclerView)findViewById(R.id.reviewlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set the options for the RecyclerView, query the data from Firebase
        //configure the adapter
        FirebaseRecyclerOptions<ReviewItem> options =
                new FirebaseRecyclerOptions.Builder<ReviewItem>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHospital).child("specialties").child(receivedSpecialty).child("reviews").child(receivedDoctor).child("reviews"), ReviewItem.class)
                        .build();

        mainAdapter = new reviewAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        //add reviews
        goToAddActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start listening for data
        // monitor changes to the Firebase query.
        mainAdapter.startListening();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Stop listening for data
        // monitor changes to the Firebase query.
        mainAdapter.stopListening();
    }

    protected void setInfo(){

        //reference to the doctor in the database
        //get the specific specialty, hospital, doctor with the keys received
        ref = FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHospital).child("specialties").child(receivedSpecialty).child("reviews");
        ref.child(receivedDoctor).addValueEventListener(new ValueEventListener(){

            //set the drName, hospital and qualification text to show the correct info
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get the doctor's name, qualification and specialty
                String dr = snapshot.child("doctor").getValue().toString();
                String qual = snapshot.child("qualifications").getValue().toString();
                String hosp = snapshot.child("hospital").getValue().toString();

                //set the info to the text
                doctor.setText(dr);
                qualifications.setText(qual);
                hospital.setText(hosp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DrInfo.this, "Error.Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Method for adding reviews
    private void goToAddActivity(){

        //get the add review image
        addButton = (ImageView) findViewById(R.id.addReview);

        //listen for a click event
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if the user is logged in
                if(currentUser != null){

                    //direct to the page to add a review
                    Intent intent = new Intent(DrInfo.this, AddReviewActivity.class);

                    //pass the keys of the specialty, hospital and the doctor to the next activity
                    intent.putExtra("clicked_specialty", receivedSpecialty);
                    intent.putExtra("clicked_hospital", receivedHospital);
                    intent.putExtra("clicked_doctor", receivedDoctor);
                    startActivity(intent);

                }
                else{

                    //User not logged in
                    //direct user to the login page
                    Intent login = new Intent(DrInfo.this, LoginActivity.class);
                    //show a toast asking user to login
                    Toast.makeText(DrInfo.this, "Please Login to review", Toast.LENGTH_LONG).show();
                    startActivity(login);
                }
            }
        });
    }

}
