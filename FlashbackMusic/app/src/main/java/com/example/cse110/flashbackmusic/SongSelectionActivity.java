package com.example.cse110.flashbackmusic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SongSelectionActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedEditor;
    private SharedPrefHelper sharedPrefHelper;
    private String [] song_data;
    private Button [] song_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        Context context = this.getApplicationContext();
        String filename = "com.example.cse110.flashbackmusic.song_data_preferences";
        sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        sharedEditor = sharedPref.edit();

        sharedPrefHelper = new SharedPrefHelper(sharedPref, sharedEditor);
        sharedPrefHelper.validateSongData();
        song_data = sharedPrefHelper.getAllSongEntries();

        song_buttons = new Button [song_data.length];
        String [] curr_data;
        Button new_button;
        for (int buttonIndex = 0; buttonIndex < song_buttons.length; buttonIndex++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.all_songs_list);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            curr_data = song_data[buttonIndex].split("; ");
            final int curr_id = Integer.parseInt(curr_data[6]);
            new_button = new Button(this);
            new_button.setText(curr_data[1]);
            new_button.setId(curr_id);
            song_buttons[buttonIndex] = new_button;
            layout.addView(new_button, params);
            new_button = ((Button) findViewById(curr_id));
            new_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
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
    }

    public void launchMusicPlay (String songID) {
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putExtra("SELECTED_ID", songID);
        intent.putExtra("MODE", "song_selection");
        startActivity(intent);
    }

}
