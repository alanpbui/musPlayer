package team20.flashbackmusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yujingwen199756 on 3/6/18.
 */

public class SortStatus implements Sort {

    @Override
    public void sortPlayList(List<Song> songList,List<String> songTitleList,List<String>songPlayingList) {
        for(int i = 0; i < songList.size(); i++) {
            int temp = songList.get(i).getStatus();
            int index = i;
            for (int j = i; j < songList.size(); j++) {
                int currStatus = songList.get(j).getStatus();
                if (temp < currStatus) {
                    temp = currStatus;
                    index = j;
                }

            }
            Collections.swap(songList, i, index);
            Collections.swap(songTitleList, i, index);
            Collections.swap(songPlayingList, i, index);
        }
    }
}
