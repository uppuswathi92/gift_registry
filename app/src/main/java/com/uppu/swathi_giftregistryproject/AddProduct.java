package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uppu.swathi_project_database.ProductsDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddProduct extends AppCompatActivity {
    private String eventId, status, productId, username;
    private ImageView productImage, signout_button;
    private ProductsDatabase myDb;
    private Uri selectedImage;
    private Button upload, addProduct;
    private EditText productName, productLink, productColor;
    private TextView addProductText, mandatory, alreadyPurchased;
    private Product currentProduct;
    private boolean edited = false, isValid;
    private static final int SELECT_PICTURE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        //Getting event id to add product to a that eventId
        eventId = getIntent().getStringExtra("eventId");
        //setStatus();
        myDb = new ProductsDatabase(this);
        productImage = (ImageView) findViewById(R.id.productImage);
        productId = getIntent().getStringExtra("productId");
        status = getIntent().getStringExtra("status");
        //getting the email id of logged in user
        username = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        alreadyPurchased = (TextView) findViewById(R.id.alreadyPurchased);
        mandatory = (TextView) findViewById(R.id.mandatory);
        upload = (Button) findViewById(R.id.upload);
        addProduct = (Button) findViewById(R.id.addProduct);
        productName = (EditText) findViewById(R.id.productName);
        productLink = (EditText) findViewById(R.id.productLink);
        productColor = (EditText) findViewById(R.id.productColor);
        addProductText = (TextView) findViewById(R.id.addProductText);
        //Firebase signout functionality in toolbar
        signout_button = (ImageView) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AddProduct.this, MainActivity.class));
            }
        });
        //Based on the status received by previous intent, if its view details are disabled, if its new/edit details are enabled to add/edit
        status = getIntent().getStringExtra("status");
        if(status.equals("edit") || status.equals("view")){
            getProductDetails();
            if(status.equals("view")){
                addProductText.setText("View Product");
                setViewProduct();
            }else{
                if(currentProduct.isPurchased() == 1){
                    alreadyPurchased.setVisibility(View.VISIBLE);
                    setViewProduct();
                }
                addProduct.setText("Edit Product");
                addProductText.setText("Edit Product");
            }
        }
    }
    //this function disables fields to just view
    public void setViewProduct(){
        productName.setEnabled(false);
        productLink.setEnabled(false);
        productColor.setEnabled(false);
        upload.setVisibility(View.GONE);
        addProduct.setVisibility(View.GONE);
    }
    //returns Bitmap based on the byte array retrieved from database
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //Retieves product details based on the productId from database
    public void getProductDetails(){
        //invokes getProductDetails from the Product database
        Product pro = myDb.getProductDetails(productId);
        currentProduct = pro;
        if(pro !=  null){
            productName.setText(pro.getProductName(), TextView.BufferType.EDITABLE);
            productLink.setText(pro.getProductLink(), TextView.BufferType.EDITABLE);
            productColor.setText(pro.getProductColor(), TextView.BufferType.EDITABLE);
            byte[] imageBytes = pro.getProductImage();
            productImage.setVisibility(View.VISIBLE);
            //sets bitmap to set the content of the imageview
            productImage.setImageBitmap(getImage(imageBytes));
        }
    }

    //Invoked on click of upload product button. this allows the user to upload a picture
    public void uploadPicture(View v){
        edited = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    //invoked once user selects a picture
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                //setting the uri to the imageview to display the selected image
                Uri selectedImageUri = data.getData();
                selectedImage = data.getData();
                productImage.setVisibility(View.VISIBLE);
                productImage.setImageURI(selectedImageUri);
            }
        }
    }
    //this function reads the inputstream (the image) and converts it into bytes
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    //this function conerts image uri to byte array
    public byte[] saveImageInDB(Uri selectedImageUri) {
        byte[] inputData = null;
        try {
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            inputData = getBytes(iStream);
        } catch (IOException ioe) {}
        return inputData;
    }
    //Validating if user has entered all details. if user has not entered all the details then error messages are displayed
    public boolean validateDetails(){
        isValid = false;
        if (TextUtils.isEmpty(productName.getText().toString()) || TextUtils.isEmpty(productLink.getText().toString()) || TextUtils.isEmpty(productColor.getText().toString()) || productImage.getDrawable() == null) {
            mandatory.setVisibility(View.VISIBLE);
            isValid = true;
            if(TextUtils.isEmpty(productName.getText().toString())){
                productName.setError("Please enter product name!");
            }
            if(TextUtils.isEmpty(productLink.getText().toString())){
                productLink.setError("Please enter product link!");
            }
            if(TextUtils.isEmpty(productColor.getText().toString())){
                productColor.setError("Please enter product color!");
            }
        }
        return isValid;
    }
    public void addNewProduct(View v){
        byte[] productImg = null;
        if(!validateDetails()){
            //if user has added the product in edit product screen then we use that image else we use the image already uploaded in database
            if(edited){
                productImg =saveImageInDB(selectedImage);
            }else{
                productImg = currentProduct.getProductImage();
            }
            //if status is new then user is adding a new product else user is updating it
            if(status.equals("new")){
                //invoking addProduct service from productDatabase
                boolean added = myDb.addProduct(eventId, productName.getText().toString(), productLink.getText().toString(), productColor.getText().toString(), productImg);
                //if its successful then it is redirected to MyEventProducts page
                if(added){
                    Intent intent = new Intent(AddProduct.this, MyEventProducts.class);
                    intent.putExtra("eventId", eventId);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Product has been added!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Product not added!", Toast.LENGTH_LONG).show();
                }
            }else{
                    updateProduct(productImg);
                }

        }
    }

    //invoked if status received is edit
    public void updateProduct(byte[] productImg){
        //invoking updateProductDetails from Products Database
        boolean updated = myDb.updateProductDetails(productId, productName.getText().toString(), productLink.getText().toString(), productColor.getText().toString(), productImg);
        //if its successful then it is redirected to MyEventProducts page
        if(updated){
            Intent productIntent = new Intent(AddProduct.this, MyEventProducts.class);
            productIntent.putExtra("eventId", eventId);
            startActivity(productIntent);
        }
    }

}
