package com.example.cse110.flashbackmusic;

import android.util.Log;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Song {
    private String song_name; // name of the song
    private String artist_name; // name of the artist of the song
    private String album_name; // name of the album to which the song belongs
    private int duration;
    private int like_status; // 0 = neutral, 1 = favorited, and 2 = disliked
    private int media_id;

    private final int TIME_OF_DAY_DIVISION = 4;

    // an array of previous coordinates/latLons at which this song was played
    private ArrayList<LatLon> latLons;

    // Dates
    private Date lastPlayedDate;

    // Time of the day
    // 0-4 AM, 4-8 AM, 8-12 PM, 12-16 PM, 16-20 PM, 20-24 AM
    private boolean [] times_of_day;

    // Days of the week
    private boolean [] days_of_the_week;

    // Recently Played Rank
    private int rank;

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
        } else {
            this.song_name = substrings[0];
            this.artist_name = substrings[1];
            this.album_name = substrings[2];
            this.duration = Integer.parseInt(substrings[3]);
            this.like_status = Integer.parseInt(substrings[4]);
            this.media_id = Integer.parseInt(substrings[5]);
        }
        this.latLons = new ArrayList<LatLon>();
        this.lastPlayedDate = null;
        this.times_of_day = new boolean[6];
        this.days_of_the_week = new boolean[7];
        this.rank = 0;
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

    public void setToNeutral() { this.like_status = 0; }

    public boolean isFavorited() { return (this.like_status == 1); }

    public void setToFavorite() { this.like_status = 1; }

    public boolean isDisliked() { return (this.like_status == 2); }

    public void setToDisliked() { this.like_status = 2; }

    public Date getLastPlayedDate() { return this.lastPlayedDate; }

    public boolean wasPlayedPreviously() {
        return ((this.lastPlayedDate != null) && (this.latLons.size() > 0));
    }

    public void setDate(Date newDate) { this.lastPlayedDate = newDate; }

    public boolean[] getTimeOfDay() { return this.times_of_day; }

    public boolean playedAtTime(Calendar currTime) {
        int currHour = currTime.HOUR_OF_DAY;
        int index = currHour/TIME_OF_DAY_DIVISION;
        return (this.times_of_day[index]);
    }

    public void setPlayedAtTime(Calendar currTime) {
        int currHour = currTime.HOUR_OF_DAY;
        int index = currHour/TIME_OF_DAY_DIVISION;
        this.times_of_day[index] = true;
    }

    public boolean[] getDaysOfTheWeek() { return this.days_of_the_week; }

    public boolean playedOnDayOfTheWeek(Calendar currTime) {
        int currDay = currTime.DAY_OF_WEEK;
        return days_of_the_week[currDay - 1];
    }

    public void setPlayedOnDayOfTheWeek(Calendar currTime) {
        int currDay = currTime.DAY_OF_WEEK;
        days_of_the_week[currDay - 1] = true;
    }

    public int getRank() { return this.rank; }

    public void setRank(int newRank) { this.rank = newRank; }

    public ArrayList<LatLon> getLatLons() { return this.latLons; }

    public LatLon getPreviousLocation() {
        if (latLons.size() == 0)
            return null;
        return this.latLons.get(latLons.size() - 1);
    }

    public void addLocation(LatLon newLocation) {
        this.latLons.add(newLocation);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
