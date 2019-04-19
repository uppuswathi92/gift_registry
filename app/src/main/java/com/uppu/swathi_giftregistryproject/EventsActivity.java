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
    private String[] eventsList = {"My Events", "Other Events"};
    private Integer[] drawableArray = {R.drawable.myevents, R.drawable.invitee};
    private String username;
    private ImageView signout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        myOnClickListener = new MyOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EventsActivity.this, MainActivity.class));
            }
        });
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
    private class MyOnClickListener implements View.OnClickListener {
        private final Context context;
        private MyOnClickListener(Context context) {
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            setIntent(v);
        }
        private void setIntent(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
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
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    }
}
