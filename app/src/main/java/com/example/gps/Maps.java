package com.example.gps;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gps.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    LatLng marca;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        marca = new LatLng(4,4);
        getCoords();

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
        //getCoords();

        //Toast.makeText(getApplicationContext(), "Latitude: " + marca.latitude + ", Longitude: " + marca.longitude, Toast.LENGTH_LONG).show();

        mMap = googleMap;

        // Add a marker in the actual pos and move the camera

        mMap.addMarker(new MarkerOptions().position(marca).title((String) getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marca));
    }

    public StringBuilder getTitle(LatLng pob) throws IOException {

        Geocoder geocoder = new Geocoder(Maps.this, Locale.getDefault());
        List<Address> addressList = geocoder.getFromLocation(pob.latitude,pob.longitude,1);

        if (addressList.size()>0){
            StringBuilder poblacio = new StringBuilder(String.valueOf(addressList.get(0).getLocality()));
            String adress = String.valueOf(addressList.get(0).getAddressLine(0));

            poblacio.append( " " + adress);
            return poblacio;
        }
        return new StringBuilder("xd");

    }

    public void getCoords() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        2);
            }
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {

                    marca = new LatLng(location.getLatitude(),location.getLongitude());

                    Toast.makeText(getApplicationContext(), "Latitude: " + marca.latitude + ", Longitude: " + marca.longitude, Toast.LENGTH_LONG).show();

                    //Toast.makeText(getApplicationContext(), "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                    //lat = location.getLatitude();
                    //lng = location.getLongitude();

                }
            }
        });
        //Toast.makeText(getApplicationContext(), "Latitude: " + marca.latitude + ", Longitude: " + marca.longitude, Toast.LENGTH_LONG).show();

    }




}

