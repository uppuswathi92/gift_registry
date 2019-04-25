package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.ProductsDatabase;

import java.util.ArrayList;
import java.util.List;

public class PurchaseProductActivity extends AppCompatActivity {
    //this activity is used by the user to view and ourchase the product
    private TextView productName, productLink, productColor, purchased, youPurchased;
    private String eventId, productId,username;
    private boolean productPurchased = false;
    private ProductsDatabase myDb;
    private Button purchaseProduct;
    private ImageView proImage, signout_button;
    private ListView productList;
    private ArrayAdapter<String> adapter;
    private Product currentProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);
        eventId = getIntent().getStringExtra("eventId");
        productId = getIntent().getStringExtra("productId");
        //getting the email id of logged in user
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        proImage = (ImageView) findViewById(R.id.proImage);
        productName = (TextView) findViewById(R.id.productName);
        productLink = (TextView) findViewById(R.id.productLink);
        productColor = (TextView) findViewById(R.id.productColor);
        purchased = (TextView) findViewById(R.id.productPurchased);
        purchaseProduct = (Button) findViewById(R.id.purchaseProduct);
        youPurchased = (TextView) findViewById(R.id.purchased);
        productList = (ListView) findViewById(R.id.purchaseList);
        //Firebase signout functionality in toolbar
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PurchaseProductActivity.this, MainActivity.class));
            }
        });
        //Retieves product details based on the productId from database
        myDb = new ProductsDatabase(this);
        getProductDetails(productId);
    }
    //returns Bitmap based on the byte array retrieved from database
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //Retieves product details based on the productId from database
    public void getProductDetails(String productId){
        //invokes getProductDetails from the Product database
        currentProduct = myDb.getProductDetails(productId);
        ArrayList<String> productDet = new ArrayList<>();
        if(currentProduct != null){
            productDet.add(currentProduct.getProductName());
            productDet.add(currentProduct.getProductLink());
            productDet.add(currentProduct.getProductColor());
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_expandable_list_item_1, productDet);
            productList.setAdapter(adapter);
            //sets bitmap to set the content of the imageview
            byte[] imageBytes = currentProduct.getProductImage();
            proImage.setImageBitmap(getImage(imageBytes));
            //if product has already been purchased then already purchased is displayed else purchase product button is displayed
            if(currentProduct.isPurchased() == 1){
                productPurchased = true;
            }
            if(productPurchased){
                if(currentProduct.getIsPurchasedBy().equals(username)){
                    youPurchased.setVisibility(View.VISIBLE);
                    purchased.setVisibility(View.GONE);
                    purchaseProduct.setVisibility(View.GONE);
                }else{
                    youPurchased.setVisibility(View.GONE);
                    purchased.setVisibility(View.VISIBLE);
                    purchaseProduct.setVisibility(View.GONE);
                }
            }else{
                youPurchased.setVisibility(View.GONE);
                purchased.setVisibility(View.GONE);
                purchaseProduct.setVisibility(View.VISIBLE);
            }
        }
    }

    //invoked when user clicks on purchase product button
    public void purchaseProduct(View v){
        byte[] bytes = null;
        Product product = new Product(productId, currentProduct.getProductColor(),currentProduct.getProductLink(), currentProduct.getProductName(), 1, username, bytes);
        //invokes updateProduct from database
        boolean updated = myDb.updateProduct(product, username);
        if(updated){
            youPurchased.setVisibility(View.VISIBLE);
            purchaseProduct.setVisibility(View.GONE);
        }else{
            Toast.makeText(getApplicationContext(), "not updated", Toast.LENGTH_SHORT).show();
        }
    }
}
