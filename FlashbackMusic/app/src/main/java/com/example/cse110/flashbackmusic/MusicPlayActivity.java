package com.example.cse110.flashbackmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MusicPlayActivity extends AppCompatActivity {

    private MusicPlayer musicPlayer;
    private SharedPrefHelper songSharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        musicPlayer = MainActivity.getMusicPlayer();
        songSharedPrefHelper = MainActivity.getSongSharedPrefHelper();

        String mode = getIntent().getStringExtra("MODE");
        musicPlayer.setPlayMode(mode);

        if (musicPlayer.getPlayMode() == 0) {
            int selected_id = Integer.parseInt(getIntent().getStringExtra("SELECTED_ID"));
            musicPlayer.selectSong(selected_id);
        }

        TextView songNameDisplay = (TextView) findViewById(R.id.song_name_music_play);
        songNameDisplay.setText(musicPlayer.getCurrentSongName());

        TextView songArtistDisplay = (TextView) findViewById(R.id.artist_name_music_play);
        songArtistDisplay.setText(musicPlayer.getCurrentSongArtist());

        TextView albumNameDisplay = (TextView) findViewById(R.id.album_name_music_play);
        albumNameDisplay.setText(musicPlayer.getCurrentSongAlbum());

        // Link the "back" button to go back to the song selection activity
        Button back = (Button) findViewById(R.id.button_exit_music_play);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

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

        final ImageButton likeButton = (ImageButton) findViewById(R.id.button_like);
        if (musicPlayer.getCurrentLikeStatus() == 1) {
            likeButton.setBackgroundResource(R.drawable.check_mark);
        } else if (musicPlayer.getCurrentLikeStatus() == 0) {
            likeButton.setBackgroundResource(R.drawable.plus);
        } else if (musicPlayer.getCurrentLikeStatus() == 2) {
            likeButton.setBackgroundResource(R.drawable.cross);
        }
        likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.changeCurrentLikeStatus();
                        if (musicPlayer.getCurrentLikeStatus() == 1) {
                            likeButton.setBackgroundResource(R.drawable.check_mark);
                        } else if (musicPlayer.getCurrentLikeStatus() == 0) {
                            likeButton.setBackgroundResource(R.drawable.plus);
                        } else if (musicPlayer.getCurrentLikeStatus() == 2) {
                            likeButton.setBackgroundResource(R.drawable.cross);
                        }
                        int currID = musicPlayer.getCurrentMediaID();
                        String currSong = musicPlayer.getCurrentWriteString();
                        songSharedPrefHelper.writeData("" + currID, currSong);
                        songSharedPrefHelper.applyChanges();
                    }
                }
        );
    }
}
