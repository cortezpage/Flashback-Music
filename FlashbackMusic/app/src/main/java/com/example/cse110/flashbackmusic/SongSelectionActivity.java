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

import java.util.ArrayList;

public class SongSelectionActivity extends AppCompatActivity {

    public static Button [] song_buttons;
    private String selecteditem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);
        final ArrayList<Song> songs = MainActivity.getSongs();
        Log.d ("songs in song selection activity", "songs" + songs);
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
        spinner.setSelection(0, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int position, long lng) {
                selecteditem = adapter.getItemAtPosition(position).toString();
                switch (selecteditem) {
                    case "Sort by":
                        break;
                    case "Title":
                        sortByTitle(songs, songs.size());
                        break;
                    case "Artist":
                        Log.e ("select item", "selected artist");
                        sortByArtist(songs, songs.size());
                        Log.d("ORDER: BY ARTIST", "SUCCESSFUL");
                        for (int i = 0; i < 10; i++) {
                            Log.d("SORTED BY ARTIST",""+i+" "+song_buttons[i].getText().toString());
                        }
                        break;
                    case "Album":
                        sortByAlbum(songs, songs.size());
                        Log.d("ORDER: BY ALBUM", "SUCCESSFUL");
                        for (int i = 0; i < 10; i++) {
                            Log.d("SORTED BY ALBUM",""+i+" "+song_buttons[i].getText().toString());
                        }
                        break;
                    case "Favorited":
                        sortByFavorite(songs, songs.size());
                        Log.d("ORDER: BY FAVORITE", "SUCCESSFUL");
                        for (int i = 0; i < 10; i++) {
                            Log.d("SORTED BY FAVORITE",""+i+" "+song_buttons[i].getText().toString());
                        }
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.i ("Drop down", "Nothing was selected");
            }
        });
    }

    public void createButtons (ArrayList<Song> songs) {
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
    }

    public void launchMusicPlay (String songID) {
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putExtra("SELECTED_ID", songID);
        intent.putExtra("MODE", "song_selection");
        startActivity(intent);
    }

    public void sortByTitle (ArrayList<Song> songs, int size) {
        Song currSong;
        String currSongName;
        int index;

        for (int i = 1; i < size; i++) {
            currSong = songs.get(i);
            currSongName = currSong.getSongName();
            index = i - 1;

            while (index >= 0 && (songs.get(index).getSongName().compareTo(currSongName)) > 0) {
                songs.set(index + 1, songs.get(index));
                index--;
            }
            songs.set(index + 1, currSong);
        }
        LinearLayout view = (LinearLayout)findViewById(R.id.all_songs_list);
        view.removeAllViews();
        createButtons(songs);
    }

    public void sortByArtist (ArrayList<Song> songs, int size) {
        Song currSong;
        String currArtist;
        int index;

        for (int i = 1; i < size; i++) {
            currSong = songs.get(i);
            currArtist = currSong.getArtistName();
            index = i - 1;

            while (index >= 0 && (songs.get(index).getArtistName().compareTo(currArtist)) > 0) {
                songs.set(index + 1, songs.get(index));
                index--;
            }
            songs.set(index + 1, currSong);
        }
        LinearLayout view = (LinearLayout)findViewById(R.id.all_songs_list);
        view.removeAllViews();
        createButtons(songs);
    }

    public void sortByAlbum (ArrayList<Song> songs, int size) {
        Song currSong;
        String currAlbum;
        int index;

        for (int i = 1; i < size; i++) {
            currSong = songs.get(i);
            currAlbum = currSong.getAlbumName();
            index = i - 1;

            while (index >= 0 && (songs.get(index).getAlbumName().compareTo(currAlbum)) > 0) {
                songs.set(index + 1, songs.get(index));
                index--;
            }
            songs.set(index + 1, currSong);
        }
        LinearLayout view = (LinearLayout)findViewById(R.id.all_songs_list);
        view.removeAllViews();
        createButtons(songs);
    }

    public void sortByFavorite (ArrayList<Song> songs, int size) {
        int low = 0;
        int mid = 0;
        int high = size - 1;
        Song currSong;

        while (mid <= high) {
            switch (songs.get(mid).getLikeStatus()) {
                case 1: {
                    currSong = songs.get(low);
                    songs.set(low, songs.get(mid));
                    songs.set(mid, currSong);
                    low++;
                    mid++;
                    break;
                }
                case 0:
                    mid++;
                    break;
                case 2: {
                    currSong = songs.get(mid);
                    songs.set(mid, songs.get(high));
                    songs.set(high, currSong);
                    high--;
                    break;
                }
            }
        }
        LinearLayout view = (LinearLayout)findViewById(R.id.all_songs_list);
        view.removeAllViews();
        createButtons(songs);
    }

    public Button[] getOriginalSongButtons() {
        for (int i = 0; i < 10; i++) {
            Log.d("UNSORTED SONGS",""+i+" "+song_buttons[i].getText().toString());
        }
        return song_buttons;
    }
}
