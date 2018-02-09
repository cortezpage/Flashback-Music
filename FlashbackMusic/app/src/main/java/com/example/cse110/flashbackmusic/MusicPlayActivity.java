package com.example.cse110.flashbackmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MusicPlayActivity extends AppCompatActivity {

    private MusicPlayer musicPlayer;
    private SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        musicPlayer = MainActivity.getMusicPlayer();
        sharedPrefHelper = MainActivity.getSongSharedPrefHelper();

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
        ImageButton back = findViewById(R.id.button_exit_music_play);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        // Link the "play" button with the play() method from the music player
        final ImageButton playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (musicPlayer.isMusicPlaying()){
                            playButton.setBackgroundResource(R.drawable.play_button);
                            musicPlayer.pause();
                        }
                        else{
                            playButton.setBackgroundResource(R.drawable.pause_button);
                            musicPlayer.play();
                        }
                    }
                }
        );

        // Link the "reset" button with the reset() method from the music player
        ImageButton resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (musicPlayer.isMusicPlaying()) {
                            playButton.setBackgroundResource(R.drawable.play_button);
                        }
                        musicPlayer.reset();
                    }
                }
        );

        // Link the "previous" button with the playPreviousSong() method from the music player
        ImageButton previousButton = findViewById(R.id.button_previous);
        if (musicPlayer.getPlayMode() == 0) {
            previousButton.getBackground().setAlpha(50);
        }
        previousButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.goToPreviousSong();
                    }
                }
        );

        // Link the "next" button with the playNextSong() method from the music player
        ImageButton nextButton = findViewById(R.id.button_next);
        if (musicPlayer.getPlayMode() == 0) {
            nextButton.getBackground().setAlpha(50);
        }
        nextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.goToNextSong();
                    }
                }
        );

        final ImageButton likeButton = findViewById(R.id.button_like);
        if (musicPlayer.getCurrentLikeStatus() == 1) {
            likeButton.setBackgroundResource(R.drawable.favorite_button);
        } else if (musicPlayer.getCurrentLikeStatus() == 0) {
            likeButton.setBackgroundResource(R.drawable.neutral_button);
        } else if (musicPlayer.getCurrentLikeStatus() == 2) {
            likeButton.setBackgroundResource(R.drawable.dislike_button);
        }
        likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicPlayer.changeCurrentLikeStatus();
                        if (musicPlayer.getCurrentLikeStatus() == 1) {
                            likeButton.setBackgroundResource(R.drawable.favorite_button);
                        } else if (musicPlayer.getCurrentLikeStatus() == 0) {
                            likeButton.setBackgroundResource(R.drawable.neutral_button);
                        } else if (musicPlayer.getCurrentLikeStatus() == 2) {
                            likeButton.setBackgroundResource(R.drawable.dislike_button);
                        }
                        int currID = musicPlayer.getCurrentMediaID();
                        String currSong = musicPlayer.getCurrentWriteString();
                        sharedPrefHelper.writeSongData("" + currID, currSong);
                        sharedPrefHelper.applySongChanges();
                    }
                }
        );

        TextView location = findViewById(R.id.song_location);
        location.setText ("Location: " + "Dummy location value");

        TextView date_of_week = findViewById(R.id.song_date);
        date_of_week.setText("Date: " + "Dummy date value");

        TextView time_of_day = findViewById(R.id.song_time);
        time_of_day.setText("Time: " + "Dummy time value");
    }
}
