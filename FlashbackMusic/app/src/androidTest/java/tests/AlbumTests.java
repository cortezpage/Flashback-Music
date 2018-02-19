package tests;

import com.example.cse110.flashbackmusic.Album;
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

public class AlbumTests {
    Album firstAlbum = new Album("AlbumName; Artist; 2; ALBUM_10");
    Album secondAlbum = new Album("Latte; Coffee; 26; ALBUM_110");
    Album emptyAlbum  = new Album(" ");

    @Test
    public void firstAlbumConstructorTest() {
        assertEquals(firstAlbum.getAlbumName(), "AlbumName");
        assertEquals(firstAlbum.getNumTracks(), 2);
        assertEquals(firstAlbum.getID(), "ALBUM_10");
    }

    @Test
    public void secondAlbumConstructorTest() {
        assertEquals(secondAlbum.getAlbumName(), "Latte");
        assertEquals(secondAlbum.getNumTracks(), 26);
        assertEquals(secondAlbum.getID(), "ALBUM_110");
    }

    @Test
    public void emptyAlbumConstructorTest() {
        assertEquals(emptyAlbum.getAlbumName(), "YouTube Audio Library");
        assertEquals(emptyAlbum.getNumTracks(), 1);
        assertEquals(emptyAlbum.getID(), "ALBUM_6");
    }

    @Test
    public void addSongAndGetSongID() {
        Song JazzInParis = new Song("");
        Song AAR = new Song("All About Ronnie; Rebecca Sayre; This is Always; 269; 0");

        firstAlbum.addSong(JazzInParis, 0);
        firstAlbum.addSong(AAR, 1);
        assertEquals(firstAlbum.getCurrSongID(), 2131427333);
        firstAlbum.toNextSong();
        assertEquals(firstAlbum.getCurrSongID(), AAR.getMediaID());
        firstAlbum.toNextSong();
        assertEquals(firstAlbum.getCurrSongID(), 2131427333);

    }

    @Test
    public void nextSongOverBound() {
        Song JazzInParis = new Song("");
        Song AAR = new Song("All About Ronnie; Rebecca Sayre; This is Always; 269; 0");

        firstAlbum.addSong(JazzInParis, 0);
        firstAlbum.addSong(AAR, 1);
        firstAlbum.toNextSong();
        firstAlbum.toNextSong();
        firstAlbum.toNextSong();
        assertEquals(firstAlbum.getCurrSongID(), 2131427333);

    }

    @Test
    public void addSongAndPreviousSong() {
        Song JazzInParis = new Song("");
        Song AAR = new Song("All About Ronnie; Rebecca Sayre; This is Always; 269; 0");

        firstAlbum.addSong(JazzInParis, 0);
        firstAlbum.addSong(AAR, 1);
        firstAlbum.toNextSong();
        firstAlbum.toPreviousSong();
        assertEquals(firstAlbum.getCurrSongID(), 2131427333);
    }

    @Test
    public void addSongAndPreviousSongOverBound () {
        Song JazzInParis = new Song("");
        Song AAR = new Song("All About Ronnie; Rebecca Sayre; This is Always; 269; 0");

        firstAlbum.addSong(JazzInParis, 0);
        firstAlbum.addSong(AAR, 1);
        firstAlbum.toPreviousSong();
        firstAlbum.toPreviousSong();
        assertEquals(firstAlbum.getCurrSongID(), 2131427333);

    }

}
