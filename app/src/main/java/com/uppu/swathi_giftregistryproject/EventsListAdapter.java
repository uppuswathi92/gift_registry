package com.uppu.swathi_giftregistryproject;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class EventsListAdapter  extends ArrayAdapter<Events> {
    private Context context;
    private ArrayList<Events> events;
    public EventsListAdapter(@NonNull Context context, ArrayList<Events> eventslist) {
        super(context,0, eventslist);
        this.context = context;
        events = eventslist;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.events_custom_layout,parent,false);

        TextView name = (TextView) listItem.findViewById(R.id.eventName);
        name.setText(events.get(position).getEventName());

        TextView datetime = (TextView) listItem.findViewById(R.id.eventDate);
        datetime.setText(events.get(position).getEventDate());

        return listItem;
    }
}
