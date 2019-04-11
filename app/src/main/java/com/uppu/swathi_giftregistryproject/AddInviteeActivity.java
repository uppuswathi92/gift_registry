package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uppu.swathi_project_database.InviteesDatabase;

public class AddInviteeActivity extends AppCompatActivity {
    private String eventId;
    private InviteesDatabase myDb;
    private EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitee);
        Intent getIntent = getIntent();
        eventId = getIntent.getStringExtra("eventId");
        email = (EditText) findViewById(R.id.inviteeEmail);
        myDb = new InviteesDatabase(this);
    }
    public void addInvitee(View v){
        boolean addInvitee = myDb.addInvitee(eventId, email.getText().toString());
        if(addInvitee){
            Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "not added", Toast.LENGTH_SHORT).show();
        }
    }
}
