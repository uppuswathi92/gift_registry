package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uppu.swathi_project_database.EventsDatabase;
import com.uppu.swathi_project_database.InviteesDatabase;

public class CreateEventActivity extends AppCompatActivity {
    private EditText eventName, eventAddress, eventDateTime;
    private EventsDatabase myDb;
    private InviteesDatabase inviteeDb;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventName = (EditText) findViewById(R.id.eventName);
        eventAddress = (EditText) findViewById(R.id.address);
        eventDateTime = (EditText) findViewById(R.id.eventDate);
        inviteeDb = new InviteesDatabase(this);
        myDb = new EventsDatabase(this);
        Intent getUser = getIntent();
        username = getUser.getStringExtra("username");
        Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();
    }
    public void addEvent(View v){
        boolean added = myDb.addEvent(eventName.getText().toString(), eventAddress.getText().toString(), eventDateTime.getText().toString(), username);
        if(added){
            Intent intent = new Intent(CreateEventActivity.this, MyEvents.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Event added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Event not added", Toast.LENGTH_LONG).show();
        }
    }
}
