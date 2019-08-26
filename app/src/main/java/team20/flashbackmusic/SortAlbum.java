package team20.flashbackmusic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by yujingwen199756 on 3/6/18.
 */

public class SortAlbum implements Sort{
    @Override
    public void sortPlayList(List<Song> songList, List<String> songTitleList,List<String>songPlayingList) {
        for(int i = 0; i < songList.size(); i++){
            String temp = songList.get(i).getAlbum();
            int index = i;
            for(int j = i; j < songList.size(); j++){
                String currAlbum = songList.get(j).getAlbum();
                if(temp.compareTo(currAlbum) > 0){
                    temp = currAlbum;
                    index = j;
                }
            }
            Collections.swap(songList, i, index);
            Collections.swap(songTitleList, i, index);
            Collections.swap(songPlayingList, i, index);
        }
    }
}
