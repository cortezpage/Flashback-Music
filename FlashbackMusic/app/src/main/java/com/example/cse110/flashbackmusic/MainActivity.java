package com.example.cse110.flashbackmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toSongSelection = (Button) findViewById(R.id.button_to_song_selection);

        toSongSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSongSelection();
            }
        });

        Button toAlbumSelection = (Button) findViewById(R.id.button_to_album_selection);

        toAlbumSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAlbumSelection();
            }
        });
    }

    public void launchSongSelection () {
        Intent intent = new Intent(this, SongSelection.class);
        startActivity(intent);
    }

    public void launchAlbumSelection () {
        Intent intent = new Intent(this, AlbumSelection.class);
        startActivity(intent);
    }
}
