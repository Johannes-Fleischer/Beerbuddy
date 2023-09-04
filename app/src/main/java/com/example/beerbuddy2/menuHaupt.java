package com.example.beerbuddy2;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Toolbar-Klasse
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;

public class menuHaupt extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override // Festlegung des Hauptmenüs
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar); // Initalisierung der Toolbar
        setSupportActionBar(toolbar);

        //Navigationsschublade
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.naw_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Verknüpfung von Navigationsschublade mit der App Leiste
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.Close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
        toolbar.setTitle("Search Map");
        toolbar.setTitle("");
        //Überprüft zustand der Aktivität, ob es gespeichert ist.
         //if (savedInstanceState == null) {
          //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new menue_haupt_home()).commit();
            //navigationView.setCheckedItem(R.id.menue_haupt_home);
       // }
    }


    //Navigation durch verschiedene Abschnitte.

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menue_haupt_home) {
            toolbar.setTitle("Search Map");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();

            // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new menue_haupt_home()).commit();
        } else if (itemId == R.id.menue_haupt_advertisement)  {
            toolbar.setTitle("");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new menue_haupt_advertisement()).commit();
        } else if (itemId == R.id.menue_haupt_message) {
            toolbar.setTitle("");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new menue_haupt_message()).commit();
        } else if (itemId == R.id.menue_haupt_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new menue_haupt_settings()).commit();
            toolbar.setTitle("");
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // Passt verhalten des Zurück buttons an.
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
