package com.example.cse110.flashbackmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class SongSelectionActivity extends AppCompatActivity {

    private Button [] song_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        ArrayList<Song> songs = MainActivity.getSongs();

        song_buttons = new Button [songs.size()];
        Song curr_song;
        Button new_button;
        for (int index = 0; index < song_buttons.length; index++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.all_songs_list);
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
            new_button = ((Button) findViewById(curr_id));
            new_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.i("SongSelection Song Clicked", "Selected Song: " + curr_id);
                    launchMusicPlay("" + curr_id);
                }
            });
        }

        ImageButton back = findViewById(R.id.button_exit_song_selection);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        // Drop down lists
        Spinner spinner = (Spinner) findViewById(R.id.drop_down);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorting_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void launchMusicPlay (String songID) {
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putExtra("SELECTED_ID", songID);
        intent.putExtra("MODE", "song_selection");
        startActivity(intent);
    }



}
