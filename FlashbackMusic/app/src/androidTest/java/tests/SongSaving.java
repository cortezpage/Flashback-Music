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

import static junit.framework.Assert.assertEquals;

/**
 * Created by hrchu on 3/6/2018.
 */

public class SongSaving {

    //Set up dummy data to test song's data
    User user1 = new User("Tiffany");
    User user2 = new User("Jeff");
    LatLon location = new LatLon(1000, 2000);
    Calendar calendar = GregorianCalendar.getInstance();
    PlayInstance playInstance1 = new PlayInstance(user1, location, calendar);
    PlayInstance playInstance2 = new PlayInstance(user2, location, calendar);
    String jazz_url = "https://l.facebook.com/l.php?u=http%3A%2F%2Fsoundbible.com%2Fgrab.php%3Fid%3D2190%26type%3Dmp3&h=ATM6Ygy1p14XfGzRGDHGrrYAkLOK5DPugg9856ij4ukNNzd-xYsT0ycqiPuaa9ld2C3_IVpv8CjGgWjLrTSVU7xi4PmV-G7oJLDZ70OkTu8EMuO5PYYdZQaucL5zT73-FdezZQ";
    Song song = new Song("Jazz", jazz_url);

    DatabaseManager databaseManager = new DatabaseManager();
    ArrayList<PlayInstance> songPlayInstances;

    /**
     * Scenario 1 Test:
     * Test storing data to and retrieving data from Firebase
     */
    @Test
    public void savingSongData() {
        Log.d("TEST", "");
        //Store the song's playInstance in the database
        databaseManager.storePlayInstance(playInstance1, song);
        databaseManager.storePlayInstance(playInstance2, song);
        //Retrieve the song's data from the database
        databaseManager.getPlayInstances(song.getSongName(), new callItBack());
    }

    /**
     * Scenario 2 Test:
     * Test retrieving the last playInstance of a specific song
     */
    @Test
    public void accessUserId() {
        databaseManager.storePlayInstance(playInstance1, song);
        databaseManager.storePlayInstance(playInstance2, song);
        databaseManager.getPlayInstances(song.getSongName(), new callLastPlayInstance());
    }


    class callItBack implements PlayInstancesCallback {

        //Set up dummy data to test song's data
        User user = new User("John");
        LatLon location = new LatLon(1000, 2000);
        Calendar calendar = GregorianCalendar.getInstance();
        PlayInstance playInstance = new PlayInstance(user, location, calendar);

        @Override
        public void onComplete(ArrayList<PlayInstance> playInstances) {
            Log.d("test", "PLAYINSTANCE SIZE " + playInstances.size());

            if (playInstances != null) {
                for (int i = 0; i < playInstances.size(); i++) {
//                    Log.e("SAVED SONG", "" + playInstances.get(i).timeInMillis);
                    assertEquals(playInstances.get(i), playInstance);
                }
            } else Log.e("TESTING", "empty :(");
        }
    }

    //Call and print last playInstance info of a song
    class callLastPlayInstance implements PlayInstancesCallback {
        int i = 0;

        @Override
        public void onComplete(ArrayList<PlayInstance> playInstances) {

            if(playInstances != null) {
                i = playInstances.size() - 1;
                Log.d("LastPlayInstance_USER", "" + playInstances.get(i).userId);
                Log.d("LastPlayInstance_TIME", "" + playInstances.get(i).timeInMillis);
            }
        }
    }
}
