package com.example.drreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editPassword, editEmail;
    private TextView title, forgotPassword;
    private Button loginButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        //get the text, edit text fields and the buttons
        progressBar = findViewById(R.id.progressBar);
        editEmail = findViewById(R.id.editemail);
        editPassword = findViewById(R.id.editpassword);
        title = findViewById(R.id.title);
        loginButton = findViewById(R.id.login_button );
        signUpButton = findViewById(R.id.signup_button );
        forgotPassword = findViewById(R.id.forgotPassword);

        //set on click listener for the title, forget password and buttons
        title.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        //get and instance of the firebase authentication
        mAuth = FirebaseAuth.getInstance();
    }

    //listen for click events
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title:
                //redirect user to main activity when click on title
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.signup_button:
                //redirect user to sign up activity when click on sign up button
                startActivity(new Intent(LoginActivity.this, SignUp.class));
                break;

            case R.id.login_button:
                //login user
                loginUser();
                break;

            case R.id.forgotPassword:
                //redirect user to reset password activity
                startActivity(new Intent(LoginActivity.this, ResetPassword.class));
                break;
        }

    }

    protected void loginUser() {

        //email and password received to send for authentication
        String email, password;

        //get the email and password from the edit text view
        email = editEmail.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        //check for valid password and email
        if(password.isEmpty()){
            editPassword.setError("Password Required");
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

        //authenticate the user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //show success message if user managed to log in
                    Toast.makeText(LoginActivity.this, "User Signed in", Toast.LENGTH_LONG).show();
                    //redirect to the profile activity
                    Intent intent = new Intent(LoginActivity.this, Profile.class);
                    //clear activity back stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //hide the progress bar
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
                else{

                    //show error message if login not successful
                    Toast.makeText(LoginActivity.this, "Failed to Login. Please Check Your Credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
