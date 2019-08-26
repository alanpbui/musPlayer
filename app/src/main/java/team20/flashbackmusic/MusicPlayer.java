package team20.flashbackmusic;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import java.io.IOException;

/**
 * Created by dhoei on 05/03/18.
 */

public class MusicPlayer {

    private MainActivity activity;
    private MediaPlayer mediaPlayer;
    private MusicLocator musicLocator;
    private Album albumToPlay;
    private FloatingActionButton playButton;

    public MusicPlayer(MediaPlayer mp, MainActivity activity,
                       MusicLocator musicLocator, Album albumToPlay,
                       FloatingActionButton playButton) {

        this.mediaPlayer = mp;
        this.activity = activity;
        this.musicLocator = musicLocator;
        this.albumToPlay = albumToPlay;
        this.playButton = playButton;
    }

    /** This play the current all the current album songs
     *  PRECONDITION: album has at least 1 song
     */
    public void playAlbum()
    {
        Song songToPlay = albumToPlay.getNextSongToPlay();

        int resID = songToPlay.getSongResId();
        playMusicId(resID);


        Log.d("album play", "playing next song in the album");

        //set button tag to pause
        changeToPauseButton();

        //set the current playing song view, location, date and time
        String currentPlayingSongDisplay = songToPlay.getTitle() + " - " + songToPlay.getArtist();
        activity.setNowPlayingView(currentPlayingSongDisplay);
        activity.showCurrentLocation(songToPlay);
        activity.showDateAndTime(songToPlay);

        //activity.showFriendOrRabbit(songToPlay);
        //activity.updateFireBaseInfo(songToPlay);

        //set media player listener
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("onCompletionCalled", "yes!");

                //check if there is any song to play left in album
                if (!albumToPlay.isQueueEmpty()) {
                    playAlbum();
                }
                //if there is no more album to play, go to waiting state
                else
                {
                    Log.d("playing album:", "Empty queue");
                    //set button tag to play
                    changeToPlayButton();
                }


            }
        });

    }

    public void playMusicId(int resID) {
        releaseMusicPlayer();
        mediaPlayer = MediaPlayer.create(activity, resID);
        mediaPlayer.start();

        //after music start, change the button into pause button
        changeToPauseButton();
    }

    public void playMusicUri(Uri uri){

        releaseMusicPlayer();
        mediaPlayer = MediaPlayer.create(activity, uri);
        mediaPlayer.start();

        //after music start, change the button into pause button
        changeToPauseButton();
    }

    public void releaseMusicPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playingAndPausing() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                changeToPlayButton();

            } else {
                mediaPlayer.start();
                changeToPauseButton();

            }
        } else {
            //when there is no music is selected, change play and pause accordingly
            if (playButton.getTag().equals(R.drawable.ic_play_arrow_black_24dp))
            {
                changeToPauseButton();
            }
            else
            {
                changeToPlayButton();
            }
        }
    }

    /** This changes the pause button into play button
     *
     */
    public void changeToPlayButton() {
        playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        playButton.setTag(R.drawable.ic_play_arrow_black_24dp);
    }

    /** This changes the play button into pause button
     *
     */
    public void changeToPauseButton() {
        playButton.setImageResource(R.drawable.ic_pause_black_24dp);
        playButton.setTag(R.drawable.ic_pause_black_24dp);
    }

    public void setAlbumToPlay(Album albumToPlay)
    {
        this.albumToPlay = albumToPlay;
    }

    public void setMusicLocator(MusicLocator locator)
    {
        this.musicLocator = locator;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void next(int index) {



//        int resID = activity.getResources().getIdentifier(playlist.get(index),
//                "raw", activity.getPackageName());
//        mediaPlayer = MediaPlayer.create(activity, resID);
//        mediaPlayer.start();
//
//        changeToPauseButton();
//
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                next(playlist);
//            }
//        });
    }

    public void stop(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        releaseMusicPlayer();
    }
}
