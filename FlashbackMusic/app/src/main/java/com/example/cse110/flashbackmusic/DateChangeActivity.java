package com.example.cse110.flashbackmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class DateChangeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_change);

        final DatePicker datePicker = findViewById(R.id.datePicker);
        Button submit = findViewById(R.id.submit_time);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                startDateChangeActivity(year, month, day);
            }
        });

        Button back = findViewById(R.id.back_time);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button reset = findViewById(R.id.reset_time);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OurCal.resetCalendar();
                finish();
            }
        });
    }

    private void startDateChangeActivity (int year, int month, int day) {
        Intent intent = new Intent(this, TimeChangeActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        startActivity(intent);
        finish();
    }
}
