package com.livetracking.DB;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class CarCount {
    public static int carcount(ArrayList<Location> path, Location begin, Location end, int disTol, long timeTol){
        Nearest beginNearest = new Nearest(disTol);
        Nearest endNearest = new Nearest(disTol);

        for(Location loc : path){
            double disb = distance(begin.lat,begin.lng,loc.lat,loc.lng);
            double dise = distance(end.lat,end.lng,loc.lat,loc.lng);


            if(beginNearest.proximity > disb){
                beginNearest.point = loc;
                beginNearest.proximity = disb;
                Log.e("BEGINNEAREST","BEGINNEAREST");
                Log.e("DISB",disb+"");
            }
            if(endNearest.proximity > dise){
                endNearest.point = loc;
                endNearest.proximity = dise;
                Log.e("ENDNEAREST","ENDNEAREST");
                Log.e("DISE",dise+"");
            }
        }

        if(beginNearest.point != null && endNearest.point != null){
            Log.e("ILK IF","ILK IF");
            boolean cond1 = beginNearest.point.getTime().toString().substring(11, 20).compareTo(begin.time.toString().substring(11, 20)) > 0;
            boolean cond2 = beginNearest.point.getTime().toString().substring(11, 20).compareTo(new Date(begin.time.getTime() + timeTol).toString().substring(11, 20)) < 0;
            boolean cond3 = endNearest.point.getTime().toString().substring(11, 20).compareTo(end.time.toString().substring(11, 20)) < 0;

            if(cond1 && cond2 && cond3){
                Log.e("IKINCI IF","IKINCI IF");
                return 1;
            }
            else{
                Log.e("ILK ELSE","ILK ELSE");
                return 0;
            }
        }
        else {
            Log.e("IKINCI ELSE","IKINCI ELSE");
            return 0;
        }
    }

    static double distance(double lat1, double lon1, double lat2, double lon2)  {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6372.8 * c*1000;
    }
}
