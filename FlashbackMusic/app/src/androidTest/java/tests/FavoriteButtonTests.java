package tests;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatImageButton;

import com.example.cse110.flashbackmusic.MainActivity;
import com.example.cse110.flashbackmusic.MusicPlayActivity;
import com.example.cse110.flashbackmusic.MusicPlayer;
import com.example.cse110.flashbackmusic.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.Assert.assertEquals;

/**
 * Created by CubicDolphin on 2/9/18.
 */

public class FavoriteButtonTests {
    private AppCompatImageButton favoriteButton;
    private MusicPlayer musicPlayer;
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
        favoriteButton = musicPlayActivity.getActivity().findViewById(R.id.button_like);
        musicPlayer = MainActivity.getMusicPlayer();
    }

    // Cycles song through all possible like statuses by clicking button
    @Test
    public void buttonChangesSongLikedStatus() {
        int prevStatus = musicPlayer.getCurrentLikeStatus();
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    favoriteButton.performClick();
                }
            });
        } catch (Throwable e) {
            assertEquals(0, 1);
        }

        int currentStatus = musicPlayer.getCurrentLikeStatus();

        if (prevStatus == 0) {
            assertEquals(1, currentStatus);
        } else if (prevStatus == 1) {
            assertEquals(2, currentStatus);
        } else if (prevStatus == 2) {
            assertEquals(0, currentStatus);
        }

        prevStatus = currentStatus;
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    favoriteButton.performClick();
                }
            });
        } catch (Throwable e) {
            assertEquals(0, 1);
        }
        currentStatus = musicPlayer.getCurrentLikeStatus();

        if (prevStatus == 0) {
            assertEquals(1, currentStatus);
        } else if (prevStatus == 1) {
            assertEquals(2, currentStatus);
        } else if (prevStatus == 2) {
            assertEquals(0, currentStatus);
        }

        prevStatus = currentStatus;
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    favoriteButton.performClick();
                }
            });
        } catch (Throwable e) {
            assertEquals(0, 1);
        }
        currentStatus = musicPlayer.getCurrentLikeStatus();

        if (prevStatus == 0) {
            assertEquals(1, currentStatus);
        } else if (prevStatus == 1) {
            assertEquals(2, currentStatus);
        } else if (prevStatus == 2) {
            assertEquals(0, currentStatus);
        }
    }


    // Cycles song through all possible like statuses by clicking button
    @Test
    public void statusMatchIcon() {
        int currentStatus = musicPlayer.getCurrentLikeStatus();
        Drawable check = musicPlayActivity.getActivity().getResources().getDrawable(R.drawable.neutral_button);
        Drawable cross = musicPlayActivity.getActivity().getResources().getDrawable(R.drawable.dislike_button);
        Drawable plus = musicPlayActivity.getActivity().getResources().getDrawable(R.drawable.favorite_button);

        if (currentStatus == 0) {
            assert(favoriteButton.getBackground() == check);
        } else if (currentStatus == 1) {
            assert(favoriteButton.getBackground() == plus);
        } else if (currentStatus == 2) {
            assert(favoriteButton.getBackground() == cross);
        }

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    favoriteButton.performClick();
                }
            });
        } catch (Throwable e) {
            assertEquals(0, 1);
        }
        currentStatus = musicPlayer.getCurrentLikeStatus();

        if (currentStatus == 0) {
            assert(favoriteButton.getBackground().hashCode() == check.hashCode());
        } else if (currentStatus == 1) {
            assert(favoriteButton.getBackground().hashCode() == plus.hashCode());
        } else if (currentStatus == 2) {
            assert(favoriteButton.getBackground().hashCode() == cross.hashCode());
        }

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    favoriteButton.performClick();
                }
            });
        } catch (Throwable e) {
            assertEquals(0, 1);
        }
        currentStatus = musicPlayer.getCurrentLikeStatus();

        if (currentStatus == 0) {
            assert(favoriteButton.getBackground().hashCode() == check.hashCode());
        } else if (currentStatus == 1) {
            assert(favoriteButton.getBackground().hashCode() == plus.hashCode());
        } else if (currentStatus == 2) {
            assert(favoriteButton.getBackground().hashCode() == cross.hashCode());
        }

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    favoriteButton.performClick();
                }
            });
        } catch (Throwable e) {
            assertEquals(0, 1);
        }
        currentStatus = musicPlayer.getCurrentLikeStatus();

        if (currentStatus == 0) {
            assert(favoriteButton.getBackground().hashCode() == check.hashCode());
        } else if (currentStatus == 1) {
            assert(favoriteButton.getBackground().hashCode() == plus.hashCode());
        } else if (currentStatus == 2) {
            assert(favoriteButton.getBackground().hashCode() == cross.hashCode());
        }
    }
}