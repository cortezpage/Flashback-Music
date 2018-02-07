package com.example.cse110.flashbackmusic;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MusicPlayer musicPlayer;
    private SharedPrefHelper sharedPrefHelper;
    private SharedPreferences.Editor dataEditor;
    private String [] song_data;

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.destroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        song_data = new String[0];

        Context context = this.getApplicationContext();
        String filename = "com.example.cse110.flashbackmusic.song_data_preferences";
        SharedPreferences sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        dataEditor = sharedPref.edit();

        sharedPrefHelper = new SharedPrefHelper(sharedPref, dataEditor);
        sharedPrefHelper.validateData();

        Map<String, ?> allEntries = sharedPref.getAll();
        song_data = new String[allEntries.size()];
        int index = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.i("Read song data:", entry.getKey() + ": " + entry.getValue().toString());
            song_data[index] = entry.getValue().toString();
            index++;
        }

        musicPlayer = new MusicPlayer(this.getResources());
        musicPlayer.loadSongs(song_data);

        // Link the "play" button with the play() method from the music player
        Button playButton = (Button) findViewById(R.id.button_play);
        playButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.play();
                    }
                }
        );

        // Link the "pause" button with the pause() method from the music player
        Button pauseButton = (Button) findViewById(R.id.button_pause);
        pauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.pause();
                    }
                }
        );

        // Link the "reset" button with the reset() method from the music player
        Button resetButton = (Button) findViewById(R.id.button_reset);
        resetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.reset();
                    }
                }
        );

        // Link the "previous" button with the playPreviousSong() method from the music player
        Button previousButton = (Button) findViewById(R.id.button_previous);
        previousButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.goToPreviousSong();
                    }
                }
        );

        // Link the "next" button with the playNextSong() method from the music player
        Button nextButton = (Button) findViewById(R.id.button_next);
        nextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.goToNextSong();
                    }
                }
        );

        Button likeButton = (Button) findViewById(R.id.button_like);
        likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.changeCurrentLikeStatus();
                        int currID = musicPlayer.getCurrentMediaID();
                        String currSong = musicPlayer.getCurrentString();
                        sharedPrefHelper.writeSongData(currID, currSong);
                        sharedPrefHelper.applyChanges();
                    }
                }
        );
    }
}
