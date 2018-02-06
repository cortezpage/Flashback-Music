package com.example.cse110.flashbackmusic;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MusicPlayer musicPlayer;
    private int [] songIDs;

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.destroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this.getApplicationContext();
        String filename = "com.example.cse110.flashbackmusic.song_data_preferences";
        SharedPreferences sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        // song name, song artist, song album, duration
        editor.putString("" + R.raw.jazz_in_paris, "Jazz in Paris; Media Right Productions; YouTube Audio Library; 102");
        editor.commit();
        editor.putString("" + R.raw.america_religious, "America Religious; Caroline Rose; I Will Not Be Afraid; 253");
        editor.commit();
        editor.putString("" + R.raw.at_midnight, "At Midnight; Caroline Rose; I Will Not Be Afraid; 198");
        editor.commit();
        editor.putString("" + R.raw.back_east, "Back East; Caroline Rose; I Will Not Be Afraid; 190");
        editor.commit();

        String returned = sharedPref.getString("" + R.raw.jazz_in_paris, "NOTHING FOUND");
        Log.i("returned: ", returned);

        songIDs = new int[] {R.raw.jazz_in_paris, R.raw.america_religious, R.raw.at_midnight, R.raw.back_east};

        musicPlayer = new MusicPlayer(this.getResources());
        musicPlayer.loadSongs(songIDs);

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

        // Link the "next" button with the playNextSong() method from the music player
        Button nextButton = (Button) findViewById(R.id.button_next);
        nextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.playNextSong();
                    }
                }
        );
    }
}
