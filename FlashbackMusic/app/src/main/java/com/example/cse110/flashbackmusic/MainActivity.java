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

        Button switchButton = (Button) findViewById(R.id.button_play_music);

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMusicPlay();
            }
        });
    }

    public void launchMusicPlay () {
        Intent intent = new Intent(this, MusicPlay.class);
        startActivity(intent);
    }
}
