package com.example.cse110.flashbackmusic;


import java.util.Calendar;

/**
 * Created by CubicDolphin on 3/3/18.
 */

public class PlayInstance {
    public String userId;
    public double latitude;
    public double longitude;
    public long timeInMillis;

    // Standard constructor
    public PlayInstance (User user, LatLon location, Calendar time) {
        this.userId = user.getUserId();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.timeInMillis = time.getTimeInMillis();
    }
}
