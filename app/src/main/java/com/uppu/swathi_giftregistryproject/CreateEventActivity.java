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
import android.widget.ImageView;
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
    private ImageView signout_button;
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
        //getting the email id of logged in user
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //Getting event id to update event to a that eventId
        eventId = getIntent().getStringExtra("eventId");
        //Based on the status received by previous intent, if its view details are disabled, if its new/update details are enabled to add/edit
        status = getIntent().getStringExtra("status");
        //if status is update we get event details to be prepopulated based on the event id
        if(status.equals("update")){
            getEventById();
        }
        eventDate=(EditText)findViewById(R.id.eventDate);
        eventTime=(EditText)findViewById(R.id.eventTime);
        addEvent = (Button) findViewById(R.id.addEvent);
        //Firebase signout functionality in toolbar
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreateEventActivity.this, MainActivity.class));
            }
        });
    }

    //retriving event details based on the eventId
    public void getEventById(){
        Events event = myDb.getEventById(eventId);
        //if that event exists in database then its respective details are pre-populated
        if(event != null){
            eventName.setText(event.getEventName());
            eventAddress.setText(event.getEventAddress());
            String[] datetime  = event.getEventDate().split(" at ");
            eventDate.setText(datetime[0]);
            eventTime.setText(datetime[1]);
            addEvent.setText("Update Event");
        }
    }
    //invoking datepicker on click of enter date edit text and once user selects date it is poulated in the edit text
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
        //disabling previous dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    //invoking timepick on click of enter time edit text and once user selects time it is poulated in the edit text
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

    //adding events once user has entered all the details
    public void addEvent(View v){
        if(!validateDetails()) {
            String eventDateTime = eventDate.getText().toString() + " at " + eventTime.getText().toString();
            //if status is new new event is added else event is updated
            if (status.equals("new")) {
                //invoking addEvent from EventsDatabase
                boolean added = myDb.addEvent(eventName.getText().toString(), eventAddress.getText().toString(), eventDateTime, username);
                //if its successful then it is redirected to MyEvents page
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

    //invoked  if status received is update
    public void updateEvent(String eventDateTime){
        //invoking updateEvent from EventsDatabase
        boolean updated = myDb.updateEvent(eventId,eventName.getText().toString(), eventAddress.getText().toString(), eventDateTime );
        //if its successful then it is redirected to MyEvents page
        if(updated){
            Intent eventsIntent  = new Intent(CreateEventActivity.this, MyEvents.class);
            startActivity(eventsIntent);
            Toast.makeText(getApplicationContext(), "Event updated!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Event not updated!", Toast.LENGTH_SHORT).show();
        }
    }

    //Validating if user has entered all details. if user has not entered all the details then error messages are displayed
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
