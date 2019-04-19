package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uppu.swathi_project_database.EventsDatabase;
import com.uppu.swathi_project_database.InviteesDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.ListAdapter;

public class MyEvents extends AppCompatActivity {
    private EventsDatabase myDb;
    private ListView eventsList;
    private TextView noEvents;
    private ArrayList<String> eventNames, eventDates;
    private ArrayList<Integer> eventIds;
    private String username;
    private InviteesDatabase inviteesDatabase;
    private ArrayList<Events> allEvents;
    private EventsListAdapter adapter;
    private ImageView signout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myDb = new EventsDatabase(this);
        eventsList = (ListView) findViewById(R.id.events_list);
        noEvents = (TextView) findViewById(R.id.no_events);
        eventNames = new ArrayList<String>();
        eventIds = new ArrayList<Integer>();
        eventDates = new ArrayList<String>();
        allEvents = new ArrayList<Events>();
        inviteesDatabase = new InviteesDatabase(this);
        getEvents();
        ArrayList<Map<String, String>> list = buildData();
        String[] from = { "name", "datetime" };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        SimpleAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2, from, to);
        registerForContextMenu(eventsList);
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MyEvents.this, MainActivity.class));
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_events_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()){
            case R.id.addInvitee:
                Intent inviteeIntent = new Intent(MyEvents.this, AddInviteeActivity.class);
                inviteeIntent.putExtra("eventId", ""+eventIds.get(index));
                startActivity(inviteeIntent);
                return true;
            case R.id.updateEvent:
                Intent updateIntent = new Intent(MyEvents.this, CreateEventActivity.class);
                updateIntent.putExtra("eventId", ""+eventIds.get(index));
                updateIntent.putExtra("status", "update");
                startActivity(updateIntent);
                return true;
            case R.id.deleteEvent:
                myDb.deleteEvent(""+eventIds.get(index));
                getEvents();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    public void getEvents(){
        allEvents = myDb.getEvents(username);
        if(allEvents.size() > 0){
            for(Events event: allEvents){
                eventNames.add(event.getEventName());
                eventIds.add(event.getEventId());
                eventDates.add(event.getEventDate());
            }
            adapter = new EventsListAdapter(this, allEvents);
            eventsList.setAdapter(adapter);
            eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyEvents.this, MyEventProducts.class);
                    intent.putExtra("eventId", ""+eventIds.get(position));
                    startActivity(intent);
                }
            });
        }else{
            noEvents.setVisibility(View.VISIBLE);
        }

    }
    private ArrayList<Map<String, String>> buildData() {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for(Events event: allEvents){
            list.add(putData("name", event.getEventName()));
            list.add(putData("datetime", event.getEventDate()));
        }

        return list;
    }
    private HashMap<String, String> putData(String name, String datetime) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("name", name);
        item.put("datetime", datetime);
        return item;
    }
    public void createEvent(View v){
        Intent createIntent = new Intent(MyEvents.this, CreateEventActivity.class);
        createIntent.putExtra("status", "new");
        startActivity(createIntent);
    }
}
