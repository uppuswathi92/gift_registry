package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.EventsDatabase;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {
    private String username, selectedEvent, selectedAddress;
    private EventsDatabase myDb;
    private ArrayList<String> eventNames;
    private ArrayList<Integer> eventIds;
    private Spinner spinner;
    private ArrayList<Events> allEvents;
    private TextView eventName, eventAddress, eventDateTime;
    private Button getDetails, getDirections;
    private ListView eventDetails;
    private ArrayAdapter<String> adapter;
    private ImageView signout_button;
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
        eventDetails = (ListView) findViewById(R.id.eventDetailsList);
        getDetails = (Button) findViewById(R.id.getDetails);
        getDirections = (Button) findViewById(R.id.getDirection);
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EventDetailsActivity.this, MainActivity.class));
            }
        });
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
    public void getEventDetails(View v) {
        ArrayList<String> eventDet = new ArrayList<>();
        for(Events event: allEvents){
            if(event.getEventName().equals(selectedEvent)){
                getDirections.setVisibility(View.VISIBLE);
                eventDet.add(event.getEventName());
                eventDet.add(event.getEventDate());
                eventDet.add(event.getEventAddress());
                selectedAddress = event.getEventAddress();
            }
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, eventDet);
        eventDetails.setAdapter(adapter);
    }
    public void getDirections(View v){
        Intent mapIntent = new Intent(EventDetailsActivity.this, AddressMapActivity.class);
        mapIntent.putExtra("address", selectedAddress);
        startActivity(mapIntent);
    }
}
