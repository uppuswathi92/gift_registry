package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.ProductsDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OtherEventsProductsActivity extends AppCompatActivity {
    private String eventId, username;
    private ListView otherProductsList;
    private ArrayAdapter<String> otherProductAdapter;
    private ProductsDatabase myDb;
    private ArrayList<Product> products;
    private ArrayList<Integer> productIds;
    private TextView productsadded, noproducts;
    private ImageView signout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_events_products);
        eventId = getIntent().getStringExtra("eventId");
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OtherEventsProductsActivity.this, MainActivity.class));
            }
        });
        otherProductsList = (ListView) findViewById(R.id.other_products_list);
        productsadded = (TextView) findViewById(R.id.productsadded);
        noproducts = (TextView) findViewById(R.id.noproducts);
        otherProductsList.setFocusable(false);
        myDb = new ProductsDatabase(this);
        getProducts(eventId);
    }
    public void getProducts(String eId){
        products = myDb.getProductsById(eId);
        productIds = new ArrayList<Integer>();
        ArrayList<String> productNames =  new ArrayList<String>();
        for(Product product: products){
            productIds.add(Integer.parseInt(product.getProductId()));
            productNames.add(product.getProductName());
        }
        if(products.size() > 0){
            productsadded.setVisibility(View.VISIBLE);
            noproducts.setVisibility(View.GONE);
        }else{
            productsadded.setVisibility(View.GONE);
            noproducts.setVisibility(View.VISIBLE);
        }
        otherProductAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, productNames);
        otherProductsList.setAdapter(otherProductAdapter);
        if(productNames.size() > 0){
            otherProductsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(OtherEventsProductsActivity.this, PurchaseProductActivity.class);
                    intent.putExtra("eventId", eventId);
                    intent.putExtra("productId", ""+productIds.get(position));
                    intent.putExtra("status", "view");
                    startActivity(intent);
                }
            });
        }
    }
}
