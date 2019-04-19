package com.uppu.swathi_giftregistryproject;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.ProductsDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyEventProducts extends AppCompatActivity {
    private String eventId, username;
    private ProductsDatabase myDb;
    private ListView productsList;
    private ArrayAdapter<String> productAdapter;
    private ArrayList<Integer> productIds;
    private ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_products);
        if(Build.VERSION.SDK_INT >=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        productsList = (ListView) findViewById(R.id.myProducts);
        eventId = getIntent().getStringExtra("eventId");
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myDb = new ProductsDatabase(this);
        getProducts(eventId);
        registerForContextMenu(productsList);
    }
    public void addProduct(View v){
        Intent intent = new Intent(this, AddProduct.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("status", "new");
        startActivity(intent);
    }
    public void getProducts(String eId){
        products = myDb.getProductsById(eId);
        productIds = new ArrayList<Integer>();
        ArrayList<String> productNames =  new ArrayList<String>();
        for(Product product: products){
            productIds.add(Integer.parseInt(product.getProductId()));
            productNames.add(product.getProductName());
        }
    productAdapter = new ArrayAdapter<String>(this,
    android.R.layout.simple_expandable_list_item_1, productNames);
            productsList.setAdapter(productAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()){
            case R.id.editProduct:
                Intent editIntent = new Intent(MyEventProducts.this, AddProduct.class);
                editIntent.putExtra("status", "edit");
                editIntent.putExtra("eventId", eventId);
                editIntent.putExtra("productId", ""+productIds.get(index));
                startActivity(editIntent);
                break;
            case R.id.viewProduct:
                Intent viewIntent = new Intent(MyEventProducts.this, AddProduct.class);
                viewIntent.putExtra("status", "view");
                viewIntent.putExtra("productId", ""+productIds.get(index));
                startActivity(viewIntent);
                break;
            case R.id.deleteProduct:
                boolean deleted = myDb.deleteProductDetail(""+productIds.get(index));
                if(deleted){
                    getProducts(eventId);
                }
        }
        return super.onContextItemSelected(item);
    }
    public void emailProducts(View v){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        File data = null;
        try {
            Date dateVal = new Date();
            StringBuffer builder = createFileContent();
            String filename = dateVal.toString();
            data = File.createTempFile("Report", ".csv");
            FileWriter out = (FileWriter) CreateProductsFile.generateCsvFile(
                    data, builder);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), "com.uppu.swathi_giftregistryproject.fileprovider", data));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{username});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your products" );
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \n PFA for your product details.");
            startActivity(Intent.createChooser(emailIntent, "E-mail"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public StringBuffer createFileContent(){
        StringBuffer builder = new StringBuffer();
        builder.append("Product ID " + "," + "Product Name" + "," + "Product Link" + "," + "Product Color");
        builder.append("\n");
        for(Product product: products){
            builder.append(product.getProductId() + "," + product.getProductName() + "," + product.getProductLink() + "," + product.getProductColor());
            builder.append("\n");
        }
        return builder;
    }
}
