package com.example.cse110.flashbackmusic;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;


public class SharedPrefHelper {

    private SharedPreferences songSharedPref;
    private SharedPreferences.Editor songSharedEditor;
    private SharedPreferences albumSharedPref;
    private SharedPreferences.Editor albumSharedEditor;
    private SharedPreferences idSharedPref;
    private SharedPreferences.Editor idSharedEditor;

    private boolean use_test_data;

    private int [] all_IDs;

    private int [] testing_IDs = {
            R.raw.america_religious,
            R.raw.at_midnight,
            R.raw.back_east,
            R.raw.all_about_ronnie,
            R.raw.dead_dove_do_not_eat,
            R.raw.dreamatorium,
            R.raw.everything_i_love,
            R.raw.i_get_a_kick_out_of_you,
            R.raw.jazz_in_paris,
            R.raw.windows_are_the_eyes_to_the_house
    };

    // format: album name, artist name, number of tracks, and album id number
    private String [] testing_albums = {
            "I Will Not Be Afraid; Caroline Rose; 3; ALBUM_0",
            "Take Yourself Too Seriously; Forum; 3; ALBUM_1",
            "This is Always; Rebecca Sayre; 3; ALBUM_2",
            "YouTube Audio Library; Media Right Productions; 1; ALBUM_3"
    };

    // format: song name, artist name, album name, duration in seconds, and like status
    private String [] testing_songs = {
            "America Religious; Caroline Rose; I Will Not Be Afraid; 253; 0; " + testing_IDs[1],
            "At Midnight; Caroline Rose; I Will Not Be Afraid; 198; 0; " + testing_IDs[2],
            "Back East; Caroline Rose; I Will Not Be Afraid; 190; 0; " + testing_IDs[3],
            "All About Ronnie; Rebecca Sayre; This is Always; 269; 0; " + testing_IDs[0],
            "Dead Dove Do Not Eat; Forum; Take Yourself Too Seriously; 288; 0; " + testing_IDs[4],
            "Dreamatorium; Forum; Take Yourself Too Seriously; 256; 0; " + testing_IDs[5],
            "Everything I Love; Rebecca Sayre; This is Always; 303; 0; " + testing_IDs[6],
            "I Get a Kick Out of You; Rebecca Sayre; This is Always; 155; 0; " + testing_IDs[7],
            "Jazz in Paris; Media Right Productions; YouTube Audio Library; 102; 0; " + testing_IDs[8],
            "Windows are the Eyes to the House; Forum; Take Yourself Too Seriously; 246; 0; " + testing_IDs[9]
    };

    public SharedPrefHelper (SharedPreferences songPrefs, SharedPreferences.Editor songEditor,
                             SharedPreferences albumPrefs, SharedPreferences.Editor albumEditor,
                             SharedPreferences idPrefs, SharedPreferences.Editor idEditor, boolean testing) {
        this.songSharedPref = songPrefs;
        this.songSharedEditor = songEditor;
        this.albumSharedPref = albumPrefs;
        this.albumSharedEditor = albumEditor;
        this.idSharedPref = idPrefs;
        this.idSharedEditor = idEditor;
        this.use_test_data = testing;
    }

    public String [] getAlbumData() {
        if (use_test_data) {
            return this.testing_albums;
        }
        String currData;
        String albumCount = albumSharedPref.getString("NUM_ALBUMS", "NOT FOUND");
        if (!albumCount.equals("NOT FOUND")) {
            int numAlbums = Integer.parseInt(albumCount);
            String [] album_data = new String [numAlbums];
            for (int index = 0; index < numAlbums; index++) {
                currData = albumSharedPref.getString("ALBUM_" + index, "NOT FOUND");
                if (!(currData.equals("NOT FOUND"))) {
                    album_data[index] = currData;
                }
            }
            return album_data;
        }
        return new String [0];
    }

