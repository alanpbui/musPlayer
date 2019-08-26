package team20.flashbackmusic;

import android.location.Location;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by lenovo on 2018/2/17.
 */

public class testforscore {
    @Rule
    public ActivityTestRule<MainActivity> scoreActivityTestRule= new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void test(){
        Song song1=new Song("test1","a1","a1", 0,0);
        Song song2 = new Song("test2", "a2","a2",0,0);
        int day = 7;
        int time =1;

        song2.addDayHistory(day);
        song2.addTimeHistory(2);
        //song2.timeHistory.add(time);//score=2
        //song2.status=1;
        song1.addDayHistory(day);     //score=1
        song1.addTimeHistory(time);
        ArrayList<Song> songList=new ArrayList<>();
        songList.add(song1);
        songList.add(song2);
        ArrayList<String> titles = new ArrayList<>();
        titles.add(song1.getTitle());
        titles.add(song2.getTitle());
        Calendar time1=Calendar.getInstance();
        time1.set(Calendar.HOUR_OF_DAY,10);
        int hours=time1.get(Calendar.HOUR_OF_DAY);
        String s= time1.toString();
        Score scoretest= new Score(titles,songList);
        Location location=new Location("12");
        scoretest.score(location, day, time);

        assertEquals(2,song1.getScore());
        assertEquals(1,song2.getScore());


    }

}

