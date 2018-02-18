package com.example.cse110.flashbackmusic;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Calendar;

/* TO-DO: SAVE THE PLAY MODE IN SHARED PREFERENCES */

public class MusicPlayer {
    private Resources song_resources;
    private MediaPlayer player;
    private Song [] songs;
    private Album [] albums;
    private Album curr_album;
    private Playlist flashback_playlist;
    private int play_index; // only used for album play and flashback mode
    private int play_mode; // 0 = song selection, 1 = album play, 2 = flashback

    private boolean loadingSong;

    public MusicPlayer(Resources resources) {
        this.song_resources = resources;
        this.player = new MediaPlayer();
        this.songs = new Song[3];
        this.play_index = -1;
        this.play_mode = 0;
        this.songs = MainActivity.getSongs();
        this.albums = MainActivity.getAlbums();
        this.curr_album = null;
        this.flashback_playlist = new Playlist();
    }

    public void destroy() { this.player.release(); }

    public void play() {
        this.player.start();
    }

    public void pause() {
        player.pause();
    }

    public void reset() {
        this.player.reset();
        loadSong(songs[play_index].getMediaID());
    }

    // only available in album play and flashback mode
    public void goToPreviousSong() {
        if (this.play_mode == 1) {
            this.curr_album.toPreviousSong();
            selectSong(this.curr_album.getCurrSongID());
        } else if (this.play_mode == 2) {
            this.flashback_playlist.toPreviousSong();
            selectSong(this.flashback_playlist.getCurrSongID());
        }
    }

    // only available in album play and flashback mode
    public void goToNextSong() {
        if (this.play_mode == 1) {
            this.curr_album.toNextSong();
            selectSong(this.curr_album.getCurrSongID());
        } else if (this.play_mode == 2) {
            this.flashback_playlist.toNextSong();
            selectSong(this.flashback_playlist.getCurrSongID());
        }
    }

    public void selectSong(int selected_id) {
        // preventing a reload of the song if the currently-playing song is selected again
        if (play_index != -1 && songs[play_index].getMediaID() == selected_id) {return;}
        for (int index = 0; index < this.songs.length; index++) {
            if (this.songs[index].getMediaID() == selected_id) {
                this.play_index = index;
            }
        }
        this.reset();
        songs[play_index].setDate(Calendar.getInstance().getTime());
        LatLon newLatLon = MainActivity.getLastLatLon();
        if (newLatLon != null) {
            songs[play_index].getLatLons().add(newLatLon);
            MainActivity.getSongSharedPrefHelper().saveSongData(selected_id);
        }
    }

    public void selectAlbum(int selected_index) {
        this.curr_album = this.albums[selected_index];
        this.selectSong(curr_album.getCurrSongID());
    }
  
    public boolean isMusicPlaying() {
        return player.isPlaying();
    }

    public void changeCurrentLikeStatus() {
        this.songs[play_index].incrementLikeStatus();
    }

    public Song getCurrentSong() {
        return this.songs[play_index];
    }

    public int getCurrentLikeStatus() {
        return this.songs[play_index].getLikeStatus();
    }

    public int getCurrentMediaID() { return this.songs[play_index].getMediaID(); }

    public String getCurrentString() { return this.songs[play_index].toString(); }

    public String getCurrentSongName() { return this.songs[play_index].getSongName(); }

    public String getCurrentSongArtist() { return this.songs[play_index].getArtistName(); }

    public String getCurrentSongAlbum() { return this.songs[play_index].getAlbumName(); }

    public int getPlayMode() { return this.play_mode; }

    public void setPlayMode(String mode) {
        if (mode.equals("album_selection")) {
            this.play_mode = 1;
        } else if (mode.equals("flashback")) {
            this.play_mode = 2;
            this.curr_album = null;
            this.flashback_playlist.sortPlaylist(Calendar.getInstance());
        } else { // default case
            this.play_mode = 0;
            this.curr_album = null;
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

            loadingSong = true;
            this.player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    player.start();
                    loadingSong = false;
                }
            });
        } catch (Exception e) {System.out.println(e.toString());}
    }

    public boolean isLoadingSong() {
        return loadingSong;
    }

    public Playlist getFlashback_playlist () {return flashback_playlist;}
}
