package com.example.cse110.flashbackmusic;

import android.content.Context;
import android.location.Geocoder;

import java.io.IOException;

public class LatLon
{
    private double latitude;
    private double longitude;

    public LatLon(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getAddressLine(Context context)
    {
        if (!Geocoder.isPresent()) { return ""; }
        try { return new Geocoder(context).getFromLocation(latitude, longitude, 1).get(0).getAddressLine(0);}
        catch (IOException e) { e.printStackTrace(); return ""; }
    }
}
