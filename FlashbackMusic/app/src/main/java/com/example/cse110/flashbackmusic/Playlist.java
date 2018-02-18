package com.example.cse110.flashbackmusic;


import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import java.util.Calendar;
import java.util.PriorityQueue;
import java.util.Queue;

public class Playlist {

    private Song [] songs;
    private int play_index;
    private Queue<Integer> idPQ = new PriorityQueue<>(100);


    public Playlist () {
        this.songs = MainActivity.getSongs();
        this.play_index = 0;

        addSongToList();
    }

    private void addSongToList () {

        // calculate the score for each song and then put them into the priority queue
        for (int k = 0; k < songs.length; k++) {
            calculateRank(songs[k]);
        }

        for (int i = 0; i < songs.length; i++) {
            idPQ.add(songs[i].getRank());
        }
    }

    private void calculateRank (Song song) {
        // calculate the score for the songs
        //TODO implement the real funcionality for rank calculating
        song.setRank(song.getMediaID());
    }

    public int getCurrSongID() {
        System.err.println("idPQ size is " + idPQ.size());

        return idPQ.poll();
        //return this.songs[this.play_index].getMediaID();
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
