package team20.flashbackmusic;

import android.location.Location;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2018/2/7.
 */

public class Song{
    private String title, artist, album,mostRecentDateTimeString,mostRecentLocationString;
    private Date mostRecentDateTime;
    private Location mostRecentLocation; //don’t know yet
    private HashSet<Location> locationHistory;
    private HashSet<Integer> dayHistory;  // 1-Monday 2-Tuesday 3-Wednesday 4- Thursday 5-Friday 6-Saturday 7-Sunday
    private HashSet<Integer> timeHistory;   //1- morning  2-afternoon 3-evening
    private int songResId;
    private int score;
    private int status; //1 - favorite, 0 - neutral, -1 - dislike
    private long duration;

    //constructor of song
    public Song(String title, String artist, String album, long duration, int id) {
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


    // GETTER AND SETTER METHOD //
    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getAlbum() { return album; }

    public Date getRecentDateTime()
    {
        return mostRecentDateTime;
    }

    public Location getRecentLoc()
    {
        return mostRecentLocation;
    }

    public int getStatus()
    {
        return status;
    }

    public int getScore()
    {
        return score;
    }

    public int getSongResId(){return songResId;}

    public HashSet<Location> getLocationHistory(){
        return locationHistory;
    }

    public HashSet<Integer> getDayHistory(){
        return dayHistory;
    }

    public HashSet<Integer> getTimeHistory(){
        return timeHistory;
    }


    public void setScore(int s) { score = s; }

    public void setStatus(int s) { status = s; }

    public void setMostRecentDateTime(Time time){
        mostRecentDateTime = time;
    }
    public void addLocationHistory(Location location){
        locationHistory.add(location);
    }

    public void addDayHistory(int day){
        dayHistory.add(day);
    }

    public void addTimeHistory(int time){
        timeHistory.add(time);
    }
    public String getMostRecentDateTimeString(){
        return this.mostRecentDateTimeString;
    }
    public String getMostRecentLocationString(){
        return this.mostRecentLocationString;
    }
    public void setMostRecentDateTimeString(String mostRecentDateTimeString){
        this.mostRecentDateTimeString = mostRecentDateTimeString;
    }
    public void setMostRecentLocationString(String mostRecentLocationString){
        this.mostRecentLocationString = mostRecentLocationString;
    }

    public Date getMostRecentDateTime(){
        return this.mostRecentDateTime;
    }
    public Location getMostRecentLocation(){
        return this.mostRecentLocation;
    }
    public void setMostRecentDateTime(Date mostRecentDateTime){
        this.mostRecentDateTime = mostRecentDateTime;
    }
    public void setMostRecentLocation(Location location){
        this.mostRecentLocation = location;
    }
}