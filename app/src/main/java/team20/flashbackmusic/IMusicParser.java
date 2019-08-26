package team20.flashbackmusic;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by dhoei on 05/03/18.
 */

public interface IMusicParser {
    void getMusic(List songList, List songTitleList);
    void populateAlbum(List<Song> songListObj, Hashtable<String, Album> albumList);
}
