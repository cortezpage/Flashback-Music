package com.example.cse110.flashbackmusic;


import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import java.util.Calendar;

public class Playlist {

    private Song [] songs;
    private int play_index;

    public Playlist () {
        this.songs = MainActivity.getSongs();
        this.play_index = 0;
    }

    public int getCurrSongID() {
        return this.songs[this.play_index].getMediaID();
    }

    public void toPreviousSong() {
        this.play_index--;
        if (this.play_index < 0) {
            this.play_index = this.songs.length - 1;
        }
    }

    public void toNextSong() {
        this.play_index++;
        if (this.play_index > this.songs.length - 1) {
            this.play_index = 0;
        }
    }

    @SuppressLint("MissingPermission")
    public void sortPlaylist(Calendar currTime) {
        // getting relevant data to help us sort the playlist according to scores
        int currHour = currTime.HOUR_OF_DAY;
        int currDay = currTime.DAY_OF_WEEK;
        LocationManager manager = MainActivity.getLocationManager();
        Location currLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

}
