package com.eaes.alarm_;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.InspectableProperty;
import androidx.appcompat.app.AppCompatActivity;

public class Navigator extends AppCompatActivity {
    Object object;
    AlarmsActivity alarmsActivity;
    public Navigator(Context context , String mode){
        if (mode.equals("Alarm"))
            object = new AlarmsActivity();
        if (mode.equals("Timer"))
            object = new Timer();
        if (mode.equals("StopWatch"))
            object = new StopWatch();
        if (mode.equals("Clock"))
            object = new Clock();

        Intent i = new Intent(context , object.getClass());
        context.startActivity(i);
    }

}
