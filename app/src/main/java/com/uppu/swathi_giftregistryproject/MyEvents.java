package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uppu.swathi_project_database.EventsDatabase;
import com.uppu.swathi_project_database.InviteesDatabase;

import java.util.ArrayList;

public class MyEvents extends AppCompatActivity {
    EventsDatabase myDb;
    ListView eventsList;
    TextView noEvents;
    ArrayAdapter eventsAdapter;
    ArrayList<String> eventNames;
    ArrayList<Integer> eventIds;
    private String username;
    private InviteesDatabase inviteesDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        myDb = new EventsDatabase(this);
        eventsList = (ListView) findViewById(R.id.events_list);
        noEvents = (TextView) findViewById(R.id.no_events);
        eventNames = new ArrayList<String>();
        eventIds = new ArrayList<Integer>();
        Intent getUser = getIntent();
        username = getUser.getStringExtra("username");
        inviteesDatabase = new InviteesDatabase(this);
        getEvents();
    }
    public void getEvents(){
        ArrayList<Events> allEvents = myDb.getEvents(username);
        if(allEvents.size() > 0){
            for(Events event: allEvents){
                eventNames.add(event.getEventName());
                eventIds.add(event.getEventId());
            }
            eventsAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_expandable_list_item_1, eventNames);
            eventsList.setAdapter(eventsAdapter);
            eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyEvents.this, MyEventProducts.class);
                    intent.putExtra("eventId", ""+eventIds.get(position));
                    startActivity(intent);
                }
            });
            eventsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), ""+eventIds.get(position), Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }else{
            noEvents.setVisibility(View.VISIBLE);
        }

    }
    public void createEvent(View v){
        Intent createIntent = new Intent(MyEvents.this, CreateEventActivity.class);
        createIntent.putExtra("username", username);
        startActivity(createIntent);
    }
}
