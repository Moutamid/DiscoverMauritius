package com.moutamid.sqlapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.data.kml.KmlLayer;
import com.moutamid.sqlapp.R;

import java.io.File;
import java.io.FileInputStream;

public class TourshipActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourship);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        File cacheDir = new File(getFilesDir(), "cached_images");
        String fullPath = cacheDir.getAbsolutePath();

        try {
            File kmzFile = new File(fullPath + "/map1.kmz");
            FileInputStream inputStream = new FileInputStream(kmzFile);

            KmlLayer kmlLayer = new KmlLayer(mMap, inputStream, getApplicationContext());
            kmlLayer.addLayerToMap();

            LatLng defaultPosition = new LatLng(-20.3484, 57.5522);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPosition, 10));
        } catch (Exception e) {
            Log.e("KML_LOAD", "Error loading KML/KMZ: " + e.getMessage());
        }

    }

    public void BackPress(View view) {
        onBackPressed();
    }
}
