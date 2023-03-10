package com.example.gps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class Coords extends AppCompatActivity {

    TextView tvLat, tvLng;
    Button coords;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coords);

        tvLat = (TextView) findViewById(R.id.lat);
        tvLng = (TextView) findViewById(R.id.lng);
        coords = findViewById(R.id.getCoords);


        coords.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getCoords();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }


    private void getCoords() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                if(location != null){
                    Log.i("auuuu", " aaaaaa ");
                    Toast.makeText(getApplicationContext(),"Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                    Log.i("auuuu",location.getLatitude() + " , " + location.getLongitude());
                    tvLat.setText(String.valueOf( location.getLatitude()));
                    tvLng.setText(String.valueOf( location.getLongitude()));

                    //testing
                    Intent intent = new Intent(Coords.this,Maps.class);
                    intent.putExtra("Latitude",(double) location.getLatitude());
                    intent.putExtra("Longitude",(double) location.getLongitude());
                    startActivity(intent);
                }
            }
        });
    }
}