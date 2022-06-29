package com.example.drreview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drreview.items.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editUser, editPassword, editEmail;
    private TextView title;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //get the text, edit text fields and the buttons
        progressBar = findViewById(R.id.progressBar);
        editUser = findViewById(R.id.editusername);
        editPassword = findViewById(R.id.editpassword);
        editEmail = findViewById(R.id.editemail);
        title = findViewById(R.id.title);
        signUpButton = findViewById(R.id.signup_button );

        //set on click listener for the title buttons
        title.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        //get and instance of the firebase authentication
        mAuth = FirebaseAuth.getInstance();
    }

    //listen for click events
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title:
                //redirect user to main activity when click on title
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.signup_button:
                //register user
                signUpUser();
                break;

        }
    }

    protected void signUpUser(){

        //email, username and password received to send for registration
        String email, password, user;

        //get the email, username and password from the edit text view
        user = editUser.getText().toString();
        password = editPassword.getText().toString();
        email = editEmail.getText().toString();

        //check for valid password and email
        if(user.isEmpty()){
            editUser.setError("Username Required");
            editUser.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("Password Required");
            editPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editPassword.setError("Password needs to be longer than 6 characters");
            editPassword.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editEmail.setError("Email Required");
            editEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Please Provide Valid Email");
            editEmail.requestFocus();
            return;
        }

        //set the progressbar to visible
        progressBar.setVisibility(View.VISIBLE);

        //register the user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            //create new user
                            Users newUser = new Users(user, email);

                            //reference to the user in the database and add to database
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>(){

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        //success message upon managing to register
                                        Toast.makeText(SignUp.this, "User Registered", Toast.LENGTH_LONG).show();
                                        //remove the progress bar
                                        progressBar.setVisibility(View.GONE);
                                        //redirect user to main activity
                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                        startActivity(intent);

                                    }
                                    else{

                                        //Error message upon failure to register
                                        Toast.makeText(SignUp.this, "Unable to Register", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{

                            //failure message upon failure to register
                            Toast.makeText(SignUp.this, "Unable to Register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
