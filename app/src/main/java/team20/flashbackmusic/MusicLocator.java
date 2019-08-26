package team20.flashbackmusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by dhoei on 05/03/18.
 */

public class MusicLocator {
    private LocationManager locationManager;
    private MainActivity activity;


    // Constructor
    public MusicLocator(LocationManager locationManager, MainActivity activity) {
        this.locationManager = locationManager;
        this.activity = activity;
    }

    /**
     * This method get the current location of the user
     * @return the location of the user
     */
    public Location getCurrentLocation(){
        String bestProvider = locationManager.getBestProvider(new Criteria(),true);

        if (ActivityCompat.checkSelfPermission(this.activity,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1", "ins");
            return  null;
        }
        else {
            return locationManager.getLastKnownLocation(bestProvider);
        }
    }

    /**
     * store the location to the song input
     * @param song
     * @throws IOException
     */
    public void storeLocation(Song song) throws IOException {
        String bestProvider = locationManager.getBestProvider(new Criteria(),true);
        if (ActivityCompat.checkSelfPermission(this.activity,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            return;
        }
        else {
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if(location != null){

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();


                String finalString = getAddress(latitude, longitude);
                song.setMostRecentLocationString(finalString);
                song.setMostRecentLocation(location);

                if(!song.getLocationHistory().contains(location)){
                    Log.d("adding location:", "does not contain current location");
//                    Iterator<Location> itr = song.getLocationHistory().iterator();
//                    while(itr.hasNext()){
//                        if(itr.next().distanceTo(location) >= 305){
//                            song.addLocationHistory(location);
//                        }
//                    }
                    song.addLocationHistory(location);
                }
            }
        }
    }

    /**
     * Get address with given latitude and longitude
     * @param latitude
     * @param longitude
     * @return address string
     * @throws IOException
     */
    public String getAddress(double latitude, double longitude) throws IOException {
        List<Address> addresses = null;

        Log.d("getAddressLat", ""+latitude);
        Log.d("getAddressLong", ""+longitude);
        try {
            // Here 1 represent max location result to returned,
            // by documents it recommended 1 to 5
            addresses = new Geocoder(this.activity, Locale.getDefault()).
                    getFromLocation(latitude,longitude,1);

            // If any additional address line present than only, check with max available
            // address lines by getMaxAddressLineIndex()
            if(addresses.size() != 0){
                String address = addresses.get(0).getAddressLine(0);
                String state = addresses.get(0).getAdminArea();
                String finalString = address + ", "+state;
                return finalString;
            }
            else
                return "invalid location";

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
