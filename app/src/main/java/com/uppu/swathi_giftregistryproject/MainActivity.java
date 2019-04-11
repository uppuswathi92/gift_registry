package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uppu.swathi_project_database.GiftRegistryDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private GiftRegistryDatabase myDb;
    private TextView success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.emailAdd);
        password = (EditText) findViewById(R.id.password);
        myDb = new GiftRegistryDatabase(this);
        success = (TextView) findViewById(R.id.success);
        Intent intent = getIntent();
        String successMsg = intent.getStringExtra("successMsg");
        if(successMsg != null){
            success.setText(successMsg);
            success.setVisibility(View.VISIBLE);
        }
    }
    public void signIn(View v){
        success.setVisibility(View.GONE);
        //checking if the credentials entered by user is valid by invoking login method of DatabaseHelper
        String userExists = myDb.login(email.getText().toString(),password.getText().toString());
        if(userExists != null){
            Toast.makeText(getApplicationContext(),"Logged in", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, EventsActivity.class);
            intent.putExtra("username",userExists);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Not Logged in",Toast.LENGTH_LONG).show();
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
