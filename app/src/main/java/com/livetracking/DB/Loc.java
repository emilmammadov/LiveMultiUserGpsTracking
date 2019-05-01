package com.livetracking.DB;

public class Loc {
    double lat, longitude;
    long time;
    String user;

    public Loc(){}

    public Loc(String user, double lat, double longitude) {
        this.lat = lat;
        this.longitude = longitude;
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

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Loc{" +
                "lat=" + lat +
                ", longitude=" + longitude +
                ", time=" + time +
                ", user='" + user + '\'' +
                '}';
    }
}
