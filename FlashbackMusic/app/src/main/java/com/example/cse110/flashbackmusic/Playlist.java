package com.example.cse110.flashbackmusic;


import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.Math.abs;

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

    /*
     * Sets the rank of a song, as well as returns said rank
     *
     * FAVORITED: +303
     * DISLIKED: rank set to -1, no other factors are counted
     *
     * WITHIN 200M: +301
     * WITHIN 400M: +201
     * WITHIN 600M: +101
     *
     * SAME DAY OF WEEK: +202
     *
     * SAME HOUR/1 HOUR APART: +300
     * 2 HOURS APART: +200
     * 1 HOUR APART: +100
     *
     * Testing for this file at `FlashbackMusic/app/src/androidTest/java/tests/FlashbackAlgorithmTests.java`
     * Please run tests after making any changes to this function, and update values as needed
     */
    public int findRank (Song song, LatLon currentLatLon, Date now) {
        int rank = 0;
        if (song.isDisliked()) {
            rank = -1;
            song.setRank(rank);
            return rank;
        } else if (song.isFavorited()) {
            rank += 303;
        }

        // If song not played before, has a rank of 0
        if (song.getPreviousLocation() == null || song.getLastPlayedDate() == null) {
            rank = 0;
            song.setRank(rank);
            return rank;
        }

        LatLon songLatLon = song.getPreviousLocation();
        // Distance in meters between current loc and previously played loc
        float locDiff = abs(songLatLon.findDistance(currentLatLon));
        if (locDiff < 200.0) {
            rank += 301;
        } else if (locDiff < 400.0) {
            rank += 201;
        } else if (locDiff < 600.0) {
            rank += 101;
        }

        Calendar songCalendar = new GregorianCalendar();
        songCalendar.setTime(song.getLastPlayedDate());
        Calendar nowCalendar = new GregorianCalendar();
        nowCalendar.setTime(now);
        if (songCalendar.get(Calendar.DAY_OF_WEEK) == nowCalendar.get(Calendar.DAY_OF_WEEK)) {
            rank += 202;
        }

        int songHour = songCalendar.get(Calendar.HOUR_OF_DAY);
        int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
        int hourDiff = abs(songHour / 4 - nowHour / 4);
        if (hourDiff == 0) {
            rank += 300;
        } /*else if (hourDiff == 1) { // Makes being in the adjacent time bracket half credit
            rank += 100;
        }*/

        song.setRank(rank);
        return rank;
    }
}