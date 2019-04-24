package com.livetracking.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.livetracking.R;

import static android.content.Context.LOCATION_SERVICE;

public class Map extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;

    SharedPreferences sp;

    private LocationManager locationManager;
    private LocationListener listener,listener2;


    public Map() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_map, container, false);

        sp= getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);

        return  rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        get_current_location();

    }

    public void get_current_location(){
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        listener2 = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("Your current location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                //if GPS setting is off. It will redirect to the setting
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(i);
            }
        };


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("Your current location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                locationManager.removeUpdates(listener);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 0, listener2);
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                //if GPS setting is off. It will redirect to the setting
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(i);
            }
        };



        update_new_location();
    }


    void update_new_location() {
        Toast.makeText(getContext(),"Getting current location..",Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, listener);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listener!=null)
            locationManager.removeUpdates(listener);
        if(listener2!=null)
            locationManager.removeUpdates(listener2);
    }
}
