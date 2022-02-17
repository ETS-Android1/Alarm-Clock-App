package com.eaes.alarm_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class Clock extends AppCompatActivity {

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Date currentTime = Calendar.getInstance().getTime();
                            TextView hourr = (TextView)findViewById(R.id.hour);
                            TextView min = (TextView)findViewById(R.id.min);
                            TextView sec = (TextView)findViewById(R.id.sec);
                            sec.setText(String.valueOf((  currentTime.getSeconds()) ));
                            min.setText(String.valueOf((currentTime.getMinutes())));
                            if ((currentTime.getHours())==12)
                                hourr.setText(String.valueOf((currentTime.getHours())));
                            else
                                hourr.setText(String.valueOf((currentTime.getHours())%12));
                            TextView type = (TextView)findViewById(R.id.am);
                            if ((currentTime.getHours())>=12)
                                type.setText("PM");
                            else
                                type.setText("AM");

                        }
                    });
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_clock);

            t.start();
            Button switchToAlarm = (Button) findViewById(R.id.Alarm_But);
            switchToAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Navigator(Clock.this , "Alarm");
                }
            });
        Button switchToTimer = (Button) findViewById(R.id.Timer_But);
        switchToTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(Clock.this , "Timer");

            }
        });
        Button switchToStop = (Button) findViewById(R.id.StopWatch_But);
        switchToStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(Clock.this , "StopWatch");

            }
        });
        Button logout = (Button) findViewById(R.id.Logout_But);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                staticVars.logout = true;
                finish();

            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(staticVars.logout)
            finish();
    }
}