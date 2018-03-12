package com.example.cse110.flashbackmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Arrays;

public class SongSelectionActivity extends AppCompatActivity {

    private Button [] song_buttons;
    private String selecteditem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        final Song [] songs = MainActivity.getSongs();
        createButtons(songs);

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int position, long lng) {
                selecteditem = adapter.getItemAtPosition(position).toString();
                switch (selecteditem) {
                    case "Title":
                        sortByTitle(song_buttons);
                        break;
                    case "Artist":
                        Log.e ("select item", "selected artist");
                        sortByArtist(songs, songs.length);
                        break;
                    case "Album":
                        sortByAlbum(songs, songs.length);
                        break;
                    case "Favorited":
                        sortByFavorite(songs, songs.length);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.i ("Drop down", "Nothing was selected");
            }
        });
    }

    public void createButtons (Song[] songs) {
        song_buttons = new Button [songs.length];
        Song curr_song;
        Button new_button;
        for (int index = 0; index < song_buttons.length; index++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.all_songs_list);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            curr_song = songs[index];
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
    }

    public void launchMusicPlay (String songID) {
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putExtra("SELECTED_ID", songID);
        intent.putExtra("MODE", "song_selection");
        startActivity(intent);
    }

    public void sortByTitle (Button [] song_buttons) {
        String [] titles = new String [song_buttons.length];
        for (int index = 0; index < song_buttons.length; index++) {
            String currSong = song_buttons[index].getText().toString();
            titles[index] = currSong;
        }
        Arrays.sort(titles);

        for (int index = 0; index < song_buttons.length; index++) {
            song_buttons[index].setText(titles[index]);
        }
    }

    public void sortByArtist (Song [] songs, int size) {
        Song currSong;
        String currArtist;
        int index;

        for (int i = 1; i < size; i++) {
            currSong = songs[i];
            currArtist = currSong.getArtistName();
            index = i - 1;

            while (index >= 0 && (songs[index].getArtistName().compareTo(currArtist)) > 0) {
                songs[index + 1] = songs[index];
                index--;
            }
            songs[index + 1] = currSong;
        }
        LinearLayout view = (LinearLayout)findViewById(R.id.all_songs_list);
        view.removeAllViews();
        createButtons(songs);
    }

    public void sortByAlbum (Song [] songs, int size) {
        Song currSong;
        String currAlbum;
        int index;

        for (int i = 1; i < size; i++) {
            currSong = songs[i];
            currAlbum = currSong.getAlbumName();
            index = i - 1;

            while (index >= 0 && (songs[index].getAlbumName().compareTo(currAlbum)) > 0) {
                songs[index + 1] = songs[index];
                index--;
            }
            songs[index + 1] = currSong;
        }
        LinearLayout view = (LinearLayout)findViewById(R.id.all_songs_list);
        view.removeAllViews();
        createButtons(songs);
    }

    public void sortByFavorite (Song [] songs, int size) {
        int low = 0;
        int mid = 0;
        int high = size - 1;
        Song currSong;

        while (mid <= high) {
            switch (songs[mid].getLikeStatus()) {
                case 1: {
                    currSong = songs[low];
                    songs[low] = songs[mid];
                    songs[mid] = currSong;
                    low++;
                    mid++;
                    break;
                }
                case 0:
                    mid++;
                    break;
                case 2: {
                    currSong = songs[mid];
                    songs[mid] = songs[high];
                    songs[high] = currSong;
                    high--;
                    break;
                }
            }
        }
        LinearLayout view = (LinearLayout)findViewById(R.id.all_songs_list);
        view.removeAllViews();
        createButtons(songs);
    }
}