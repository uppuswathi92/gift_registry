package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private TextView success, mandatory;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private boolean isValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.emailAdd);
        password = (EditText) findViewById(R.id.password);
        success = (TextView) findViewById(R.id.success);
        String successMsg = getIntent().getStringExtra("successMsg");
        if(successMsg != null){
            success.setText(successMsg);
            success.setVisibility(View.VISIBLE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        mandatory = (TextView) findViewById(R.id.mandatory);
    }
   public boolean validateDetails(String emailAddress, String pwd){
       isValid = false;
       if (TextUtils.isEmpty(pwd)||TextUtils.isEmpty(emailAddress)) {
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
   public void signIn(View v){
       String emailAddress = email.getText().toString();
       String pwd = password.getText().toString();
       if(!validateDetails(emailAddress, pwd)) {
           progressBar.setVisibility(View.VISIBLE);
           auth.signInWithEmailAndPassword(emailAddress.trim(), pwd.trim())
                   .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           // If sign in fails, display a message to the user. If sign in succeeds
                           // the auth state listener will be notified and logic to handle the
                           // signed in user can be handled in the listener.
                           progressBar.setVisibility(View.GONE);
                           if (!task.isSuccessful()) {
                               // there was an error
                               Toast.makeText(getApplicationContext(), "Authentication failed!!", Toast.LENGTH_LONG).show();
                           } else {
                               Intent intent = new Intent(MainActivity.this, EventsActivity.class);
                               startActivity(intent);

                           }
                       }
                   });
       }
   }
    public void signUp(View v){
        Intent registerIntent = new Intent(MainActivity.this, RegistrationActivity.class);
        if(registerIntent.resolveActivity(getPackageManager()) != null){
            startActivity(registerIntent);
        }
    }
    public void resetPwd(View v){
        Intent resetIntent = new Intent(MainActivity.this, ResetPasswordActivity.class);
        if(resetIntent.resolveActivity(getPackageManager()) != null){
            startActivity(resetIntent);
        }
    }
}
