package com.example.cse110.flashbackmusic;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;

public class LatLon {
    private double latitude;
    private double longitude;

    public LatLon(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;

        if (lat < -90 || lat > 90)  {
            Log.e("LatLon Constructor", "the latitude is out of range");
            this.latitude = 0;
        }
        if (lon < -180 || lon > 180) {
            Log.e("LatLon Constructor", "the latitude is out of range");
            this.longitude = 0;
        }
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getAddressLine(Context context)
    {
        Log.i("LatLon getAddressLine", "Getting Address Line from LatLon");
        if (!Geocoder.isPresent()) { return ""; }
        try { return new Geocoder(context).getFromLocation(latitude, longitude, 1).get(0).getAddressLine(0);}
        catch (IOException e) { e.printStackTrace(); return ""; }
    }

    // Finds the approx distance in meters between two LatLocs
    public float findDistance(LatLon other) {
        if (other == null) {
            Log.e("LatLon findDistance", "Destination is null, returning 0 to caller");
            return 0;
        }

        Log.i("LatLon findDistance", "Calculating distance between two Location " +
        this.latitude + " " + this.longitude + " the other Location: " + other.getLatitude() + " "
        + other.getLongitude());

        Location locThis = new Location("Current location");
        locThis.setLatitude(this.getLatitude());
        locThis.setLongitude(this.getLongitude());
        Location locOther = new Location("Other location");
        locOther.setLatitude(other.getLatitude());
        locOther.setLongitude(other.getLongitude());
        return locThis.distanceTo(locOther);
    }
}
