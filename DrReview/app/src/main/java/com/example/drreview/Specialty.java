package com.example.drreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drreview.adapters.specialtyAdapter;
import com.example.drreview.items.SpecialtyItems;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Specialty extends AppCompatActivity {

    private RecyclerView recyclerViewSp;
    private specialtyAdapter spAdapter;
    FirebaseUser user;   //User profile, to check if user logged in
    private String receivedHosp;
    private SearchView searchView;
    private ImageView gotoProfile, gotoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialty);

        //on create run the methods
        search();
        gotToProfile();
        gotToMain();

        //get the current user to check if user is logged in or not
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Get the key of the clicked hospital from the last activity
        receivedHosp = getIntent().getExtras().get("clicked_hosp").toString();

        //get the specialty recycler view
        recyclerViewSp = findViewById(R.id.specialtylist);
        recyclerViewSp.setLayoutManager(new LinearLayoutManager(this));
        //set the options for the RecyclerView, query the data from Firebase
        //configure the adapter
        FirebaseRecyclerOptions<SpecialtyItems> SPOptions =
                new FirebaseRecyclerOptions.Builder<SpecialtyItems>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHosp).child("specialties"), SpecialtyItems.class)
                        .build();
        spAdapter = new specialtyAdapter(SPOptions);
        recyclerViewSp.setAdapter(spAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Start listening for data
        //monitor changes to the Firebase query.
        spAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Stop listening for data
        //monitor changes to the Firebase query.
        spAdapter.stopListening();
    }

    //Method to search for a particular specialty
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
        FirebaseRecyclerOptions<SpecialtyItems> SPOptions =
                new FirebaseRecyclerOptions.Builder<SpecialtyItems>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("hospitals").child(receivedHosp).child("specialties").orderByChild("specialty").startAt(str).endAt(str+"~"), SpecialtyItems.class)
                        .build();

        spAdapter = new specialtyAdapter(SPOptions);
        //Start listening for data
        //monitor changes to the Firebase query.
        spAdapter.startListening();
        recyclerViewSp.setAdapter(spAdapter);
    }

    //go to the user profile
    protected void gotToProfile(){

        //get the user button image
        gotoProfile = (ImageView) findViewById(R.id.userbutton);

        //listen for click event
        gotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If user not logged in, redirect to login page
                //If user logged in, redirect to profile page
                Intent intent;
                if(user != null){

                    intent = new Intent(Specialty.this, Profile.class);
                }
                else{

                    intent = new Intent(Specialty.this, LoginActivity.class);
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
                Intent intent = new Intent(Specialty.this, MainActivity.class);
                //Clear the back stack of the activities
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }



}
