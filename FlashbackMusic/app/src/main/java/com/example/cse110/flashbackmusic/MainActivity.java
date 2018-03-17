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
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasLocationPermission(this)) {
            Log.i("MainActivity Permission", "Missing permission to access the location");
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
            Log.i("MainActivity Request Permission Result", "Permission granted");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();}
            else {
                Log.i("MainActivity Request Permission Result", "Permission denied, asking again");
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

    public static boolean hasLocationPermission(Context context)
    {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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
