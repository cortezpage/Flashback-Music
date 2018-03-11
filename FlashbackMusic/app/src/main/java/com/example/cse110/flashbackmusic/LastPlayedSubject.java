package com.example.cse110.flashbackmusic;

/**
 * Created by CubicDolphin on 3/11/18.
 */

public interface LastPlayedSubject {
    public void addListener (LastPlayedObserver lastPlayedObserver);
    public void removeListener (LastPlayedObserver lastPlayedObserver);
    public void removeAllListeners ();
    public void callListeners (Song song);
}
