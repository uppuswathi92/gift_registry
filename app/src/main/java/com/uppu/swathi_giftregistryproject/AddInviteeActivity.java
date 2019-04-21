package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.InviteesDatabase;

import org.w3c.dom.Text;

public class AddInviteeActivity extends AppCompatActivity {
    private String eventId;
    private InviteesDatabase myDb;
    private EditText email;
    private TextView mandatory;
    private boolean isValid;
    private ImageView signout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitee);
        Intent getIntent = getIntent();
        eventId = getIntent.getStringExtra("eventId");
        email = (EditText) findViewById(R.id.inviteeEmail);
        mandatory = (TextView) findViewById(R.id.mandatory);
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AddInviteeActivity.this, MainActivity.class));
            }
        });
        myDb = new InviteesDatabase(this);
    }
    public boolean validateDetails(){
        isValid = false;
        if (TextUtils.isEmpty(email.getText().toString())) {
            mandatory.setVisibility(View.VISIBLE);
            email.setError("Please enter email address!");
            isValid = true;
        }
        return isValid;
    }
    public void addInvitee(View v){
        if(!validateDetails()){
            boolean addInvitee = myDb.addInvitee(eventId, email.getText().toString());
            if(addInvitee){
                startActivity(new Intent(AddInviteeActivity.this, MyEvents.class));
                Toast.makeText(getApplicationContext(), "Invitee has been added!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Invitee not added", Toast.LENGTH_LONG).show();
            }
        }
    }
}
