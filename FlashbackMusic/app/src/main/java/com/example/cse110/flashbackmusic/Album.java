package com.example.cse110.flashbackmusic;

import com.google.gson.Gson;

public class Album {
    private String album_name;
    private String artist_name;
    private int num_tracks;
    private String id;
    private Song [] songs;

    public Album (String album_data) {
        String [] data = album_data.split("; ");
        this.album_name = data[0];
        this.artist_name = data[1];
        this.num_tracks = Integer.parseInt(data[2]);
        this.id = data[3];
        this.songs = new Song[this.num_tracks];
    }

    public String getAlbumName() { return this.album_name; }

    public String getID() {
        return this.id;
    }

    public String toString() { return new Gson().toJson(this); }
}
