package com.livetracking.fragments;

public class Loc {
    double lat, longitude, accur;
    String user;

    public Loc(){}

    public Loc(String user, double lat, double longitude, double accur) {
        this.lat = lat;
        this.longitude = longitude;
        this.accur = accur;
        this.user = user;
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

    public String getUser() {
        return user;
    }
}
