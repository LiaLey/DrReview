package com.example.drreview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddReviewActivity extends AppCompatActivity {

    private EditText reviews;
    private Button addReview;
    private String receivedSpecialty, receivedHospital, receivedDoctor, userID, username, doctorName;
    DatabaseReference ref;  // Firebase database reference
    FirebaseUser currentUser; // user profile


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_review);

        //get the current user, check if logged in
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //get the user id
        userID = currentUser.getUid();

        //store the keys of the specialty, hospital and doctors received
        receivedSpecialty = getIntent().getExtras().get("clicked_specialty").toString();
        receivedHospital = getIntent().getExtras().get("clicked_hospital").toString();
        receivedDoctor = getIntent().getExtras().get("clicked_doctor").toString();

        //get the review edit text
        reviews = (EditText) findViewById(R.id.reviewtxt);

        //run the methods to get the names of the doctor and user
        receiveNames();
        //run the method to submit reviews
        submitReview();

    }

    // get the names of the current doctor and user
    protected void receiveNames(){

        //reference to the user in the database
        //get the specific user with the key(userID)
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.child(userID).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get the username of the current user
                username = snapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Message on error
                Toast.makeText(AddReviewActivity.this, "Error.Try Again", Toast.LENGTH_LONG).show();

            }
        });

        //reference to the doctor in the database
        //get the specific doctor with the key recieved
        ref = FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHospital).child("specialties").child(receivedSpecialty).child("reviews");
        ref.child(receivedDoctor).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get the specific doctor name
                doctorName = snapshot.child("doctor").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Message on error
                Toast.makeText(AddReviewActivity.this, "Error.Try Again", Toast.LENGTH_LONG).show();
            }
        });

    }

    //submit reviews
    protected void submitReview(){

        //get the submit review button
        addReview = (Button) findViewById(R.id.submit_review);

        //listen for click activity
        addReview.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //inset review
                insertReview();
                //end activity
                finish();

            }
        });
    }

    // add reviews to database
    private void insertReview(){

        //HashMap to store the data to send to database
        Map<String, Object> map = new HashMap<>();
        //add user and the reviews
        map.put("user", username);
        map.put("reviews", reviews.getText().toString());

        //reference to the  specific doctor in the database based on the keys received
        FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHospital).child("specialties").child(receivedSpecialty).child("reviews").child(receivedDoctor).child("reviews").push().setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>(){

                    @Override
                    public void onSuccess(Void unused) {

                        //send message on successful submission of review
                        Toast.makeText(AddReviewActivity.this, "Review Submitted", Toast.LENGTH_SHORT).show();
                        //redirect user to the previous page
                        AddReviewActivity.this.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //send message on failure
                        Toast.makeText(AddReviewActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        //redirect user to the previous page
                        AddReviewActivity.this.finish();

                    }

                });


        //Hash Map to send the reviews to database (This is for the user table)
        Map<String, Object> userMap = new HashMap<>();
        //add doctor name and review
        userMap.put("doctor", doctorName);
        userMap.put("reviews", reviews.getText().toString());

        //reference to the specific user with the keys (userID)
        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("reviews").push().setValue(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>(){

                    @Override
                    public void onSuccess(Void unused) {

                        //redirect user back to previous page after successfully adding the review
                        AddReviewActivity.this.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //redirect user to the previous page on failure
                        AddReviewActivity.this.finish();

                    }

                });
    }
}
