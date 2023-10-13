package com.example.lamproj;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lamproj.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMyLocationClickListener,
    OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        

        // Add a marker in Bologna and move the camera
        LatLng BOLOGNA = new LatLng(44.496781, 11.356387);
        mMap.addMarker(new MarkerOptions().position(BOLOGNA).title("Marker in Bologna"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BOLOGNA, 7));
        /*
        LatLng upperLeft = new LatLng(44.5404, 11.2993);   // Adatta queste coordinate
        LatLng upperRight = new LatLng(44.5404, 11.4188);  // alle coordinate reali di Bologna
        LatLng lowerRight = new LatLng(44.4472, 11.4188);
        LatLng lowerLeft = new LatLng(44.4472, 11.2993);

        PolygonOptions rectOptions = new PolygonOptions().add(upperLeft)
                .add(upperRight).add(lowerRight).add(lowerLeft);
        Polygon poligono = mMap.addPolygon(rectOptions);
        poligono.setStrokeColor(Color.BLUE);
        */

        Polygon hexagonPolygon = mMap.addPolygon(new PolygonOptions()
                .addAll(createHexagon(BOLOGNA, 1000)));
        hexagonPolygon.setStrokeColor(Color.BLUE);

        drawHorizontalHexagonGrid(BOLOGNA, 1000);

    }

    /*
        distance è in metri.
     */
    private List<LatLng> createHexagon(LatLng center, double distance){
        List<LatLng> hexagon = new ArrayList<>();
        double angle = 360.0/6.0;
        for(int i = 0; i < 6; i++){
            LatLng vertex = SphericalUtil.computeOffset(center, distance, i * angle);
            hexagon.add(vertex);
        }
        return hexagon;
    }

    private void drawHorizontalHexagonGrid(LatLng startPosition, int radius){
        double width = radius * 2 * Math.sqrt(3) /2;
        LatLng[] adjacentPositions = new LatLng[6];

        for (int i = 0; i < 6; i++){
            double angle = 60 * i;
            LatLng adjacentPosition = SphericalUtil.computeOffset(startPosition, width, angle);
            adjacentPositions[i] = adjacentPosition;
        }

        for(LatLng position : adjacentPositions){
            createHexagon(position, radius);
        }

    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            return;
        }
        PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,  Manifest.permission.ACCESS_FINE_LOCATION, true);
    }

    @Override
    public boolean onMyLocationButtonClick(){
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location){
        Toast.makeText(this, "Current location: \n" + location, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode != LOCATION_PERMISSION_REQUEST_CODE){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION)){
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments(){
        super.onResumeFragments();
        if(permissionDenied){
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}