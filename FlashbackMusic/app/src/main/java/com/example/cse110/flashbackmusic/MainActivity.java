package com.example.cse110.flashbackmusic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {//implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final boolean ERASE_DATA_AT_START = true; // for testing (set to false for release)

    private final boolean START_WITH_SONGS = true; // set to true if you want to start with some songs loaded

    private static MusicPlayer musicPlayer = null;
    private static SharedPrefHelper sharedPrefHelper;
    private static ArrayList<Song> songs;
    private static ArrayList<Album> albums;
    private static LocationManager locationManager;
    private SharedPreferences songSharedPref;
    private SharedPreferences.Editor songDataEditor;
    private SharedPreferences idSharedPref;
    private SharedPreferences.Editor idDataEditor;
    private SharedPreferences albumSharedPref;
    private SharedPreferences.Editor albumDataEditor;
    private SharedPreferences modeSharedPref;
    private static SharedPreferences.Editor modeDataEditor;
    private static File storage;

    private static DownloadManager downloadManager;
    private static BroadcastReceiver broadcastReceiver;

    public static File getStorageFile()
    {
        return storage;
    }

    public static MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static SharedPrefHelper getSongSharedPrefHelper() {
        return sharedPrefHelper;
    }

    public static ArrayList<Song> getSongs() {
        return songs;
    }

    public static ArrayList<Album> getAlbums() {
        return albums;
    }

    @Override
    public void onDestroy() {
        Log.i("MainActivity Destructor", "Destroying the MainActivity");
        updateSongData();
        updateAlbumData();
        musicPlayer.destroy();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasPermission(this)) {
            Log.i("MainActivity Permission", "Missing permission");
            requestPermissions();
            return;
        }
        init();
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults)
        {
            if (grantResult != PackageManager.PERMISSION_GRANTED)
            {
                Log.i("MainActivity Request Permission Result", "Permission denied, asking again");
                requestPermissions();
                return;
            }
        }
        Log.i("MainActivity Request Permission Result", "Permission granted");
        init();
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
        String id_data_filename = "com.example.cse110.flashbackmusic.id_data_preferences";
        String album_data_filename = "com.example.cse110.flashbackmusic.album_data_preferences";
        String mode_data_filename = "com.example.cse110.flashbackmusic.saved_mode_pref";

        Log.i("MainActivty init", "Initializing song shared preference and album shared" +
                "preference");

        songSharedPref = context.getSharedPreferences(song_data_filename, Context.MODE_PRIVATE);
        songDataEditor = songSharedPref.edit();
        idSharedPref = context.getSharedPreferences(id_data_filename, Context.MODE_PRIVATE);
        idDataEditor = idSharedPref.edit();
        albumSharedPref = context.getSharedPreferences(album_data_filename, Context.MODE_PRIVATE);
        albumDataEditor = albumSharedPref.edit();
        modeSharedPref = context.getSharedPreferences(mode_data_filename, Context.MODE_PRIVATE);
        modeDataEditor = modeSharedPref.edit();

        if (ERASE_DATA_AT_START) {
            songDataEditor.clear().commit();
            idDataEditor.clear().commit();
            albumDataEditor.clear().commit();
            modeDataEditor.clear().commit();
        }

        Log.i("MainActivity: starting with hardcoded songs?", "" + START_WITH_SONGS);

        sharedPrefHelper = new SharedPrefHelper(songSharedPref, songDataEditor, albumSharedPref,
                albumDataEditor, idSharedPref, idDataEditor, START_WITH_SONGS);
        songs = sharedPrefHelper.createSongList();
        albums = sharedPrefHelper.createAlbums();

        musicPlayer = new MusicPlayer (this.getResources());

        String mode = modeSharedPref.getString("LAST_PLAYED_MODE", "NOT FOUND");
        Log.i("MODE", mode);
        if (mode.equals("flashback")) {
            launchFlashbackMode();
        }

        Button toDownloadActivity = (Button) findViewById(R.id.button_to_download_mode);

        toDownloadActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDownloadActivity();
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

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);


        storage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "FBMusic");
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.d(getClass().getName(), "Download complete");

                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                for (Song song : songs)
                {
                    if(song.downloadId == downloadId)
                    {
                        song.downloaded = true;
                        updateSongData();
                        Log.d("getClass().getName()", "Song downloaded." + song.getSongName() + song.getArtistName());
                        return;
                    }
                }
                for (Album album : albums)
                {
                    if(album.downloadId == downloadId)
                    {
                        album.downloaded = true;
                        updateAlbumData();
                        new UnzipTask(new File(storage, album.getAlbumName() + ".zip"),
                            new File(storage, album.getAlbumName()),
                            album).execute();
                        Log.d(getClass().getName() + ", Broadcast Receiver onReceive", "Started unzip task");
                        return;
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    // Source (used for info), 3/7/18:
    // Android Download Manager Tutorial: How to Download Files using Download Manager from Internet
    // https://www.codeproject.com/Articles/1112730/Android-Download-Manager-Tutorial-How-to-Download
    // Returns download id
    public static long download(String url, String name)
    {
        Uri uri = Uri.parse(url);

        try
        {
            Log.d("MainActivity download", "in download try");
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("Download song/album");
            request.setDescription("Downloading from " + url);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/FBMusic/" + name);
            long downloadId = downloadManager.enqueue(request);
            return downloadId;
        }
        catch (IllegalArgumentException e)
        {
            return -1;
        }
    }

    private class UnzipTask extends AsyncTask<Void, Void, Void>
    {
        File zipFile;
        File targetDir;
        Album album;

        UnzipTask(File zipFile, File targetDir, Album album)
        {
            this.zipFile = zipFile;
            this.targetDir = targetDir;
            this.album = album;
        }

        protected Void doInBackground(Void... params)
        {
            unzip(zipFile, targetDir);
            return null;
        }

        protected void onPostExecute(Void param) {
            File[] files = new File(storage, album.getAlbumName()).listFiles();
            album.num_tracks = files.length;

            Log.d(getClass().getName(), "Unzip task complete, Album " + album.getAlbumName() +
                " { num_tracks:  " + album.num_tracks + " }");

            for (File file : files)
            {
                String songName = file.getName();
                songName = songName.substring(0, songName.length() - 4); // removes .mp3 from song name
                songName = songName.replace('.', ' '); // for firebase
                songName = songName.replace('$', ' ');
                songName = songName.replace('#', ' ');
                songName = songName.replace('[', ' ');
                songName = songName.replace(']', ' ');
                String songData = songName + "; " + album.artist_name + "; " + album.getAlbumName() + "; 0; 0; -1";
                Song newSong = new Song(songData, album.getID());
                newSong.inAlbum = true;
                newSong.downloaded = true;
                newSong.storedInRaw = false;
                newSong.fileName = file.getName();
                MainActivity.getSongs().add(newSong);
                Log.d(getClass().getName(), newSong.getSongName() + " added");
            }
            updateSongData();
        }
    }

    // Source (used for info), 3/6/18:
    // How to unzip files programmatically in Android?
    // https://stackoverflow.com/questions/3382996/how-to-unzip-files-programmatically-in-android
    public static void unzip(File zipFile, File targetDir)
    {
        Log.d("Unzip", "Starting unzip of " + zipFile.getAbsolutePath() + " to " + targetDir.getAbsolutePath());
        final int BUF_SIZE = 8192;
        //ZipInputStream zipInputStream;
        try (ZipInputStream zipInputStream = new ZipInputStream(
            new BufferedInputStream(new FileInputStream(zipFile))))
        {
            try
            {
                byte[] buf = new byte[BUF_SIZE];
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null)
                {
                    File file = new File(targetDir, zipEntry.getName());
                    File dir = zipEntry.isDirectory() ? file : file.getParentFile();
                    if (!(dir.isDirectory() || dir.mkdirs()))
                    {
                        throw new FileNotFoundException("Error with directories");
                    }
                    if (!zipEntry.isDirectory())
                    {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        int numBytesRead;
                        while ((numBytesRead = zipInputStream.read(buf)) != -1)
                        {
                            fileOutputStream.write(buf, 0, numBytesRead);
                        }
                        fileOutputStream.close();
                    }
                    Log.d("Unzip", "Unzipped a file...");
                }
            }
            catch (IOException e) { e.printStackTrace(); return; }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e1){e1.printStackTrace(); }
        Log.d("Unzip", "Finished unzip");
    }

    public void launchDownloadActivity () {
        Log.i("MainAcitivity LaunchDonwloadActivity", "Launching Download activity");
        Intent intent = new Intent(this, DownloadActivity.class);
        startActivity(intent);
    }

    public void launchFlashbackMode () {
        updateMode(2);
        Log.i("MainAcitivity LaunchFlashbackMode", "Launching Flashback Mode");
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putExtra("MODE", "flashback");
        startActivity(intent);
    }

    public void launchSongSelection () {
        updateMode(0);
        Log.i("MainAcitivity LaunchSongSelection", "Launching Song Selection Mode");
        Intent intent = new Intent(this, SongSelectionActivity.class);
        startActivity(intent);
    }

    public void launchAlbumSelection () {
        updateMode(1);
        Log.i("MainAcitivity LaunchAlbumSelection", "Launching Album Selection Mode");
        Intent intent = new Intent(this, AlbumSelectionActivity.class);
        startActivity(intent);
    }

    public void updateSongData() {
        Song curr_song;
        for (int index = 0; index < songs.size(); index++) {
            curr_song = songs.get(index);
            sharedPrefHelper.writeSongData("" + curr_song.getMediaID(), curr_song.toString());
            sharedPrefHelper.writeIDData("" + index, "" + curr_song.getMediaID());
            Log.i("MainActivity updateSongData", "updating Song: " + curr_song.getSongName()
                    + " data into shared preference");
        }
    }

    public void updateAlbumData() {
        Album curr_album;
        for (int index = 0; index < albums.size(); index++) {
            curr_album = albums.get(index);
            sharedPrefHelper.writeAlbumData(curr_album.getID(), curr_album.toString());
            sharedPrefHelper.writeAlbumData("NUM_ALBUMS", "" + albums.size());
            Log.i("MainActivity updateAlbumData", "updating Album: " + curr_album.getAlbumName()
                    + " data into shared preference");
        }
    }

    public static void updateMode(int app_mode) {
        String toWrite = "NOT SPECIFIED";
        if (app_mode == 0) {
            toWrite = "song_selection";
        } else if (app_mode == 1) {
            toWrite = "album_selection";
        } else if (app_mode == 2) {
            toWrite = "flashback";
        } else if (app_mode == -1) {
            toWrite = "main";
        }
        modeDataEditor.putString("LAST_PLAYED_MODE", "" + toWrite);
        modeDataEditor.apply();
    }

    public static boolean hasPermission(Context context)
    {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED  &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED  &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressWarnings({"MissingPermission"})
    public static LatLon getLastLatLon()
    {
        if (locationManager == null) { return null; }
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation != null) {
            Log.i("MainActivity getLastLatLon", "returning lon = " + lastLocation.getLongitude() +
            "; returning lat = " + lastLocation.getLatitude());
            return new LatLon(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
        // Return the CSE Building if the location manager fails
        Log.e("MainActivity getLastLatLon", "location access failed, returning default location");
        return new LatLon(32.881801, -117.233523);
    }

}
