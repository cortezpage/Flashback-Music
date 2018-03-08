package com.example.cse110.flashbackmusic;

import android.util.Log;

import com.google.gson.Gson;

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

    // the previous coordinates/LatLon at which this song was played
    private LatLon lastPlayedLocation;

    // the date when the song was last played
    private Calendar lastPlayedCalendar;

    // Recently Played Rank
    private int rank;

    // Song's url
    private String song_url;

    // This constructor builds a song from a string filled with data about the song.
    public Song(String data, String url) {
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
        this.lastPlayedLocation = null;
        this.lastPlayedCalendar = null;
        this.rank = 0;
        this.song_url = url;
        Log.i("Song Constructor", "Song " + song_name + " with artist " + artist_name +
            " in album " + album_name + "URL: " + song_url + " is created.");
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
        Log.i("Song incrementLikeStatus", "Like status changed to " + like_status);
    }

    public boolean isNeutral() { return (this.like_status == 0); }

    public void setToNeutral() { this.like_status = 0; }

    public boolean isFavorited() { return (this.like_status == 1); }

    public void setToFavorite() { this.like_status = 1; }

    public boolean isDisliked() { return (this.like_status == 2); }

    public void setToDisliked() { this.like_status = 2; }

    public boolean wasPlayedPreviously() {
        return ((this.lastPlayedCalendar != null) && (this.lastPlayedLocation != null));
    }

    public Calendar getLastPlayedCalendar() { return this.lastPlayedCalendar; }

    public void setLastPlayedCalendar(Calendar newCalendar) { this.lastPlayedCalendar = newCalendar; }

    public Date getLastPlayedDate() {
        if (this.lastPlayedCalendar != null) {
            return this.lastPlayedCalendar.getTime();
        }
        return null;
    }

    public LatLon getLastPlayedLocation() { return this.lastPlayedLocation; }

    public void setLastPlayedLocation(LatLon newLocation) { this.lastPlayedLocation = newLocation; }

    public boolean playedAtTimeOfDay(Calendar currTime) {
        if (this.lastPlayedCalendar != null) {
            int currHour = currTime.get(Calendar.HOUR_OF_DAY);
            int prevHour = this.lastPlayedCalendar.get(Calendar.HOUR_OF_DAY);
            return ((currHour / TIME_OF_DAY_DIVISION) == (prevHour / TIME_OF_DAY_DIVISION));
        }
        return false;
    }

    public boolean playedOnDayOfTheWeek(Calendar currTime) {
        if (this.lastPlayedCalendar != null) {
            int currDay = currTime.get(Calendar.DAY_OF_WEEK);
            int prevDay = lastPlayedCalendar.get(Calendar.DAY_OF_WEEK);
            return (currDay == prevDay);
        }
        return false;
    }

    public int getRank() { return this.rank; }

    public void setRank(int newRank) { this.rank = newRank; }

    public String toString() {
        return new Gson().toJson(this);
    }
}
