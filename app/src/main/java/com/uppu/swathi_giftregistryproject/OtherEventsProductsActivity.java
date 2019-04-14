package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.uppu.swathi_project_database.ProductsDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OtherEventsProductsActivity extends AppCompatActivity {
    private String eventId, username;
    private ListView otherProductsList;
    ArrayAdapter<String> otherProductAdapter;
    private ProductsDatabase myDb;
    ArrayList<String> products;
    ArrayList<Integer> productIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_events_products);
        eventId = getIntent().getStringExtra("eventId");
        username = getIntent().getStringExtra("username");
        otherProductsList = (ListView) findViewById(R.id.other_products_list);
        otherProductsList.setFocusable(false);
        myDb = new ProductsDatabase(this);
        getProducts(eventId);
    }
    public void getProducts(String eId){
        products = new ArrayList<String>();
        productIds = new ArrayList<Integer>();
        HashMap<Integer, String> getProducts = myDb.getProductsById(eId);
        Iterator it = getProducts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            products.add((String) pair.getValue());
            productIds.add((Integer) pair.getKey());
            // System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        if(products.size() > 0){

            otherProductAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_expandable_list_item_1, products);
            otherProductsList.setAdapter(otherProductAdapter);
            otherProductsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(OtherEventsProductsActivity.this, PurchaseProductActivity.class);
                    intent.putExtra("eventId", eventId);
                    Toast.makeText(getApplicationContext(), ""+productIds.get(position), Toast.LENGTH_SHORT).show();
                    intent.putExtra("productId", ""+productIds.get(position));
                    intent.putExtra("username",username);
                    intent.putExtra("status", "view");
                    startActivity(intent);
                }
            });
        }
    }
}
