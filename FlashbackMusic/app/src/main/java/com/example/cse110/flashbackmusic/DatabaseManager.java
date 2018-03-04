package com.example.cse110.flashbackmusic;

import java.util.GregorianCalendar;

/**
 * Created by CubicDolphin on 3/3/18.
 */

public class DatabaseManager {
    // Call sto store a specific play of a specific song
    public void storePlayInstance (PlayInstance playInstance, User user) {};

    // Returns ALL play instances by a user, for all songs
    public PlayInstance[] getPlayInstances (User user) {
        PlayInstance playInstance = new PlayInstance(MainActivity.getSongs()[0], new LatLon(0, 0), new GregorianCalendar());
        PlayInstance[] instances = { playInstance };
        return instances;
    };
}
