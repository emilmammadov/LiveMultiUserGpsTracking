package com.livetracking.DB;

public class Nearest {
    double proximity;
    Location point;

    public Nearest() {
    }

    public Nearest(double proximity) {
        this.proximity = proximity;
    }
    
    public Nearest(double proximity, Location point) {
        this.proximity = proximity;
        this.point = point;
    }
}
