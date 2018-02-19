package tests;

import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.MainActivity;
import com.example.cse110.flashbackmusic.MusicPlayActivity;
import com.example.cse110.flashbackmusic.MusicPlayer;
import com.example.cse110.flashbackmusic.Playlist;
import com.example.cse110.flashbackmusic.R;
import com.example.cse110.flashbackmusic.SharedPrefHelper;
import com.example.cse110.flashbackmusic.Song;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Stacy on 2/18/18.
 */

public class SongTests {
    Song firstSong = new Song("SongName; Artist; Album; 250; 0; 123");
    Song emptySong = new Song(" ");

    @Test
    public void songConstructorTest() {
        assertEquals(firstSong.getSongName(), "SongName");
        assertEquals(firstSong.getAlbumName(), "Album");
        assertEquals(firstSong.getArtistName(), "Artist");
        assertEquals(firstSong.getLikeStatus(), 0);
        assertEquals(firstSong.getMediaID(), 123);
    }

    @Test
    public void emptySongConstructorTest() {
        assertEquals(emptySong.getSongName(), "Jazz in Paris");
        assertEquals(emptySong.getArtistName(), "Media Right Productions");
        assertEquals(emptySong.getAlbumName(), "YouTube Audio Library");
        assertEquals(emptySong.getLikeStatus(), 0);
        assertEquals(emptySong.getMediaID(), 2131427333);
    }


}
