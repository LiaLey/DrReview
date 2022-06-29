package com.example.drreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.drreview.adapters.hospitalAdapter;
import com.example.drreview.adapters.specialtyAdapter;
import com.example.drreview.items.HospitalItems;
import com.example.drreview.items.SpecialtyItems;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ImageView gotoProfile;
    FirebaseUser user;                                  //User profile
    private RecyclerView recyclerView, recyclerViewSp;  //Adapters for the items of the RecyclerView
    private hospitalAdapter mainAdapter;
    private specialtyAdapter spAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gotToProfile();

        //Get the current user logged in, check if the user is logged in
        user = FirebaseAuth.getInstance().getCurrentUser();


        //get the hospital RecyclerView
        recyclerView = findViewById(R.id.hospitallist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set the options for the RecyclerView, query the data from Firebase
        //configure the adapter
        FirebaseRecyclerOptions<HospitalItems> options =
                new FirebaseRecyclerOptions.Builder<HospitalItems>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("hospitals"), HospitalItems.class)
                        .build();
        //Pass the options(data) to the adapter
        mainAdapter = new hospitalAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        //get the specialty RecyclerView
        recyclerViewSp = findViewById(R.id.specialtylist);
        recyclerViewSp.setLayoutManager(new LinearLayoutManager(this));
        //set the options for the RecyclerView, query the data from Firebase
        //configure the adapter
        FirebaseRecyclerOptions<SpecialtyItems> SPOptions =
                new FirebaseRecyclerOptions.Builder<SpecialtyItems>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("hospitals").child("h1").child("specialties"), SpecialtyItems.class)
                        .build();
        //Pass the options(data) to the adapter
        spAdapter = new specialtyAdapter(SPOptions);
        recyclerViewSp.setAdapter(spAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start listening for data
        // monitor changes to the Firebase query.
        mainAdapter.startListening();
        spAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Stop listening for data
        // monitor changes to the Firebase query.
        mainAdapter.stopListening();
        spAdapter.stopListening();
    }

    private void gotToProfile(){

        //get the user button
        gotoProfile = (ImageView) findViewById(R.id.userbutton);

        //listen for click event
        gotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If user is logged in, switch activity to the profile page
                //If user not logged in, redirect to login activity
                Intent intent;
                if(user != null){

                    intent = new Intent(MainActivity.this, Profile.class);
                }
                else{

                    intent = new Intent(MainActivity.this, LoginActivity.class);

                }
                startActivity(intent);

            }
        });
    }


}