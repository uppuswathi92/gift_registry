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
import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.GiftRegistryDatabase;

public class ResetPasswordActivity extends AppCompatActivity {
    //this activity is used to reset password
    private EditText password, confirm_password, emailId;
    private boolean isValid;
    private TextView mandatory;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emailId = (EditText) findViewById(R.id.reset_email);
        password = (EditText) findViewById(R.id.reset_pwd);
        confirm_password = (EditText) findViewById(R.id.confirm_pwd);
        mandatory = (TextView) findViewById(R.id.mandatory);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //gets instance of firebase auth
        auth = FirebaseAuth.getInstance();
    }

    //invoked on click of reset password button
    public void resetPassword(View v){
        if(validateDetails()) {
                String email = emailId.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                //if all details are entered then sendPasswordResetEmail of firebase auth is invoked
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //if its successful then email is sent to the user with instructions
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_LONG).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
    }

    //Validating if user has entered all details. if user has not entered all the details then error messages are displayed
    public boolean validateDetails(){
        isValid = true;
        String error = getString(R.string.mandatory);
        if(TextUtils.isEmpty(emailId.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(confirm_password.getText().toString())){
            isValid = false;
        }
        if(TextUtils.isEmpty(emailId.getText().toString())){
            emailId.setError("Please enter email address!");
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("Please enter password!");
        }
        if(TextUtils.isEmpty(confirm_password.getText().toString())){
            confirm_password.setError("Please confirm password!");
        }
        if(isValid && (!password.getText().toString().equals(confirm_password.getText().toString()))){
            isValid = false;
            error = getString(R.string.confirm_error);
        }
        if(!isValid){
            mandatory.setText(error);
            mandatory.setVisibility(View.VISIBLE);
        }
        return isValid;
    }
}
