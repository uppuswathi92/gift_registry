package com.uppu.swathi_giftregistryproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class TermsAndConditions extends AppCompatActivity {
    //this activity displays terms of service and privacy policy
    private TextView terms, privacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        terms = (TextView) findViewById(R.id.termsOfService);
        privacy = (TextView) findViewById(R.id.privacyPolicy);
        try{
            //jsonObject is created based on the file in asset folder
            JSONObject termsJson = new JSONObject(loadTermsJson());
            //returns value mapped by termsAndConditions array
            JSONArray termsFile = termsJson.getJSONArray("termsAndConditions");
            int size = termsFile.length();
            for (int i = 0; i < size; i++){
                //gets the value in array and sets to th text view
                JSONObject termsAndConditions = termsFile.getJSONObject(i);
                terms.setText(termsAndConditions.getString("defaultTermsOfService"));
                privacy.setText(termsAndConditions.getString("defaultPrivacyPolicy"));
            }
        }
        catch (JSONException jex){
            jex.printStackTrace();
        }
    }

    //loads json file in assets folder
    public String loadTermsJson(){
        String jsonString = null;
        try {
            //gets terms json file from assets folder
            InputStream is = getAssets().open("terms.json");
            //reads the bytes and add it tot he string
            int size = is.available();
            byte [] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        }
        catch (IOException io){

        }
        return jsonString;
    }
}
