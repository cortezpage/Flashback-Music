package tests;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import com.example.cse110.flashbackmusic.MainActivity;
import com.example.cse110.flashbackmusic.MusicPlayActivity;
import com.example.cse110.flashbackmusic.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by hrchu on 3/15/2018.
 */

public class viewTrackListTests {

    private Button viewTracklistButton;
    public ActivityTestRule<MusicPlayActivity> musicPlayActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, false, true);

    @Before
    public void setup() {
        Intent intent = new Intent(mActivityTestRule.getActivity(), MusicPlayActivity.class);
        intent.putExtra("MODE", "song_selection");
        intent.putExtra("SELECTED_ID", "5");
        musicPlayActivity = new ActivityTestRule<>(MusicPlayActivity.class);;
        musicPlayActivity.launchActivity(intent);
        ///////////////// Change to viewTracklistButton ////////////////
        viewTracklistButton = musicPlayActivity.getActivity().findViewById(R.id.button_like);
    }

    @Test
    public void clickViewTracklistTest() {

        try {
            musicPlayActivity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                        viewTracklistButton.performClick();
                        }
                    }
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        ///////////// Test if tracklist's status is changed to view ///////////////////////
    }



}
