package com.eaes.alarm_;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDBHelper extends SQLiteOpenHelper {
    private static String databaseName = "alarmDatabase";
    SQLiteDatabase alarmDatabase;


    public AlarmDBHelper (Context context){
        super(context , databaseName , null , 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Alarms (AlarmId integer primary key," +
                "UserId integer ," +
                "Time integer ," +
                "Active integer)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists Alarms");
        onCreate(db);
    }

    public void createNewAlarm(int userId , int alarmId , boolean isActive , long time){
        ContentValues row = new ContentValues();
        row.put("AlarmId" , alarmId);
        row.put("UserId",userId);
        row.put("Time",time);
        if (isActive)
            row.put("Active",1);
        else
            row.put("Active",0);
        alarmDatabase = getWritableDatabase();
        alarmDatabase.insert("Alarms" , null , row);
        alarmDatabase.close();
    }


    public Cursor fetchAlarms(){
        alarmDatabase = getReadableDatabase();
        String[] rowDetails = {"AlarmId","UserId","Time","Active"};
        Cursor cursor = alarmDatabase.query("Alarms", rowDetails , null,null,null,null,null);
        if (cursor != null)
            cursor.moveToFirst();
        alarmDatabase.close();
        return cursor;
    }

    public void deleteAlarm (int alarmId){
        alarmDatabase = getWritableDatabase();
        String x = String.valueOf(alarmId);
        alarmDatabase.execSQL("Delete from Alarms where alarmId = ?", new String[]{x} );
        alarmDatabase.close();
    }
}