    public Album [] createAlbums () {
        String [] albumData;
        Album [] albums;
        Album newAlbum = null;
        albumData = this.getAlbumData();
        albums = new Album[albumData.length];

        Song [] songs = MainActivity.getSongs();
        Gson gson = new Gson();

        for (int index = 0; index < albums.length; index++) {
            if (use_test_data) {
                newAlbum = new Album(albumData[index]);
                int songCount = 0;
                for (int song_index = 0; song_index < songs.length && songCount < newAlbum.getNumTracks(); song_index++) {
                    if (songs[song_index].getAlbumName().equals(newAlbum.getAlbumName())) {
                        newAlbum.addSong(songs[song_index], songCount);

                        Log.i("SharedPrefHelper createAlbums", "Adding the song " +
                                songs[song_index].getSongName() + " into the album " + newAlbum.getAlbumName());

                        songCount++;
                    }
                }
            } else {
                String json = albumSharedPref.getString("ALBUM_" + index, "NOT FOUND");
                if (!(json.equals("NOT FOUND"))) {
                    newAlbum = gson.fromJson(json, Album.class);
                    albums[index] = newAlbum;
                }
            }
            albums[index] = newAlbum;
            writeAlbumData(newAlbum.getID(), newAlbum.toString());
        }
        writeAlbumData("NUM_ALBUMS", "" + albums.length);
        return albums;
    }

    public void setAllIDs() {
        if (use_test_data) {
            this.all_IDs = this.testing_IDs;
        } else {
            Map<String, ?> allIDEntries = idSharedPref.getAll();
            this.all_IDs = new int[allIDEntries.size()];
            for (Map.Entry<String, ?> entry : allIDEntries.entrySet()) {
                int id = Integer.parseInt(entry.getValue().toString());
                int index = Integer.parseInt(entry.getKey().toString());
                if (index < all_IDs.length) {
                    this.all_IDs[index] = id;
                } else {
                    Log.e("SharedPrefHelper Index Issue", "ID out of bounds!");
                }
            }
        }
    }

    public String [] getSongData() {
        if (use_test_data) {
            return this.testing_songs;
        }
        String curr_data;
        String [] song_data = new String [all_IDs.length];
        for (int index = 0; index < all_IDs.length; index++) {
            curr_data = songSharedPref.getString("" + all_IDs[index], "NOT FOUND");
            if (!(curr_data.equals("NOT FOUND"))) {
                song_data[index] = curr_data;
            }
        }
        return song_data;
    }

    public Song [] createSongList () {
        Song [] songs;
        Song newSong;
        String [] songData;
        Gson gson = new Gson();

        this.setAllIDs();
        songData = this.getSongData();
        songs = new Song[songData.length];

        String unknownSong = "Unknown Name; Unknown Artist; Unknown Album; 0; 0; ";

        for (int index = 0; index < songData.length && index < all_IDs.length; index++) {
           if (use_test_data) {
               newSong = new Song(songData[index], "");
           } else {
               String json = songSharedPref.getString("" + all_IDs[index], "NOT FOUND");
               if (!(json.equals("NOT FOUND"))) {
                   newSong = gson.fromJson(json, Song.class);
               } else {
                   newSong = new Song(unknownSong + all_IDs[index], "");
               }
           }
           songs[index] = newSong;
           writeSongData("" + newSong.getMediaID(), newSong.toString());
           writeIDData("" + index, "" + newSong.getMediaID());
        }

        return songs;
    }

    public void writeAlbumData(String id, String data) {
        Log.i("SharedPrefHelper", "Writing updates to Album data");
        this.albumSharedEditor.putString(id, data);
        this.albumSharedEditor.apply();
    }

    public void writeSongData(String id, String data) {
        Log.i("SharedPrefHelper", "Writing updates to Song data");
        this.songSharedEditor.putString(id, data);
        songSharedEditor.apply();
    }

    public void writeIDData(String id, String data) {
        Log.i("SharedPrefHelper", "Writing updates to ID data");
        this.idSharedEditor.putString(id, data);
        idSharedEditor.apply();
    }
}
