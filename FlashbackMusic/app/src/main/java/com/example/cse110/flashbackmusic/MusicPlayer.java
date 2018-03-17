package com.example.cse110.flashbackmusic;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/* TO-DO: SAVE THE PLAY MODE IN SHARED PREFERENCES */

public class MusicPlayer {
    private Resources song_resources;
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private ArrayList<Album> albums;
    private Album curr_album;
    private Playlist flashback_playlist;
    private int play_index; // only used for album play and flashback mode
    private int play_mode; // 0 = song selection, 1 = album play, 2 = flashback

    private DatabaseManager databaseManager;

    private boolean loadingSong;

    public MusicPlayer(Resources resources) {

        this.song_resources = resources;
        this.player = new MediaPlayer();

        this.play_index = -1;
        this.play_mode = 0;
        this.songs = MainActivity.getSongs();
        this.albums = MainActivity.getAlbums();
        this.curr_album = null;
        this.databaseManager = new DatabaseManager();
        Log.i("MusicPlayer Constructor", "Initialized with parameters play_index " +
        play_index + "\nplay_mode " + play_mode);
    }

    public void destroy() {
        Log.i("MusicPlayer Destructor", "Destroyed the music player");
        this.player.release();
    }

    public void play() {
        Log.i("MusicPlayer play", "Start to play current song");
        this.player.start();
    }

    public void pause() {
        Log.i("MusicPlayer pause", "Paused the current song");
        player.pause();
    }

    public void reset() {
        this.player.reset();
        loadSong(songs.get(play_index).getMediaID());
        Log.i("MusicPlayer reset", "Reseting current song and loading song " +
                songs.get(play_index).getMediaID());
    }

    public void stop() {
        this.player.stop();
    }

    public MediaPlayer getMediaPlayer() {
        return this.player;
    }

    public void updateSongInfo() {
        Song curr_song = songs.get(play_index);
        curr_song.setLastPlayedCalendar(Calendar.getInstance());
        LatLon newLatLon = MainActivity.getLastLatLon();
        if (newLatLon != null) {
            songs.get(play_index).setLastPlayedLocation(newLatLon);
            MainActivity.getSongSharedPrefHelper().writeSongData("" + curr_song.getMediaID(), curr_song.toString());

            Log.i("MusicPlayer updateSongInfo", "Song " + songs.get(play_index).getSongName() + " is" +
                    "being updated Location with latitude " + newLatLon.getLatitude() + " and longitude " +
                    newLatLon.getLongitude());

            Log.i("MusicPlayer updateSongInfo", "Calling DatabaseManager to push play instance" +
            "to remote database");

            User dummyUser = new User("DummyUser");
            //TODO if we wanna manipulate the time we should modify here & grab the User

            Calendar cal = Calendar.getInstance();
            PlayInstance curr_instance = new PlayInstance(dummyUser, newLatLon, cal);
            databaseManager.storePlayInstance(curr_instance, curr_song);

            Log.i("MusicPlayer updateSongInfo", "update completed");
        }
    }

    public void updatePlaylist(boolean startingFBMode) {
        return;/*
        Calendar currTime = Calendar.getInstance();
        LatLon currLoc = MainActivity.getLastLatLon();
        if (flashback_playlist == null) {
            return;
        }
        if (this.flashback_playlist.shouldSort(currTime, currLoc) || startingFBMode) {

            Log.i("MusicPlayer updatePlaylist", "updating the playlist with current time "
                    + currTime.getTime().getTime() + " current location with latitude and longitude: " +
                    currLoc.getLatitude() + " " + currLoc.getLongitude());

            this.flashback_playlist.sortPlaylist(currTime, currLoc);
        }*/
    }

    // only available in album play and flashback mode
    public void goToPreviousSong() {
        if (this.play_mode == 1) {
            this.curr_album.toPreviousSong();
            selectSong(this.curr_album.getCurrSongID());
        } else if (this.play_mode == 2 && !this.flashback_playlist.atEnd()) {
            selectSong(this.flashback_playlist.getCurrSongID());
        }
    }

    // only available in album play and flashback mode
    public void goToNextSong() {
        if (this.play_mode == 1) {
            this.curr_album.toNextSong();
            selectSong(this.curr_album.getCurrSongID());
        } else if (this.play_mode == 2 && !this.flashback_playlist.atEnd()) {
            Log.i("MusicPlayer", "Playing next song");
            selectSong(this.flashback_playlist.getCurrSongID());
        }
    }

    public boolean reachedEndOfAlbum() {
        return this.curr_album.atEnd();
    }

    public boolean reachedEndOfPlaylist() {
        return this.flashback_playlist.atEnd();
    }

    public void selectSong(int selected_id) {
        // preventing a reload of the song if the currently-playing song is selected again
        if (play_index != -1 && songs.get(play_index).getMediaID() == selected_id) {return;}
        for (int index = 0; index < this.songs.size(); index++) {
            if (this.songs.get(index).getMediaID() == selected_id) {
                this.play_index = index;
                Log.e("MusicPlayer selectSong", "the current song id is " + index);

                // update the song play instances from remote database
                databaseManager.updatePlayInstance(songs.get(play_index));

//                Thread updateUIInfo = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        wait(1000);
//
//                    }
//                });

                Log.e("TESTING", "song: " + songs.get(play_index).getSongName() +
                " User "  );
            }
        }
        this.reset();
    }

    public void selectAlbum(int selected_index) {
        this.curr_album = this.albums.get(selected_index);
        this.selectSong(curr_album.getCurrSongID());
        Log.i("MusicPlayer selectAlbum", "the current album id is " + curr_album);

    }
  
    public boolean isMusicPlaying() {
        return player.isPlaying();
    }

    public void changeCurrentLikeStatus() {
        this.songs.get(play_index).incrementLikeStatus();
    }

    public Song getCurrentSong() {
        return this.songs.get(play_index);
    }

    public int getCurrentLikeStatus() {
        return this.songs.get(play_index).getLikeStatus();
    }

    public int getCurrentMediaID() { return this.songs.get(play_index).getMediaID(); }

    public String getCurrentString() { return this.songs.get(play_index).toString(); }

    public String getCurrentSongName() { return this.songs.get(play_index).getSongName(); }

    public String getCurrentSongArtist() { return this.songs.get(play_index).getArtistName(); }

    public String getCurrentSongAlbum() { return this.songs.get(play_index).getAlbumName(); }

    public int getPlayMode() { return this.play_mode; }

    public void setPlayMode(String mode) {
        if (mode.equals("album_selection")) {
            this.play_mode = 1;
            flashback_playlist = null;
        } else if (mode.equals("flashback")) {
            this.play_mode = 2;
            this.curr_album = null;
            Log.i("MusicPlayer", "Starting Vibe Mode");
            flashback_playlist = new Playlist();
        } else { // default case
            this.play_mode = 0;
            this.curr_album = null;
            flashback_playlist = null;
        }
    }

    /* loadSong is based on the loadMedia method from Lab 4.
     * This method allows us to load a song's data into the media player.
     */
    public void loadSong(int resourceId) {

        Log.e ("loadSong", "loading song ID: " + resourceId);

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

        } catch (Exception e) {System.err.println(e.toString());}
    }

    public boolean isLoadingSong() {
        return loadingSong;
    }

    // This is to be called ONLY ONCE when we first enter flashback mode.
    public int getPlaylistSongID () {return flashback_playlist.getCurrSongID();}
}
