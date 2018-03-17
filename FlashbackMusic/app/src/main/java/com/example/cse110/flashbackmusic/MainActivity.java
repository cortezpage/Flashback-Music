package com.example.cse110.flashbackmusic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.net.Uri;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final boolean ERASE_DATA_AT_START = true; // for testing (set to false for release)

    private final boolean START_WITH_SONGS = true; // set to true if you want to start with some songs loaded

    private final int LOCATION_PERMISSION_REQUEST_CODE = 0; // arbitrary number chosen

    private final String CLIENT_AUTH = "787403082568-fgl6qfqr64ed5jsd0uk8a7a4jmoo996m.apps.googleusercontent.com";

    private final String CLIENT_SECRET = "-i7_U5cbS3Eybh0Wuq7IQmmY";

    private final int LOGIN_SUCCESS_RESULT_CODE = 178900;

    private final int SIGN_IN_REQ_CODE = 1;

    private static MusicPlayer musicPlayer = null;
    private static SharedPrefHelper sharedPrefHelper;
    private static ArrayList<Song> songs;
    private static ArrayList<Album> albums;
    private static LocationManager locationManager;
    private static ArrayList<User> friends;
    private static GoogleSignInAccount googleAccount;
    private static GoogleSignInClient googleSignInClient;
    private HttpTransport httpTransport;
    private JacksonFactory jsonFactory;
    private GoogleTokenResponse tokenResponse;
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

    public static Playlist getPlaylist () {return musicPlayer.getPlaylist(); }

    public static int getPlayMode () {return musicPlayer.getPlayMode();}

    public static Intent getGoogleSignInIntent() { return googleSignInClient.getSignInIntent(); }

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

    public static ArrayList<User> getFriends() {
        return friends;
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

        Log.d("HERE","");

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

        httpTransport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
        tokenResponse = null;

        GoogleAuthentication();

        String mode = modeSharedPref.getString("LAST_PLAYED_MODE", "NOT FOUND");
        mode = "default"; // TODO remove this line
        Log.i("MODE", mode);
        // if the previous mode was flashback mode AND the sign in activity was not already launched
        if ((!(googleAccount == null)) && mode.equals("flashback")) {
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

        Button toTimeChangeActivity = findViewById(R.id.button_to_change_time);
        toTimeChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTimeChangeActivity();
            }
        });
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
            album.setNumTracks(files.length);

            Log.d(getClass().getName(), "Unzip task complete, Album " + album.getAlbumName() +
                " { num_tracks:  " + album.num_tracks + " }");

            int songCount=0;
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
                album.addSong(newSong, songCount);
                songCount++;
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

    public void GoogleAuthentication() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(CLIENT_AUTH)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                    new Scope(PeopleServiceScopes.CONTACTS_READONLY))
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        googleAccount = GoogleSignIn.getLastSignedInAccount(this);
        launchSignInActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the login activity just finished and the login was successful, update the googleAccount field
        if (resultCode == LOGIN_SUCCESS_RESULT_CODE) {
            googleAccount = GoogleSignIn.getLastSignedInAccount(this.getApplicationContext());
            Log.i("LOGIN SUCCESSFUL", "USER: " + googleAccount.getEmail());
            new SetUpPeopleAPITask().execute();
        }
    }

    public void launchSignInActivity () {
        Log.i("MainActivity LaunchSignInActivity", "Launching Sign In Activity");
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, SIGN_IN_REQ_CODE);    }

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

    public void launchTimeChangeActivity () {
        Log.i("MainActivity", "Launching TimeChangeActivity");
        Intent intent = new Intent(this, DateChangeActivity.class);
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

    private class SetUpPeopleAPITask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void ... params) {
            String code = googleAccount.getServerAuthCode();
            String redirectUrl = "https://vibemode-2b73b.firebaseapp.com/__/auth/handler";

            if (code == null) { Log.e("AUTH CODE RETRIEVED", "NULL"); }
            else {
                Log.i("AUTH CODE RETRIEVED", "SUCCESS");
                try {
                    tokenResponse =
                            new GoogleAuthorizationCodeTokenRequest(
                                    httpTransport, jsonFactory, CLIENT_AUTH, CLIENT_SECRET, code, redirectUrl)
                                    .execute();
                } catch (IOException e) {
                    Log.e("ASYNC TASK ERROR", e.getMessage());
                }
            }
            return null;
        }

        protected void onPostExecute(Void param) {
            new GetFriendsTask().execute();
        }
    }

    private class GetFriendsTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void ... params) {

            if (tokenResponse != null) {
                GoogleCredential credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(CLIENT_AUTH, CLIENT_SECRET)
                        .build()
                        .setFromTokenResponse(tokenResponse);

                PeopleService peopleService =
                        new PeopleService.Builder(httpTransport, jsonFactory, credential).build();

                try {
                    ListConnectionsResponse connections = peopleService.people().
                            connections()
                            .list("people/me")
                            .setPersonFields("names,emailAddresses")
                            .execute();
                        List<Person> friendList = connections.getConnections();
                    if (friendList != null) {
                        Log.i("NUM FRIENDS", "" + friendList.size());
                        friends = new ArrayList<User> (friendList.size());
                        String currID;
                        int substringIndex = 8; // cutting of "people/c"
                        for (int index = 0; index < friendList.size(); index++) {
                            currID = friendList.get(index).getResourceName().substring(substringIndex);
                            friends.add(new User(currID));
                        }
                    } else {
                        Log.i("FRIEND LIST CREATION", "NO FRIENDS FOUND");
                    }
                } catch (IOException e) {
                    Log.e("PEOPLE ERROR", e.getMessage());
                }
            }
            return null;
        }

        protected void onPostExecute(Void param) {
            for (int index = 0; index < friends.size(); index++) {
                Log.i("FRIEND LIST CREATION", "" + friends.get(index).getUserId());
            }
        }
    }

}
