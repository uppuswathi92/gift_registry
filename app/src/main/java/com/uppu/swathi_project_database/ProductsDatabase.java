package com.uppu.swathi_project_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uppu.swathi_giftregistryproject.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsDatabase extends SQLiteOpenHelper {
    public static final String database_name = "products.db";
    public static final String table_name = "Products";
    public static final String col1 = "eventId";
    public static final String col2 = "productName";
    public static final String col3 = "productLink";
    public static final String col4 = "productColor";
    public static final String col5 = "productId";
    ContentValues contentValues;

    public ProductsDatabase(Context context) {
        super(context, table_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ table_name + "("+col1+" INTEGER, "+col2 + " TEXT, " +  col3 + " TEXT, " +  col4 + " TEXT, " + col5 + " INTEGER PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }

    public boolean addProduct(String eventId, String productName, String productLink, String productColor){
        //Using contentValues to store the values to insert in database
        SQLiteDatabase db =  this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(col1,eventId);
        contentValues.put(col2,productName);
        contentValues.put(col3,productLink);
        contentValues.put(col4,productColor);
        contentValues.put(col5,createProductId());
        //inserting into db
        long result = db.insert(table_name, null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public HashMap<Integer, String> getProductsById(String id){
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        ArrayList<String> products = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String rawquery = "select * from "+table_name + " where eventId = "+id;
        String[] columns = {col1,col2,col3,col4};
        String selection = "eventId = ?";
        String[] selectionArgs = {""+id};
        Cursor cursor =db.rawQuery(rawquery, null);

        while (cursor.moveToNext()){
            map.put(cursor.getInt(cursor.getColumnIndex(col5)), cursor.getString(cursor.getColumnIndex(col2)));

            //products.add(cursor.getString(cursor.getColumnIndex(col2)));
        }

        return map;
    }

    public int createProductId(){
        int id = (int) Math.round((Math.random()) * 100000);
        while(validProductId(id)){
            id = (int) Math.round((Math.random()) * 100000);
        }
        return id;
    }

    public boolean validProductId(int id){
            boolean getCount = false;
            SQLiteDatabase db= this.getWritableDatabase();
            String[] columns = {"productId"};
            //? represents that we are going to give those values in selectionArgs
            String selection = "productId = ?";
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
    public ArrayList<Product> getProductDetails(String productId){
        ArrayList<Product> productDetails = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();
        String rawquery = "select * from "+table_name + " where productId = "+productId;
        String[] columns = {col1,col2,col3,col4};
        String selection = "productId = ?";
        String[] selectionArgs = {""+productId};
        Cursor cursor =db.rawQuery(rawquery, null);

        while (cursor.moveToNext()){
            productDetails.add(new Product(productId ,cursor.getString(cursor.getColumnIndex(col4)),cursor.getString(cursor.getColumnIndex(col3)),cursor.getString(cursor.getColumnIndex(col2))));
        }

        return productDetails;
    }
}
