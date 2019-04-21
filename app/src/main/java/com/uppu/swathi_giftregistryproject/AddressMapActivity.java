package com.uppu.swathi_giftregistryproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AddressMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String address;
    private EditText locationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationText = (EditText) findViewById(R.id.location);
        address = getIntent().getStringExtra("address");
        locationText.setText(address);
    }
    UiSettings mUiSettings;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUiSettings = mMap.getUiSettings();
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        search(address);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    public void searchLocation(View v){
        EditText locationSearch = findViewById(R.id.location);
        String location = locationSearch.getText().toString();
        search(location);
    }
    public void search(String location){
        List<Address> addressList = null;
        if(location != null || !location.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try{
                addressList = geocoder.getFromLocationName(location,1);
            }
            catch (IOException e){}
            Address address =addressList.get(0);
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latlng).title("Your Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        }
    }
}
