package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uppu.swathi_project_database.ProductsDatabase;

import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {
    private String eventId, status, productId;
    ProductsDatabase myDb;
    private EditText productName, productLink, productColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        eventId = getEventId();
        setStatus();
        myDb = new ProductsDatabase(this);
        productId = getIntent().getStringExtra("productId");
        status = getIntent().getStringExtra("status");
        productName = (EditText) findViewById(R.id.productName);
        productLink = (EditText) findViewById(R.id.productLink);
        productColor = (EditText) findViewById(R.id.productColor);
        if(status.equals("edit") || status.equals("view")){
            getProductDetails();
            if(status.equals("view")){
                productName.setEnabled(false);
                productLink.setEnabled(false);
                productLink.setEnabled(false);
            }
        }
    }

    public void getProductDetails(){
        ArrayList<Product> productDetails = myDb.getProductDetails(productId);
        Toast.makeText(getApplicationContext(), ""+productDetails.get(0).getProductColor(), Toast.LENGTH_SHORT).show();
        if(productDetails.size() > 0){

            productName.setText(productDetails.get(0).getProductName(), TextView.BufferType.EDITABLE);
            productLink.setText(productDetails.get(0).getProductLink(), TextView.BufferType.EDITABLE);
            productColor.setText(productDetails.get(0).getProductColor(), TextView.BufferType.EDITABLE);
        }
    }
    public void setStatus(){
        Intent eventIntent = getIntent();
        status = eventIntent.getStringExtra("status");
        if(status == "view"){
            productName.setEnabled(false);
            productLink.setEnabled(false);
            productColor.setEnabled(false);
        }
    }
    public String getEventId(){
        Intent eventIntent = getIntent();
        return eventIntent.getStringExtra("eventId");
    }
    public void addNewProduct(View v){
        boolean added = myDb.addProduct(eventId, productName.getText().toString(), productLink.getText().toString(), productColor.getText().toString());
        if(added){
            Intent intent = new Intent(AddProduct.this, MyEventProducts.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "product added", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "product not added", Toast.LENGTH_LONG).show();
        }
    }

}
