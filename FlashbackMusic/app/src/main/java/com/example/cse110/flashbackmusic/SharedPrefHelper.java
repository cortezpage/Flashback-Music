package com.example.cse110.flashbackmusic;

import android.content.SharedPreferences;
import android.util.Log;


public class SharedPrefHelper {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedEditor;

    private String [] all_songs = {
            "Jazz in Paris; Media Right Productions; YouTube Audio Library; 102; 0",
            "America Religious; Caroline Rose; I Will Not Be Afraid; 253; 0",
            "At Midnight; Caroline Rose; I Will Not Be Afraid; 198; 0",
            "Back East; Caroline Rose; I Will Not Be Afraid; 190; 0",
            "Blood On Your Bootheels; Caroline Rose; I Will Not Be Afraid; 195; 0",
            "I Will Not Be Afraid; Caroline Rose; I Will Not Be Afraid; 146; 0",
            "Tightrope Walker; Caroline Rose; I Will Not Be Afraid; 212; 0",
            "When You Go; Caroline Rose; I Will Not Be Afraid; 224; 0"
    };

    private int [] all_IDs;
    private String [] all_data;

    public SharedPrefHelper (SharedPreferences prefs, SharedPreferences.Editor editor) {
        this.sharedPref = prefs;
        this.sharedEditor = editor;

        this.all_IDs = new int [] {
                R.raw.jazz_in_paris,
                R.raw.america_religious,
                R.raw.at_midnight,
                R.raw.back_east,
                R.raw.blood_on_your_bootheels,
                R.raw.i_will_not_be_afraid,
                R.raw.tightrope_walker,
                R.raw.when_you_go
        };

        this.all_data = new String[this.all_songs.length];
        for (int index = 0; index < this.all_data.length; index++) {
            this.all_data[index] = this.all_songs[index] + "; " + this.all_IDs[index];
        }
    }

    public void validateData() {
        String currSong;
        int write_count = 0;
        for (int index = 0; index < all_data.length; index++) {
            currSong = this.sharedPref.getString("" + all_IDs[index], "NOTHING FOUND");
            if (currSong == "NOTHING FOUND") {
                writeSongData(all_IDs[index], all_data[index]);
                write_count++;
                Log.i("new data written: ", all_data[index]);
            }
        }
        if (write_count > 0) { this.applyChanges(); }
    }

    public void writeSongData(int ID, String data) {
        Log.i("Updating song: ", data);
        this.sharedEditor.putString("" + ID, data);
    }

    public void applyChanges() {
        this.sharedEditor.apply();
    }
}
