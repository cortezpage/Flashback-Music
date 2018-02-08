package com.example.cse110.flashbackmusic;

public class Location {
    private float latitude;
    private float longitude;

    public Location(float lat, float lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    // Use the Google API to get the name of a location from the latitude and longitude
    // public String getLocationName();
}
