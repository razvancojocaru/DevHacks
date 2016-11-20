package com.shic.shic;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DirectionsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent i = getIntent();

        Bundle extras = i.getExtras();
        String ngoLatitude = extras.getString("latitude");
        String ngoLongitude = extras.getString("longitude");
        String ngoName = extras.getString("name");
        String ngoAddress = extras.getString("address");

        // Add a marker in Sydney and move the camera
        LatLng NGO = new LatLng(Double.parseDouble(ngoLatitude), Double.parseDouble(ngoLongitude));

        Marker marker = mMap.addMarker(new MarkerOptions().position(NGO).title(ngoName).snippet(ngoAddress));
        marker.showInfoWindow();

        Toast.makeText(getApplicationContext(), "Don't forget to collect you reward points from the donation drop point!", Toast.LENGTH_LONG).show();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NGO, 18));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}