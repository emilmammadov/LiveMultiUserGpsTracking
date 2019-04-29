package com.livetracking.DB;

public class Loc {
    double lat, longitude, accur;
    long time;
    String user;

    public Loc(){}

    public Loc(String user, double lat, double longitude, double accur) {
        this.lat = lat;
        this.longitude = longitude;
        this.accur = accur;
        this.user = user;
    }

    public Loc(double lat, double longitude, long time) {
        this.lat = lat;
        this.longitude = longitude;
        this.time = time;
    }

    public long getTime() {
        return time;
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
