package com.example.cse110.flashbackmusic;

import android.util.Log;

import com.google.gson.Gson;

public class Album {
    private String album_name;
    public String artist_name;
    public int num_tracks;
    private String id;
    private Song [] songs;
    private int play_index;

    private String url;
    public long downloadId;
    public boolean downloaded;
    public boolean storedInRaw = true; // default

    public Album (String album_data, String url) {
        String [] data = album_data.split("; ");
        if (data.length != 4) {
            Log.e("Album Constructor", "Data String does not satisfy the format");
            this.album_name = "YouTube Audio Library";
            this.artist_name = "Media Right Productions";
            this.num_tracks = 1;
            this.id = "ALBUM_-1";
            this.songs = new Song[this.num_tracks];
            this.play_index = 0;
        } else {
            this.album_name = data[0];
            this.artist_name = data[1];
            this.num_tracks = Integer.parseInt(data[2]);
            this.id = data[3];
            this.songs = new Song[this.num_tracks];
            this.play_index = 0;
        }
        this.url = url;
    }

    public Album (String album_data, String url, long downloadId) {
        this(album_data, url);
        this.downloadId = downloadId;
        downloaded = false;
        storedInRaw = false;
    }

    public String getAlbumName() { return this.album_name; }

    public int getNumTracks() { return this.num_tracks; }

    public String getID() {
        return storedInRaw ? id : url;
    }

    public String toString() { return new Gson().toJson(this); }

    public void addSong(Song new_song, int index) {
        if ((index < 0) || (index >= songs.length)) {
            Log.e("Album addSong", "index out of range");
            return;
        }
        this.songs[index] = new_song;
        Log.i("Album addSong", "Adding song " + new_song.getSongName() +
                " to index " + index);
    }

    public int getCurrSongID() {
        return this.songs[this.play_index].getMediaID();
    }

    public void toPreviousSong() {
        this.play_index--;
        if (this.play_index < 0) {
            this.play_index = this.songs.length - 1;
        }
        Log.i("Album toPreviousSong", "play_index changed to " + this.play_index);
    }

    public void toNextSong() {
        this.play_index++;
        if (this.play_index > this.songs.length - 1) {
            this.play_index = 0;
        }
        Log.i("Album toNextSong", "play_index changed to " + this.play_index);
    }

    public boolean atEnd() {
        return (this.play_index > this.songs.length - 1);
    }
}
