package tests;

import android.location.Location;

import com.example.cse110.flashbackmusic.LatLon;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Stacy on 2/18/18.
 */

public class LatLonTests {

    @Test
    public void toDistanceDifferentLatLon () {
        LatLon ll1 = new LatLon(20.0, 20.0);
        LatLon ll2 = new LatLon(20.0, 40.0);
        Location loc1 = new Location("spot1");
        Location loc2 = new Location("spot2");
        loc2.setLatitude(20.0);
        loc2.setLongitude(20.0);
        loc1.setLatitude(20.0);
        loc1.setLongitude(40.0);
        float result = loc1.distanceTo(loc2);

        assertEquals(ll1.findDistance(ll2), result);
        assertEquals(ll2.findDistance(ll1), result);
    }

    @Test
    public void toDistanceSameLatLon () {
        LatLon ll1 = new LatLon(20.0, 20.0);
        LatLon ll2 = new LatLon(45.4, 20.384);

        assertEquals(ll2.findDistance(ll2), (float) 0.0);
        assertEquals(ll1.findDistance(ll1), (float) 0.0);
    }

    @Test
    public void toDistanceNullPointer () {
        LatLon ll1 = new LatLon(20.0, 20.0);
        assertEquals(ll1.findDistance(null), (float) 0.0);
    }

    @Test
    public void createLatLonLatOutOfRange () {
        LatLon llLoutRange = new LatLon(-100.0, 100.0);
        LatLon llLoutRange2 = new LatLon(100.0, 100.0);

        assertEquals(llLoutRange.getLatitude(), 0.0);
        assertEquals(llLoutRange2.getLatitude(), 0.0);
    }

    @Test
    public void createLatLonLonOutOfRange () {
        LatLon llLonOutRange = new LatLon(0.0, -200.0);
        LatLon llLonOutRange2 = new LatLon(0.0, 200.0);

        assertEquals(llLonOutRange.getLongitude(), 0.0);
        assertEquals(llLonOutRange2.getLongitude(), 0.0);
    }

    @Test
    public void createLatLonBothOutOfRange () {
        LatLon llOutRange = new LatLon(-100.0, -200.0);
        LatLon llOutRange2 = new LatLon(100.0, 200.0);

        assertEquals(llOutRange.getLongitude(), 0.0);
        assertEquals(llOutRange.getLatitude(), 0.0);

        assertEquals(llOutRange2.getLatitude(), 0.0);
        assertEquals(llOutRange2.getLongitude(), 0.0);
    }
}
