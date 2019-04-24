package com.livetracking.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.ALARM_SERVICE;


public class Boot_receiver extends BroadcastReceiver {
    Context context;
    SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;

        sp=context.getSharedPreferences("myData",Context.MODE_PRIVATE);
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            startService();
        }
    }

    public void startService() {
        if(sp.getString("service","").equals("on")){
            Intent intent=new Intent(context,Alarm_broadcast.class);
            PendingIntent pi= PendingIntent.getBroadcast(context,0,intent,0);
            AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+2000,5*60000,pi);

        }
    }
}