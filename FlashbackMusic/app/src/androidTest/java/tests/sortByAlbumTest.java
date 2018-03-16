package tests;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.cse110.flashbackmusic.MainActivity;
import com.example.cse110.flashbackmusic.R;
import com.example.cse110.flashbackmusic.SongSelectionActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Created by hrchu on 3/13/2018.
 */
public class sortByAlbumTest {

    private Spinner spinner;
    private SpinnerAdapter sortAdapter;
    private ActivityTestRule<SongSelectionActivity> songSelectionActivity;
    View itemView;

    public Button [] sortedByAlbum;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){
        Intent intent = new Intent(mActivityTestRule.getActivity(), SongSelectionActivity.class);
        intent.putExtra("MODE", "song_selection");
        intent.putExtra("SELECTED_ID", "5");

        songSelectionActivity = new ActivityTestRule<>(SongSelectionActivity.class);;
        songSelectionActivity.launchActivity(intent);
        spinner = songSelectionActivity.getActivity().findViewById(R.id.drop_down);
        sortAdapter = spinner.getAdapter();
    }

    @Test
    public void sortByAlbum() {
        try {
            songSelectionActivity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            spinner.requestFocus();
                            itemView = sortAdapter.getView(3, null, spinner);
                            spinner.performItemClick(itemView, 3, spinner.getItemIdAtPosition(3));
                            Log.d("CLICKED",""+spinner.getItemAtPosition(3));
                            spinner.setSelection(3);
                        }
                    }
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        sortedByAlbum = songSelectionActivity.getActivity().getOriginalSongButtons();
    }
    
/*
    @Test
    public void sortByTitle() {
        try {
            songSelectionActivity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            spinner.requestFocus();
//                            spinner.performClick();
                            itemView = sortAdapter.getView(0, null, spinner);
                            spinner.performItemClick(itemView, 0, spinner.getItemIdAtPosition(0));
                            Log.d("CLICKEDTITLE", ""+spinner.getItemAtPosition(0));
                        }
                    }
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        songs = songSelectionActivity.getActivity().getSongButtons();
        assertEquals(songs[firstSongPosition].getText(), "All About Ronnie");
        assertEquals(songs[lastSongPosition].getText(), "Windows are the Eyes to the House");
    }*/

}
