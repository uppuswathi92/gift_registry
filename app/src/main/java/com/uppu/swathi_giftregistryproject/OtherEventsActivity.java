package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    ArrayList<String> eventNames, eventDates;
    ArrayList<Integer> eventIds;
    ListView otherEventsList;
    ArrayAdapter eventsAdapter;
    EventsListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        //not set the tool bar to act as action bar for this activity
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        username = getIntent().getStringExtra("username");
        otherEventsList = (ListView) findViewById(R.id.other_events_list);
        otherEventsList.setFocusable(false);
        otherEventsList.setFocusableInTouchMode(false);
        otherEventsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        myDb = new EventsDatabase(this);
        eventNames = new ArrayList<String>();
        eventIds = new ArrayList<Integer>();
        eventDates = new ArrayList<String>();
        getOtherEvents();
    }
    public void getOtherEvents(){
        ArrayList<Events> allEvents = myDb.getOtherEvents(username);
        if(allEvents.size() > 0){
            for(Events event: allEvents){
                eventNames.add(event.getEventName());
                eventIds.add(event.getEventId());
                eventDates.add(event.getEventDate());
            }
            adapter = new EventsListAdapter(this, allEvents);
            otherEventsList.setAdapter(adapter);
            otherEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(OtherEventsActivity.this, OtherEventsProductsActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("eventId", ""+eventIds.get(position));
                    startActivity(intent);
                }
            });
            /*eventsAdapter = new ArrayAdapter<String>(this,
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
            });*/
        }else{
            //noEvents.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other_events_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
            case R.id.getEventDetails:
                Intent eventsIntent = new Intent(OtherEventsActivity.this, EventDetailsActivity.class);
                eventsIntent.putExtra("username", username);
                startActivity(eventsIntent);
                return true;
            case R.id.purchasedProducts:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
