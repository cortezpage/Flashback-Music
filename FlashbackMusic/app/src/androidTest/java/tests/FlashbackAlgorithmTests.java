package tests;

import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.Playlist;
import com.example.cse110.flashbackmusic.Song;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertEquals;

/**
 * Created by CubicDolphin on 2/9/18.
 */

public class FlashbackAlgorithmTests {
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
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(0, score);
    }

    @Test
    public void dislikedSongReturnsNeg1() {
        firstSong.setToDisliked();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date();
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(-1, score);
    }

    @Test
    public void sameTimePlayed() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0);
        Date songDate = new Date(fakeDate.getTime() + 86400000); // epoch plus one day
        Calendar songCal = new GregorianCalendar();
        songCal.setTime(songDate);
        firstSong.setLastPlayedCalendar(songCal);
        firstSong.setLastPlayedLocation(new LatLon(0, 180));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(300, score);
    }

    @Test
    public void sameLocation() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0);
        Date songDate = new Date(fakeDate.getTime() + 86400000 + 3600000 * 12); // Now plus a day and 12 hours
        Calendar songCal = new GregorianCalendar();
        songCal.setTime(songDate);
        firstSong.setLastPlayedCalendar(songCal);
        firstSong.setLastPlayedLocation(fakeLatLon);
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(301, score);
    }

    @Test
    public void sameDayOfWeek() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0); // Unix epoch
        Date songDate = new Date(86400000 * 7 + 3600000 * 12); // Epoch plus 7 days and 12 hours
        Calendar songCal = new GregorianCalendar();
        songCal.setTime(songDate);
        firstSong.setLastPlayedCalendar(songCal);
        firstSong.setLastPlayedLocation(new LatLon(0, 180));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(202, score);
    }

    @Test
    public void nonIdealSong() {
        firstSong.setToNeutral();
        LatLon fakeLatLon = new LatLon(0, 0);
        Date fakeDate = new Date(0); // Unix epoch
        Date songDate = new Date(86400000 + 3600000 * 12); // Epoch plus 1 day and 12 hours
        Calendar songCal = new GregorianCalendar();
        songCal.setTime(songDate);
        firstSong.setLastPlayedCalendar(songCal);
        firstSong.setLastPlayedLocation(new LatLon(0, 180));
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(0, score);
    }

    @Test
    public void idealSong() {
        firstSong.setToFavorite();
        LatLon fakeLatLon = new LatLon(32.867876, -117.223629);
        Date fakeDate = new Date(0); // Unix epoch
        Date songDate = new Date(604800000 + 7200000); // Epoch plus a week and two hours
        Calendar songCal = new GregorianCalendar();
        songCal.setTime(songDate);
        firstSong.setLastPlayedCalendar(songCal);
        firstSong.setLastPlayedLocation(new LatLon(32.867770, -117.224080)); // Near to fakeLatLon
        int score = playlist.findRank(firstSong, fakeLatLon, fakeDate);
        assertEquals(1106, score);
    }
}