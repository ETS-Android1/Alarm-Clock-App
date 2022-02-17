package com.eaes.alarm_;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import android.os.Vibrator;
public class Timer extends AppCompatActivity {
    private ProgressBar pro;
    private EditText Sec_text;
    private EditText Min_text;
    private EditText Hour_text;
    private TextView count_down;
    private Button set_button;
    private Button start_pause_button;
    private Button rest_button;
    private CountDownTimer count_down_timer;
    private boolean timer_continue;
    private long start_time;
    private long time_left;
    Vibrator vibrat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Button switchToAlarm = (Button) findViewById(R.id.Alarm_But);
        switchToAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(Timer.this , "Alarm");
            }
        });
        Button switchToClock = (Button) findViewById(R.id.Clock_But);
        switchToClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(Timer.this , "Clock");
            }
        });
        Button switchToStop = (Button) findViewById(R.id.StopWatch_But);
        switchToStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Navigator(Timer.this , "StopWatch");
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
        vibrat=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        pro=findViewById(R.id.progress_bar);
        Sec_text =findViewById(R.id.SeditTextTextPersonName2);
        Min_text = findViewById(R.id.miedit_text_input);
        Hour_text =findViewById(R.id.HeditTextTextPersonName);
        count_down = findViewById(R.id.text_view_countdown);
        set_button = findViewById(R.id.button_set);
        start_pause_button = findViewById(R.id.start_pause);
        rest_button = findViewById(R.id.reset);
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_s= Sec_text.getText().toString();
                String input_m = Min_text.getText().toString();
                String input_h= Hour_text.getText().toString();
                long all_time = 0,mall_time=0,hall_time=0;
                if(input_s.length()!=0){
                 all_time = Long.parseLong(input_s) * 1000 ;}
                if(input_m.length()!=0){
                 mall_time=  Long.parseLong(input_m) * 60000 ;}
                if(input_h.length()!=0){
                hall_time =   Long.parseLong(input_h) * 3600000;}
                if (input_m.length() == 0&&input_s.length()==0&&input_h.length()==0) {
                    Toast.makeText(getApplicationContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                start_pause_button.setVisibility(View.VISIBLE);
                long millisInput = all_time+mall_time+hall_time ;

                setTime(millisInput);
                Sec_text.setText("");
                Min_text.setText("");
                Hour_text.setText("");
            }
        });
        start_pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer_continue) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        rest_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void setTime(long milliseconds) {
        start_time = milliseconds;
        pro.setMax((int)(start_time));
        resetTimer();

    }
    private void startTimer() {

        count_down_timer = new CountDownTimer(time_left, 1) {

            @Override
            public void onTick(long time_until_finshed) {
                time_left = time_until_finshed;
                updateCountDownText();
            }
            @Override
            public void onFinish() {

                timer_continue = false;
                updateWatchInterface();
            }
        }.start();

        timer_continue = true;
        updateWatchInterface();
    }
    private void pauseTimer() {
        count_down_timer.cancel();
        timer_continue = false;
        updateWatchInterface();
    }
    private void resetTimer() {
        time_left = start_time;
        updateCountDownText();
        updateWatchInterface();
    }
    private void updateCountDownText() {
        int hours = (int) (time_left / 1000) / 3600;
        int minutes = (int) ((time_left / 1000) % 3600) / 60;
        int seconds = (int) (time_left / 1000) % 60;


        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);


        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);

        }

        pro.setProgress((int)(time_left));
        if(time_left==0){
            
            vibrat.vibrate(3000);}
        count_down.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (timer_continue) {
            Sec_text.setVisibility(View.INVISIBLE);
            Min_text.setVisibility(View.INVISIBLE);
            Hour_text.setVisibility(View.INVISIBLE);
            set_button.setVisibility(View.INVISIBLE);
            rest_button.setVisibility(View.INVISIBLE);
            start_pause_button.setText("Pause");
        } else {
            Sec_text.setVisibility(View.VISIBLE);
            Min_text.setVisibility(View.VISIBLE);
            Hour_text.setVisibility(View.VISIBLE);
            set_button.setVisibility(View.VISIBLE);
            start_pause_button.setText("Start");
            if (time_left < 1000) {
                start_pause_button.setVisibility(View.INVISIBLE);
            } else {
                start_pause_button.setVisibility(View.VISIBLE);
            }
            if (time_left < start_time) {
                rest_button.setVisibility(View.VISIBLE);
            } else {
                rest_button.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (staticVars.logout)
            finish();

    }

}