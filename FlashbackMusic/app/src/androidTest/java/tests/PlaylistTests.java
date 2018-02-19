package tests;

import android.support.test.rule.ActivityTestRule;

import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.MainActivity;
import com.example.cse110.flashbackmusic.Playlist;
import com.example.cse110.flashbackmusic.Song;


import org.junit.Before;
import org.junit.Rule;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Stacy on 2/18/18.
 */

public class PlaylistTests {
    Song[] songs;
    Playlist playlist;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class, false, true);

    @Before
    public void setup() {

    }
}
