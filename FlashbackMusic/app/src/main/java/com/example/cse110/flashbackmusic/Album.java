package com.example.cse110.flashbackmusic;

public class Album {
    private String album_name;
    private String artist_name;
    private Song [] songs;
    private int curr_index;

    public Album (String name, String artist) {
        this.album_name = name;
        this.artist_name = artist;
        this.curr_index = 0;
    }
}
