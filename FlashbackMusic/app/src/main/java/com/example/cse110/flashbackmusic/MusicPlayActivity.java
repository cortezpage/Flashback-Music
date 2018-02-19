package com.example.cse110.flashbackmusic;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MusicPlayActivity extends AppCompatActivity {

    private MusicPlayer musicPlayer;
    private SharedPrefHelper sharedPrefHelper;
    private LatLon latLon;
    private ImageButton playButton;
    private int play_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        musicPlayer = MainActivity.getMusicPlayer();
        sharedPrefHelper = MainActivity.getSongSharedPrefHelper();

        String mode = getIntent().getStringExtra("MODE");
        musicPlayer.setPlayMode(mode);
        play_mode = musicPlayer.getPlayMode();

        if (play_mode == 0) {
            int selected_id = Integer.parseInt(getIntent().getStringExtra("SELECTED_ID"));
            musicPlayer.selectSong(selected_id);
        }
        else if (play_mode == 1) {
            int selected_index = Integer.parseInt(getIntent().getStringExtra("SELECTED_INDEX"));
            musicPlayer.selectAlbum(selected_index);
        }
        else if (musicPlayer.getPlayMode() == 2) {
            int curr_id = musicPlayer.getPlaylistSongID();
            musicPlayer.selectSong(curr_id);
        }

        musicPlayer.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (play_mode == 1 || play_mode == 2) {
                    if ((play_mode == 1 && musicPlayer.reachedEndOfAlbum()) ||
                            (play_mode == 2 && musicPlayer.reachedEndOfPlaylist())) {
                        musicPlayer.stop();
                        finish();
                    } else {
                        musicPlayer.goToNextSong();
                        updateUIWithSongInfo();
                        musicPlayer.updatePlaylist(false);
                    }
                }
            }
        });

        // Link the "back" button to go back to the song selection activity
        ImageButton back = findViewById(R.id.button_exit_music_play);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        // Link the "play" button with the play() method from the music player
        playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicPlayer.isLoadingSong()) {
                    if (musicPlayer.isMusicPlaying()) {
                        musicPlayer.pause();}
                    else {
                        musicPlayer.play();
                    }
                }
                updatePlayButtonImage();
            }
        });

        // Link the "reset" button with the reset() method from the music player
        ImageButton resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.reset();
                updatePlayButtonImage();
            }
        });

        // Link the "previous" button with the playPreviousSong() method from the music player
        ImageButton previousButton = findViewById(R.id.button_previous);
        previousButton.getBackground().setAlpha(40);
        if (musicPlayer.getPlayMode() != 0) { previousButton.getBackground().setAlpha(255); }
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicPlayer.goToPreviousSong();
                updateUIWithSongInfo();
                updatePlayButtonImage();
            }
        });

        // Link the "next" button with the playNextSong() method from the music player
        ImageButton nextButton = findViewById(R.id.button_next);
        nextButton.getBackground().setAlpha(40);
        if (musicPlayer.getPlayMode() != 0) { nextButton.getBackground().setAlpha(255); }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((play_mode == 1 && musicPlayer.reachedEndOfAlbum()) ||
                        (play_mode == 2 && musicPlayer.reachedEndOfPlaylist())) {
                    musicPlayer.stop();
                    finish();
                }
                musicPlayer.goToNextSong();
                updateUIWithSongInfo();
                updatePlayButtonImage();
            }
        });

        final ImageButton likeButton = findViewById(R.id.button_like);
        likeButton.setOnClickListener(new View.OnClickListener() {
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
                sharedPrefHelper.saveSongData(musicPlayer.getCurrentMediaID());
            }
        });

        updateUIWithSongInfo();
        updatePlayButtonImage();
    }

    private void updatePlayButtonImage() {
        playButton.setBackgroundResource(musicPlayer.isMusicPlaying() || musicPlayer.isLoadingSong() ? R.drawable.pause_button : R.drawable.play_button);
    }

    public void updateUIWithSongInfo () {
        TextView songNameDisplay = (TextView) findViewById(R.id.song_name_music_play);
        songNameDisplay.setText(musicPlayer.getCurrentSongName());

        TextView songArtistDisplay = (TextView) findViewById(R.id.artist_name_music_play);
        songArtistDisplay.setText(musicPlayer.getCurrentSongArtist());

        TextView albumNameDisplay = (TextView) findViewById(R.id.album_name_music_play);
        albumNameDisplay.setText(musicPlayer.getCurrentSongAlbum());

        Song curSong = musicPlayer.getCurrentSong();
        ((TextView) findViewById(R.id.song_last_played_info)).setText(!curSong.wasPlayedPreviously() ?
            "Never played before" :
            "Last played on \n" +
            curSong.getPreviousLocation().getAddressLine(this) + "\n" +
            new SimpleDateFormat("MMM d, yyyy").format(curSong.getLastPlayedDate()) + "\n" +
            new SimpleDateFormat("h:mm a").format(curSong.getLastPlayedDate()));

        final ImageButton likeButton = findViewById(R.id.button_like);
        if (musicPlayer.getCurrentLikeStatus() == 1) {
            likeButton.setBackgroundResource(R.drawable.favorite_button);
        } else if (musicPlayer.getCurrentLikeStatus() == 0) {
            likeButton.setBackgroundResource(R.drawable.neutral_button);
        } else if (musicPlayer.getCurrentLikeStatus() == 2) {
            likeButton.setBackgroundResource(R.drawable.dislike_button);
        }
    }
}
