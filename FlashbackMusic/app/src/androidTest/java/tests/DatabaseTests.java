package tests;

import android.util.Log;

import com.example.cse110.flashbackmusic.Callbacks.Callback;
import com.example.cse110.flashbackmusic.Callbacks.PlayInstancesCallback;
import com.example.cse110.flashbackmusic.DatabaseManager;
import com.example.cse110.flashbackmusic.LatLon;
import com.example.cse110.flashbackmusic.PlayInstance;
import com.example.cse110.flashbackmusic.Song;
import com.example.cse110.flashbackmusic.User;

import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void getAllThings () {
        User user = new User("Bob");
        LatLon location = new LatLon(1, 2);
        Calendar calendar = GregorianCalendar.getInstance();
        PlayInstance playInstance = new PlayInstance(user, location, calendar);

        Song song = new Song("Fried Rice; Media Right Productions; YouTube Audio Library; 0; 102; 0");
        final DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.storePlayInstance(playInstance, song);

        class PrintThing implements PlayInstancesCallback {
            public void onComplete(ArrayList<PlayInstance> playInstances) {
                Log.i("TESTING", "callback b");
                Log.i("TESTING", "" + playInstances.get(0).getTimeInMillis());
            }
        }

        databaseManager.getAllEntries(new PrintThing());
        Log.i("TESTING", "done");
    }
}
