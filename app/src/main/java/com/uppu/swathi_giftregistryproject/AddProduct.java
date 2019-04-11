package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uppu.swathi_project_database.ProductsDatabase;

public class AddProduct extends AppCompatActivity {
    private String eventId, status;
    ProductsDatabase myDb;
    private EditText productName, productLink, productColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        eventId = getEventId();
        setStatus();
        myDb = new ProductsDatabase(this);
        productName = (EditText) findViewById(R.id.productName);
        productLink = (EditText) findViewById(R.id.productLink);
        productColor = (EditText) findViewById(R.id.productColor);
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
