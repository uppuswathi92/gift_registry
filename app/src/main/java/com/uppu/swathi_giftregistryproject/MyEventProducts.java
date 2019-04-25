package com.uppu.swathi_giftregistryproject;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
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
    //this activity is used to displat the products added by the host for the host to view
    private String eventId, username;
    private ProductsDatabase myDb;
    private ListView productsList;
    private ArrayAdapter<String> productAdapter;
    private ArrayList<Integer> productIds;
    private ArrayList<Product> products;
    private TextView noproducts;
    private ImageView signout_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_products);
        /*if(Build.VERSION.SDK_INT >=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }*/
        productsList = (ListView) findViewById(R.id.myProducts);
        //Getting event id to get products added to that eventId
        eventId = getIntent().getStringExtra("eventId");
        //getting the email id of logged in user
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        noproducts = (TextView) findViewById(R.id.noproducts) ;
        //Firebase signout functionality in toolbar
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MyEventProducts.this, MainActivity.class));
            }
        });
        myDb = new ProductsDatabase(this);
        //getting the list of products based on the product id
        getProducts(eventId);
        //adding a context menu for the products
        registerForContextMenu(productsList);
    }

    //called on click of add product button and redirected to AddProduct activity
    public void addProduct(View v){
        Intent intent = new Intent(this, AddProduct.class);
        intent.putExtra("eventId", eventId);
        intent.putExtra("status", "new");
        startActivity(intent);
    }

    //getting the list of products based on the product id
    public void getProducts(String eId){
        //invoking getProductsById from Products Database and assigning products, productIds and productNames list
        products = myDb.getProductsById(eId);
        productIds = new ArrayList<Integer>();
        ArrayList<String> productNames =  new ArrayList<String>();
        for(Product product: products){
            productIds.add(Integer.parseInt(product.getProductId()));
            productNames.add(product.getProductName());
        }
        //if no products have been added then no products message is diplayed
        if(products.size() > 0){
            noproducts.setVisibility(View.GONE);
        }else{
            noproducts.setVisibility(View.VISIBLE);
        }
        //adapter is set to display products in listview
        productAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_expandable_list_item_1, productNames);
                productsList.setAdapter(productAdapter);
    }

    //Context menu is set and its menu layout is inflated
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_menu, menu);
    }

    //invoked on click of context item
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()){
            //editing a product. redirected to AddProduct page with edit status
            case R.id.editProduct:
                Intent editIntent = new Intent(MyEventProducts.this, AddProduct.class);
                editIntent.putExtra("status", "edit");
                editIntent.putExtra("eventId", eventId);
                editIntent.putExtra("productId", ""+productIds.get(index));
                startActivity(editIntent);
                break;
            //viewing a product. redirected to AddProduct page with view status
            case R.id.viewProduct:
                Intent viewIntent = new Intent(MyEventProducts.this, AddProduct.class);
                viewIntent.putExtra("status", "view");
                viewIntent.putExtra("productId", ""+productIds.get(index));
                startActivity(viewIntent);
                break;
            //deleting a product. deleteProductDetail is invoked from ProductsDatabase
            case R.id.deleteProduct:
                boolean deleted = myDb.deleteProductDetail(""+productIds.get(index));
                if(deleted){
                    getProducts(eventId);
                }
        }
        return super.onContextItemSelected(item);
    }

    //popup menu invoked on click of Email Products button
    public void emailProductDetails(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.email_products_menu, popup.getMenu());
        //on click listener for menu item click.
        // OnMenuItemClickListener has abstract method onMenuItemClick which must be implemented
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //method that takes MenuItem and returns a boolean value
                return setMenuClick(item);
            }
        });
        popup.show();
    }

    //invoked when item is selected from popup menu
    public boolean setMenuClick(MenuItem item){
        switch(item.getItemId()){
            case R.id.emailNames:
                emailNames();
                return true;
            case R.id.emailAllDetails:
                emailProducts();
                return true;
            default:
                return false;
        }
    }

    //email is sent to the user with the list pf product names in the message
    public void emailNames(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+username+"?cc="+"&subject= List of the names of products"));
        intent.putExtra(Intent.EXTRA_EMAIL,username);
        //string builder is used to appened all the product names into one string
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for(Product product: products){
            count++;
            builder.append(count + ". " +product.getProductName());
            builder.append("\n");
        }
        intent.putExtra(Intent.EXTRA_TEXT,builder.toString());
        intent.putExtra(Intent.EXTRA_SUBJECT, "List of the names of products");
        startActivity(Intent.createChooser(intent, "E-mail"));
    }
    //email is sent to the user with all the product details in a csv file
    public void emailProducts(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        File data = null;
        try {
            //builder contains all the content to be sent in csv file
            StringBuffer builder = createFileContent();
            //this creates a temporary csv file
            data = File.createTempFile("Products", ".csv");
            //CreateProductsFile is used to generate a csv file specified in data with the content
            FileWriter out = (FileWriter) CreateProductsFile.generateCsvFile(
                    data, builder);
            //read operations are allowed if this permission is granted
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //the file with content is set as the stream
            emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), "com.uppu.swathi_giftregistryproject.fileprovider", data));
            //email is sent to the user with following details
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{username});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your products" );
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \n PFA for your product details.");
            startActivity(Intent.createChooser(emailIntent, "E-mail"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this function is used create the content in the csv file
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
