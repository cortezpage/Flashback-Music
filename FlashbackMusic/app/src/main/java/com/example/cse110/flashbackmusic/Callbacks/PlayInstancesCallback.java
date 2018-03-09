package com.example.cse110.flashbackmusic.Callbacks;

import com.example.cse110.flashbackmusic.PlayInstance;

import java.util.ArrayList;

/**
 * Created by CubicDolphin on 3/6/18.
 */

public interface PlayInstancesCallback {
    public void onComplete(ArrayList<PlayInstance> playInstances);
}
