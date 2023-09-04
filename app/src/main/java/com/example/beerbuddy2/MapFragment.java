package com.example.beerbuddy2;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {

    SupportMapFragment supportMapFragment;
    GoogleMap googleMap;
    double currentLat=0 ,currentLong=0;
    SearchView searchViewMap;
    String completeAddress;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private double selectedLat, selectedLng;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_map, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        searchViewMap=view.findViewById(R.id.searchViewMap);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        mAdView = view.findViewById(R.id.adView);
        showMap();
        loadAds();
        searchViewMap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            List<Address> addressList = null;

            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchViewMap.getQuery().toString();
                if (location != null) {
                    Geocoder geocoder1 = new Geocoder(getContext());
                    try {
                        addressList = geocoder1.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap gMap) {
                            googleMap = gMap;
                            Address address1 = addressList.get(0);
                            LatLng latLng = new LatLng(address1.getLatitude(), address1.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(location);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                            googleMap.addMarker(markerOptions).showInfoWindow();

                            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    checkConnection();
                                    if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                                        selectedLat = latLng.latitude;
                                        selectedLng = latLng.longitude;
                                        GetAddress(selectedLat, selectedLng);

                                    } else {
                                        Toast.makeText(getContext(), "Please check your Connection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    });
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }

    private void loadAds() {

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void checkConnection() {
        connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
    }
    private void showMap() {

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        }
    }
    private void GetAddress(double mLat, double mLng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;

        if (mLat != 0) {
            try {
                addresses = geocoder.getFromLocation(mLat, mLng, 1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (addresses != null) {
                String mAddress = addresses.get(0).getAddressLine(0);
                String completeAddress = addresses.get(0).getAddressLine(0);


                if (mAddress != null) {

                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(mLat, mLng);
                        markerOptions.position(latLng).title(completeAddress);
                        googleMap.addMarker(markerOptions).showInfoWindow();

                    } else {
                        Toast.makeText(getContext(), "please Cancel or Add press", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Some thing wrong", Toast.LENGTH_SHORT).show();
            }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses;
                if(location!=null){
                    currentLat=location.getLatitude();
                    currentLong=location.getLongitude();
                    try {
                        addresses = geocoder.getFromLocation(currentLat, currentLong, 1);
                        Address address = addresses.get(0);
                         completeAddress = address.getAddressLine(0);
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap google) {
                                googleMap=google;
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat,currentLong),16));
                                LatLng latLng=new LatLng(currentLat,currentLong);
                                MarkerOptions markerOptions=new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(completeAddress);
                                googleMap.addMarker(markerOptions);
                            }
                        });

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==111){
            if(grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

}