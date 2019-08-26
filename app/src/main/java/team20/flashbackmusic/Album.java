package team20.flashbackmusic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Created by dhoei on 13/02/18.
 */

public class Album {

    private ArrayList<Song> listOfTracks;
    private Queue<Song> queueTrackPlaying;
    private String name;


    public Album(String name)
    {
        this.name = name;
        listOfTracks = new ArrayList<>();
        queueTrackPlaying = new LinkedList<>();

    }

    public void addTrack(Song song)
    {
        listOfTracks.add(song);
    }


    public void queueAllSong()
    {
        for(int i = 0; i < listOfTracks.size(); i++)
        {
            queueTrackPlaying.add(listOfTracks.get(i));
        }
    }

    public void clearQueue()
    {
        queueTrackPlaying.clear();
    }


    public boolean isQueueEmpty()
    {
        return queueTrackPlaying.isEmpty();
    }

    public Song getNextSongToPlay()
    {
        if(queueTrackPlaying.isEmpty())
            throw new NullPointerException();
        return queueTrackPlaying.remove();
    }

    public Song getCurrentSongInQueue() {
        return queueTrackPlaying.peek();
    }

    public String toString()
    {
        return name;
    }

    public ArrayList<Song> getListOfTracks()
    {
        return listOfTracks;
    }

    public void setupAlbum()
    {
        clearQueue();
        //queue all songs to play in album
        queueAllSong();
    }

    public String getName() {
        return name;
    }
}