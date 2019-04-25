package com.uppu.swathi_giftregistryproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;
    //data for the cardview and recycler view
    private String[] eventsList = {"My Events", "Other Events", "How To Use"};
    private Integer[] drawableArray = {R.drawable.myevents, R.drawable.invitee, R.drawable.howtouse};
    private String username;
    private ImageView signout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        myOnClickListener = new MyOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //setting layout manager to linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Firebase signout functionality in toolbar
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EventsActivity.this, MainActivity.class));
            }
        });
        //setting the custom adapter to the recycler view
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
        //getting the email id of logged in user
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    //invoked on click of each card
    private class MyOnClickListener implements View.OnClickListener {
        private final Context context;
        private MyOnClickListener(Context context)
        {
            this.context = context;
        }
        @Override
        public void onClick(View v)
        {
            setIntent(v);
        }
        //setting the intent for each card
        private void setIntent(View v) {
            //get selected position
            int selectedItemPosition = recyclerView.getChildPosition(v);
            //get the view holder based on the selected position
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            //get the text view details in the view holder and based on that name , it is redirected to its respective page
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();
            if(selectedName.equals(eventsList[0])){
                Intent myEventActivity = new Intent(EventsActivity.this, MyEvents.class);
                startActivity(myEventActivity);
            }
            if(selectedName.equals(eventsList[1])){
                Intent otherEventActivity = new Intent(EventsActivity.this, OtherEventsActivity.class);
                startActivity(otherEventActivity);
            }
            if(selectedName.equals(eventsList[2])){
                Intent otherEventActivity = new Intent(EventsActivity.this, HowToUseActivity.class);
                startActivity(otherEventActivity);
            }
            //adapter is notified of any alterations
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    }
}
