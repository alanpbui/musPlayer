package team20.flashbackmusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yujingwen199756 on 3/6/18.
 */

public class SortDefault implements Sort {
    @Override
    public void sortPlayList(List<Song> songList,List<String> songTitleList, List<String>songPlayingList) {
        for(int i = 0; i < songList.size(); i++) {
            if (songList.get(i).getMostRecentDateTime() != null) {
                Date temp = songList.get(i).getMostRecentDateTime();
                int index = i;
                for (int j = i; j < songList.size(); j++) {
                    if(songList.get(j).getMostRecentDateTime()!=null) {
                        Date currDate = songList.get(j).getMostRecentDateTime();
                        if (temp.before(currDate)) {
                            temp = currDate;
                            index = j;
                        }
                    }
                }
                Collections.swap(songList, i, index);
                Collections.swap(songTitleList, i, index);
                Collections.swap(songPlayingList, i, index);
            }
        }
    }
}
