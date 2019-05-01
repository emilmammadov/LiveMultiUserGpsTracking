package com.livetracking.DB;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class CarCount {
    public static int carcount(ArrayList<Location> path, Location begin, Location end, int disTol, long timeTol){
        List<Location> possibleBeginPoints = new ArrayList();
        List<Location> possibleEndPoints = new ArrayList();
        int count=0;
        
        Nearest beginNearest = new Nearest(disTol);
        Nearest endNearest = new Nearest(disTol);

        for(Location loc : path){
            double bdis = distance(begin,loc);
            double edis = distance(end,loc);
            if(beginNearest.proximity > bdis){
                beginNearest.point = loc;
                beginNearest.proximity = bdis;
            }
            else if(beginNearest.proximity != disTol){
                possibleBeginPoints.add(beginNearest.point);
                beginNearest.proximity = disTol;
                Log.e("BeginPoint","BeginPoint");
            }
            if(endNearest.proximity > edis){
                endNearest.point = loc;
                endNearest.proximity = edis;
            }
            else if(endNearest.proximity != disTol){
                possibleEndPoints.add(endNearest.point);
                endNearest.proximity = disTol;
                Log.e("EndPoint","EndPoint");
            }
        }

        Log.e("PossibleBeginPoints",possibleBeginPoints.size()+"");
        Log.e("PossibleEndPoints", possibleEndPoints.size() + "");
        
        Map<Location, Location> voyages = new HashMap<>();
        int i = 0;
        for(Location loce : possibleEndPoints){
            int lastBeginPoint = -1;
            int j = 0;
            for(Location locb : possibleBeginPoints){
                if(locb.time.compareTo(loce.time) < 0){
                    lastBeginPoint = j;
                }
                else{
                    break;
                }
                j++;
            }
            if(lastBeginPoint != -1){
                voyages.put(possibleBeginPoints.get(lastBeginPoint), possibleEndPoints.get(i));
                possibleBeginPoints = possibleBeginPoints.subList(lastBeginPoint+1, possibleBeginPoints.size());
            }
            i++;
        }

        i = 0;
        for(Map.Entry<Location, Location> voyage : voyages.entrySet()){
            /*boolean cond1 = voyage.getKey().time.toString().substring(11, 20).compareTo(begin.time.toString().substring(11, 20)) < 0;
            String d1 = voyage.getKey().time.toString().substring(11, 20);
            long date = begin.getTime().getTime() + (new Date(timeTol)).getTime();
            Date sum = new Date(date);
            String d2 = sum.toString().substring(11, 20);
            boolean cond2 = d1.compareTo(d2) > 0;
            
            long a = end.time.getTime() - begin.time.getTime();
            long b = voyages.get(voyage).time.getTime() - voyage.getKey().time.getTime();
            
            boolean cond3 = a < b;*/
            if(false){
                System.out.println("zamanı aşıyor: " + i);
                voyages.remove(i);
            }else{
                count++;
            }
        }

        return count;

    }
    
    /*private void printVoyage(Map<Location, Location> voyages){
        for(Map.Entry<Location, Location> voyage : voyages.entrySet()){
            System.out.println("Start: " + voyage.toString());
            System.out.println("End: " + voyages.get(voyage).toString());
            System.out.println("\n");
        }
    }*/
    
    /*public static double distance(Location p1, Location p2){
        return Math.sqrt(Math.pow((p1.lat-p2.lat), 2) + Math.pow((p1.lng - p2.lng), 2));
    }*/

    static double distance(Location p1, Location p2)  {
        double dLat = Math.toRadians(p2.lat - p1.lat);
        double dLon = Math.toRadians(p2.lng - p1.lng);
        p1.lat = Math.toRadians(p1.lat);
        p2.lat = Math.toRadians(p2.lat);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(p1.lat) * Math.cos(p2.lat);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6372.8 * c*1000;
    }
}
