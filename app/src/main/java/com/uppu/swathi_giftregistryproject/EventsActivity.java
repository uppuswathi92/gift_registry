package com.uppu.swathi_giftregistryproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;
    //private static ArrayList<Integer> removedItems;
    private String[] eventsList = {"My Events", "Other Events"};
    static Integer[] drawableArray = {R.drawable.myevents, R.drawable.invitee};
    private String username;
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

        /*data = new ArrayList<DataModel>();
        for (int i = 0; i < eventsList.length; i++) {
            data.add(new DataModel(
                    eventsList[i],
                    drawableArray[i]
            ));
        }*/

//        removedItems = new ArrayList<Integer>();

        //adapter = new CustomAdapter(data);
        adapter = new CustomAdapter(eventsList);
        recyclerView.setAdapter(adapter);
        Intent getUser = getIntent();
        username = getUser.getStringExtra("username");
    }
    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();
            int selectedItemId = -1;
            if(selectedName.equals(eventsList[0])){
                Intent myEventActivity = new Intent(EventsActivity.this, MyEvents.class);
                myEventActivity.putExtra("username",username);
                startActivity(myEventActivity);
            }
            if(selectedName.equals(eventsList[1])){
                Intent otherEventActivity = new Intent(EventsActivity.this, OtherEventsActivity.class);
                otherEventActivity.putExtra("username",username);
                startActivity(otherEventActivity);
            }
            /*for (int i = 0; i < nameArray.length; i++) {
                if (selectedName.equals(nameArray[i])) {
                    selectedItemId = MyData.id_[i];
                }
            }*/
           // removedItems.add(selectedItemId);
            //data.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    }
}
