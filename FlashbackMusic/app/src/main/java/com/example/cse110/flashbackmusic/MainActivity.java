package com.example.cse110.flashbackmusic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {//implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final boolean ERASE_DATA_AT_START = false; // for testing (set to false for release)

    private final int LOCATION_PERMISSION_REQUEST_CODE = 0; // arbitrary number chosen

    private static MusicPlayer musicPlayer = null;
    private static SharedPrefHelper sharedPrefHelper;
    private static Song [] songs;
    private static Album [] albums;
    private static LocationManager locationManager;
    private SharedPreferences songSharedPref;
    private SharedPreferences.Editor songDataEditor;
    private SharedPreferences albumSharedPref;
    private SharedPreferences.Editor albumDataEditor;

    public static MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static SharedPrefHelper getSongSharedPrefHelper() {
        return sharedPrefHelper;
    }

    public static Song [] getSongs() {
        return songs;
    }

    public static Album [] getAlbums() {
        return albums;
    }

    @Override
    public void onDestroy() {
        updateSongData();
        updateAlbumData();
        musicPlayer.destroy();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasLocationPermission(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();}
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);}
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void init()
    {
        LocationListener locationListener = new LocationListener() {
            @Override public void onLocationChanged(Location location) {}
            @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override public void onProviderEnabled(String s) {}
            @Override public void onProviderDisabled(String s) {}
        };
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        Context context = this.getApplicationContext();
        String song_data_filename = "com.example.cse110.flashbackmusic.song_data_preferences";
        String album_data_filename = "com.example.cse110.flashbackmusic.album_data_preferences";

        songSharedPref = context.getSharedPreferences(song_data_filename, Context.MODE_PRIVATE);
        songDataEditor = songSharedPref.edit();
        albumSharedPref = context.getSharedPreferences(album_data_filename, Context.MODE_PRIVATE);
        albumDataEditor = albumSharedPref.edit();

        if (ERASE_DATA_AT_START) {songDataEditor.clear().commit(); albumDataEditor.clear().commit();}

        sharedPrefHelper = new SharedPrefHelper(songSharedPref, songDataEditor, albumSharedPref, albumDataEditor);
        songs = sharedPrefHelper.createSongList();
        albums = sharedPrefHelper.createAlbums();

        musicPlayer = new MusicPlayer (this.getResources());

        Button toFlashbackMode = (Button) findViewById(R.id.button_to_flashback_mode);

        toFlashbackMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFlashbackMode();
            }
        });

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

    public void launchFlashbackMode () {
        getMusicPlayer().setPlayMode("flashback");
        Intent intent = new Intent(this, FlashbackActivity.class);
        startActivity(intent);
    }

    public void launchSongSelection () {
        Intent intent = new Intent(this, SongSelectionActivity.class);
        startActivity(intent);
    }

    public void launchAlbumSelection () {
        Intent intent = new Intent(this, AlbumSelectionActivity.class);
        startActivity(intent);
    }

    public void updateSongData() {
        Song curr_song;
        for (int index = 0; index < songs.length; index++) {
            curr_song = songs[index];
            sharedPrefHelper.writeSongData("" + curr_song.getMediaID(), curr_song.toString());
        }
    }

    public void updateAlbumData() {
        Album curr_album;
        for (int index = 0; index < albums.length; index++) {
            curr_album = albums[index];
            sharedPrefHelper.writeAlbumData(curr_album.getID(), curr_album.toString());
        }
    }

    public static Song getSong(int id)
    {
        for (int index = 0; index < songs.length; index++) {
            if (songs[index].getMediaID() == id) {
                return songs[index];
            }
        }
        return null;
    }

    public static boolean hasLocationPermission(Context context)
    {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressWarnings({"MissingPermission"})
    public static LatLon getLastLatLon()
    {
        if (locationManager == null) { return null;}
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation != null) {
            return new LatLon(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
        return null;
    }

}
