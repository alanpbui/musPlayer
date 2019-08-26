package team20.flashbackmusic;

import android.location.Location;

import java.util.Date;
import java.util.HashSet;

/**
 * Created by dhoei on 08/03/18.
 */

public class SongData {

    private String title, artist, album,mostRecentDateTimeString,mostRecentLocationString;
    private Date mostRecentDateTime;
    private Location mostRecentLocation;
    private HashSet<Location> locationHistory;
    private HashSet<Integer> dayHistory;  // 1-Monday 2-Tuesday 3-Wednesday 4- Thursday 5-Friday 6-Saturday 7-Sunday
    private HashSet<Integer> timeHistory;   //1- morning  2-afternoon 3-evening
    private int songResId;
    private int score;
    private int status; //1 - favorite, 0 - neutral, -1 - dislike
    private long duration;

    public SongData(String title, String artist, String album, long duration, int id)
    {
        mostRecentDateTime = null;
        mostRecentLocation = null;

        //for status, 1 = favorite, 0 = neutral, -1 = dislike
        //initialize with neutral status
        status = 0;

        //initialize with score 0 because the music has not been
        //played
        score = 0;

        this.title = title;
        this.artist = artist;
        this.album = album;
        this.songResId = id;
        // Convert duration to time
        this.duration = duration;

        locationHistory=new HashSet<>();
        dayHistory = new HashSet<>();
        timeHistory= new HashSet<>();
    }
}
