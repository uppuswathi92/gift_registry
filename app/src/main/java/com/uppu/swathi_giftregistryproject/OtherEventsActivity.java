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
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.EventsDatabase;

import java.util.ArrayList;

public class OtherEventsActivity extends AppCompatActivity {
    private String username;
    private EventsDatabase myDb;
    private ArrayList<String> eventNames, eventDates;
    private ArrayList<Integer> eventIds;
    private ListView otherEventsList;
    private EventsListAdapter adapter;
    private ImageView signout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OtherEventsActivity.this, MainActivity.class));
            }
        });
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
        if(allEvents.size() > 0) {
            for (Events event : allEvents) {
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
                    intent.putExtra("eventId", "" + eventIds.get(position));
                    startActivity(intent);
                }
            });
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
                startActivity(eventsIntent);
                return true;
            case R.id.purchasedProducts:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
