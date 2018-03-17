package com.example.cse110.flashbackmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimeChangeActivity extends AppCompatActivity {
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_change);

        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);

        Button submitTime = findViewById(R.id.submit_time);
        submitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker timePicker = findViewById(R.id.timePicker);
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                Calendar calendar = OurCal.getCalendar();
                calendar.set(year, month, day, hour, minute);
                OurCal.setCalendar(calendar);
                Log.i("TimeChangeAcitivity", "Set time to " + hour + ":" + minute);
                finish();
            }
        });

        Button cancel = findViewById(R.id.cancel_time);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
