package com.example.cse110.flashbackmusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private boolean ERASE_DATA_AT_START = false; // for testing (set to false for release)

    private static MusicPlayer musicPlayer = null;
    private static SharedPrefHelper sharedPrefHelper;
    private static Song [] songs;
    private static Album [] albums;
    private SharedPreferences songSharedPref;
    private SharedPreferences.Editor songDataEditor;
    private SharedPreferences albumSharedPref;
    private SharedPreferences.Editor albumDataEditor;

    public static MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static SharedPrefHelper getSongSharedPrefHelper() {
        return sharedPrefHelper;
    }

    public static Song [] getSongs() {
        return songs;
    }

    public static Album [] getAlbums() {
        return albums;
    }

    @Override
    public void onDestroy() {
        updateSongData();
        updateAlbumData();
        musicPlayer.destroy();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this.getApplicationContext();
        String song_data_filename = "com.example.cse110.flashbackmusic.song_data_preferences";
        String album_data_filename = "com.example.cse110.flashbackmusic.album_data_preferences";

        songSharedPref = context.getSharedPreferences(song_data_filename, Context.MODE_PRIVATE);
        songDataEditor = songSharedPref.edit();
        albumSharedPref = context.getSharedPreferences(album_data_filename, Context.MODE_PRIVATE);
        albumDataEditor = albumSharedPref.edit();

        if (ERASE_DATA_AT_START) {songDataEditor.clear().commit(); albumDataEditor.clear().commit();}

        sharedPrefHelper = new SharedPrefHelper(songSharedPref, songDataEditor, albumSharedPref, albumDataEditor);
        songs = sharedPrefHelper.createSongList();
        albums = sharedPrefHelper.createAlbums();

        musicPlayer = new MusicPlayer (this.getResources());

        Button toSongSelection = (Button) findViewById(R.id.button_to_song_selection);

        toSongSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSongSelection();
            }
        });

        Button toAlbumSelection = (Button) findViewById(R.id.button_to_album_selection);

        toAlbumSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAlbumSelection();
            }
        });
    }

    public void launchSongSelection () {
        Intent intent = new Intent(this, SongSelectionActivity.class);
        startActivity(intent);
    }

    public void launchAlbumSelection () {
        Intent intent = new Intent(this, AlbumSelectionActivity.class);
        startActivity(intent);
    }

    public void updateSongData() {
        Song curr_song;
        for (int index = 0; index < songs.length; index++) {
            curr_song = songs[index];
            sharedPrefHelper.writeSongData("" + curr_song.getMediaID(), curr_song.toString());
        }
    }

    public void updateAlbumData() {
        Album curr_album;
        for (int index = 0; index < albums.length; index++) {
            curr_album = albums[index];
            sharedPrefHelper.writeAlbumData(curr_album.getID(), curr_album.toString());
        }
    }

    public static Song getSong(int id)
    {
        for (int index = 0; index < songs.length; index++) {
            if (songs[index].getMediaID() == id) {
                return songs[index];
            }
        }
        return null;
    }
}
