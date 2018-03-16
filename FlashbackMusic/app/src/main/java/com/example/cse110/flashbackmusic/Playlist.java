package com.example.cse110.flashbackmusic;

import android.util.Log;

import com.example.cse110.flashbackmusic.Callbacks.Callback;
import com.example.cse110.flashbackmusic.Callbacks.PlayInstancesCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Date;

import static java.lang.Math.abs;

public class Playlist {

    private final int TIME_OF_DAY_DIVISION = 4;

    private ArrayList<Song> songs;
    private int play_index;
    private Queue<Song> songPQ;

    public static Comparator<Song> rankComp = new Comparator<Song>() {
        @Override
        public int compare(Song s1, Song s2) {
            int temp = s2.getRank() - s1.getRank();
            if (temp != 0) {
                return temp;
            }
            return breakTieWithLike(s1, s2);
        }
    };
  
    private Calendar lastSortedCal;
    private LatLon lastSortedLoc;

    public Playlist () {
        this.songs = MainActivity.getSongs();
        this.play_index = 0;
        this.lastSortedCal = null;
        this.lastSortedLoc = null;
        this.songPQ = new PriorityQueue<>(100, rankComp);
        addSongToList();
        Log.i("Playlist Constructor", "Created a playlist with play_index" + play_index);
    }

    // FAKE CONSTRUCTOR FOR TESTING
    public Playlist (boolean fake) {}
    // FAKE CONSTRUCTOR FOR TESTING

    private void addSongToList () {
        // calculate the score for each song and then put them into the priority queue
        for (int k = 0; k < songs.size(); k++) {
            calculateRank(songs.get(k));
            Log.i("Playlist addSongToList", "Calculating the rank of "
                    + songs.get(k).getSongName());
        }

        for (int i = 0; i < songs.size(); i++) {
            // skipping over disliked songs
            if (songs.get(i).getLikeStatus() == 2) {
                continue;
            }
            songPQ.add(songs.get(i));
            Log.i("Playlist addSongToList", "Adding the song " + songs.get(i).getSongName() +
                    " to the priority queue");
        }

        Log.i("Playlist addSongToList", "The size of priority queue is " + songPQ.size());
    }

    private void calculateRank (Song song) {
        // calculate the score for the song
        LatLon latlon = MainActivity.getLastLatLon();
        Date currDate = Calendar.getInstance().getTime();
        /* TODO make work =P
        int rank = findRank(song, latlon, currDate);
        Log.i("Playlist calculateRank", "rank of current song is " + rank);
        song.setRank(rank);*/
    }

    public int getCurrSongID() {
        return songPQ.poll().getMediaID();
    }

    public boolean atEnd() {
        return (this.songPQ.size() == 0);
    }

    public boolean shouldSort (Calendar currTime, LatLon currLoc) {
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

    public boolean timeOfDayHasChanged (int currHour) {
        int currHourIndex = currHour/TIME_OF_DAY_DIVISION;
        int prevHourIndex = lastSortedCal.HOUR_OF_DAY/TIME_OF_DAY_DIVISION;
        if (currHourIndex != prevHourIndex) {
            return true;
        }
        return false;
    }

    public boolean locationHasChanged (LatLon currLoc) {
        // distanceTo returns a float representing the distance in meters
        if (currLoc.findDistance(lastSortedLoc) > 250.0) {
            return true;
        }
        return false;
    }

    public void sortPlaylist(Calendar currTime, LatLon currLoc) {
        Log.i("Playlist sortPlaylist", "Playlist is being resorted by current location and time");
        lastSortedLoc = currLoc;
        lastSortedCal = currTime;
        songPQ.clear();
        addSongToList();
    }

    //0 - neutral; 1 - favorite; 2 - dislike
    public static int breakTieWithLike (Song song1, Song song2){
        int songStatus1 = song1.getLikeStatus();
        int songStatus2 = song2.getLikeStatus();

        //If the like statuses are the same, break the tie based on mostly recently played
        if(songStatus1 == songStatus2) {
            return breakTieWithRecentPlay(song1, song2);
        }
        else if ((songStatus1 == 1) || (songStatus1 == 0 && songStatus2 == 2)){
            return -1;
        }
        return 1;
    }

    public static int breakTieWithRecentPlay (Song songOne, Song songTwo) {
        if (songOne.getLastPlayedDate() == null || songTwo.getLastPlayedDate() == null) {
            return -1;
        }
        if(songOne.getLastPlayedDate().compareTo(songTwo.getLastPlayedDate()) < 0) {
            return -1;
        }
        else {
            return 1;
        }
    }
    /*
     * Sets the rank of a song, as well as returns said rank
     *
     * FAVORITED: doesn't matter :\
     * DISLIKED: rank set to -1, no other factors are counted
     *
     * WITHIN 314M: +102
     *
     * PLAYED WITHIN A WEEK: +101
     *
     * PLAYED BY FRIEND: +100
     *
     * Testing for this file at `FlashbackMusic/app/src/androidTest/java/tests/VibeAlgorithmTests.java`
     * Please run tests after making any changes to this function, and update values as needed
     */
    public int findRank (final Song song, final LatLon currentLatLon, final Date now,
                          final ArrayList<User> friends, ArrayList<PlayInstance> playInstances) {
        int rank = 0;
        if (song.isDisliked()) {
            song.setRank(-1);
            return -1;
        }
        if (playInstances.size() == 0) {
            song.setRank(0);
            return 0;
        }

        boolean playedNear = false;
        boolean playedLastWeek = false;
        boolean playedFriend = false;

        for (int i = 0; i < playInstances.size(); i++) {
            PlayInstance thisPlayInstance = playInstances.get(i);

            LatLon songLoc = new LatLon(thisPlayInstance.latitude, thisPlayInstance.longitude);
            if (!playedNear && abs(songLoc.findDistance(currentLatLon)) < Math.PI * 100) {
                playedNear = true;
                rank += 102;
            }

            // Check to see if the now time is less than a week's worth of milliseconds from the time it was played
            if (!playedLastWeek && now.getTime() - thisPlayInstance.getTimeInMillis() <= 604800000) {
                playedLastWeek = true;
                rank += 101;
            }

            for (int j = 0; j < friends.size() && !playedFriend; j++) {
                if (thisPlayInstance.getUserId().equals(friends.get(j).getUserId())) {
                    playedFriend = true;
                    rank += 100;
                }
            }
        }
        song.setRank(rank);
        return rank;
    }
}
