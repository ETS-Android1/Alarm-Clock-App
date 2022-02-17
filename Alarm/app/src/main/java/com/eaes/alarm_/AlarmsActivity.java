package com.eaes.alarm_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AlarmsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    AlarmDBHelper alarmDBHelper;
    LinkedList<Alarm> alarmLinkedList;
    List<Alarm>alarmList;
    ListView listView;
    int maxId;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        init();

        Button switchToClock = (Button) findViewById(R.id.Clock_But);
        switchToClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(AlarmsActivity.this , "Clock");
            }
        });
        Button switchToTimer = (Button) findViewById(R.id.Timer_But);
        switchToTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new Navigator(AlarmsActivity.this , "Timer");
            }
        });
        Button switchToStop = (Button) findViewById(R.id.StopWatch_But);
        switchToStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(AlarmsActivity.this , "StopWatch");
            }
        });
        final Button logout = (Button) findViewById(R.id.Logout_But);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //(new Navigator(AlarmsActivity.this , "lol")).logout(AlarmsActivity.this);
                staticVars.logout = true;
                finish();

            }
        });

        ImageButton addBtn=(ImageButton) findViewById(R.id.addAlarmBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager() , "time picker");
            }
        });
    }

    protected void init() {
        alarmDBHelper=new AlarmDBHelper(this);
        alarmLinkedList=new LinkedList<Alarm>();

        alarmList=new ArrayList<>();
        listView=(ListView)findViewById(R.id.alarmsList);

        maxId = 0;

        Cursor cursor=alarmDBHelper.fetchAlarms();

        while(!cursor.isAfterLast() && cursor != null) {
            int alarmId,userId;
            Long time;
            boolean isActive;
            alarmId=Integer.parseInt(cursor.getString(0));
            userId=Integer.parseInt(cursor.getString(1));
            time=Long.parseLong(cursor.getString(2));
            int temp=Integer.parseInt(cursor.getString(3));

            if(temp==0)
                isActive=false;
            else
                isActive=true;

            if(staticVars.userPublicId == userId) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                Alarm alarm=new Alarm(userId,alarmId,calendar,isActive);
                alarmList.add(alarm);
            }
            if(maxId < alarmId)
            {
                maxId=alarmId;
            }
            cursor.moveToNext();
        }

        Collections.sort(alarmList, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm alarm, Alarm t1) {
                return Long.compare(alarm.time , t1.time);
            }
        });

        staticVars.alarmPublicId = maxId+1;
        myListAdapter adapter=new myListAdapter(this,R.layout.alarm_list_item,alarmList , this);
        listView.setAdapter(adapter);

        for (int i = 0 ; i < alarmList.size() ; i++){
            if (alarmList.get(i).isActive)
                setAlarm(alarmList.get(i));
        }
    }

    public void setAlarm(Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        PendingIntent alarmintent = PendingIntent.getBroadcast(this , alarm.alarmId ,intent , 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP , alarm.time , alarmintent);
    }

    public void clearAlarm(Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReciever.class);
        PendingIntent alarmintent = PendingIntent.getBroadcast(this , alarm.alarmId ,intent , 0);
        alarmManager.cancel(alarmintent);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int Minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY , hourOfDay);
        calendar.set(Calendar.MINUTE , Minute);
        calendar.set(Calendar.SECOND , 0);

        alarmList.add(new Alarm(staticVars.userPublicId , staticVars.alarmPublicId , calendar , true));

        alarmDBHelper.createNewAlarm(staticVars.userPublicId , staticVars.alarmPublicId , true ,  calendar.getTimeInMillis());

        setAlarm(alarmList.get(alarmList.size()-1));

        Collections.sort(alarmList, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm alarm, Alarm t1) {
                return Long.compare(alarm.time , t1.time);
            }
        });

        listView.invalidateViews();

        staticVars.alarmPublicId++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(staticVars.logout)
            finish();
    }

    protected void clearAllAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        for (int i = 0; i < alarmList.size() - 1; i++) {
            if (alarmList.get(i).isActive) {
                Intent intent = new Intent(this, AlertReciever.class);
                PendingIntent alarmintent = PendingIntent.getBroadcast(this, alarmList.get(i).alarmId, intent, 0);
                alarmManager.cancel(alarmintent);
            }
        }
    }
}