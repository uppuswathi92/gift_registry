package com.uppu.swathi_project_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GiftRegistryDatabase extends SQLiteOpenHelper {
    public static final String database_name = "registry_user.db";
    public static final String table_name = "Registry_User";
    public static final String col1 = "username";
    public static final String col2 = "password";
    public static final String col3 = "email";
    public static final String col4 = "firstname";
    public static final String col5 = "lastname";
    public static final String col6 = "phonenumber";
    ContentValues contentValues;

    public GiftRegistryDatabase(Context context) {
        super(context, table_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating the table
        db.execSQL("create table "+ table_name + "("+col1+" TEXT PRIMARY KEY, "+col2 + " TEXT, " +  col3 + " TEXT, " +  col4 + " TEXT, " +  col5 + " TEXT, " + col6 + " TEXT)");
    }
    public void createRegisterUserTable(SQLiteDatabase db){

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }
    public boolean registerUser(String username, String password, String email, String firstname, String lastname, String phonenumber){
        //Using contentValues to store the values to insert in database
        SQLiteDatabase db =  this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(col1,username);
        contentValues.put(col2,password);
        contentValues.put(col3,email);
        contentValues.put(col4,firstname);
        contentValues.put(col5,lastname);
        contentValues.put(col6,phonenumber);
        //inserting into db
        long result = db.insert(table_name, null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public String login(String email, String password){
        String username = null;
        //Checks if user details exists in database
        SQLiteDatabase db= this.getWritableDatabase();
        String[] columns = {"email"};
        //? represents that we are going to give those values in selectionArgs
        String selection = "email = ? and password = ?";
        String[] selectionArgs = {email, password};
        String rawQuery = "select username from "+table_name + " where email = '"+email + "' and password = '" + password+"'";
        //sending the query and values to search in db
        //Cursor cursor =db.query(table_name, columns,selection,selectionArgs,null,null,null);
        Cursor cursor = db.rawQuery(rawQuery, null);
        //if cusor count > 0 then account exists else account does not exists
        while (cursor.moveToNext()){
            username = cursor.getString(cursor.getColumnIndex(col1));
        }
        cursor.close();
        close();
        return username;
        /*if(count >0){
            return true;
        }else {
            return false;
        }*/
    }
    public boolean validUsername(String email){
        //Checks if user details exists in database
        SQLiteDatabase db= this.getWritableDatabase();
        String[] columns = {"email"};
        //? represents that we are going to give those values in selectionArgs
        String selection = "email = ?";
        String[] selectionArgs = {email};
        //sending the query and values to search in db
        Cursor cursor =db.query(table_name, columns,selection,selectionArgs,null,null,null);
        //if cusor count > 0 then account exists else account does not exists
        int count = cursor.getCount();
        cursor.close();
        close();
        if(count >0){
            return true;
        }else {
            return false;
        }
    }

    public boolean updatePassword(String email, String password) {
        //using content values to store the updated password, where the username entered by user
        ContentValues values = new ContentValues();
        values.put(col2, password);
        String where = "email = ?";
        String[] whereArgs = { ""+email };
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updateSuccessful = db.update(table_name, values, where, whereArgs) > 0;
        db.close();
        return updateSuccessful;
    }

    public String getUsername(String email){
        String uname = "";
        //Checks if user details exists in database
        SQLiteDatabase db= this.getWritableDatabase();
        String[] columns = {"email"};
        //? represents that we are going to give those values in selectionArgs
        String selection = "email = ?";
        String[] selectionArgs = {email};
        String rawQuery = "select username from "+table_name + " where email = '"+email +"'";
        //sending the query and values to search in db
        //Cursor cursor =db.query(table_name, columns,selection,selectionArgs,null,null,null);
        Cursor cursor = db.rawQuery(rawQuery, null);
        //if cusor count > 0 then account exists else account does not exists
        while (cursor.moveToNext()){
            uname = cursor.getString(cursor.getColumnIndex(col1));
        }
        cursor.close();
        close();
        return uname;
        /*if(count >0){
            return true;
        }else {
            return false;
        }*/
    }
}
