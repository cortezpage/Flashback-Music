package com.example.cse110.flashbackmusic;

import android.arch.persistence.room.Database;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by CubicDolphin on 3/3/18.
 */

public class DatabaseManager {
    // Instance variable needed to get data out of onDataChange listener
    private ArrayList<PlayInstance> playInstances;

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

        getPlayInstances(song, new Callback() {
            @Override
            public void onComplete(Object o) {
                PlayInstance lastInstance;
                ArrayList<PlayInstance> instanceList = (ArrayList<PlayInstance>)o;

                if (instanceList != null) {
                    Log.i("DatabaseManager Callback", "InstanceList size " + instanceList.size());
                } else {
                    Log.i("DatabaseManager Callback", "No history found on this song.");
                }

                // check if there is no history stored in the database
                if (instanceList != null) {

                    lastInstance = instanceList.get(instanceList.size() - 1);
                    song.setLastPlayedUser(lastInstance.userId);
                    song.setLastPlayedLocation(new LatLon(lastInstance.latitude, lastInstance.longitude));

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(lastInstance.timeInMillis);

                    // TODO somehow to update the UI info
                }
            }
        });

    }

    // Returns ALL play instances for a song, by all users
    public void getPlayInstances (final Song song, final Callback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference(song.getSongName());

        // Retrieves data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String codedEntry = (String) dataSnapshot.getValue();

                if (codedEntry == null || codedEntry == "") {
                    playInstances = new ArrayList<PlayInstance>();
                    Log.i("Database Manager", "No plays found for " + song.getSongName());
                } else {
                    // If object already created, just add our play to end
                    DatabaseEntry databaseEntry = DatabaseEntry.decodeJson(codedEntry);
                    playInstances = databaseEntry.playInstances;
                }

                callback.onComplete(playInstances);
            }
            // Not used
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(new ArrayList<PlayInstance>());
            }
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