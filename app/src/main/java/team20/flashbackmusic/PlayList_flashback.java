package team20.flashbackmusic;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by lenovo on 2018/2/7.
 */

public class PlayList_flashback implements Serializable {
    public ArrayList<String> sortingList;  //Use a TreeSet to store sorting songs
    public ArrayList<Song> songs;
    public Hashtable<String, Integer> indexTosong;
    public int needChange=0;      //Every time play from the start of the playlist, judge whether the songlist need to be sorted. 0 means no need,1 means need.
    public PlayList_flashback(ArrayList<String> sortingList, ArrayList<Song> songs, Hashtable<String, Integer> indexTosong) {    //constructor
        this.sortingList = sortingList;
        this.songs = songs;
        this.indexTosong = indexTosong;
    }
    public void sorter(){            //callback this function to sort the playlist
        Collections.sort(sortingList,new Comparator<String>() {
            @Override
            public int compare(String songname1, String songname2) {
                if(songname1.equals(songname2))
                    return 0;
                Song song1 = songs.get(indexTosong.get(songname1));
                Song song2 = songs.get(indexTosong.get(songname2));
                if(song2.getScore()!=song1.getScore())//sort songs with the score
                    return song2.getScore()-song1.getScore();            //If the scores are not the same, play the song with the higher score first
                else if(song1.getRecentDateTime()!=null&& song2.getRecentDateTime()!=null){
                    Date time1 = song1.getRecentDateTime();
                    Date time2 = song2.getRecentDateTime();
                    if(time1.after(time2))                     //If the scores are the same, play the song with the more recent play time first
                        return -1;
                    else
                        return 1;
                }
                else if(song1.getRecentDateTime()!=null&& song2.getRecentDateTime()==null){
                    return -1;
                }
                else if(song1.getRecentDateTime()==null&& song2.getRecentDateTime()!=null)
                    return 1;
                else
                    return 1;
            }
        });
        needChange = 0;     //no need to resort the play list
    }
    public void changeToDislike(int index){    //add to dislike, score set to -1, skip it
        Song song = songs.get(index);
        song.setStatus(-1);
        song.setScore(-1);
        needChange=1;   //need to resort the play list
    }

    public  void changeToNeutral(int index, Location location, int day, int time){   //change from dislike to neutral, calculate the score
        Song song = songs.get(index);
        song.setStatus(0);
        song.setScore(0);
        HashSet<Location> locationHistory = song.getLocationHistory();    //get location and time history of corresponding song
        HashSet<Integer> dayHistory = song.getDayHistory();
        HashSet<Integer> timeHistory = song.getTimeHistory();
        if (locationHistory.contains(location))            //If location is the same, score increase
            song.setScore(song.getScore()+1);
        if (timeHistory.contains(time))          //If time is the same, score increase
            song.setScore(song.getScore()+1);
        if (dayHistory.contains(day))    //If the song was played in the same day, score increase
            song.setScore(song.getScore()+1);
        needChange=1;        //need to resort the play list
    }

    public void changeToFavorite(int index){      //change from neutral to favorite, add 1 to score
        Song song = songs.get(index);
        song.setStatus(1);
        song.setScore(song.getScore()+2);
        needChange=1;       //need to resort the play list
    }
}
