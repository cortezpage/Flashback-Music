package com.example.cse110.flashbackmusic;


import java.util.Calendar;

/**
 * Created by CubicDolphin on 3/3/18.
 */

public class PlayInstance {
    Song song;
    LatLon location;
    Calendar time;

    // Standard constructor
    public PlayInstance (Song song, LatLon location, Calendar time) {
        this.song = song;
        this.location = location;
        this.time = time;
    }

    // Constructor that builds PlayInstance from String retrieved from Firebase
    public PlayInstance (String stringified) {};

    // Convert PlayInstance into String easily stored in Firebase
    public String stringify () {
        return "Example! =D";
    };
}
