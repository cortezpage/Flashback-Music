package com.example.cse110.flashbackmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AlbumSelectionActivity extends AppCompatActivity {

    private Button[] album_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_selection);

        Album [] albums = MainActivity.getAlbums();

        album_buttons = new Button [albums.length];
        Album curr_album;
        Button new_button;
        for (int index = 0; index < album_buttons.length; index++) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.all_albums_list);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            curr_album = albums[index];
            final int curr_id = index;
            new_button = new Button(this);
            new_button.setText(curr_album.getAlbumName());
            new_button.setId(curr_id);
            album_buttons[index] = new_button;
            layout.addView(new_button, params);
            new_button = ((Button) findViewById(curr_id));
            new_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    launchMusicPlay("" + curr_id);
                }
            });
        }

        ImageButton back = findViewById(R.id.button_exit_album_selection);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    public void launchMusicPlay (String albumID) {
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putExtra("SELECTED_ID", albumID);
        intent.putExtra("MODE", "album_selection");
        startActivity(intent);
    }

}
