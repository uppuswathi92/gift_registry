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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.uppu.swathi_project_database.EventsDatabase;
import com.uppu.swathi_project_database.InviteesDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.ListAdapter;

public class MyEvents extends AppCompatActivity {
    EventsDatabase myDb;
    ListView eventsList;
    TextView noEvents;
    ArrayAdapter eventsAdapter;
    ArrayList<String> eventNames;
    ArrayList<Integer> eventIds;
    ArrayList<String> eventDates;
    private String username;
    private InviteesDatabase inviteesDatabase;
    ArrayList<Events> allEvents;
    EventsListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        myDb = new EventsDatabase(this);
        eventsList = (ListView) findViewById(R.id.events_list);
        noEvents = (TextView) findViewById(R.id.no_events);
        eventNames = new ArrayList<String>();
        eventIds = new ArrayList<Integer>();
        eventDates = new ArrayList<String>();
        allEvents = new ArrayList<Events>();
        Intent getUser = getIntent();
        username = getUser.getStringExtra("username");
        inviteesDatabase = new InviteesDatabase(this);
        getEvents();
        ArrayList<Map<String, String>> list = buildData();
        String[] from = { "name", "datetime" };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        SimpleAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2, from, to);
        registerForContextMenu(eventsList);
       /* getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar_layout);
        View view =getSupportActionBar().getCustomView();

        ImageButton imageButton= (ImageButton)view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton imageButton2= (ImageButton)view.findViewById(R.id.action_bar_forward);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Forward Button is clicked",Toast.LENGTH_LONG).show();
            }
        });*/
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
                updateIntent.putExtra("username", username);
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
                    intent.putExtra("username", username);
                    intent.putExtra("eventId", ""+eventIds.get(position));
                    startActivity(intent);
                }
            });
            /*eventsAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, eventNames);
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
            });*/
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
        createIntent.putExtra("username", username);
        createIntent.putExtra("status", "new");
        startActivity(createIntent);
    }
}
