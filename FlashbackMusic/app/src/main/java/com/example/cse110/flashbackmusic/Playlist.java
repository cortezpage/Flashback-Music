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

        //TODO: If 2 songs have the same score (which is based on location, day, time), call breakTieWithLike

    }

    //0 - neutral; 1 - favorite; 2 - dislike
    public Song breakTieWithLike (Song song1, Song song2){
        Song winningSong = null;
        int songStatus1 = song1.getLikeStatus();
        int songStatus2 = song2.getLikeStatus();

        //If the like statuses are the same, break the tie based on mostly recently played
        if(songStatus1 == songStatus2) {
            winningSong = breakTieWithRecentPlay(song1, song2);
        }
        else if ((songStatus1 == 1) || (songStatus1 == 0 && songStatus2 == 2)){
            winningSong = song1;
        }
        else {
            winningSong = song2;
        }
        return winningSong;
    }

    public Song breakTieWithRecentPlay (Song songOne, Song songTwo) {
        if(songOne.getLastPlayedDate().compareTo(songTwo.getLastPlayedDate()) < 0) {
            return songOne;
        }
        else {
            return songTwo;
        }
    }

}
