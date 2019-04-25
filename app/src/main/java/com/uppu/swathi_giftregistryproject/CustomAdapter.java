package com.uppu.swathi_giftregistryproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private String[] eventsList;

    public CustomAdapter(String[] eventsList){
        this.eventsList = eventsList;
    }

    //viewholder is used to describe the items and the metadata
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //Initializing the text view and image view
        TextView textViewName;
        ImageView imageViewIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    //returns the view holder inflated with the layout
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);
        view.setOnClickListener(EventsActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    //binding the data to the view
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        //seeting the textview and imageview content
        TextView textViewName = holder.textViewName;
        ImageView imageView = holder.imageViewIcon;
        textViewName.setText(eventsList[listPosition]);
        if(eventsList[listPosition].equals("My Events")){
            imageView.setImageResource(R.drawable.myevents);
        }
        else if(eventsList[listPosition].equals("Other Events")){
            imageView.setImageResource(R.drawable.invitee);
        }
        else if(eventsList[listPosition].equals("How To Use")){
            imageView.setImageResource(R.drawable.howtouse);
        }
    }

    //this returns the count of the item to display
    @Override
    public int getItemCount() {
        return eventsList.length;
    }
}
