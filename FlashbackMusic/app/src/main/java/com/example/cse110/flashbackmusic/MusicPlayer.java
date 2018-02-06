package com.example.cse110.flashbackmusic;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;

public class MusicPlayer {
    private Resources song_resources;
    private MediaPlayer player;
    private Song [] songs;
    // private Album [] albums ?
    private int play_index;

    public MusicPlayer(Resources resources) {
        this.song_resources = resources;
        this.player = new MediaPlayer();
        this.songs = new Song[3];
        this.play_index = 0;
    }

    public void destroy() {
        this.player.release();
    }

    public void play() {
        this.player.start();
    }

    public void pause() {
        if (this.player.isPlaying()) {
            this.player.pause();
        }
    }

    public void reset() {
        this.player.reset();
        loadSong(songs[play_index].getMediaID());
    }

    public void playNextSong() {
        play_index++;
        if (play_index >= songs.length) {
            play_index = 0;
        }
        this.reset();
        this.player.start();
    }

    // Use this method when you implement the ability to click and choose a song!
    public void selectSong() {
        // update the play index
        // this.reset();
        // this.player.start();
    }

    // Calls loadSong() on each song to be loaded
    public void loadSongs(int [] songIDs) {
        // Load songs: here is where we will place the songs in an array
        this.songs = new Song[songIDs.length];
        String songName = "Song #";
        for (int index = 0; index < songIDs.length; index++) {
            songs[index] = new Song(songName + index, "Artist", "Album", songIDs[index]);
        }
        loadSong(songIDs[0]); // load the first song to be played
    }

    /* loadSong is based on the loadMedia method from Lab 4.
     * This method allows us to load a song's data into the media player.
     */
    public void loadSong(int resourceId) {

        this.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        AssetFileDescriptor songFD = this.song_resources.openRawResourceFd(resourceId);
        try {
            this.player.setDataSource(songFD);
            this.player.prepareAsync();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
