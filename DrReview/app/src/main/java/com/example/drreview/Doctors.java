package com.example.drreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drreview.adapters.doctorAdapter;
import com.example.drreview.items.DoctorItems;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Doctors extends AppCompatActivity {


    private RecyclerView recyclerView;
    private doctorAdapter mainAdapter;
    private String receivedSpecialty, receivedHospital;
    private SearchView searchView;
    DatabaseReference ref;   //Firebase database reference
    private TextView department;
    private ImageView gotoProfile, gotoMain;
    FirebaseUser user;      //User profile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors);

        //On create, run these methods
        search();
        gotToProfile();
        gotToMain();

        //get the department text
        department = findViewById(R.id.departmenttxt);

        //get the current user, to check if user logged in
        user = FirebaseAuth.getInstance().getCurrentUser();

        //store the keys of the hospitals and specialty selected
        receivedSpecialty = getIntent().getExtras().get("clicked_specialty").toString();
        receivedHospital = getIntent().getExtras().get("clicked_hospital").toString();

        //run the method to set the department text
        getDepartment();

        //get the doctor list RecyclerView
        recyclerView = findViewById(R.id.doctorlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set the options for the RecyclerView, query the data from Firebase
        //configure the adapter
        FirebaseRecyclerOptions<DoctorItems> options =
                new FirebaseRecyclerOptions.Builder<DoctorItems>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHospital).child("specialties").child(receivedSpecialty).child("reviews"), DoctorItems.class)
                        .build();

        mainAdapter = new doctorAdapter(options);
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

    protected void getDepartment(){

        //reference to the specialty in the database
        //get the specific specialty with the specialty key
        ref = FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHospital).child("specialties");
        ref.child(receivedSpecialty).addValueEventListener(new ValueEventListener(){

            //set the department text to show the specialty previously selected
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get the name of the specialty
                String specialty = snapshot.child("specialty").getValue().toString();
                //set the text to show the specialty
                department.setText(specialty);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Message on error
                Toast.makeText(Doctors.this, "Error.Try Again", Toast.LENGTH_LONG).show();

            }
        });
    }


    //search view method
    protected void search(){

        //get the searchView
        searchView = findViewById(R.id.searchView);

        //Listen for a query
        //Set the actions for querying
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });
    }

    //Search for the specialty in the database
    protected void txtSearch(String str){

        //change the query text to upper case for easy search
        str = str.toUpperCase();

        //set the adapter
        //set the options for the RecyclerView, query the data from Firebase
        FirebaseRecyclerOptions<DoctorItems> options =
                new FirebaseRecyclerOptions.Builder<DoctorItems>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHospital).child("specialties").child(receivedSpecialty).child("reviews").orderByChild("doctor").startAt(str).endAt(str+"~"), DoctorItems.class)
                        .build();

        mainAdapter = new doctorAdapter(options);
        //Start listening for data
        //monitor changes to the Firebase query.
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

    //go to the user profile
    private void gotToProfile() {

        //get the user button image
        gotoProfile = (ImageView) findViewById(R.id.userbutton);

        //listen for click event
        gotoProfile.setOnClickListener(new View.OnClickListener() {

            //If user not logged in, redirect to login page
            //If user logged in, redirect to profile page
            @Override
            public void onClick(View v) {

                Intent intent;
                if (user != null) {

                    intent = new Intent(Doctors.this, Profile.class);
                } else {

                    intent = new Intent(Doctors.this, LoginActivity.class);

                }
                startActivity(intent);
            }
        });
    }

    //Go to the main page
    private void gotToMain(){

        //get the image of the home button
        gotoMain = (ImageView) findViewById(R.id.home);

        //listen for a click event
        gotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Redirect the user to the main page once clicked
                Intent intent = new Intent(Doctors.this, MainActivity.class);
                //Clear the back stack of the activities
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

}
