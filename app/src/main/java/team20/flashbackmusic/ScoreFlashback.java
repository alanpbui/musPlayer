package team20.flashbackmusic;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lenovo on 2018/2/7.
 */

public class ScoreFlashback implements IScore {
    ArrayList<String> listSong;
    ArrayList<Song> songs;

    public ScoreFlashback(List<String> listSong, List<Song> songs) {   //add the tracks from the album to the score
        this.listSong =new ArrayList<String>(listSong);
        this.songs = new ArrayList<>(songs);
        //Create score system
    }

    //public void addSong(Song song){
    //  songList.add(song.songName);   // add song to the score system;
    // }
    public void score(Location location, int day, int time) {
        int len = listSong.size();// real-time location and time
        for (int i = 0; i < len; i++) {
            String name = listSong.get(i);
            Song song = songs.get(i);
            song.setScore(0);
            HashSet<Location> locationHistory = song.getLocationHistory();    //get location and time history of corresponding song
            HashSet<Integer> dayHistory = song.getDayHistory();
            HashSet<Integer> timeHistory = song.getTimeHistory();
            int status = song.getStatus();
            if (locationHistory.contains(location))          //If location is the same, score increase
                song.setScore(song.getScore()+1);
            else{
                Iterator<Location> itr = locationHistory.iterator();
                while (itr.hasNext()) {
                    if (itr.next().distanceTo(location) <= 305) {
                        song.setScore(song.getScore()+1);
                    }
                }
            }
            if (timeHistory.contains(time))          //If time is the same, score increase
                song.setScore(song.getScore()+1);
            if (dayHistory.contains(day))    //If the song was played in the same day, score increase
                song.setScore(song.getScore()+1);
            if (status == 1)                 //If the song is favorite, score increase
                song.setScore(song.getScore()+2);
            else if (status == -1)           //If the song is disliked, not play it
                song.setScore(-1);
        }
    }
}
