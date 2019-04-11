package com.uppu.swathi_project_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uppu.swathi_giftregistryproject.Events;

import java.util.ArrayList;

public class EventsDatabase  extends SQLiteOpenHelper {
    public static final String database_name = "events.db";
    public static final String table_name = "Events";
    public static final String col1 = "eventId";
    public static final String col2 = "eventName";
    public static final String col3 = "eventAddress";
    public static final String col4 = "eventDateTime";
    InviteesDatabase inviteeDb;
    ContentValues contentValues;

    public EventsDatabase(Context context) {

        super(context, table_name, null, 1);
        inviteeDb = new InviteesDatabase(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating the table
        db.execSQL("create table "+ table_name + "("+col1+" TEXT PRIMARY KEY, "+col2 + " TEXT, " +  col3 + " TEXT, " +  col4 + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }
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
            inviteeDb.createInvitee(eventId, username, true);
            return true;
        }
    }

    public int createEventId(){
        int id = (int) Math.round((Math.random()) *100000);
        while(validEventId(id)){
            id = (int) Math.round((Math.random()) * 100000);
        }
        return id;
    }

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

    public ArrayList<Events> getEvents(String username){
        ArrayList<Events> allEvents = new ArrayList<Events>();
        ArrayList<String> userEvents = inviteeDb.getUserEventIds(username);
        SQLiteDatabase db = this.getWritableDatabase();
        for(String userEvent: userEvents){
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
        /*String[] columns = {col1,col2,col3,col4};
        Cursor cursor =db.query(table_name,columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            int eId = cursor.getInt(cursor.getColumnIndex(col1));
            String eName = cursor.getString(cursor.getColumnIndex(col2));
            String eAddress = cursor.getString(cursor.getColumnIndex(col3));
            String eDate = cursor.getString(cursor.getColumnIndex(col4));
            Events e = new Events(eId, eName, eAddress, eDate);
            allEvents.add(e);
        }*/
        return allEvents;
    }
    public ArrayList<String> getUserEventIds(String username){
        //Checks if user details exists in database
        ArrayList<String> eventIds = new ArrayList<String>();
        SQLiteDatabase db= this.getWritableDatabase();
        String rawQuery = "select eventId from Invitees"+ " where inviteeName = '"+username + "'";
        //sending the query and values to search in db
        //Cursor cursor =db.query(table_name, columns,selection,selectionArgs,null,null,null);
        Cursor cursor = db.rawQuery(rawQuery, null);
        //if cusor count > 0 then account exists else account does not exists
        while (cursor.moveToNext()){
            eventIds.add(cursor.getString(cursor.getColumnIndex(col1)));
        }
        return eventIds;
    }
    public ArrayList<Events> getOtherEvents(String username){
        ArrayList<Events> allEvents = new ArrayList<Events>();
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

}
