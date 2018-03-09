package com.example.cse110.flashbackmusic;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by CubicDolphin on 3/5/18.
 */

public class DatabaseEntry {
    Song song;
    ArrayList<PlayInstance> playInstances;

    public static DatabaseEntry decodeJson (String jsonString) {
        return new Gson().fromJson(jsonString, DatabaseEntry.class);
    }

    public static String encodeJson (DatabaseEntry databaseEntry) {
        return new Gson().toJson(databaseEntry);
    }

    public DatabaseEntry (Song song) {
        this.song = song;
        playInstances = new ArrayList<PlayInstance>();
    }

    public void addPlayInstance (PlayInstance playInstance) {
        playInstances.add(playInstance);
    }

    public ArrayList<PlayInstance> getPlayInstances () {
        return playInstances;
    }
}
