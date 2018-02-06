package com.example.cse110.flashbackmusic;

public class Song {
    private String song_name; // name of the song
    private String artist_name; // name of the artist of the song
    private String album_name; // name of the album to which the song belongs
    private int media_id;
    private int like_status; // 0 = neutral, 1 = favorited, and 2 = disliked

    public Song(String name, String artist, String album, int id) {
        this.song_name = name;
        this.artist_name = artist;
        this.album_name = album;
        this.media_id = id;
    }

    public String getSongName() { return this.song_name; }

    public String getArtistName() {
        return this.artist_name;
    }

    public String getAlbumName() {
        return this.album_name;
    }

    public int getMediaID() { return this.media_id; }

    public int getLikeStatus() { return this.like_status; }

    public boolean isNeutral() { return (this.like_status == 0); }

    public boolean isFavorited() { return (this.like_status == 1); }

    public boolean isDisliked() { return (this.like_status == 2); }

    public void setToNeutral() { this.like_status = 0; }

    public void setToFavorite() { this.like_status = 1; }

    public void setToDisliked() { this.like_status = 2; }

}
