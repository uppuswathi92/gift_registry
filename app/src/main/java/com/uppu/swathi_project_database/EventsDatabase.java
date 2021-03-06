package com.uppu.swathi_project_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.EventLog;

import com.uppu.swathi_giftregistryproject.Events;

import java.util.ArrayList;

public class EventsDatabase  extends SQLiteOpenHelper {
    //Database helper for Events
    public static final String database_name = "events.db";
    public static final String table_name = "Events";
    public static final String col1 = "eventId";
    public static final String col2 = "eventName";
    public static final String col3 = "eventAddress";
    public static final String col4 = "eventDateTime";
    InviteesDatabase inviteeDb;
    ProductsDatabase productsDb;
    ContentValues contentValues;

    public EventsDatabase(Context context) {
        super(context, table_name, null, 1);
        inviteeDb = new InviteesDatabase(context);
        productsDb = new ProductsDatabase(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating the table
        db.execSQL("create table "+ table_name + "("+col1+" TEXT PRIMARY KEY, "+col2 + " TEXT, " +  col3 + " TEXT, " +  col4 + " TEXT )");
    }

    //invoked when database table is updated with new version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }

    //used to insert an event in table
    public boolean addEvent(String eventName, String eventAddress, String eventDate, String username){
        //Using contentValues to store the values to insert in database
        SQLiteDatabase db =  this.getWritableDatabase();
        contentValues = new ContentValues();
        int eventId = createEventId();
        contentValues.put(col1,eventId);
        contentValues.put(col1,eventId);
        contentValues.put(col2,eventName);
        contentValues.put(col3,eventAddress);
        contentValues.put(col4,eventDate);
        //inserting into db
        long result = db.insert(table_name, null,contentValues);
        if(result == -1){
            return false;
        }else{
            //if event has been added then user is added to invitee table with isHost as true
            inviteeDb.createInvitee(eventId, username, true);
            return true;
        }
    }

    //creates random event id
    public int createEventId(){
        int id = (int) Math.round((Math.random()) *100000);
        //checks if event id already exists in database
        while(validEventId(id)){
            id = (int) Math.round((Math.random()) * 100000);
        }
        return id;
    }

    //checks if event id already exists in database
    public boolean validEventId(int id){
        boolean getCount = false;
        SQLiteDatabase db= this.getWritableDatabase();
        String[] columns = {"eventId"};
        //? represents that we are going to give those values in selectionArgs
        String selection = "eventId = ?";
        String[] selectionArgs = {""+id};
        //sending the query and values to search in db
        Cursor cursor =db.query(table_name, columns,selection,selectionArgs,null,null,null);
        //if cusor count > 0 then account exists else account does not exists

        if(cursor != null && !cursor.isClosed()){
            int count = cursor.getCount();
            getCount = (count>0);
            cursor.close();
        }
        return getCount;
    }

    //gets all events for the given username
    public ArrayList<Events> getEvents(String username){
        ArrayList<Events> allEvents = new ArrayList<Events>();
        //gets all the eventsIds related to the user
        ArrayList<String> userEvents = inviteeDb.getUserEventIds(username);
        SQLiteDatabase db = this.getWritableDatabase();
        //gets all the eventDetails based the eventIds retrieved
        for(String userEvent: userEvents){
            String rawQuery = "select * from "+table_name + " where eventId = " +userEvent;
            //cursor provides read-write access to the result set returned by database query
            Cursor cursor = db.rawQuery(rawQuery, null);
            while (cursor.moveToNext()){
                int eId = cursor.getInt(cursor.getColumnIndex(col1));
                String eName = cursor.getString(cursor.getColumnIndex(col2));
                String eAddress = cursor.getString(cursor.getColumnIndex(col3));
                String eDate = cursor.getString(cursor.getColumnIndex(col4));
                Events e = new Events(eId, eName, eAddress, eDate);
                allEvents.add(e);
            }
        }
        return allEvents;
    }

    //gets all the eventsIds related to the user
    public ArrayList<String> getUserEventIds(String username){
        ArrayList<String> eventIds = new ArrayList<String>();
        SQLiteDatabase db= this.getWritableDatabase();
        String rawQuery = "select eventId from Invitees"+ " where inviteeName = '"+username + "'";
        Cursor cursor = db.rawQuery(rawQuery, null);
        //if cusor count > 0 then record exists else account does not exists
        while (cursor.moveToNext()){
            eventIds.add(cursor.getString(cursor.getColumnIndex(col1)));
        }
        return eventIds;
    }

    //gets all events user has been invited to
    public ArrayList<Events> getOtherEvents(String username){
        ArrayList<Events> allEvents = new ArrayList<Events>();
        //gets all the eventsIds for which user has been invited to
        ArrayList<String> otherUserEvents = inviteeDb.getOtherEventIds(username);
        SQLiteDatabase db = this.getWritableDatabase();
        for(String userEvent: otherUserEvents){
            String rawQuery = "select * from "+table_name + " where eventId = " +userEvent;
            Cursor cursor = db.rawQuery(rawQuery, null);
            while (cursor.moveToNext()){
                int eId = cursor.getInt(cursor.getColumnIndex(col1));
                String eName = cursor.getString(cursor.getColumnIndex(col2));
                String eAddress = cursor.getString(cursor.getColumnIndex(col3));
                String eDate = cursor.getString(cursor.getColumnIndex(col4));
                Events e = new Events(eId, eName, eAddress, eDate);
                allEvents.add(e);
            }
        }
        return allEvents;
    }

    //gets event details based on event id
    public Events getEventById(String eventId){
        //Checks if user details exists in database
        Events event = null;
        SQLiteDatabase db= this.getWritableDatabase();
        String rawQuery = "select * from Events"+ " where eventId = '"+eventId + "'";
        Cursor cursor = db.rawQuery(rawQuery, null);
        //if cusor count > 0 then account exists else account does not exists
        int eId = Integer.parseInt(eventId);
        while (cursor.moveToNext()){
            event = new Events(eId, cursor.getString(cursor.getColumnIndex(col2)), cursor.getString(cursor.getColumnIndex(col3)), cursor.getString(cursor.getColumnIndex(col4)));
        }
        return event;
    }

    //update an event based on event id
    public boolean updateEvent(String eventId, String eventName, String eventAdd, String eventDateTime){
        //using content values to store the updated password, where the employee id entered by user
        ContentValues values = new ContentValues();
        values.put(col2, eventName);
        values.put(col3, eventAdd);
        values.put(col4, eventDateTime);
        String where = "eventId = ?";
        String[] whereArgs = { eventId };
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updateSuccessful = db.update(table_name, values, where, whereArgs) > 0;
        db.close();
        return updateSuccessful;
    }

    //delete an event based on event id
    public boolean deleteEvent(String eventId){
        //deletes records from invitee and products tables with respect to that eventid
        inviteeDb.deleteInvitee(eventId);
        productsDb.deleteProduct(eventId);
        //query for deleting record based on eventId
        boolean deleteSuccessful = false;
        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete(table_name, "eventId ='" + eventId + "'", null) > 0;
        db.close();
        return deleteSuccessful;
    }

}
