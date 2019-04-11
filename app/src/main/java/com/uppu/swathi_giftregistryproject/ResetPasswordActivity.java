package com.uppu.swathi_giftregistryproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uppu.swathi_project_database.GiftRegistryDatabase;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText password, confirm_password, email;
    private boolean isValid;
    private TextView mandatory;
    private GiftRegistryDatabase myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        email = (EditText) findViewById(R.id.reset_email);
        password = (EditText) findViewById(R.id.reset_pwd);
        confirm_password = (EditText) findViewById(R.id.confirm_pwd);
        mandatory = (TextView) findViewById(R.id.mandatory);
        myDb = new GiftRegistryDatabase(this);
    }
    public void resetPassword(View v){
        if(validateDetails()){
            if(myDb.validUsername(email.getText().toString())){
                boolean updatedPwd = myDb.updatePassword(email.getText().toString(), password.getText().toString());
                if(updatedPwd){
                    Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                    intent.putExtra("successMsg", getString(R.string.reset_success));
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_LONG).show();
                }
            }else{
                mandatory.setText(getString(R.string.valid_username));
                mandatory.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean validateDetails(){
        isValid = true;
        String error = getString(R.string.mandatory);
        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError("Please enter username!");
            isValid = false;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("Please enter password!");
            isValid = false;
        }
        if(TextUtils.isEmpty(confirm_password.getText().toString())){
            confirm_password.setError("Please confirm password!");
            isValid = false;
        }
        if(isValid && (!password.getText().toString().equals(confirm_password.getText().toString()))){
            isValid = false;
            error = getString(R.string.confirm_error);
        }
        if(!isValid){
            mandatory.setText(error);
            mandatory.setVisibility(View.VISIBLE);
        }
        return isValid;
    }
}
