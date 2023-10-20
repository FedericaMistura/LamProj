package com.example.lamproj.gmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.lamproj.App;
import com.example.lamproj.PermissionUtils;
import com.example.lamproj.tiles.TileGrid;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapManager implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener {
    public GoogleMap mMap=null;
    public int mapType=GoogleMap.MAP_TYPE_SATELLITE;
    public void onMapReady(GoogleMap googleMap) {
        this.mMap=googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setMapType(mapType);

        App.A.context.enableMyLocation();
        LatLng BOLOGNA = new LatLng(44.496781, 11.356387);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BOLOGNA, 12));

    }

    public void showTestData(){
        mMap.clear();

        // Add a marker in Bologna and move the camera
        LatLng BOLOGNA = new LatLng(44.496781, 11.356387);
        LatLng BOLOGNA_NW = new LatLng(44.52, 11.286387);
//        mMap.addMarker(new MarkerOptions().position(BOLOGNA_NW).title("Marker in Bologna"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BOLOGNA, 12));

        TileGrid tg=new TileGrid(BOLOGNA_NW,10000.0,10000.0,500);
        tg.addToGoogleMap(mMap);


    }

    public void showLteData(){
        mMap.clear();
        showTestData();
    }

    public void showWiFiData(){
        mMap.clear();
    }

    public void onShowNoiseData(){
        mMap.clear();
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // When clicked on map
        // Initialize marker options
        MarkerOptions markerOptions=new MarkerOptions();
        // Set position of marker
        markerOptions.position(latLng);
        // Set title of marker
        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
        // Remove all marker
        mMap.clear();
        // Animating to zoom the marker
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        // Add marker on map
        mMap.addMarker(markerOptions);
    }

}