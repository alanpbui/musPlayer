package team20.flashbackmusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yujingwen199756 on 3/6/18.
 */

public class SortTitle implements Sort {
    @Override
    public void sortPlayList(List<Song> songList,List<String> songTitleList,List<String>songPlayingList) {

        for(int i = 0; i < songList.size(); i++){
            String temp = songList.get(i).getTitle();
            int index = i;

            for(int j = i; j < songList.size(); j++) {
                String currSong = songList.get(j).getTitle();
                if (temp.compareTo(currSong) > 0) {
                    temp = currSong;
                    index = j;
                }
            }
            Collections.swap(songList, i, index);
            Collections.swap(songTitleList, i, index);
            Collections.swap(songPlayingList, i, index);
        }
    }
}
