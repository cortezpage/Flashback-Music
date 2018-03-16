package tests;

import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.PlayInstance;
import com.example.cse110.flashbackmusic.Playlist;
import com.example.cse110.flashbackmusic.Song;
import com.example.cse110.flashbackmusic.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertEquals;

/**
 * Created by CubicDolphin on 2/9/18.
 */

public class VibeAlgorithmTests {
    Song firstSong;
    Playlist playlist;

    @Before
    public void setup() {
        firstSong = new Song("Jazz in Paris; Media Right Productions; YouTube Audio Library; 102; 0; 2131427333", "");
        playlist = new Playlist(false);
    }

    @Test
    public void nonPlayedSongReturns0() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<PlayInstance> playInstances = new ArrayList<>();
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate, users, playInstances);
        assertEquals(0, score);
    }

    @Test
    public void dislikedSongReturnsNeg1() {
        firstSong.setToDisliked();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<PlayInstance> playInstances = new ArrayList<>();
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate, users, playInstances);
        assertEquals(-1, score);
    }

    @Test
    public void sameWeek() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(23812938);
        ArrayList<User> users = new ArrayList<>();
        ArrayList<PlayInstance> playInstances = new ArrayList<>();
        Calendar prevCal = Calendar.getInstance();
        prevCal.setTimeInMillis(fakeDate.getTime() - 1000);
        playInstances.add(new PlayInstance(new User("Bob"), new LatLon(0, 180), prevCal));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate, users, playInstances);
        assertEquals(101, score);
    }

    @Test
    public void sameLocation() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(999999999);
        ArrayList<User> users = new ArrayList<>();
        ArrayList<PlayInstance> playInstances = new ArrayList<>();
        Calendar prevCal = Calendar.getInstance();
        prevCal.setTimeInMillis(0);
        playInstances.add(new PlayInstance(new User("Bob"), new LatLon(0, 0), prevCal));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate, users, playInstances);
        assertEquals(102, score);
    }

    @Test
    public void playedByFriend() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(999999999);
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Bob"));
        ArrayList<PlayInstance> playInstances = new ArrayList<>();
        Calendar prevCal = Calendar.getInstance();
        prevCal.setTimeInMillis(0);
        playInstances.add(new PlayInstance(new User("Bob"), new LatLon(0, 180), prevCal));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate, users, playInstances);
        assertEquals(100, score);
    }

    @Test
    public void nonIdealSong() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(999999999);
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Joe"));
        ArrayList<PlayInstance> playInstances = new ArrayList<>();
        Calendar prevCal = Calendar.getInstance();
        prevCal.setTimeInMillis(0);
        playInstances.add(new PlayInstance(new User("Bob"), new LatLon(0, 180), prevCal));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate, users, playInstances);
        assertEquals(0, score);
    }

    @Test
    public void idealSong() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0);
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Joe"));
        users.add(new User("Bob"));
        ArrayList<PlayInstance> playInstances = new ArrayList<>();
        Calendar prevCal = Calendar.getInstance();
        prevCal.setTimeInMillis(0);
        playInstances.add(new PlayInstance(new User("Bob"), new LatLon(0, 0), prevCal));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate, users, playInstances);
        assertEquals(303, score);
    }
}