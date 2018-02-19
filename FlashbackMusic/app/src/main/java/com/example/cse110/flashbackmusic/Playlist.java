package com.example.cse110.flashbackmusic;


import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import java.util.Calendar;

public class Playlist {

    private final int TIME_OF_DAY_DIVISION = 4;

    private Song [] songs;
    private int play_index;
    private Calendar lastSortedCal;
    private Location lastSortedLoc;

    public Playlist () {
        this.songs = MainActivity.getSongs();
        this.play_index = 0;
        lastSortedCal = null;
        lastSortedLoc = null;
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

    // TODO: may need to replace with LatLon
    public boolean shouldSort (Calendar currTime, Location currLoc) {
        if (lastSortedCal == null || lastSortedLoc == null || currTime == null || currLoc == null) {
            return true;
        }
        if (currTime.DAY_OF_WEEK != lastSortedCal.DAY_OF_WEEK) {
            return true;
        }
        if (timeOfDayHasChanged(currTime.HOUR_OF_DAY)) {
            return true;
        }
        if (locationHasChanged(currLoc)) {
            return true;
        }
        return false;
    }

    // TODO: may need to change this if we change our time groups
    public boolean timeOfDayHasChanged (int currHour) {
        int currHourIndex = currHour/TIME_OF_DAY_DIVISION;
        int prevHourIndex = lastSortedCal.HOUR_OF_DAY/TIME_OF_DAY_DIVISION;
        if (currHourIndex != prevHourIndex) {
            return true;
        }
        return false;
    }

    /* TODO: may need to use LatLon instead
     * Also need to
     */
    public boolean locationHasChanged (Location currLoc) {
        // distanceTo returns a float representing the distance in meters
        if (currLoc.distanceTo(lastSortedLoc) > 250.0) {
            return true;
        }
        return false;
    }

    // TODO: may need to use LatLon instead
    @SuppressLint("MissingPermission")
    public void sortPlaylist(Calendar currTime, Location currLoc) {
        // getting relevant data to help us sort the playlist according to scores
        int currHour = currTime.HOUR_OF_DAY;
        int currDay = currTime.DAY_OF_WEEK;
        lastSortedCal = currTime;
        lastSortedLoc =  currLoc;
    }

}
