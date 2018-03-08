package tests;

import com.example.cse110.flashbackmusic.DatabaseManager;
import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.PlayInstance;
import com.example.cse110.flashbackmusic.Song;
import com.example.cse110.flashbackmusic.User;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by CubicDolphin on 3/5/18.
 */

public class DatabaseTests {
    @Test
    public void addPlayInstance () {
        User user = new User("Bob");
        LatLon location = new LatLon(1, 2);
        Calendar calendar = GregorianCalendar.getInstance();
        PlayInstance playInstance = new PlayInstance(user, location, calendar);

        Song song = new Song("Jazz in Paris; Media Right Productions; YouTube Audio Library; 102; 0", "");
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.storePlayInstance(playInstance, song);
        assert(true);
    }
}
