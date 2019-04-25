package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.GiftRegistryDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    //this activity is used for registration
    private EditText username, password, email;
    private TextView mandatory;
    private boolean isValid;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        password = (EditText) findViewById(R.id.pwd);
        email = (EditText) findViewById(R.id.email);
        mandatory = (TextView) findViewById(R.id.mandatory);
        //gets instance of firebase auth
        auth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    //invoked on click of register
    public void register(View v){
        String pwd = password.getText().toString();
        String emailAddress = email.getText().toString();
        //validates details
        if(!validateDetails(emailAddress, pwd)) {
            //if validated it invokes createUserWithEmailAndPassword function of firebase auth to insert a new user
            progressBar.setVisibility(View.VISIBLE);
            //create user
            auth.createUserWithEmailAndPassword(emailAddress.trim(), pwd.trim())
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                //if task is successful it is redirected to Login page with success message
                                Intent loginIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                                loginIntent.putExtra("successMsg", "Registration Successful! Please login.");
                                startActivity(loginIntent);
                                finish();
                            }
                        }
                    });
        }
    }

    //Validating if user has entered all details. if user has not entered all the details then error messages are displayed
    public boolean validateDetails(String emailAddress, String pwd){
        isValid = false;
        if (TextUtils.isEmpty(pwd)||TextUtils.isEmpty(emailAddress)) {
            Toast.makeText(getApplicationContext(), "Please enter all the details", Toast.LENGTH_SHORT).show();
            mandatory.setVisibility(View.VISIBLE);
            isValid = true;
            if(TextUtils.isEmpty(emailAddress)){
                email.setError("Please enter email address!");
            }
            if(TextUtils.isEmpty(pwd)){
                password.setError("Please enter password!");
            }
        }
        return isValid;
    }

    //redirected to terms and conditions page
    public void goToTerms(View v){
        startActivity(new Intent(RegistrationActivity.this, TermsAndConditions.class));
    }
}
