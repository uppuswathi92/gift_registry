package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class MyEventProducts extends AppCompatActivity {
    private String eventId;
    private ProductsDatabase myDb;
    private ListView productsList;
    private ArrayAdapter<String> productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_products);
        productsList = (ListView) findViewById(R.id.myProducts);
        eventId = getEventId();
        myDb = new ProductsDatabase(this);
        getProducts(eventId);
    }
    public String getEventId(){
        Intent eventsIntent = getIntent();
        return eventsIntent.getStringExtra("eventId");
    }
    public void addProduct(View v){
        Intent intent = new Intent(this, AddProduct.class);
        Toast.makeText(getApplicationContext(), getEventId(),Toast.LENGTH_LONG).show();
        intent.putExtra("eventId", getEventId());
        intent.putExtra("status", "new");
        startActivity(intent);
    }
    public void getProducts(String eId){

        /*ArrayList<String> getProducts = myDb.getProductsById(eId);
        for(String product: getProducts){
            products.add(product);
        }


        if(products.size() > 0){

            productAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_expandable_list_item_1, products);
            productsList.setAdapter(productAdapter);
            productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyEventProducts.this, AddProduct.class);
                    intent.putExtra("eventId", getEventId());
                    intent.putExtra("status", "view");
                    startActivity(intent);
                }
            });
        }else{
            //noEvents.setVisibility(View.VISIBLE);
        }*/
        ArrayList<String> products = new ArrayList<String>();
        HashMap<Integer, String> getProducts = myDb.getProductsById(eId);
        Iterator it = getProducts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            products.add((String) pair.getValue());
            //productIds.add((Integer) pair.getKey());
            // System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }


        if(products.size() > 0){

            productAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_expandable_list_item_1, products);
            productsList.setAdapter(productAdapter);
            productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MyEventProducts.this, AddProduct.class);
                    intent.putExtra("eventId", getEventId());
                    intent.putExtra("status", "view");
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //This gives a MenuInflater object that will be used to
        // inflate(convert our XML file into Java Object)
        // the option_menu.xml file.
        MenuInflater inflater = getMenuInflater();
        //inflate() method is used to inflate the option_menu.xml.xml file.
        inflater.inflate(R.menu.my_events_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Handling click events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addInvitee:
                Intent inviteeIntent = new Intent(MyEventProducts.this, AddInviteeActivity.class);
                inviteeIntent.putExtra("eventId", eventId);
                startActivity(inviteeIntent);
                return true;
            case R.id.deleteEvent:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
