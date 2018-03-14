package com.example.cse110.flashbackmusic;

import android.util.Log;

/**
 * Created by CubicDolphin on 3/3/18.
 */

public class User {
    String userId;

    public User (String userId) {
        this.userId = userId;
    }

    public String getUserId () {
        return userId;
    }

    public static String nameGenerator (String userId) {
        if (userId == "") return "Nully";

        String[] adjectives = {"Fluffy", "Cuddly", "Pink", "Adorable", "Cute", "Snuggly", "Lovely",
                "Wonderful", "Amazing", "Fantastic", "Purple"};
        String[] nouns = {"Puppy", "Kitty", "Fluffle", "Heart", "Friend", "Princess", "Bestie",
                "Hug", "Unicorn", "Sparkle", "Mermaid"};
        char[] userIdArr = userId.toCharArray();
        int total = 0;
        int prev = 1;
        for (int i = 0; i < userIdArr.length; i++) {
            total += prev * userIdArr[i];
            prev = userIdArr[i] + i;
        }
        int adjectiveIndex = total % 10;
        int nounIndex = (prev + userIdArr[0]) % 11;
        int nums = (total - 7 * prev) % 10000;

        return adjectives[adjectiveIndex] + nouns[nounIndex] + nums;
    }
}
