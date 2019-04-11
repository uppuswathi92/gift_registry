package com.uppu.swathi_giftregistryproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uppu.swathi_project_database.ProductsDatabase;

import java.util.ArrayList;

public class PurchaseProductActivity extends AppCompatActivity {
    TextView productName, productLink, productColor;
    String eventId, productId;
    private ProductsDatabase myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);
        eventId = getIntent().getStringExtra("eventId");
        productId = getIntent().getStringExtra("productId");
        Toast.makeText(getApplicationContext(), ""+productId, Toast.LENGTH_SHORT).show();
        productName = (TextView) findViewById(R.id.productName);
        productLink = (TextView) findViewById(R.id.productLink);
        productColor = (TextView) findViewById(R.id.productColor);
        myDb = new ProductsDatabase(this);
        getProductDetails(productId);
    }
    public void getProductDetails(String productId){
        ArrayList<Product> productDetails = myDb.getProductDetails(productId);
        if(productDetails.size() > 0){

            productName.setText(productDetails.get(0).getProductName());
            productLink.setText(productDetails.get(0).getProductLink());
            productColor.setText(productDetails.get(0).getProductColor());
        }
    }
}
