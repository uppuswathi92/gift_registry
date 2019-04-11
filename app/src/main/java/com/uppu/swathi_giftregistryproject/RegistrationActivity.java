package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uppu.swathi_project_database.GiftRegistryDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    private EditText username, password, email, firstname, lastname, phone;
    private TextView mandatory;
    private boolean isValid;
    private GiftRegistryDatabase myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username = (EditText) findViewById(R.id.uname);
        password = (EditText) findViewById(R.id.pwd);
        email = (EditText) findViewById(R.id.email);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        phone = (EditText) findViewById(R.id.phone);
        mandatory = (TextView) findViewById(R.id.mandatory);
        myDb = new GiftRegistryDatabase(this);
    }
    public void register(View v){
        if(!validateDetails()){
            mandatory.setVisibility(View.GONE);
            //Registering the user by invoking insertData method of database helper
            boolean registered = myDb.registerUser(username.getText().toString(), password.getText().toString(), email.getText().toString(), firstname.getText().toString(), lastname.getText().toString(), phone.getText().toString());
            if(registered){
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                intent.putExtra("successMsg", getString(R.string.register_success));
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Registered! Please login", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Not registered!", Toast.LENGTH_LONG).show();
            }
        }
    }
    public boolean validateDetails(){

        List<EditText> list = new ArrayList<>();
        list.add(username);
        list.add(password);
        list.add(email);
        list.add(firstname);
        list.add(lastname);
        list.add(phone);
        String[] details = new String[] {"Username", "Password", "Email Address", "First Name", "Last Name", "Phone Number"};
        for(int i=0; i<list.size();i++){
            isValid = false;
            String detail = list.get(i).getText().toString();
            if(detail.length() == 0){
                list.get(i).setError("Please enter " + details[i]);
                mandatory.setVisibility(View.VISIBLE);
                isValid = true;
            }
        }
        return isValid;
    }
}
