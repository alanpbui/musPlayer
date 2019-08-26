package team20.flashbackmusic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by dhoei on 16/02/18.
 */

public class AlbumClassTest {


    ArrayList<Song> songList = new ArrayList<Song>();
    Album albumTest;

    @Before
    public void setup() {

        Song song1 = new Song("Amass", "foo", "car", 10, 200);
        Song song2 = new Song("Github", "bar", "car", 12, 5);
        Song song3 = new Song("Short", "foobar", "car", 15, 10);
        Song song4 = new Song("Long", "foolar", "car", 20, 23);

        albumTest = new Album("Yellow");

        // Put into song list for check
        songList.add(song1);
        songList.add(song2);
        songList.add(song3);
        songList.add(song4);


        // Put into album class
        albumTest.addTrack(song1);
        albumTest.addTrack(song2);
        albumTest.addTrack(song3);
        albumTest.addTrack(song4);

        // Queue all songs
        albumTest.queueAllSong();

    }

    @Test
    public void constructorCheck() {
        assertEquals("Yellow", albumTest.toString());
    }

    @Test
    public void queueShouldNotBeEmptyCheck() {
        assertEquals(false, albumTest.isQueueEmpty());
    }

    @Test
    public void clearQueueCheck() {
        albumTest.clearQueue();
        assertEquals(true, albumTest.isQueueEmpty());
    }

    @Test
    public void nextSongPlayCheck() {
        // When function nextSong() called
        // must be playing some other song.

        while (!albumTest.isQueueEmpty()) {
            Song temp = albumTest.getCurrentSongInQueue();
            albumTest.getNextSongToPlay();
            assertNotEquals(temp, albumTest.getCurrentSongInQueue());
        }
    }

    @Test
    public void queueAllSongCheck() {
        Song songInList;
        Song songInQueue;

        for (int i = 0; i < songList.size(); i++) {
            songInList = songList.get(i);
            songInQueue = albumTest.getCurrentSongInQueue();
            assertEquals(songInList, songInQueue);

            albumTest.getNextSongToPlay();
        }
    }

}
