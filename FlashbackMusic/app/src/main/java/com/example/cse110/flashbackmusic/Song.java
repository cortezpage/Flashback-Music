package com.example.cse110.flashbackmusic;

import android.util.Log;
import com.google.gson.Gson;

import java.util.Date;

public class Song {
    private String song_name; // name of the song
    private String artist_name; // name of the artist of the song
    private String album_name; // name of the album to which the song belongs
    private int duration;
    private int like_status; // 0 = neutral, 1 = favorited, and 2 = disliked
    private int media_id;

    /* an array of T/F values indicating the times of day at which the song has been played
     * possible split:
     * index 0 = 6 AM to 9 AM, index 1 = 9 AM to 12 PM, index 2 = 12 PM to 3 PM,
     * index 3 = 3 PM to 6 PM, index 4 = 6 PM to 9 PM, index 5 = 9 PM to 12 AM
     * We could split into 3 or 4 times of day if that is preferred!
     */
    private boolean [] times_of_day;

    // an array of T/F values indicating the days of the week on which the song has been played
    // index 0 = Sunday, 1 = Monday, 2 = Tuesday, etc.
    private boolean [] days_of_the_week;

    // an array of previous coordinates/locations at which this song was played
    private Location [] locations;

    private Date dateLastPlayedOld = null;
    private Date dateLastPlayed = null;

    // This constructor builds a song from a string filled with data about the song.
    public Song(String data) {
        String [] substrings = data.split("; ");
        if (substrings.length < 6) {
            Log.e("Song Constructor", "The song data is invalid!");
            Log.e("Data", data);
            // Set Jazz in Paris as the default media
            this.song_name = "Jazz in Paris";
            this.artist_name = "Media Right Productions";
            this.album_name = "YouTube Audio Library";
            this.duration = 102;
            this.like_status = 0;
            this.media_id = 2131427333;
            this.locations = new Location[0];
            this.times_of_day = new boolean[6];
            this.days_of_the_week = new boolean[7];
        } else {
            this.song_name = substrings[0];
            this.artist_name = substrings[1];
            this.album_name = substrings[2];
            this.duration = Integer.parseInt(substrings[3]);
            this.like_status = Integer.parseInt(substrings[4]);
            this.media_id = Integer.parseInt(substrings[5]);
            this.locations = new Location[0];
            this.times_of_day = new boolean[6];
            this.days_of_the_week = new boolean[7];
        }
    }

    public String getSongName() { return this.song_name; }

    public String getArtistName() {
        return this.artist_name;
    }

    public String getAlbumName() {
        return this.album_name;
    }

    public int getMediaID() { return this.media_id; }

    public int getLikeStatus() { return this.like_status; }

    public void incrementLikeStatus() {
        this.like_status++;
        if (this.like_status > 2) {
            this.like_status = 0;
        }
    }

    public boolean isNeutral() { return (this.like_status == 0); }

    public boolean isFavorited() { return (this.like_status == 1); }

    public boolean isDisliked() { return (this.like_status == 2); }

    public void setToNeutral() { this.like_status = 0; }

    public void setToFavorite() { this.like_status = 1; }

    public void setToDisliked() { this.like_status = 2; }

    public Location [] getLocations() { return this.locations; }

    public void addNewLocation(Location newLocation) {
        Location [] newLocations = new Location[this.locations.length + 1];
        for (int index = 0; index < locations.length; index++) {
            newLocations[index] = locations[index];
        }
        newLocations[this.locations.length] = newLocation;
        this.locations = newLocations;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public void setDateLastPlayed(Date dateLastPlayed) {
        dateLastPlayedOld = this.dateLastPlayed;
        this.dateLastPlayed = dateLastPlayed;
    }

    // may return null
    public Date getDateLastPlayedOld()
    {
        return dateLastPlayedOld;
    }
}
