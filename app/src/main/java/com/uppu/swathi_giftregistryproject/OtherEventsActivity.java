package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.uppu.swathi_project_database.EventsDatabase;

import java.util.ArrayList;

public class OtherEventsActivity extends AppCompatActivity {
    private String username;
    private EventsDatabase myDb;
    ArrayList<String> eventNames;
    ArrayList<Integer> eventIds;
    ListView otherEventsList;
    ArrayAdapter eventsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_events);
        username = getIntent().getStringExtra("username");
        otherEventsList = (ListView) findViewById(R.id.other_events_list);
        otherEventsList.setFocusable(false);
        otherEventsList.setFocusableInTouchMode(false);
        otherEventsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myDb = new EventsDatabase(this);
        eventNames = new ArrayList<String>();
        eventIds = new ArrayList<Integer>();
        getOtherEvents();
    }
    public void getOtherEvents(){
        ArrayList<Events> allEvents = myDb.getOtherEvents(username);
        if(allEvents.size() > 0){
            for(Events event: allEvents){
                eventNames.add(event.getEventName());
                eventIds.add(event.getEventId());
            }
            eventsAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_expandable_list_item_1, eventNames);
            otherEventsList.setAdapter(eventsAdapter);
            otherEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(OtherEventsActivity.this, OtherEventsProductsActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("eventId", ""+eventIds.get(position));
                    startActivity(intent);
                }
            });
        }else{
            //noEvents.setVisibility(View.VISIBLE);
        }
    }
}
