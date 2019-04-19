package com.uppu.swathi_giftregistryproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.ProductsDatabase;

import java.util.ArrayList;

public class PurchaseProductActivity extends AppCompatActivity {
    private TextView productName, productLink, productColor, purchased, youPurchased;
    private String eventId, productId,username;
    private boolean productPurchased = false;
    private ProductsDatabase myDb;
    private Button purchaseProduct;
    private ImageView proImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);
        eventId = getIntent().getStringExtra("eventId");
        productId = getIntent().getStringExtra("productId");
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        proImage = (ImageView) findViewById(R.id.proImage);
        productName = (TextView) findViewById(R.id.productName);
        productLink = (TextView) findViewById(R.id.productLink);
        productColor = (TextView) findViewById(R.id.productColor);
        purchased = (TextView) findViewById(R.id.productPurchased);
        purchaseProduct = (Button) findViewById(R.id.purchaseProduct);
        youPurchased = (TextView) findViewById(R.id.purchased);
        myDb = new ProductsDatabase(this);
        getProductDetails(productId);
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public void getProductDetails(String productId){
        Product pro = myDb.getProductDetails(productId);
        if(pro != null){
            productName.setText(pro.getProductName());
            productLink.setText(pro.getProductLink());
            productColor.setText(pro.getProductColor());
            byte[] imageBytes = pro.getProductImage();
            proImage.setImageBitmap(getImage(imageBytes));
            if(pro.isPurchased() == 1){
                productPurchased = true;
            }
            if(productPurchased){
                purchased.setVisibility(View.VISIBLE);
                purchaseProduct.setVisibility(View.GONE);
            }
        }
    }
    public void purchaseProduct(View v){
        byte[] bytes = null;
        Product product = new Product(productId, productColor.getText().toString(),productLink.getText().toString(), productName.getText().toString(), 1, username, bytes);
        boolean updated = myDb.updateProduct(product, username);
        if(updated){
            youPurchased.setVisibility(View.VISIBLE);
            purchaseProduct.setVisibility(View.GONE);
        }else{
            Toast.makeText(getApplicationContext(), "not updated", Toast.LENGTH_SHORT).show();
        }
    }
}
