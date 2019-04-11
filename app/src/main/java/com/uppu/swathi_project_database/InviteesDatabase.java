package com.uppu.swathi_project_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InviteesDatabase extends SQLiteOpenHelper {
    public static final String database_name = "invitees.db";
    public static final String table_name = "Invitees";
    public static final String col1 = "eventId";
    public static final String col2 = "inviteeName";
    public static final String col3 = "isHost";
    ContentValues contentValues;
    private GiftRegistryDatabase registerDb;
    public InviteesDatabase(Context context) {

        super(context, table_name, null, 1);
        registerDb = new GiftRegistryDatabase(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ table_name + "("+col1+" TEXT,  "+col2 + " TEXT, " +  col3 + " BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }
    public boolean createInvitee(int eventId, String username, boolean isHost){
        //Using contentValues to store the values to insert in database
        SQLiteDatabase db =  this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(col1,eventId);
        contentValues.put(col2,username);
        contentValues.put(col3,isHost);
        //inserting into db
        long result = db.insert(table_name, null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<String> getUserEventIds(String username){
        //Checks if user details exists in database
        ArrayList<String> eventIds = new ArrayList<String>();
        SQLiteDatabase db= this.getWritableDatabase();
        String rawQuery = "select eventId from "+table_name+ " where inviteeName = '"+username + "' and isHost = 1";
        //sending the query and values to search in db
        //Cursor cursor =db.query(table_name, columns,selection,selectionArgs,null,null,null);
        Cursor cursor = db.rawQuery(rawQuery, null);
        //if cusor count > 0 then account exists else account does not exists
        while (cursor.moveToNext()){
            eventIds.add(cursor.getString(cursor.getColumnIndex(col1)));
        }
        return eventIds;
    }
    public boolean addInvitee(String eventId, String inviteeEmail){
        //Using contentValues to store the values to insert in database
        SQLiteDatabase db =  this.getWritableDatabase();
        contentValues = new ContentValues();
        String username = registerDb.getUsername(inviteeEmail);
        contentValues.put(col1,eventId);
        contentValues.put(col2,username);
        contentValues.put(col3,false);
        //inserting into db
        long result = db.insert(table_name, null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public ArrayList<String> getOtherEventIds(String username){
        //Checks if user details exists in database
        ArrayList<String> eventIds = new ArrayList<String>();
        SQLiteDatabase db= this.getWritableDatabase();
        String rawQuery = "select eventId from "+table_name+ " where inviteeName = '"+username + "' and isHost = 0";
        //sending the query and values to search in db
        //Cursor cursor =db.query(table_name, columns,selection,selectionArgs,null,null,null);
        Cursor cursor = db.rawQuery(rawQuery, null);
        //if cusor count > 0 then account exists else account does not exists
        while (cursor.moveToNext()){
            eventIds.add(cursor.getString(cursor.getColumnIndex(col1)));
        }
        return eventIds;
    }
}
