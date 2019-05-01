package com.livetracking.DB;

import java.util.Date;

public class Location {
    double lat;
    double lng;
    Date time;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public Date getTime() {
        return time;
    }

    public Location() {
    }

    public Location(double lat, double lng, Date time) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }
}
