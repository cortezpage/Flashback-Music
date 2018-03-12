package com.example.cse110.flashbackmusic;

import java.util.ArrayList;

/**
 * Created by CubicDolphin on 3/11/18.
 */

public class LastPlayedController implements LastPlayedSubject {
    private static LastPlayedController lastPlayedController;

    public static LastPlayedController getInstance () {
        if (LastPlayedController.lastPlayedController == null) {
            LastPlayedController.lastPlayedController = new LastPlayedController();
        }

        return LastPlayedController.lastPlayedController;
    }

    private ArrayList<LastPlayedObserver> lastPlayedObservers;

    public LastPlayedController () {
        lastPlayedObservers = new ArrayList<LastPlayedObserver>();
    }

    public void addListener (LastPlayedObserver lastPlayedObserver) {
        lastPlayedObservers.add(lastPlayedObserver);
    }

    public void removeListener (LastPlayedObserver lastPlayedObserver) {
        lastPlayedObservers.remove(lastPlayedObserver);
    }

    public void callListeners (Song song) {
        if (lastPlayedObservers == null) return;
        for (int i = 0; i < lastPlayedObservers.size(); i++) {
            lastPlayedObservers.get(i).lastPlayedUpdate(song);
        }
    }

    public void removeAllListeners () {
        lastPlayedObservers = new ArrayList<LastPlayedObserver>();
    }
}
