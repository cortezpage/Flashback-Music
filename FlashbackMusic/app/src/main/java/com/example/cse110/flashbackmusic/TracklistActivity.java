package com.example.cse110.flashbackmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Queue;

public class TracklistActivity extends AppCompatActivity {

    private Button [] song_buttons;
    private ArrayList<Song> songs_toshowlist;
    private int play_mode;

    private void updateSongsToShow () {
        play_mode = MainActivity.getPlayMode();
        Log.i("Tracklist", "Play Mode is " + play_mode);

        songs_toshowlist = new ArrayList<>();
        Song[] temp_songs;
        Playlist playlist = MainActivity.getPlaylist();
        Queue<Song> temp_songs_pq = null;
        if (playlist != null) {
            temp_songs_pq = playlist.getSongPQ();
        }

        if (play_mode == 1) {
            temp_songs = MusicPlayActivity.songs_toshow;
            for (int i = 0; i < temp_songs.length; i++) {
                songs_toshowlist.add(temp_songs[i]);
            }
        } else if (play_mode == 2) {
            // get the temp_songs from playlist
            if (temp_songs_pq != null) {
                for (Song item : temp_songs_pq) {
                    songs_toshowlist.add(item);
                }
            }
        }

        Log.i("TrackList", "size of songs to show on the list is " + songs_toshowlist.size());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracklist);

        // Link the "back" button to go back to the song selection activity
        ImageButton back = findViewById(R.id.button_exit_tracklist_display);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MainActivity.updateMode(-1);
                finish();
            }
        });

        updateSongsToShow();

        createDisplayButtons(songs_toshowlist);
    }

    public void createDisplayButtons (ArrayList<Song> songs) {
        song_buttons = new Button[songs.size()];
        Song curr_song;
        Button new_button;
        for (int index = 0; index < song_buttons.length; index++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.tracklist_container);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            curr_song = songs.get(index);
            final int curr_id = curr_song.getMediaID();
            new_button = new Button(this);
            new_button.setText(curr_song.getSongName());
            new_button.setId(curr_id);
            song_buttons[index] = new_button;
            layout.addView(new_button, params);
        }
    }
}
