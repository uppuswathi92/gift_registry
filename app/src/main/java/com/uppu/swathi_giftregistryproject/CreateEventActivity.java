package com.uppu.swathi_giftregistryproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.uppu.swathi_project_database.EventsDatabase;
import com.uppu.swathi_project_database.InviteesDatabase;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {
    private EditText eventName, eventAddress, eventDateTime;
    private EventsDatabase myDb;
    private InviteesDatabase inviteeDb;
    private String username, eventId, status;
    Button btnDatePicker,addEvent;
    EditText eventDate, eventTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
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
        eventId = getIntent().getStringExtra("eventId");
        status = getIntent().getStringExtra("status");
        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), eventId, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();
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
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                eventDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
    public void addEvent(View v){
        String eventDateTime = eventDate.getText().toString() + " at " + eventTime.getText().toString();
        if(status.equals("new")){
            boolean added = myDb.addEvent(eventName.getText().toString(), eventAddress.getText().toString(), eventDateTime, username);
            if(added){
                Intent intent = new Intent(CreateEventActivity.this, MyEvents.class);
                intent.putExtra("username", username);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Event added", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Event not added", Toast.LENGTH_LONG).show();
            }
        }else{
            updateEvent(eventDateTime);
        }
    }
    public void updateEvent(String eventDateTime){
        boolean updated = myDb.updateEvent(eventId,eventName.getText().toString(), eventAddress.getText().toString(), eventDateTime );
        if(updated){
            Intent eventsIntent  = new Intent(CreateEventActivity.this, MyEvents.class);
            eventsIntent.putExtra("username", username);
            startActivity(eventsIntent);
            Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "not updated", Toast.LENGTH_SHORT).show();
        }
    }
    public void getTimePicker(View v){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        eventTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
