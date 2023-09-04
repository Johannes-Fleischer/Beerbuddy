package com.example.beerbuddy2;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class menue_haupt_home extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;



     //@Override
   // public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            // Bundle savedInstanceState) {
      //  Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_menue_haupt_home, container, false);
   // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menue_haupt_home);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;
        LatLng stuttgart = new LatLng(-48.7, 9.1);
        myMap.addMarker(new MarkerOptions().position(stuttgart).title("stuttgart"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(stuttgart));
    }
}