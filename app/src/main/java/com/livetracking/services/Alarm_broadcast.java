package com.livetracking.services;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.Date;

import static android.content.Context.LOCATION_SERVICE;

public class Alarm_broadcast extends BroadcastReceiver {
    Context context2;

    private LocationManager locationManager;
    private LocationListener listener;

    SQLiteDatabase db;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context2=context;

        initialize();
    }

    public void initialize(){
        locationManager = (LocationManager) context2.getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Date new_date=new Date();
                long long_date=new_date.getTime();

                db.close();
                Toast.makeText(context2,"Location added.",Toast.LENGTH_SHORT).show();

                locationManager.removeUpdates(listener);
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
                context2.startActivity(i);
            }
        };

        update_new_location();
    }


    void update_new_location() {
        Toast.makeText(context2,"Updating GPS",Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(context2, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 7000, 0, listener);

            //last location is inserted
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


            Date new_date=new Date();
            long long_date=new_date.getTime();

            db.close();
            Toast.makeText(context2,"Location added.",Toast.LENGTH_SHORT).show();
        }

    }
}