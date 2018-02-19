package tests;

import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.Song;

import org.junit.Assert;
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

}
