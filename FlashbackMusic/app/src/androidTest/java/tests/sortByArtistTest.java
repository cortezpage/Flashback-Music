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
 * Created by hrchu on 3/16/2018.
 */

public class sortByArtistTest {

    private Spinner spinner;
    private SpinnerAdapter sortAdapter;
    private ActivityTestRule<SongSelectionActivity> songSelectionActivity;
    View itemView;

    public Button[] sortedByAlbum;

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
    public void sortByArtist(){
        try {
            songSelectionActivity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            spinner.requestFocus();
                            itemView = sortAdapter.getView(2, null, spinner);
                            spinner.performItemClick(itemView, 2, spinner.getItemIdAtPosition(2));
                            Log.d("CLICKED",""+spinner.getItemAtPosition(2));
                            spinner.setSelection(2);
                        }
                    }
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        sortedByAlbum = songSelectionActivity.getActivity().getOriginalSongButtons();
    }

}
