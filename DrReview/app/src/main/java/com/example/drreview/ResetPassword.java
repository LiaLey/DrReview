package com.example.drreview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText email;
    private Button resetButton;
    private ProgressBar progressBar;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        //get the email, progressBar and buttons
        email = (EditText) findViewById(R.id.editemail);
        resetButton = (Button) findViewById(R.id.resetbutton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //get and instance of the firebase authentication
        auth = FirebaseAuth.getInstance();

        //Listen for click event (rest button)
        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    //reset password
    private void resetPassword() {

        //email received
        String receivedEmail = email.getText().toString().trim();

        //check for valid email
        if(receivedEmail.isEmpty()){
            email.setError("Email Required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(receivedEmail).matches()){
            email.setError("Please Provide Valid Email");
            email.requestFocus();
            return;
        }

        // show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        //send a verification email to the user email
        auth.sendPasswordResetEmail(receivedEmail).addOnCompleteListener(new OnCompleteListener<Void>(){

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    //send message on success
                    Toast.makeText(ResetPassword.this, "Check Email to Reset Password", Toast.LENGTH_LONG).show();
                    //remove the progress bar
                    progressBar.setVisibility(View.GONE);
                    //redirect to the login page
                    Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                    //clear the activity back stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }
                else{

                    //show error message on failure
                    Toast.makeText(ResetPassword.this, "Something Wrong Happened. Try Again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }



}
