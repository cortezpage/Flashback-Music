package com.example.cse110.flashbackmusic;

import android.util.Log;

import com.example.cse110.flashbackmusic.Callbacks.PlayInstancesCallback;
import com.example.cse110.flashbackmusic.Callbacks.SongNamesCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


/**
 * Created by CubicDolphin on 3/3/18.
 */

public class DatabaseManager {

    // Call sto store a specific play of a specific song
    public void storePlayInstance (final PlayInstance playInstance, final Song song) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(song.getSongName());

        // Retrieves data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String codedEntry = (String)dataSnapshot.getValue();
                DatabaseEntry databaseEntry;

                if (codedEntry == null || codedEntry == "") {
                    // If first time song is played, create new object to store
                    databaseEntry = new DatabaseEntry(song);
                    databaseEntry.addPlayInstance(playInstance);
                    Log.i("Database Manager", "Added " + song.getSongName() + " to database");
                    addSongToSongList(song); // Adds song's name to list of all song names
                } else {
                    // If object already created, just add our play to end
                    databaseEntry = DatabaseEntry.decodeJson(codedEntry);
                    databaseEntry.addPlayInstance(playInstance);
                    Log.i("Database Manager", "Stored new play of " + song.getSongName());
                }

                databaseReference.setValue(DatabaseEntry.encodeJson(databaseEntry));
            }

            // Not used
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    };

    // Returns ALL play instances for a song, by all users
    public void getPlayInstances (final String songName, final PlayInstancesCallback playInstancesCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(songName);

        // Retrieves data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String codedEntry = (String) dataSnapshot.getValue();
                ArrayList<PlayInstance> playInstances;

                if (codedEntry == null || codedEntry == "") {
                    playInstances = new ArrayList<PlayInstance>();
                    Log.i("Database Manager", "No plays found for " + songName);
                } else {
                    // If object already created, just add our play to end
                    DatabaseEntry databaseEntry = DatabaseEntry.decodeJson(codedEntry);
                    playInstances = databaseEntry.playInstances;
                }

                playInstancesCallback.onComplete(playInstances);
            }
            // Not used
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(new ArrayList<PlayInstance>());
            }
        });
    };

    // Adds new Song to Song list
    private void addSongToSongList (final Song song) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("songs");

        // Retrieves data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String codedEntry = (String)dataSnapshot.getValue();
                ArrayList<String> songNames;

                if (codedEntry == null || codedEntry == "") {
                    // If first time song is played, create new object to store
                    songNames = new ArrayList<String>();
                    songNames.add(song.getSongName());
                    Log.i("Song name array", "Empty song name array added");
                } else {
                    // If object already created, just add our play to end
                    songNames = new Gson().fromJson(codedEntry, new TypeToken<ArrayList<String>>() {}.getType());
                    songNames.add(song.getSongName());
                    Log.i("Database Manager", "Stored new play of " + song.getSongName());
                }

                databaseReference.setValue(new Gson().toJson(songNames));
            }

            // Not used
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // Does what is in callback to ALL playInstances
    public void getAllEntries (final PlayInstancesCallback playInstancesCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("songs");

        class GetAllSongsCallback implements SongNamesCallback {
            public void onComplete(ArrayList<String> names) {
                Log.d("TESTING", "callback a, " + names.size());
                for (int i = 0; i < names.size(); i++) {
                    getPlayInstances(names.get(i), playInstancesCallback);
                }
            }
        }

        // Retrieves data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String codedEntry = (String) dataSnapshot.getValue();
                ArrayList<String> songNames;

                if (codedEntry == null || codedEntry == "") {
                    // No songs found, return empty ArrayList
                    new GetAllSongsCallback().onComplete(new ArrayList<String>());
                    Log.i("Database Manager", "No songs found");
                } else {
                    // If object already created, just add our play to end
                    songNames = new Gson().fromJson(codedEntry, new TypeToken<ArrayList<String>>() {}.getType());

                    new GetAllSongsCallback().onComplete(songNames);
                }
            }
            // Not used
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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