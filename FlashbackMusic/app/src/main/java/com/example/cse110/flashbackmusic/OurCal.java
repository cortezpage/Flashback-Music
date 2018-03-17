package com.example.cse110.flashbackmusic;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by CubicDolphin on 3/16/18.
 */

public class OurCal {
    private static Calendar calendar;

    public static Calendar getCalendar () {
        if (calendar == null) {
            return Calendar.getInstance();
        } else {
            return (Calendar)calendar.clone();
        }
    }

    public static void setCalendar (Calendar calendar) {
        OurCal.calendar = calendar;
        Log.i("MainActivity", "Set calendar to " + calendar.getTime());
    }

    public static void resetCalendar () {
        OurCal.calendar = null;
        Log.i("MainActivity", "Reset calendar");
    }
}
