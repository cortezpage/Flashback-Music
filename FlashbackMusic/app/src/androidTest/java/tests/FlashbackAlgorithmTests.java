package tests;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatImageButton;

import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.MainActivity;
import com.example.cse110.flashbackmusic.MusicPlayActivity;
import com.example.cse110.flashbackmusic.MusicPlayer;
import com.example.cse110.flashbackmusic.Playlist;
import com.example.cse110.flashbackmusic.R;
import com.example.cse110.flashbackmusic.SharedPrefHelper;
import com.example.cse110.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.Assert.assertEquals;

/**
 * Created by CubicDolphin on 2/9/18.
 */

public class FlashbackAlgorithmTests {
    Song[] songs;
    Playlist playlist;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, false, true);

    @Before
    public void setup() {
        songs = mActivityTestRule.getActivity().getSongs();
        playlist = new Playlist();
    }

    @Test
    public void nonPlayedSongReturns0() {
        Song firstSong = songs[0];
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date();
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(0, score);
    }

    @Test
    public void dislikedSongReturnsNeg1() {
        Song firstSong = songs[0];
        firstSong.setToDisliked();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date();
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(-1, score);
    }

    @Test
    public void sameTimePlayed() {
        Song firstSong = songs[0];
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0);
        Date songDate = new Date(fakeDate.getTime() + 86400000); // epoch plus one day
        firstSong.setDate(songDate);
        firstSong.addLocation(new LatLon(0, 180));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(300, score);
    }

    @Test
    public void sameLocation() {
        Song firstSong = songs[0];
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0);
        Date songDate = new Date(fakeDate.getTime() + 86400000 + 3600000 * 12); // Now plus a day and 12 hours
        firstSong.setDate(songDate);
        firstSong.addLocation(fakeLatLon);
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(301, score);
    }

    @Test
    public void sameDayOfWeek() {
        Song firstSong = songs[0];
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0); // Unix epoch
        Date songDate = new Date(86400000 * 7 + 3600000 * 12); // Epoch plus 7 days and 12 hours
        firstSong.setDate(songDate);
        firstSong.addLocation(new LatLon(0, 180));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(202, score);
    }

    @Test
    public void nonIdealSong() {
        Song firstSong = songs[0];
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0); // Unix epoch
        Date songDate = new Date(86400000 + 3600000 * 12); // Epoch plus 1 day and 12 hours
        firstSong.setDate(songDate);
        firstSong.addLocation(new LatLon(0, 180));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(0, score);
    }

    @Test
    public void idealSong() {
        Song firstSong = songs[0];
        firstSong.setToFavorite();
        LatLon fakeLatLon = new LatLon(32.867876, -117.223629);
        Date fakeDate = new Date(0); // Unix epoch
        Date songDate = new Date(604800000 + 7200000); // Epoch plus a week and two hours
        firstSong.setDate(songDate);
        firstSong.addLocation(new LatLon(32.867770, -117.224080)); // Near to fakeLatLon
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(1106, score);
    }
}