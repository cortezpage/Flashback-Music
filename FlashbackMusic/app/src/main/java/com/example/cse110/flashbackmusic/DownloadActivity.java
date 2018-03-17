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

    private String url_str;
    private String artist_str;
    private String songOrAlbum_name_str;
    private long downloadId;

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

        Button submitSong = findViewById(R.id.submit_song_button);
        submitSong.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                submit(false);
            }
        });

        Button submitAlbum = findViewById(R.id.submit_album_button);
        submitAlbum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                submit(true);
            }
        });
    }

    public void submit(boolean submitAlbum)
    {
        EditText url = findViewById(R.id.url);
        EditText artist = findViewById(R.id.artist);
        EditText songOrAlbum_name = findViewById(R.id.name);

        url_str = url.getText().toString().trim();
        artist_str = artist.getText().toString().trim();
        songOrAlbum_name_str = songOrAlbum_name.getText().toString().trim();

        if (TextUtils.isEmpty(url_str) || TextUtils.isEmpty(artist_str) || TextUtils.isEmpty(songOrAlbum_name_str) ){
            Toast.makeText(DownloadActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        downloadId = MainActivity.download(url_str, songOrAlbum_name_str + (!submitAlbum ? ".mp3" : ".zip"));
        if (downloadId == -1)
        {
            Toast.makeText(DownloadActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!submitAlbum) {
            String songData = songOrAlbum_name_str + "; " + artist_str + "; " + "No Album" + "; 0; 0; -1";
            Song newSong = new Song(songData, url_str, downloadId);
            newSong.inAlbum = false;
            MainActivity.getSongs().add(newSong);
        }
        else {
            String albumData = songOrAlbum_name_str + "; " + artist_str + "; " + "0; " +
                "ALBUM_" + MainActivity.getAlbums().size(); //size is index after last index
            Album newAlbum = new Album(albumData, url_str, downloadId);
            MainActivity.getAlbums().add(newAlbum);
        }
        Toast.makeText(DownloadActivity.this, (!submitAlbum ? "Song" : "Album") +
            " information submitted", Toast.LENGTH_SHORT).show();

        // clear fields
        url.setText("");
        songOrAlbum_name.setText("");
        artist.setText("");
    }
}
