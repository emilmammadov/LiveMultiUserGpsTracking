package com.livetracking.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.livetracking.DB.CarCount;
import com.livetracking.DB.Loc;
import com.livetracking.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.LOCATION_SERVICE;

public class Map extends Fragment implements OnMapReadyCallback {

    DatabaseReference liveYayaReference = FirebaseDatabase.getInstance().getReference("liveYaya");
    DatabaseReference trackReference = FirebaseDatabase.getInstance().getReference("track");
    DatabaseReference liveSurucuReference = FirebaseDatabase.getInstance().getReference("liveSurucu");
    ArrayList<com.livetracking.DB.Location> locs = new ArrayList<>();
    ArrayList<ArrayList<com.livetracking.DB.Location>> trajectories = new ArrayList<>();
    private GoogleMap mMap;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private LocationManager locationManager;
    private LocationListener listener;
    private LatLng latLng;
    Criteria criteria;
    Marker marker;
    ArrayList<Marker> markers = new ArrayList<>();
    static long time;
    static String[] opts = {"Başlangıç","Bitiş"};
    Date date, finishDate;
    LatLng start,finish;
    int predictCount=0;

    public Map() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        sp = getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
        editor = sp.edit();

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
            getLocation("find");
        else if(sp.getString("mapFrom","").equals("predict")){
            predict();
        }
        else if(sp.getString("mapFrom","").equals("track"))
        {
            time = System.currentTimeMillis();
            getLocation("track");
        }


    }

    private void predict() {
        final int disTol = sp.getInt("distancePredict",0);
        final int timeTol = sp.getInt("tolTimePredict",0);
        final int maxTime = sp.getInt("maxTimePredict",0);

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    boolean check = true;

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (check){
                            check = false;
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            cal.set(Calendar.MINUTE,minute);
                            date = cal.getTime();
                            finishDate = new Date(cal.getTimeInMillis() + ((maxTime + timeTol)*60000));

                        }

                    }
                },mHour, mMinute,true);
        timePickerDialog.show();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                if(sp.getString("mapFrom","").equals("predict")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setItems(opts, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if ("Başlangıç".equals(opts[which])) {
                                Log.e("Baslangic","deneme");
                                start = latLng;
                            } else {
                                finish = latLng;

                                for (ArrayList<com.livetracking.DB.Location> den: trajectories){
                                    predictCount += new CarCount().carcount(den, new com.livetracking.DB.Location(start.latitude,start.longitude,date), new com.livetracking.DB.Location(finish.latitude,finish.longitude,finishDate), disTol,timeTol);
                                }
                                Toast.makeText(getContext(),predictCount+"",Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                    builder.show();
                }
            }
        });



        trackReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trajectories.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    locs = new ArrayList<com.livetracking.DB.Location>();
                    for (DataSnapshot post: postSnapshot.getChildren()){
                        Loc loc = post.getValue(Loc.class);
                        locs.add(new com.livetracking.DB.Location(loc.getLat(),loc.getLongitude(), new Date(loc.getTime())));
                    }
                    trajectories.add(locs);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void find(final double latitude, final double longitude, String where){

        if(where.equals("find")){
            liveSurucuReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("onDataChange","ONDATACHANGE(1)");
                    for(Marker marker: markers){
                        marker.setVisible(false);
                    }
                    markers.clear();
                    if(sp.getString("find","").equals("on")){


                        for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                            Loc loc = locationSnapshot.getValue(Loc.class);

                            if((haversine(loc.getLat(),loc.getLongitude(),latitude,longitude))<sp.getInt("distanceYaya",0)){
                                Toast.makeText(getContext(),sp.getInt("distanceYaya",0)+"",Toast.LENGTH_SHORT).show();
                                markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLat(),loc.getLongitude())).title(loc.getUser())));
                            }

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(where.equals("share")){
            liveYayaReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("onDataChange","ONDATACHANGE(1)");
                    for(Marker marker: markers){
                        marker.setVisible(false);
                    }
                    markers.clear();
                    if(sp.getString("share","").equals("on")){


                        for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                            Loc loc = locationSnapshot.getValue(Loc.class);

                            if((haversine(loc.getLat(),loc.getLongitude(),latitude,longitude))<sp.getInt("distanceSurucu",0)){
                                Toast.makeText(getContext(),sp.getInt("distanceSurucu",0)+"",Toast.LENGTH_SHORT).show();
                                markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLat(),loc.getLongitude())).title(loc.getUser())));
                            }

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    }


    public void getLocation(final String strFrom) {

        final String username = sp.getString("username","");
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        final String provider = locationManager.getBestProvider(criteria, true);
        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(30, 30)).title("Lokasyonum"));

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(strFrom.equals("find")){
                    liveYayaReference.child(username).setValue(new Loc(username,location.getLatitude(),location.getLongitude()));
                    find(location.getLatitude(),location.getLongitude(),"find");
                }
                else if(strFrom.equals("share")){
                    liveSurucuReference.child(username).setValue(new Loc(username,location.getLatitude(),location.getLongitude()));
                    find(location.getLatitude(),location.getLongitude(),"share");
                }
                else if(strFrom.equals("track")){
                    trackReference.child(time+"").child(System.currentTimeMillis()+"").setValue(new Loc(location.getLatitude(),location.getLongitude(),System.currentTimeMillis()));
                }


                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                marker.setPosition(latLng);

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

    static double haversine(double lat1, double lon1, double lat2, double lon2)  {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6372.8 * c*1000;
    }
}
