package com.livetracking.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.livetracking.DB.Loc;
import com.livetracking.R;

import static android.content.Context.LOCATION_SERVICE;

public class Map extends Fragment implements OnMapReadyCallback {

    DatabaseReference liveReference = FirebaseDatabase.getInstance().getReference("live");
    private GoogleMap mMap;
    SharedPreferences sp;
    private LocationManager locationManager;
    private LocationListener listener;
    private LatLng latLng;
    Criteria criteria;
    Marker marker;
    Circle circle;

    public Map() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        sp = getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if(sp.getString("mapFrom","").equals("share"))
            getLocation("share");
        else if(sp.getString("mapFrom","").equals("find"))
            find();
        else if(sp.getString("mapFrom","").equals("track"))
            getLocation("track");

    }

    public void find(){
        liveReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    Loc location = locationSnapshot.getValue(Loc.class);
                    Log.e("Locations updated", "location: " + location.getAccur());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getLocation(String strFrom) {

        final String username = sp.getString("username","");
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        final String provider = locationManager.getBestProvider(criteria, true);
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(30, 30)).title("Lokasyon"));
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(30, 30))
                .radius(5)
                .strokeWidth(3f)
                .strokeColor(Color.CYAN)
                .fillColor(Color.argb(70, 0, 255, 255)));

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("ACCURACY", location.getAccuracy() + "");

                liveReference.child(username).setValue(new Loc(username,location.getLatitude(),location.getLongitude(),location.getAccuracy()));

                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                marker.setPosition(latLng);
                circle.setCenter(latLng);
                circle.setRadius(location.getAccuracy());

                locationManager.removeUpdates(listener);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

                if (provider == null) {
                    Toast.makeText(getContext(), "NULL_PROVIDER", Toast.LENGTH_SHORT).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(provider, 0, 0, listener);
                    }
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


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.e("requestLocationUpdates", "1");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);

        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listener != null)
            locationManager.removeUpdates(listener);
    }
}
