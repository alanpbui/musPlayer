package team20.flashbackmusic;

import android.location.Location;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by dhoei on 08/03/18.
 */

public interface PlaylistFBM {
    void sorter();
    void changeToDislike(int index);
    void changeToNeutral(int index, Location location, int day, int time);
    void changeToFavorite(int index);

    ArrayList<String> getSortingList();
    void setSortingList(ArrayList<String> sortingList);
    ArrayList<Song> getSongs();
    void setSongs(ArrayList<Song> songs);
    Hashtable<String, Integer> getIndexTosong();
    void setIndexTosong(Hashtable<String, Integer> indexTosong);
    int getNeedChange();
    void setNeedChange(int needChange);

}
