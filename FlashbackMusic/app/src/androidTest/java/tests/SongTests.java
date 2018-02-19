package tests;

import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.MainActivity;
import com.example.cse110.flashbackmusic.MusicPlayActivity;
import com.example.cse110.flashbackmusic.MusicPlayer;
import com.example.cse110.flashbackmusic.Playlist;
import com.example.cse110.flashbackmusic.R;
import com.example.cse110.flashbackmusic.SharedPrefHelper;
import com.example.cse110.flashbackmusic.Song;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Stacy on 2/18/18.
 */

public class SongTests {
    Song firstSong = new Song("SongName; Artist; Album; 250; 0; 123");
    Song secondSong = new Song("Chocolate; Candy; Lollipop; 27; 1; 33445");
    Song emptySong = new Song(" ");

    @Test
    public void firstsongConstructorTest() {
        assertEquals(firstSong.getSongName(), "SongName");
        assertEquals(firstSong.getAlbumName(), "Album");
        assertEquals(firstSong.getArtistName(), "Artist");
        assertEquals(firstSong.getLikeStatus(), 0);
        assertEquals(firstSong.getMediaID(), 123);
    }

    @Test
    public void secondsongConstructorTest() {
        assertEquals(secondSong.getSongName(), "Chocolate");
        assertEquals(secondSong.getAlbumName(), "Lollipop");
        assertEquals(secondSong.getArtistName(), "Candy");
        assertEquals(secondSong.getLikeStatus(), 1);
        assertEquals(secondSong.getMediaID(), 33445);
    }

    @Test
    public void emptySongConstructorTest() {
        assertEquals(emptySong.getSongName(), "Jazz in Paris");
        assertEquals(emptySong.getArtistName(), "Media Right Productions");
        assertEquals(emptySong.getAlbumName(), "YouTube Audio Library");
        assertEquals(emptySong.getLikeStatus(), 0);
        assertEquals(emptySong.getMediaID(), 2131427333);
    }

    @Test
    public void increamentLikeStatusTest() {
        assertEquals(firstSong.getLikeStatus(), 0);
        firstSong.incrementLikeStatus();
        assertEquals(firstSong.getLikeStatus(), 1);
        firstSong.incrementLikeStatus();
        assertEquals(firstSong.getLikeStatus(), 2);
        firstSong.incrementLikeStatus();
        assertEquals(firstSong.getLikeStatus(), 0);
        firstSong.incrementLikeStatus();
        assertEquals(firstSong.getLikeStatus(), 1);
    }

    @Test
    public void addLocationTest () {
        assertEquals(secondSong.getLatLons().size(), 0);
        LatLon latlon = new LatLon(32.867876, -117.223629);
        secondSong.addLocation(latlon);
        assertEquals(secondSong.getLatLons().size(), 1);
        Assert.assertTrue(secondSong.getLatLons().contains(latlon));


        assertEquals(secondSong.getLatLons().size(), 1);
        LatLon latlon2 = new LatLon(87.3, -1.2);
        secondSong.addLocation(latlon2);
        assertEquals(secondSong.getLatLons().size(), 2);
        Assert.assertTrue(secondSong.getLatLons().contains(latlon2));
    }

    @Test
    public void setPlayedAtTimeTest () {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 18, 18, 5, 20);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2018, 2, 18, 18, 10, 20);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2018, 2, 18, 19, 5, 20);

        Calendar fakeCalendar = Calendar.getInstance();
        fakeCalendar.set(2018, 2, 18, 8, 5, 20);
        Calendar fakeCalendar2 = Calendar.getInstance();
        fakeCalendar2.set(2018, 2, 18, 20, 5, 20);

        firstSong.setPlayedAtTime(calendar);

        /*Assert.assertTrue(firstSong.playedAtTime(calendar));
        Assert.assertTrue(firstSong.playedAtTime(calendar2));
        Assert.assertTrue(firstSong.playedAtTime(calendar3));

        Assert.assertFalse(firstSong.playedAtTime(fakeCalendar));
        Assert.assertFalse(firstSong.playedAtTime(fakeCalendar2));*/
    }

    @Test
    public void wasPrevioueslyPlayedTest () {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 2, 18, 18, 5, 20);

        firstSong.setPlayedAtTime(calendar);
    }

}
