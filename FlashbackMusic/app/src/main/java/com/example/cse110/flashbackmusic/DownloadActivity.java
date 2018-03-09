package com.example.cse110.flashbackmusic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class DownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ImageButton back = findViewById(R.id.back_to_main);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MainActivity.updateMode(-1);
                finish();
            }
        });

        Button submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText url = findViewById(R.id.url);
                EditText song_name = findViewById(R.id.name);
                EditText artist = findViewById(R.id.artist);
                EditText album_name = findViewById(R.id.album_name);
                final CheckBox isAlbum = findViewById(R.id.isAlbum);

                final String url_str = url.getText().toString().trim();
                final String song_name_str = song_name.getText().toString().trim();
                final String artist_str = artist.getText().toString().trim();
                final String album_str = album_name.getText().toString().trim();

                if (TextUtils.isEmpty(url_str) || TextUtils.isEmpty(song_name_str) ||
                        TextUtils.isEmpty(artist_str) || TextUtils.isEmpty(album_str)){
                    Toast.makeText(DownloadActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    String songData = song_name_str + "; " + artist_str + "; " + album_str + "; 0; 0; -1";
                    if (!isAlbum.isChecked()) {
                        Song newSong = new Song(songData, url_str);
                        Toast.makeText(DownloadActivity.this, "Song information submitted", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // TODO: Porcess downloading albums
                        Toast.makeText(DownloadActivity.this, "Album information submitted", Toast.LENGTH_SHORT).show();
                    }

                    clearFields();
                    // TODO: Call downloadManager.enqueue on the url
                }
            }
        });
    }

    public void clearFields () {
        EditText url = findViewById(R.id.url);
        EditText song_name = findViewById(R.id.name);
        EditText artist = findViewById(R.id.artist);
        EditText album_name = findViewById(R.id.album_name);
        final CheckBox isAlbum = findViewById(R.id.isAlbum);

        url.setText("");
        song_name.setText("");
        artist.setText("");
        album_name.setText("");
        isAlbum.setChecked(false);
    }
}
