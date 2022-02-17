package com.eaes.alarm_;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class myListAdapter extends ArrayAdapter<Alarm> {


    List<Alarm>alarmList;

    Context context;
    int resource;
    AlarmsActivity alarmsActivity;

    public myListAdapter(@NonNull Context context, int resource, @NonNull List<Alarm> objects , AlarmsActivity alarmsActivity) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.alarmList=objects;
        this.alarmsActivity = alarmsActivity;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        TextView clock=view.findViewById(R.id.timeText);
        Button deleteBtn=view.findViewById(R.id.buttonDelete);
        final Switch enableSwitch=view.findViewById(R.id.enableSwitch);

        Alarm alarm=alarmList.get(position);
        clock.setText(alarm.time_as_String);
        enableSwitch.setChecked(alarm.isActive);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeHero(position);

            }
            private void removeHero(final int position) {
                //Creating an alert dialog to confirm the deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure you want to delete this?");

                //if the response is positive in the alert
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //removing the item

                        AlarmDBHelper alarmDB = new AlarmDBHelper(context);
                        alarmDB.deleteAlarm(alarmList.get(position).alarmId);

                        alarmsActivity.clearAlarm(alarmList.get(position));

                        alarmList.remove(position);
                        Toast.makeText(context,"Alarm Was Removed.",Toast.LENGTH_LONG).show();

                        //reloading the list
                        notifyDataSetChanged();
                    }
                });

                //if response is negative nothing is being done
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //creating and displaying the alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        enableSwitch.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean enabled = enableSwitch.isChecked();
                AlarmDBHelper alarmDB = new AlarmDBHelper(context);
                alarmDB.deleteAlarm(alarmList.get(position).alarmId);
                alarmDB.createNewAlarm(alarmList.get(position).userId , alarmList.get(position).alarmId , enabled , alarmList.get(position).time);
                alarmList.get(position).isActive = enabled;
                if (enabled)
                    alarmsActivity.setAlarm(alarmList.get(position));
                else
                    alarmsActivity.clearAlarm(alarmList.get(position));
            }
        });
        return view;

    }
}
