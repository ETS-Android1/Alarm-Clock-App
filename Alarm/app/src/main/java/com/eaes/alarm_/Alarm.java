package com.eaes.alarm_;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm implements Comparable{
    int userId, alarmId;
    long time;
    boolean isActive;
    String time_as_String;




    public Alarm(int userId, int alarmId, Calendar calendar, boolean isActive) {
        this.userId = userId;
        this.alarmId = alarmId;
        this.time = calendar.getTimeInMillis();
        this.isActive = isActive;

        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        time_as_String = timestamp.toString();
        time_as_String = time_as_String.substring(11 , 16);
    }
    public static int toMillis(int hr , int mins)
    {
        hr = hr*60*60;
        mins = mins*60;
        hr = hr + mins;
        return hr*1000;
    }
    static long getDiffMillis(int hr , int mins , long currentTime){
        Date date = Calendar.getInstance().getTime();
        int currentHours , currentMins;
        currentHours = date.getHours();
        currentMins = date.getMinutes();

        int hours = hr - currentHours;
        int minutes = mins = currentMins;

        return toMillis(hours , minutes);
    }

    @Override
    public int compareTo(Object o) {
        Alarm other = (Alarm) o;
        return Long.compare(other.time, this.time);


    }
}