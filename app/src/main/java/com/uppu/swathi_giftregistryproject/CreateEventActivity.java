package com.uppu.swathi_giftregistryproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.EventsDatabase;
import com.uppu.swathi_project_database.InviteesDatabase;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {
    private EditText eventName, eventAddress, eventDateTime, eventDate, eventTime;
    private EventsDatabase myDb;
    private InviteesDatabase inviteeDb;
    private String username, eventId, status;
    private Button btnDatePicker,addEvent;
    private int year, month, day, hour, minute;
    private boolean isValid;
    private TextView mandatory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventName = (EditText) findViewById(R.id.eventName);
        eventAddress = (EditText) findViewById(R.id.address);
        eventDateTime = (EditText) findViewById(R.id.eventDate);
        inviteeDb = new InviteesDatabase(this);
        myDb = new EventsDatabase(this);
        mandatory = (TextView) findViewById(R.id.mandatory);
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        eventId = getIntent().getStringExtra("eventId");
        status = getIntent().getStringExtra("status");
        eventDate=(EditText)findViewById(R.id.eventDate);
        eventTime=(EditText)findViewById(R.id.eventTime);
        addEvent = (Button) findViewById(R.id.addEvent);
        if(status.equals("update")){
            getEventById();
        }

    }
    public void getEventById(){
        Events event = myDb.getEventById(eventId);
        if(event != null){
            eventName.setText(event.getEventName());
            eventAddress.setText(event.getEventAddress());
            String[] datetime  = event.getEventDate().split(" at ");
            eventDate.setText(datetime[0]);
            eventTime.setText(datetime[1]);
            addEvent.setText("Update Event");
        }
    }
    public void getDatePicker(View v){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                eventDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();

    }
    public void addEvent(View v){
        if(!validateDetails()) {
            String eventDateTime = eventDate.getText().toString() + " at " + eventTime.getText().toString();
            if (status.equals("new")) {
                boolean added = myDb.addEvent(eventName.getText().toString(), eventAddress.getText().toString(), eventDateTime, username);
                if (added) {
                    Intent intent = new Intent(CreateEventActivity.this, MyEvents.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Event has been added!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Event not added!", Toast.LENGTH_LONG).show();
                }
            } else {
                updateEvent(eventDateTime);
            }
        }
    }
    public void updateEvent(String eventDateTime){
        boolean updated = myDb.updateEvent(eventId,eventName.getText().toString(), eventAddress.getText().toString(), eventDateTime );
        if(updated){
            Intent eventsIntent  = new Intent(CreateEventActivity.this, MyEvents.class);
            startActivity(eventsIntent);
            Toast.makeText(getApplicationContext(), "Event updated!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Event not updated!", Toast.LENGTH_SHORT).show();
        }
    }
    public void getTimePicker(View v){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        eventTime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }
    public boolean validateDetails(){
        isValid = false;
        if (TextUtils.isEmpty(eventName.getText().toString()) || TextUtils.isEmpty(eventAddress.getText().toString()) || TextUtils.isEmpty(eventDate.getText().toString()) || TextUtils.isEmpty(eventTime.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter all the details", Toast.LENGTH_SHORT).show();
            mandatory.setVisibility(View.VISIBLE);
            isValid = true;
            if(TextUtils.isEmpty(eventName.getText().toString())){
                eventName.setError("Please enter event name!");
            }
            if(TextUtils.isEmpty(eventAddress.getText().toString())){
                eventAddress.setError("Please enter event address!");
            }
            if(TextUtils.isEmpty(eventDate.getText().toString())){
                eventDate.setError("Please enter event date!");
            }
            if(TextUtils.isEmpty(eventTime.getText().toString())){
                eventTime.setError("Please enter event time!");
            }
        }
        return isValid;
    }
}
