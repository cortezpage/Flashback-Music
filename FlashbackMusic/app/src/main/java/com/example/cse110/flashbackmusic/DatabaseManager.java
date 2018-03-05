package com.example.cse110.flashbackmusic;

import android.arch.persistence.room.Database;
import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by CubicDolphin on 3/3/18.
 */

public class DatabaseManager {
    // Call sto store a specific play of a specific song
    public void storePlayInstance (PlayInstance playInstance, Song song) {
        DatabaseEntry databaseEntry = new DatabaseEntry(song);
        databaseEntry.addPlayInstance(playInstance);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myFirebaseRef = database.getReference();

        myFirebaseRef.child(song.getSongName()).setValue(databaseEntry.toString());
        myFirebaseRef.setValue("Fred");
    };

    // Returns ALL play instances for a song, by all users
    public PlayInstance[] getPlayInstances (Song song) {
        PlayInstance playInstance = new PlayInstance(new User("Fred"), new LatLon(0, 0), new GregorianCalendar());
        PlayInstance[] instances = { playInstance };
        return instances;
    };

    /* HOW TO USE DATABASE:
     *   A) check if song played near current location:
     *     call getPlayInstances(songInQuestion), check each playInstance to see if it is near your
     *     current location
     *   B) whether it was played in the last week:
     *     call getPlayInstances(songInQuestion), grab last playInstance (the most recent). If the
     *     date is a week or less from now, then yes, it was played within the last week
     *   C) whether it was played by a friend
     *     call getPlayInstances(songInQuestion), check each playInstance to see if the user who
     *     played it matches any friends. Once you find a friend, return true. If you reach the end
     *     of the array, no friends have played this song.
     *
     *   To check the data of lastPlayedBy, lastPlayedLoc, etc:
     *     Call getPlayInstances(songInQuestion), grab last playInstance. All of the data in this is
     *     the "lastPlayed..." data, as it is the last time the song was played.
     */
}