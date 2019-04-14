package com.uppu.swathi_giftregistryproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uppu.swathi_project_database.ProductsDatabase;

import java.util.ArrayList;

public class PurchaseProductActivity extends AppCompatActivity {
    TextView productName, productLink, productColor, purchased, youPurchased;
    String eventId, productId,username;
    boolean productPurchased = false;
    private ProductsDatabase myDb;
    Button purchaseProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);
        eventId = getIntent().getStringExtra("eventId");
        productId = getIntent().getStringExtra("productId");
        username = getIntent().getStringExtra("username");
        Toast.makeText(getApplicationContext(), ""+productId, Toast.LENGTH_SHORT).show();
        productName = (TextView) findViewById(R.id.productName);
        productLink = (TextView) findViewById(R.id.productLink);
        productColor = (TextView) findViewById(R.id.productColor);
        purchased = (TextView) findViewById(R.id.productPurchased);
        purchaseProduct = (Button) findViewById(R.id.purchaseProduct);
        youPurchased = (TextView) findViewById(R.id.purchased);
        myDb = new ProductsDatabase(this);
        getProductDetails(productId);
    }
    public void getProductDetails(String productId){
        ArrayList<Product> productDetails = myDb.getProductDetails(productId);
        if(productDetails.size() > 0){
            productName.setText(productDetails.get(0).getProductName());
            productLink.setText(productDetails.get(0).getProductLink());
            productColor.setText(productDetails.get(0).getProductColor());
            Toast.makeText(getApplicationContext(), ""+productDetails.get(0).isPurchased(), Toast.LENGTH_SHORT).show();
            if(productDetails.get(0).isPurchased() == 1){
                productPurchased = true;
            }
            if(productPurchased){
                purchased.setVisibility(View.VISIBLE);
                purchaseProduct.setVisibility(View.GONE);
            }
        }
    }
    public void purchaseProduct(View v){
        Product product = new Product(productId, productColor.getText().toString(),productLink.getText().toString(), productName.getText().toString(), 1, username);
        boolean updated = myDb.updateProduct(product, username);
        if(updated){
            youPurchased.setVisibility(View.VISIBLE);
            purchaseProduct.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "not updated", Toast.LENGTH_SHORT).show();
        }
    }
}
