package com.example.cse110.flashbackmusic;

import android.arch.persistence.room.Database;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.cse110.flashbackmusic.Callbacks.DatabaseEntryCallback;
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
import java.util.Calendar;


/**
 * Created by CubicDolphin on 3/3/18.
 */

public class DatabaseManager {
    private int numSongs;

    public DatabaseManager () {
        numSongs = -1;
    }

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

    public void updatePlayInstance (final Song song) {
        ArrayList<PlayInstance> instanceList;

        Log.i("DatabaseManager updatePlayInstance", "updating the played history of song " +
        song.getSongName());

        getPlayInstances(song.getSongName(), new PlayInstancesCallback() {
            @Override
            public void onComplete(ArrayList<PlayInstance> instanceList) {
                PlayInstance lastInstance;
                //ArrayList<PlayInstance> instanceList = (ArrayList<PlayInstance>)o;

                if (instanceList != null) {
                    Log.i("DatabaseManager Callback", "InstanceList size " + instanceList.size());
                } else {
                    Log.i("DatabaseManager Callback", "No history found on this song.");
                }

                // check if there is no history stored in the database
                if (instanceList != null) {

                    if (instanceList.size() != 0) {

                        lastInstance = instanceList.get(instanceList.size() - 1);

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(lastInstance.timeInMillis);

                        song.setLastPlayedAll(lastInstance.userId, new LatLon(lastInstance.latitude,
                                lastInstance.longitude), cal);

                        Log.i("DatabaseManager", "Retrieved the data from firebase");

                        //TODO update the UI with the info.
                    }
                }
            }
        });

    }

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
                playInstancesCallback.onComplete(new ArrayList<PlayInstance>());
            }
        });
    };

    // Runs callback on any DatabaseEntry found for a songName
    // If no entry found, runs callback with a null object
    public void getDatabaseEntry (final String songName, final DatabaseEntryCallback databaseEntryCallback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(songName);

        // Retrieves data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String codedEntry = (String) dataSnapshot.getValue();

                if (codedEntry == null || codedEntry == "") {
                    Log.i("Database Manager", "No plays found for " + songName);
                    databaseEntryCallback.onComplete(null);
                } else {
                    // If object already created, just add our play to end
                    databaseEntryCallback.onComplete(DatabaseEntry.decodeJson(codedEntry));
                }
            }
            // Not used
            @Override
            public void onCancelled(DatabaseError databaseError) {
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
    public void getAllEntries (final DatabaseEntryCallback databaseEntryCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("songs");

        class GetAllSongsCallback implements SongNamesCallback {
            public void onComplete(ArrayList<String> names) {
                Log.d("Database Manager", "Getting all " + names.size() + " database entries");
                for (int i = 0; i < names.size(); i++) {
                    getDatabaseEntry(names.get(i), databaseEntryCallback);
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
                    songNames = new Gson().fromJson(codedEntry, new TypeToken<ArrayList<String>>() {
                    }.getType());
                    numSongs = songNames.size();

                    new GetAllSongsCallback().onComplete(songNames);
                }
            }

            // Not used
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public int getNumSongs () {
        return numSongs;
    }

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