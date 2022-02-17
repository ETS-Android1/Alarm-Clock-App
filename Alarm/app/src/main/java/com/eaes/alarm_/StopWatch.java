package com.eaes.alarm_;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopWatch extends AppCompatActivity {
    Thread T = new Thread() {

        @Override
        public void run() {


            try {
                while (!isInterrupted()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            TextView Seconds_Text = (TextView) findViewById(R.id.StopWatch_Seconds);
                            TextView Minutes_Text = (TextView) findViewById(R.id.StopWatch_Minutes);
                            TextView Hours_Text = (TextView) findViewById(R.id.StopWatch_Hours);
                            if (StopWatchHelper.Working) {
                                StopWatchHelper.MilliSeconds++;
                                if (StopWatchHelper.MilliSeconds == 100) {
                                    StopWatchHelper.Seconds++;
                                    StopWatchHelper.MilliSeconds = 0;

                                }
                                if (StopWatchHelper.Seconds == 60) {
                                    StopWatchHelper.Minutes++;
                                    StopWatchHelper.Seconds = 0;
                                }

                            }

                            Seconds_Text.setText(String.valueOf(StopWatchHelper.MilliSeconds));
                            Minutes_Text.setText(String.valueOf(StopWatchHelper.Seconds));
                            Hours_Text.setText(String.valueOf(StopWatchHelper.Minutes));

                        }

                    });
                    if(StopWatchHelper.Reset)
                        StopWatchHelper.Reset = false;
                    else
                        Thread.sleep(10);
                }
            } catch (InterruptedException e) {
            }
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        T.start();
        final Button Reset_StopWatch = (Button) findViewById(R.id.Reset_But);
        final Button Resume_StopWatch = (Button) findViewById(R.id.Resume_But);


        Reset_StopWatch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                StopWatchHelper.ResetWatch();
                StopWatchHelper.StopWatch();
                Resume_StopWatch.setText("Start");
            }

        });

        Resume_StopWatch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (StopWatchHelper.Working) {
                    StopWatchHelper.Working = false;
                    Resume_StopWatch.setText("Start");
                }
                else {
                    StopWatchHelper.Working = true;
                    Resume_StopWatch.setText("Stop");

                }
            }
        });

        Button switchToAlarm = (Button) findViewById(R.id.Alarm_But);
        switchToAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(StopWatch.this , "Alarm");

            }
        });
        Button switchToTimer = (Button) findViewById(R.id.Timer_But);
        switchToTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(StopWatch.this , "Timer");

            }
        });
        Button switchToClock = (Button) findViewById(R.id.Clock_But);
        switchToClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(StopWatch.this , "Clock");

            }
        });
        Button logout = (Button) findViewById(R.id.Logout_But);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                staticVars.logout = true;
                T.interrupt();
                StopWatchHelper.ResetWatch();
                StopWatchHelper.StopWatch();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(staticVars.logout)
        {
            T.interrupt();
            StopWatchHelper.StopWatch();
            StopWatchHelper.ResetWatch();

            finish();
        }
    }
}


class StopWatchHelper
{
    static int MilliSeconds = 0, Seconds = 0, Minutes = 0;
    static boolean Working = false;
    static boolean Reset = false;
    static boolean Exit = false;

    protected static void StopWatch(){
        Working = false;
    }
    protected static void ResetWatch()
    {
        Reset = true;
        MilliSeconds = 0;
        Seconds = 0;
        Minutes = 0;
    }
}