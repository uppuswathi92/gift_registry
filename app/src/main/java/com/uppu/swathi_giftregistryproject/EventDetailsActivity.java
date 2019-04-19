package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.EventsDatabase;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {
    private String username, selectedEvent;
    private EventsDatabase myDb;
    private ArrayList<String> eventNames;
    private ArrayList<Integer> eventIds;
    private Spinner spinner;
    private ArrayList<Events> allEvents;
    private TextView eventName, eventAddress, eventDateTime;
    private Button getDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myDb = new EventsDatabase(this);
        spinner = (Spinner) findViewById(R.id.events);
        eventNames = new ArrayList<String>();
        eventIds = new ArrayList<Integer>();
        eventName = (TextView) findViewById(R.id.eventName);
        eventAddress = (TextView) findViewById(R.id.eventAddress);
        eventDateTime = (TextView) findViewById(R.id.eventDateTime);
        getDetails = (Button) findViewById(R.id.getDetails);
        getOtherEvents();
    }
    public void getOtherEvents(){
        allEvents = myDb.getOtherEvents(username);
        if(allEvents.size() > 0){
            for(Events event: allEvents){
                eventNames.add(event.getEventName());
                eventIds.add(event.getEventId());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, eventNames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            //Invoked on click on spinner
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedEvent = spinner.getSelectedItem().toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
    public void getEventDetails(View v){
        for(Events event: allEvents){
            if(event.getEventName().equals(selectedEvent)){
                eventName.setText(event.getEventName());
                eventAddress.setText(event.getEventAddress());
                eventDateTime.setText(event.getEventDate());
            }
        }
    }
}
