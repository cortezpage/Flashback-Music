package com.example.cse110.flashbackmusic;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;


public class SharedPrefHelper {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedEditor;

    private String [] all_albums = {
            "I Will Not Be Afraid; Caroline Rose",
            "Love Is Everywhere; Stacy Jones",
            "New & Best of Keaton Simons; Keaton Simons",
            "Origins - The Best of Terry Oldfield; Terry Oldfield",
            "Take Yourself Too Seriously; Forum",
            "This is Always; Rebecca Sayre",
            "YouTube Audio Library; Media Right Productions"
    };

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
    private String [] all_data;

    public SharedPrefHelper (SharedPreferences prefs, SharedPreferences.Editor editor) {
        this.sharedPref = prefs;
        this.sharedEditor = editor;

        this.all_IDs = new int [] {
                R.raw.one_two_three_go,
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

        this.all_data = new String[this.all_songs.length];
        for (int index = 0; index < this.all_data.length; index++) {
            this.all_data[index] = "SONG; " + this.all_songs[index] + "; " + this.all_IDs[index];
        }
    }

    public void validateSongData() {
        String currSong;
        int write_count = 0;
        for (int index = 0; index < all_data.length && index < all_IDs.length; index++) {
            currSong = this.sharedPref.getString("" + all_IDs[index], "NOTHING FOUND");
            if (currSong == "NOTHING FOUND") {
                writeSongData(all_IDs[index], all_data[index]);
                write_count++;
                Log.i("New data written", all_data[index]);
            }
        }
        if (write_count > 0) { this.applyChanges(); }
    }

    public String [] getAllSongEntries() {
        Map<String, ?> allEntries = sharedPref.getAll();
        String [] song_data = new String[allEntries.size()];
        String curr_data;
        int index = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            curr_data = entry.getValue().toString();
            Log.i("Read data", entry.getKey() + ": " + curr_data);
            if (curr_data.split("; ")[0].equals("SONG")) {
                song_data[index] = curr_data;
                index++;
            }
        }
        return song_data;
    }

    public void writeSongData(int ID, String data) {
        Log.i("Updating song: ", data);
        this.sharedEditor.putString("" + ID, data);
    }

    public void applyChanges() {
        this.sharedEditor.apply();
    }
}
