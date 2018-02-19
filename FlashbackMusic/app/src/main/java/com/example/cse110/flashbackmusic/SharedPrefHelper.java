package com.example.cse110.flashbackmusic;

import android.content.SharedPreferences;

import com.google.gson.Gson;


public class SharedPrefHelper {

    private SharedPreferences songSharedPref;
    private SharedPreferences.Editor songSharedEditor;
    private SharedPreferences albumSharedPref;
    private SharedPreferences.Editor albumSharedEditor;

    // format: album name, artist name, number of tracks, and album id number
    private String [] all_albums = {
            "I Will Not Be Afraid; Caroline Rose; 7; ALBUM_0",
            "Love Is Everywhere; Stacy Jones; 11; ALBUM_1",
            "New & Best of Keaton Simons; Keaton Simons; 10; ALBUM_2",
            "Origins - The Best of Terry Oldfield; Terry Oldfield; 11; ALBUM_3",
            "Take Yourself Too Seriously; Forum; 6; ALBUM_4",
            "This is Always; Rebecca Sayre; 12; ALBUM_5",
            "YouTube Audio Library; Media Right Productions; 1; ALBUM_6"
    };

    // format: song name, artist name, album name, duration in seconds, and like status
    private String [] all_songs = {
            "123 Go; Keaton Simons; New & Best of Keaton Simons; 209; 0",
            "After the Storm; Terry Oldfield; Origins - The Best of Terry Oldfield; 318; 0",
            "All About Ronnie; Rebecca Sayre; This is Always; 269; 0",
            "America Religious; Caroline Rose; I Will Not Be Afraid; 253; 0",
            "At Midnight; Caroline Rose; I Will Not Be Afraid; 198; 0",
            "Back East; Caroline Rose; I Will Not Be Afraid; 190; 0",
            "Beautiful Pain; Keaton Simons; New & Best of Keaton Simons; 277; 0",
            "Blood On Your Bootheels; Caroline Rose; I Will Not Be Afraid; 195; 0",
            "Can't Find Love; Stacy Jones; Love Is Everywhere; 208; 0",
            "Can't You Be Mine; Stacy Jones; Love Is Everywhere; 311; 0",
            "Crane City; Keaton Simons; New & Best of Keaton Simons; 231; 0",
            "Currently; Keaton Simons; New & Best of Keaton Simons; 189; 0",
            "Dead Dove Do Not Eat; Forum; Take Yourself Too Seriously; 288; 0",
            "Dreamatorium; Forum; Take Yourself Too Seriously; 256; 0",
            "Everything I Love; Rebecca Sayre; This is Always; 303; 0",
            "Flight of the Eagle; Terry Oldfield; Origins - The Best of Terry Oldfield; 355; 0",
            "Gotta Get Over You; Stacy Jones; Love Is Everywhere; 154; 0",
            "Hero Hell; Keaton Simons; New & Best of Keaton Simons; 285; 0",
            "Hey There; Rebecca Sayre; This is Always; 162; 0",
            "I Fell In Love; Stacy Jones; Love Is Everywhere; 211; 0",
            "I Get a Kick Out of You; Rebecca Sayre; This is Always; 155; 0",
            "I Just Want To Tell You Both Good Luck; Forum; Take Yourself Too Seriously; 353; 0",
            "I Will Not Be Afraid; Caroline Rose; I Will Not Be Afraid; 146; 0",
            "I'll Be On My Way; Stacy Jones; Love Is Everywhere; 227; 0",
            "In the Still of the Night; Rebecca Sayre; This is Always; 226; 0",
            "Jazz in Paris; Media Right Productions; YouTube Audio Library; 102; 0",
            "Just in Time; Rebecca Sayre; This is Always; 254; 0",
            "Just This Side of Insane; Keaton Simons; New & Best of Keaton Simons; 234; 0",
            "Lift Me Up; Keaton Simons; New & Best of Keaton Simons; 195; 0",
            "Love Is Everywhere; Stacy Jones; Love Is Everywhere; 286; 0",
            "Mangalam; Terry Oldfield; Origins - The Best of Terry Oldfield; 448; 0",
            "Mojo Potion; Stacy Jones; Love Is Everywhere; 256; 0",
            "Om Namo Bhavagate; Terry Oldfield; Origins - The Best of Terry Oldfield; 434; 0",
            "One Stop Light; Stacy Jones; Love Is Everywhere; 192; 0",
            "Origin; Terry Oldfield; Origins - The Best of Terry Oldfield; 290; 0",
            "Other Side; Keaton Simons; New & Best of Keaton Simons; 220; 0",
            "Out of this World; Rebecca Sayre; This is Always; 331; 0",
            "Perdido; Rebecca Sayre; This is Always; 182; 0",
            "Reach Out; Terry Oldfield; Origins - The Best of Terry Oldfield; 335; 0",
            "Set Me Free; Terry Oldfield; Origins - The Best of Terry Oldfield; 354; 0",
            "Sisters of the Sun; Forum; Take Yourself Too Seriously; 277; 0",
            "Sky Dancer; Terry Oldfield; Origins - The Best of Terry Oldfield; 259; 0",
            "Sky Full of Ghosts; Forum; Take Yourself Too Seriously; 331; 0",
            "Stomp Jump Boogie; Stacy Jones; Love Is Everywhere; 226; 0",
            "Sweet Sue, Just You; Rebecca Sayre; This is Always; 178; 0",
            "The Writing On The Wall; Terry Oldfield; Origins - The Best of Terry Oldfield; 323; 0",
            "This Is Always; Rebecca Sayre; This is Always; 274; 0",
            "Tightrope Walker; Caroline Rose; I Will Not Be Afraid; 212; 0",
            "Tough Girls Never Cry; Stacy Jones; Love Is Everywhere; 199; 0",
            "Unstoppable; Keaton Simons; New & Best of Keaton Simons; 225; 0",
            "Wait For Heaven; Stacy Jones; Love Is Everywhere; 290; 0",
            "Waves; Terry Oldfield; Origins - The Best of Terry Oldfield; 363; 0",
            "What's Your Story, Morning Glory; Rebecca Sayre; This is Always; 401; 0",
            "When I Go; Keaton Simons; New & Best of Keaton Simons; 205; 0",
            "When You Go; Caroline Rose; I Will Not Be Afraid; 224; 0",
            "Who Cares?; Rebecca Sayre; This is Always; 196; 0",
            "Wilderness; Terry Oldfield; Origins - The Best of Terry Oldfield; 362; 0",
            "Windows are the Eyes to the House; Forum; Take Yourself Too Seriously; 246; 0"
    };

