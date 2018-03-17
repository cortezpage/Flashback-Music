package com.example.cse110.flashbackmusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;

public class MusicPlayActivity extends AppCompatActivity implements LastPlayedObserver {

    private MusicPlayer musicPlayer;
    private SharedPrefHelper sharedPrefHelper;
    private ImageButton playButton;
    private int play_mode;

    final boolean testing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LastPlayedController.getInstance().addListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        musicPlayer = MainActivity.getMusicPlayer();
        sharedPrefHelper = MainActivity.getSongSharedPrefHelper();

        String mode = getIntent().getStringExtra("MODE");
        musicPlayer.setPlayMode(mode);
        play_mode = musicPlayer.getPlayMode();

        // Link the toggle button to the vibe mode activity
        ToggleButton toggleVibe = findViewById(R.id.button_toggle);
        toggleVibe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Can only toogle to vibe mode in song selection activity
                if (musicPlayer.getPlayMode() != 1) {
                    if (isChecked) {
                        // The toggle is enabled
                        updateTracklistButton(true);
                        musicPlayer.setPlayMode("flashback");
                        MainActivity.updateMode(2);
                        updateButtonImages();
                    } else {
                        // The toggle is disabled
                        updateTracklistButton(false);
                        musicPlayer.setPlayMode("song_selection");
                        MainActivity.updateMode(0);
                        musicPlayer.stop();
                        finish();
                    }
                }
            }
        });

        Log.i("MusicPlayActivity", "set play_mode to " + play_mode);
        if (play_mode == 0) {
            int selected_id = Integer.parseInt(getIntent().getStringExtra("SELECTED_ID"));
            musicPlayer.selectSong(selected_id);
        }
        else {
            updateTracklistButton(true);
            if (play_mode == 1) {
                int selected_index = Integer.parseInt(getIntent().getStringExtra("SELECTED_INDEX"));
                musicPlayer.selectAlbum(selected_index);
            } else if (musicPlayer.getPlayMode() == 2) {
                int curr_id = musicPlayer.getPlaylistSongID();
                musicPlayer.selectSong(curr_id);
                if (!toggleVibe.isChecked()) {
                    toggleVibe.setChecked(true);
                }
            }
        }

        musicPlayer.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (play_mode == 1 || play_mode == 2) {
                    if ((play_mode == 1 && musicPlayer.reachedEndOfAlbum()) ||
                            (play_mode == 2 && musicPlayer.reachedEndOfPlaylist())) {
                        Log.i("MusicPlayActivity", "Reached the end of Album or playlist");
                        musicPlayer.stop();
                        finish();
                    } else {
                        musicPlayer.updateSongInfo();
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
                MainActivity.updateMode(-1);
                finish();
            }
        });

        Button showTracklist = findViewById(R.id.show_tracklist);
        showTracklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTracklistActivity();
            }
        });

        // Link the "play" button with the play() method from the music player
        playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MusicPlayActivity Play Button", "Play Button is clicked");
                if (!musicPlayer.isLoadingSong()) {
                    if (musicPlayer.isMusicPlaying()) {
                        musicPlayer.pause();}
                    else {
                        musicPlayer.play();
                    }
                }
                updateButtonImages();
            }
        });

        // Link the "reset" button with the reset() method from the music player
        ImageButton resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MusicPlayActivity Reset Button", "Reset Button is clicked");
                musicPlayer.reset();
                updateButtonImages();
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
                Log.i("MusicPlayActivity Like Button", "Changing the status to" + musicPlayer.getCurrentLikeStatus());
                sharedPrefHelper.writeSongData("" + musicPlayer.getCurrentMediaID(), musicPlayer.getCurrentString());
            }
        });

        updateUIWithSongInfo();
        updateButtonImages();
    }

    private void launchTracklistActivity() {
        Log.i("MusicPlayActivity LaunchTracklistSelection", "Launching Tracklist Display");
        Intent intent = new Intent(this, TracklistActivity.class);
        startActivity(intent);
    }

    private void updateButtonImages() {
        if (musicPlayer.isMusicPlaying() || musicPlayer.isLoadingSong()) {
            playButton.setBackgroundResource(R.drawable.pause_button);
            Log.i("MusicPlayActivity", "Displaying the pause button!");
        } else {
            playButton.setBackgroundResource(R.drawable.play_button);
            Log.i("MusicPlayActivity", "Displaying the play button!");
        }

        // Link the "previous" button with the playPreviousSong() method from the music player
        ImageButton previousButton = findViewById(R.id.button_previous);
        previousButton.getBackground().setAlpha(40);
        if (musicPlayer.getPlayMode() == 1) { previousButton.getBackground().setAlpha(255); }
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MusicPlayActivity Previous Button", "Previous Button is clicked");
                if (musicPlayer.getPlayMode() == 1) {
                    musicPlayer.goToPreviousSong();
                    updateUIWithSongInfo();
                }
            }
        });

        // Link the "next" button with the playNextSong() method from the music player
        ImageButton nextButton = findViewById(R.id.button_next);
        nextButton.getBackground().setAlpha(40);
        if (musicPlayer.getPlayMode() != 0) { nextButton.getBackground().setAlpha(255); }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MusicPlayActivity Next Button", "Next Button is clicked");
                play_mode = musicPlayer.getPlayMode();
                if ((play_mode == 1 && musicPlayer.reachedEndOfAlbum()) ||
                        (play_mode == 2 && musicPlayer.reachedEndOfPlaylist())) {
                    musicPlayer.stop();
                    finish();
                }
                if (testing) { musicPlayer.updateSongInfo(); }

                if (play_mode != 0) {
                    musicPlayer.goToNextSong();
                    updateUIWithSongInfo();
                }
            }
        });
    }

    public void updateTracklistButton (boolean visible) {
        Button showTracklist = findViewById(R.id.show_tracklist);
        if (visible) {
            showTracklist.setVisibility(View.VISIBLE);
        } else {
            showTracklist.setVisibility(View.INVISIBLE);
        }
    }

    public void updateUIWithSongInfo () {
        TextView songNameDisplay = (TextView) findViewById(R.id.song_name_music_play);
        songNameDisplay.setText(musicPlayer.getCurrentSongName());

        TextView songArtistDisplay = (TextView) findViewById(R.id.artist_name_music_play);
        songArtistDisplay.setText(musicPlayer.getCurrentSongArtist());

        TextView albumNameDisplay = (TextView) findViewById(R.id.album_name_music_play);
        albumNameDisplay.setText(musicPlayer.getCurrentSongAlbum());

        Song currSong = musicPlayer.getCurrentSong();
        String toShow;
        if (!currSong.wasPlayedPreviously()) {
            toShow = "This track has never been played before.";
        } else {
            toShow = "Last played on: \n" +
                    currSong.getLastPlayedLocation().getAddressLine(this) + "\n" +
                    new SimpleDateFormat("MMM d, yyyy").format(currSong.getLastPlayedDate()) + "\n" +
                    new SimpleDateFormat("h:mm a").format(currSong.getLastPlayedDate());
        }

        ((TextView) findViewById(R.id.song_last_played_info)).setText(toShow);

        Log.i("MusicPlayActivity", "Now displaying: " + toShow);

        final ImageButton likeButton = findViewById(R.id.button_like);
        if (musicPlayer.getCurrentLikeStatus() == 1) {
            likeButton.setBackgroundResource(R.drawable.favorite_button);
        } else if (musicPlayer.getCurrentLikeStatus() == 0) {
            likeButton.setBackgroundResource(R.drawable.neutral_button);
        } else if (musicPlayer.getCurrentLikeStatus() == 2) {
            likeButton.setBackgroundResource(R.drawable.dislike_button);
        }
    }

    public void lastPlayedUpdate (Song song) {
        Song currentSong = musicPlayer.getCurrentSong();
        if (song.getSongName().equals(currentSong.getSongName())) {
            updateUIWithSongInfo();
        }
    }

    public void onDestroy () {
        super.onDestroy();
        LastPlayedController.getInstance().removeListener(this);
    }
}
