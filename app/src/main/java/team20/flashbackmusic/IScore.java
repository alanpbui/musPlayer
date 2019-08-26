package team20.flashbackmusic;

import android.location.Location;

/**
 * Created by christhoperbernard on 04/03/18.
 */

public interface IScore {
    void score(Location location, int day, int time);
}
