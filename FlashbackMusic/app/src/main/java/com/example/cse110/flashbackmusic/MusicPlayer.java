package com.example.cse110.flashbackmusic;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;

/* TO-DO: SAVE THE PLAY MODE IN SHARED PREFERENCES */

public class MusicPlayer {
    private Resources song_resources;
    private MediaPlayer player;
    private Song [] songs;
    // private Album [] albums ?
    private int play_index; // only used for album play and flashback mode
    private int play_mode; // 0 = song selection, 1 = album play, 2 = flashback

    public MusicPlayer(Resources resources) {
        this.song_resources = resources;
        this.player = new MediaPlayer();
        this.songs = new Song[3];
        this.play_index = 0;
        this.play_mode = 0;
        this.songs = MainActivity.getSongs();
    }

    public void destroy() { this.player.release(); }

    public void play() {
        Log.i("Now playing:", this.getCurrentString());
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

    // only available in album play and flashback mode
    public void goToPreviousSong() {
        if (play_mode != 0) {
            play_index--;
            if (play_index < 0) {
                play_index = songs.length - 1;
            }
            this.reset();
        } else {
            Log.i("MusicPlayer.goToPreviousSong()", "Cannot go to previous song in single song selection mode!");
        }
    }

    // only available in album play and flashback mode
    public void goToNextSong() {
        if (play_mode != 0) {
            play_index++;
            if (play_index >= songs.length) {
                play_index = 0;
            }
            this.reset();
        } else {
            Log.i("MusicPlayer.goToNextSong()", "Cannot go to next song in single song selection mode!");
        }
    }

    public void selectSong(int selected_id) {
        for (int index = 0; index < this.songs.length; index++) {
            if (this.songs[index].getMediaID() == selected_id) {
                this.play_index = index;
            }
        }
        this.reset();
    }
  
    public boolean isMusicPlaying() {
        if (this.player.isPlaying()){
            return true;
        }

        return false;
    }

    public void changeCurrentLikeStatus() {
        this.songs[play_index].incrementLikeStatus();
    }

    public int getCurrentLikeStatus() {
        return this.songs[play_index].getLikeStatus();
    }

    public int getCurrentMediaID() { return this.songs[play_index].getMediaID(); }

    public String getCurrentString() { return this.songs[play_index].toString(); }

    public String getCurrentWriteString() { return this.songs[play_index].toString(); }

    public String getCurrentSongName() { return this.songs[play_index].getSongName(); }

    public String getCurrentSongArtist() { return this.songs[play_index].getArtistName(); }

    public String getCurrentSongAlbum() { return this.songs[play_index].getAlbumName(); }

    public int getPlayMode() { return this.play_mode; }

    public void setPlayMode(String mode) {
        if (mode.equals("song_selection")) {
            this.play_mode = 0;
        } else if (mode.equals("album_play")) {
            this.play_mode = 1;
        } else if (mode.equals("flashback")) {
            this.play_mode = 2;
        } else {
            this.play_mode = 0; // default case
        }
    }

    /* loadSong is based on the loadMedia method from Lab 4.
     * This method allows us to load a song's data into the media player.
     */
    public void loadSong(int resourceId) {

        // Determines the behavior that will occur when the song is over
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

            this.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    player.start();
                }

            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
