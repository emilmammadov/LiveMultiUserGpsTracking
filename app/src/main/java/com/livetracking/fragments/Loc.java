package com.livetracking.fragments;

public class Loc {
    double lat, longitude, accur;

    public Loc(){}

    public Loc(double lat, double longitude, double accur) {
        this.lat = lat;
        this.longitude = longitude;
        this.accur = accur;
    }

    public double getLat() {
        return lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAccur() {
        return accur;
    }
}
