package team20.flashbackmusic;

import android.location.Location;
import android.text.format.Time;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dhoei & chbernar on 16/02/18.
 */

public class SongClassTest {


    Location loc;
    Time time;
    Song testSong;

    @Before
    public void setup() {

        testSong = new Song("Test", "Hello", "Album", 132, 1234);
        loc = new Location("");
        time = new Time("lajolla");


    }
    @Test
    public void songConstructorAndGetterTest() throws Exception {

        assertEquals("Test", testSong.getTitle());
        assertEquals("Hello", testSong.getArtist());
        assertEquals("Album", testSong.getAlbum());
        assertEquals(1234, testSong.getSongResId());
        assertEquals(null, testSong.getRecentDateTime());
        assertEquals(null, testSong.getRecentLoc());
        assertEquals(0, testSong.getStatus());
        assertEquals(0, testSong.getScore());
    }

    @Test
    public void setterTest() throws Exception {

        testSong.setScore(20);
        testSong.setStatus(1);
        assertEquals(1, testSong.getStatus());
        assertEquals(20, testSong.getScore());
    }

    @Test
    public void testAddHistoryLocation()
    {
        //add random location
        testSong.addLocationHistory(loc);
        assertTrue(testSong.getLocationHistory().contains(loc));
    }

    @Test
    public void testAddHistoryTime()
    {

        //add random time
        testSong.addTimeHistory(time.hour);
        assertTrue(testSong.getTimeHistory().contains(time.hour));


    }

    @Test
    public  void testAddHistoryDay()
    {
        int monday = 1;
        int tuesday = 2;

        testSong.addDayHistory(monday);
        assertTrue(testSong.getDayHistory().contains(monday));

        testSong.addDayHistory(tuesday);
        assertTrue(testSong.getDayHistory().contains(tuesday));
    }

}
