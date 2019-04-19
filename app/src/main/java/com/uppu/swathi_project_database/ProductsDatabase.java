package com.uppu.swathi_project_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String col6 = "isPurchased";
    public static final String col7 = "purchasedBy";
    public static final String col8 = "productImage";
    ContentValues contentValues;

    public ProductsDatabase(Context context) {
        super(context, table_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ table_name + "("+col1+" INTEGER, "+col2 + " TEXT, " +  col3 + " TEXT, " +  col4 + " TEXT, " + col5 + " INTEGER PRIMARY KEY, " + col6 + " INTEGER, " + col7 + " TEXT," +col8 + " BLOB NOT NULL )");
        //db.execSQL("create table "+ table_name + "("+col1+" INTEGER, "+col2 + " TEXT, " +  col3 + " TEXT, " +  col4 + " TEXT, " + col5 + " INTEGER PRIMARY KEY, " + col6 + " INTEGER, " + col7 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);
    }

    public boolean addProduct(String eventId, String productName, String productLink, String productColor, byte[] imageBytes){
        //Using contentValues to store the values to insert in database
        SQLiteDatabase db =  this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(col1,eventId);
        contentValues.put(col2,productName);
        contentValues.put(col3,productLink);
        contentValues.put(col4,productColor);
        contentValues.put(col5,createProductId());
        contentValues.put(col8,imageBytes);
        //inserting into db
        long result = db.insert(table_name, null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public ArrayList<Product> getProductsById(String id){
        ArrayList<Product> products = new ArrayList<Product>();
        //ArrayList<String> products = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        String rawquery = "select * from "+table_name + " where eventId = "+id;
        String[] columns = {col1,col2,col3,col4};
        String selection = "eventId = ?";
        String[] selectionArgs = {""+id};
        Cursor cursor =db.rawQuery(rawquery, null);

        while (cursor.moveToNext()){
            products.add(new Product(cursor.getString(cursor.getColumnIndex(col5)), cursor.getString(cursor.getColumnIndex(col4)), cursor.getString(cursor.getColumnIndex(col3)), cursor.getString(cursor.getColumnIndex(col2)), cursor.getInt(cursor.getColumnIndex(col6)), cursor.getString(cursor.getColumnIndex(col7)), retreiveImageFromDB(cursor.getString(cursor.getColumnIndex(col5)))));

            //products.add(cursor.getString(cursor.getColumnIndex(col2)));
        }

        return products;
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
    public byte[] retreiveImageFromDB(String productId) {
        String selection = "productId = ?";
        String[] selectionArgs = {""+productId};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.query(true, table_name, new String[]{col8,},
                selection, selectionArgs, null, null,
                null, null);
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex("productImage"));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }
    //public ArrayList<Product> getProductDetails(String productId){
    public Product getProductDetails(String productId){
        Product pro = null;
        byte[] image = retreiveImageFromDB(productId);
        ArrayList<Product> productDetails = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();
        String rawquery = "select * from "+table_name + " where productId = "+productId;
        String[] columns = {col1,col2,col3,col4,col5,col6,col7,col8};
        String selection = "productId = ?";
        String[] selectionArgs = {""+productId};
        Cursor cursor =db.rawQuery(rawquery, null);
        if(cursor.moveToFirst() && cursor.getCount() >= 1){
            do{
                Log.i("count!" , ""+cursor.getBlob(cursor.getColumnIndex(col8)));
                pro = new Product(productId ,cursor.getString(cursor.getColumnIndex(col4)),cursor.getString(cursor.getColumnIndex(col3)),cursor.getString(cursor.getColumnIndex(col2)),cursor.getInt(cursor.getColumnIndex(col6)),cursor.getString(cursor.getColumnIndex(col7)), image);
            }while(cursor.moveToNext());
        }
        //Cursor cursor =db.query(table_name, columns,selection,selectionArgs,null,null,null);
        //byte[] blob = cursor.getBlob(cursor.getColumnIndex(col8));
        /*while (cursor.moveToNext()){
            //productDetails.add(new Product(productId ,cursor.getString(cursor.getColumnIndex(col4)),cursor.getString(cursor.getColumnIndex(col3)),cursor.getString(cursor.getColumnIndex(col2)),cursor.getInt(cursor.getColumnIndex(col6)),cursor.getString(cursor.getColumnIndex(col7)), image));
            pro = new Product(productId ,cursor.getString(cursor.getColumnIndex(col4)),cursor.getString(cursor.getColumnIndex(col3)),cursor.getString(cursor.getColumnIndex(col2)),cursor.getInt(cursor.getColumnIndex(col6)),cursor.getString(cursor.getColumnIndex(col7)), image);
        }*/
        cursor.close();
        return pro;
    }

    public boolean updateProduct(Product product, String username){
        updatePurchase(product, username);
        //using content values to store the updated password, where the employee id entered by user
        ContentValues values = new ContentValues();
        values.put(col6, product.isPurchased());
        values.put(col7, product.getIsPurchasedBy());
        String where = "productId = ?";
        String[] whereArgs = { ""+product.getProductId() };
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updateSuccessful = db.update(table_name, values, where, whereArgs) > 0;
        db.close();
        return updateSuccessful;
    }

    public void updatePurchase(Product product, String username){
        ContentValues values = new ContentValues();
        values.put(col6, 0);
        values.put(col7, "");
        String where = "purchasedBy = ?";
        String[] whereArgs = { ""+username };
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updateSuccessful = db.update(table_name, values, where, whereArgs) > 0;
        db.close();
        return;
    }

    public boolean deleteProduct(String eventId){
        //query for deleting record based on eventId
        boolean deleteSuccessful = false;
        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete(table_name, "eventId ='" + eventId + "'", null) > 0;
        db.close();
        return deleteSuccessful;
    }
    public boolean updateProductDetails(String productId, String productName, String productLink, String productColor, byte[] productImg){
        SQLiteDatabase db =  this.getWritableDatabase();
        //using content values to store the updated password, where the employee id entered by user
        ContentValues values = new ContentValues();
        values.put(col2,productName);
        values.put(col3,productLink);
        values.put(col4,productColor);
        values.put(col8,productImg);
        String where = "productId = ?";
        String[] whereArgs = { ""+productId };
        boolean updateSuccessful = db.update(table_name, values, where, whereArgs) > 0;
        db.close();
        return updateSuccessful;
    }
    public boolean deleteProductDetail(String productId){
        //query for deleting record based on eventId
        boolean deleteSuccessful = false;
        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete(table_name, "productId ='" + productId + "'", null) > 0;
        db.close();
        return deleteSuccessful;
    }
}