    private int [] all_IDs;
    private String [] initial_song_data;

    public SharedPrefHelper (SharedPreferences songPrefs, SharedPreferences.Editor songEditor,
                             SharedPreferences albumPrefs, SharedPreferences.Editor albumEditor) {
        this.songSharedPref = songPrefs;
        this.songSharedEditor = songEditor;
        this.albumSharedPref = albumPrefs;
        this.albumSharedEditor = albumEditor;

        this.all_IDs = new int [] {
                //R.raw.one_two_three_go,
                R.raw.after_the_storm,
                R.raw.all_about_ronnie,
                R.raw.america_religious,
                R.raw.at_midnight,
                R.raw.back_east,
                R.raw.beautiful_pain,
                R.raw.blood_on_your_bootheels,
                R.raw.cant_find_love,
                R.raw.cant_you_be_mine,
                R.raw.crane_city,
                R.raw.currently,
                R.raw.dead_dove_do_not_eat,
                R.raw.dreamatorium,
                R.raw.everything_i_love,
                R.raw.flight_of_the_eagle,
                R.raw.gotta_get_over_you,
                R.raw.hero_hell,
                R.raw.hey_there,
                R.raw.i_fell_in_love,
                R.raw.i_get_a_kick_out_of_you,
                R.raw.i_just_want_to_tell_you_both_good_luck,
                R.raw.i_will_not_be_afraid,
                R.raw.ill_be_on_my_way,
                R.raw.in_the_still_of_the_night,
                R.raw.jazz_in_paris,
                R.raw.just_in_time,
                R.raw.just_this_side_of_insane,
                R.raw.lift_me_up,
                R.raw.love_is_everywhere_1,
                R.raw.mangalam,
                R.raw.mojo_potion_61_49,
                R.raw.om_namo_bhavagate,
                R.raw.one_stop_light,
                R.raw.origin,
                R.raw.other_side,
                R.raw.out_of_this_world,
                R.raw.perdido,
                R.raw.reach_out,
                R.raw.set_me_free,
                R.raw.sisters_of_the_sun,
                R.raw.sky_dancer,
                R.raw.sky_full_of_ghosts,
                R.raw.stomp_jump_boogie,
                R.raw.sweet_sue_just_you,
                R.raw.the_writing_on_the_wall,
                R.raw.this_is_always,
                R.raw.tightrope_walker,
                R.raw.tough_girls_never_cry,
                R.raw.unstoppable,
                R.raw.wait_for_heaven,
                R.raw.waves,
                R.raw.whats_your_story_morning_glory,
                R.raw.when_i_go,
                R.raw.when_you_go,
                R.raw.who_cares_,
                R.raw.wilderness,
                R.raw.windows_are_the_eyes_to_the_house
        };

        this.initial_song_data = new String[this.all_songs.length];
        for (int index = 0; index < this.initial_song_data.length; index++) {
            this.initial_song_data[index] = this.all_songs[index] + "; " + this.all_IDs[index];
        }
    }

    public boolean hasPreviousAlbumData() {
        String hasData = this.albumSharedPref.getString("ALBUM_DATA_EXISTENCE_STATUS", "NONEXISTENT");
        if (hasData.equals("EXISTS")) {
            return true;
        }
        return false;
    }

    public String [] getInitialAlbumData() { return this.all_albums; }

    public String [] getStoredAlbumData() {
        String curr_data;
        String [] album_data = new String [all_albums.length];
        for (int index = 0; index < all_albums.length; index++) {
            curr_data = albumSharedPref.getString("ALBUM_" + index, "NOT FOUND");
            if (!(curr_data.equals("NOT FOUND"))) {
                album_data[index] = curr_data;
            }
        }
        return album_data;
    }

    public Album [] createAlbums () {
        String [] album_data;
        Album [] albums;
        Song [] songs;

        boolean hasStoredData = this.hasPreviousAlbumData();
        if (!hasStoredData) {
            album_data = this.getInitialAlbumData();
        } else {
            album_data = this.getStoredAlbumData();
        }

        albums = new Album[album_data.length];
        songs = MainActivity.getSongs();
        Gson gson = new Gson();
        Album new_album;
        for (int index = 0; index < album_data.length; index++) {
            if (hasStoredData) {
                String json = albumSharedPref.getString("ALBUM_" + index, "NOT FOUND");
                if (!(json.equals("NOT FOUND"))) {
                    new_album = gson.fromJson(json, Album.class);
                    albums[index] = new_album;
                }
            } else {
                new_album = new Album(album_data[index]);
                albums[index] = new_album;
                int song_count = 0;
                for (int song_index = 0; song_index < songs.length && song_count < new_album.getNumTracks(); song_index++) {
                    if (songs[song_index].getAlbumName().equals(new_album.getAlbumName())) {
                        new_album.addSong(songs[song_index], song_count);
                        song_count++;
                    }
                }
                writeAlbumData("" + new_album.getID(), new_album.toString());
            }
        }

        if (!hasStoredData) {
            this.writeAlbumData("ALBUM_DATA_EXISTENCE_STATUS", "EXISTS");
        }

        return albums;
    }

    public boolean hasPreviousSongData() {
        String hasData = this.songSharedPref.getString("SONG_DATA_EXISTENCE_STATUS", "NONEXISTENT");
        if (hasData.equals("EXISTS")) {
            return true;
        }
        return false;
    }

    public String [] getInitialSongData() {
        return initial_song_data;
    }

    public String [] getStoredSongData() {
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
        String [] song_data;
        boolean hasStoredData = this.hasPreviousSongData();
        if (!hasStoredData) {
            song_data = this.getInitialSongData();
            this.writeSongData("SONG_DATA_EXISTENCE_STATUS", "EXISTS");
        } else {
            song_data = this.getStoredSongData();
        }

        songs = new Song[song_data.length];
        Gson gson = new Gson();
        Song new_song;
        for (int index = 0; index < song_data.length && index < all_IDs.length; index++) {
            if (hasStoredData) {
                String json = songSharedPref.getString("" + all_IDs[index], "NOT FOUND");
                if (!(json.equals("NOT FOUND"))) {
                    new_song = gson.fromJson(json, Song.class);
                    songs[index] = new_song;
                }
            } else {
                new_song = new Song(song_data[index]);
                songs[index] = new_song;
                writeSongData("" + new_song.getMediaID(), new_song.toString());
            }
        }

        return songs;
    }

    public void writeAlbumData(String id, String data) {
        this.albumSharedEditor.putString(id, data);
        this.albumSharedEditor.apply();
    }

    public void writeSongData(String id, String data) {
        this.songSharedEditor.putString(id, data);
        songSharedEditor.apply();
    }
}
