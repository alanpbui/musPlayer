package team20.flashbackmusic;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements LocationListener {

    //initialize current index playing song
    private int currentIndex = 0;

    // List of songs
    private ListView listView;

    // Mechanism that plays songs
    private MediaPlayer mediaPlayer;
    private MusicPlayer player;

    // Buttons that the user can interact with
    private FloatingActionButton  playButton, nextSong, previousSong,
                showCurrentPlaylist;
    private Switch flashback;
    private Button sortButton;

    // Song's status kept through a numerical value
    private IScore score;

    // Acquires the music within the phone's memory
    private LocalMusicParser musicParser;
    private MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    // Playlist of the flashback mode
    private PlaylistFBM playlist_FB;

    // List of albums view
    private GridView albumView;
    private ListAdapter albumAdapter;

    // Indicator of whether flashback mode is on/off
    private boolean flashOn = false;

    //TextViews of this activity
    private TextView nowPlayingView, locationView, dateTimeView; //durationView;

    //list of songs according to purpose
    private List<String> songList; //to play music
    private List<String> songTitleList; //for display
    private List<Song> songListObj; //for storage of songs

    //data for playlist_FB of song
    private ArrayList<String> sortingList;
    private Hashtable<String, Integer> indexTosong;

    //Create list of albums
    private Hashtable<String, Album> albumList; //for checking if album exist
    private ArrayList<Album> tempListAlbum;

    // Album variables
    private boolean playingAlbumFlag = false;
    private Album albumToPlay;

    // Location variables and variables that overall contribute to song's score
    private LocationManager locationManager;
    private int currentUserMNEIndex = -1;
    private int currentUserDayOfWeek = -1;
    private Location currentUserlocation;

    // Adapt view of list by adding status of songs in picture format
    private MyAdapter adapter;

    private MusicLocator musicLocator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createButton();

        //setup media player
        mediaPlayer = new MediaPlayer();

        // Set up mechanism to actually play the songs
        player = new MusicPlayer(mediaPlayer, this, musicLocator, albumToPlay,
                playButton);


        //set up location manager, ask user for permission
        detectTimeChanges();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            100);
                    return;
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 305, (LocationListener) MainActivity.this);
                }
            }
        });


        //initialize location of user
        currentUserlocation = getCurrentLocation();

        initializeView();

        detectTimeChanges();

        intializeDataStruc();


        //GET LIST OF SONGS FROM RAW DIRECTORY
        musicParser = new LocalMusicParser(this, mmr, songListObj);
        musicParser.getMusic(songList,songTitleList);
        musicParser.populateAlbum(songListObj, albumList);


        //initialize flashback mode data
        sortingList = new ArrayList<>(songList);

        // Initializes data structure to hold the song's location
        for(int i=0;i<songList.size();i++){
            indexTosong.put(songList.get(i),i);
        }

        score = new ScoreFlashback(songList, songListObj);

        //playList_flashback = new PlayList_flashback(sortingList, (ArrayList<Song>) songListObj, indexTosong);
        playlist_FB = new PlaylistFlashback(sortingList, (ArrayList<Song>) songListObj, indexTosong);

        // Sets up all the responses to user interaction
        setListeners();

    }

    /** This makes the back button into home button
     *  so that the music player is not closed
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * this is the originaly implemented method from android studio
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * this is the originaly implemented method from android studio
     * @param item
     * @return true or super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Creates UI button
    private void createButton() {
        playButton = findViewById(R.id.playButton);
        nextSong = findViewById(R.id.next);
        previousSong = findViewById(R.id.previous);
        showCurrentPlaylist = findViewById(R.id.currentPlaylist);
    }

    // Creates the display of the songs
    private void initializeView() {
        nowPlayingView = findViewById(R.id.nowPlaying);
        albumView = findViewById(R.id.albumList);
        dateTimeView = findViewById(R.id.playingTime);
        locationView = findViewById(R.id.playingLoc);
        //displayRabbit = findViewById(R.id.displayRabbit);
    }

    // Sets up data structures used to contain songs
    public void intializeDataStruc()
    {
        songList = new ArrayList<>();
        songTitleList = new ArrayList<>();
        songListObj = new ArrayList<>();
        albumList = new Hashtable<>();
        indexTosong = new Hashtable<>();
    }


    /** This saves the last state when the music player
     *  app is closed
     */
    @Override
    public void onStop() { super.onStop(); }

    /** This will set the text view to show the current playing song
     *  @param nowPlayingString the current playing song title - artist
     */
    public void setNowPlayingView(String nowPlayingString)
    {
        //hover the now playing text view
        String repeat = new String(new char[1]).replace("\0", " ");
        nowPlayingView.setText(repeat + nowPlayingString + repeat);
        nowPlayingView.setSelected(true);
    }

    /**
     * This method is a listener when the location change
     * it will play new song in flashback mode
     * @param location
     */
    public void onLocationChanged(Location location) {
        if(flashOn) {
            if(currentUserlocation.distanceTo(location)>305) {
                currentUserlocation = location;
                score.score(location, currentUserDayOfWeek, currentUserMNEIndex);
                //playList_flashback.sorter();
                playlist_FB.sorter();
                playTracksOrder();
            }
        }
    }

    /**
     * This method play the music in order for flashback mode
     */
    public void playTracksOrder(){

        player.stop();

        currentIndex = 0;

        Song curPlaying = songListObj.get(indexTosong.get(playlist_FB.getSortingList().get(currentIndex)));
        String display = curPlaying.getTitle() + " - " + curPlaying.getArtist();

        setNowPlayingView(display);
        showCurrentLocation(curPlaying);
        showDateAndTime(curPlaying);

        int resID = getResources().getIdentifier(playlist_FB.getSortingList().get(currentIndex), "raw", getPackageName());
        player.playMusicId(resID);

        player.changeToPauseButton();

        player.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next(playlist_FB.getSortingList());
            }
        });

    }

    /**
     * Method to play the next song
     * @param playlist the list of songs in the playlist_FB
     */
    public void next(final List<String> playlist){
        if(playingAlbumFlag)
        {
            Toast.makeText(MainActivity.this, "No next song available. " +
                    "You are playing an album", Toast.LENGTH_SHORT).show();
        }
        else
        {
            currentIndex++;
            if (currentIndex < playlist.size())
            {


                if (songListObj.get(indexTosong.
                        get(playlist.get(currentIndex))).getStatus() != -1) {

                    if(player.getMediaPlayer().isPlaying()){
                        player.getMediaPlayer().stop();
                        player.releaseMusicPlayer();
                    }


                    //set the now playing text view
                    if(!flashOn) {
                        setNowPlayingView(songTitleList.get(currentIndex));
                        showCurrentLocation(songListObj.get(currentIndex));
                        showDateAndTime(songListObj.get(currentIndex));
                        storeDateAndTime(songListObj.get(currentIndex));
                        try {
                            storeLocation(songListObj.get(currentIndex));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Song curPlaying = songListObj.get(indexTosong.get(playlist_FB.getSortingList().get(currentIndex)));

                        String display = curPlaying.getTitle() + " - " + curPlaying.getArtist();

                        setNowPlayingView(display);
                        showCurrentLocation(curPlaying);
                        showDateAndTime(curPlaying);

                    }


                    int resID = getResources().getIdentifier(playlist.get(currentIndex),
                            "raw", getPackageName());
                    player.playMusicId(resID);

                    player.changeToPauseButton();

                    player.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next(playlist);
                        }
                    });
                }

                else
                {
                    next(playlist);
                }

            }
            else
            {
                currentIndex = playlist.size() - 1;
                if (flashOn)
                {
                    currentIndex = 0;
                    next(playlist);
                }
                else
                {
                    currentIndex = playlist.size()-1;
                    Toast.makeText(MainActivity.this, "No next song available"
                            , Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    /**
     * Method to play the previous song
     * @param playlist the list of songs in the playlist_FB
     */
    public void previous(final List<String> playlist) {
        if(playingAlbumFlag)
        {
            Toast.makeText(MainActivity.this, "No previous song available." +
                    "You are playing an album", Toast.LENGTH_SHORT).show();
        }
        else
        {
            currentIndex--;
            if (currentIndex >= 0)
            {

                if(songListObj.get(indexTosong.
                        get(playlist.get(currentIndex))).getStatus() != -1)
                {
                    player.stop();

                    //update title, location, time view
                    setNowPlayingView(songTitleList.get(currentIndex));
                    showCurrentLocation(songListObj.get(currentIndex));
                    showDateAndTime(songListObj.get(currentIndex));
                    storeDateAndTime(songListObj.get(currentIndex));
                    try {
                        storeLocation(songListObj.get(currentIndex));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    int resID = getResources().getIdentifier(playlist.get(currentIndex), "raw", getPackageName());
                    player.playMusicId(resID);

                    player.changeToPauseButton();

                    player.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next(playlist);
                        }
                    });
                }
                else
                {
                    previous(playlist);
                }

            }
            else
            {
                currentIndex = 0;
                Toast.makeText(MainActivity.this, "No previous song available",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

    /**
     * Method to show date and time text view
     * @param song
     */
    public void showDateAndTime(Song song){
        if(song.getMostRecentDateTime() != null){
            dateTimeView.setText(song.getMostRecentDateTimeString());
        }
        else{
            dateTimeView.setText("No Last Current Time and Date are available");
        }
    }

    /**
     * Method to show location text view
     * @param song
     */
    public void showCurrentLocation(Song song){
        if(song.getMostRecentLocation() != null){

            locationView.setText(song.getMostRecentLocationString());
        }
        else{
            locationView.setText("No Last Current location is available");
        }
    }

    /**
     * Method to store current date and time in the song
     * @param song
     */
    public void storeDateAndTime(Song song){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("kk:mm:ss   dd MMM yyyy");
        String dateAndTime = dateFormatter.format(new Date());
        song.setMostRecentDateTime(new Date());
        song.setMostRecentDateTimeString(dateAndTime);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = convertToMNEIndex(hour);
        if(!song.getTimeHistory().contains(hour)){
            song.addTimeHistory(hour);
        }
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(!song.getDayHistory().contains(day)){
            song.addDayHistory(day);
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
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String state = addresses.get(0).getAdminArea();
        String finalString = address + ", "+state;
        return finalString;
    }

    /**
     * This method get the current location of the user
     * @return the location of the user
     */
    public Location getCurrentLocation(){
        Criteria criteria = new Criteria();
        String bestProvider =locationManager.getBestProvider(criteria,true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
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
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria,true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1", "ins");
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
                    Iterator<Location> itr = song.getLocationHistory().iterator();
                    while(itr.hasNext()){
                        if(itr.next().distanceTo(location) >= 305){
                            song.addLocationHistory(location);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param hour: the time of day
     * @return return hour which indicates if morning then hour = 1, afternoon then hour = 2
     *                                        evening then hour = 3
     */
    public int convertToMNEIndex(int hour){
        //hour = 1 if it's a morning time between 4am and 12pm
        if(4<=hour && hour < 12 ){
            hour = 1;
        }
        //hour=2 if it's an afternoon time between 12pm and 8pm
        else if(12<=hour && hour < 20 ){
            hour = 2;
        }
        //otherwise hour = 3
        else{
            hour = 3;
        }
        return hour;
    }

    /**
     * get the hour and the day of week
     */
    public void setCurrentUserMNEAndDay(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        currentUserDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        currentUserMNEIndex = convertToMNEIndex(hour);
    }

    /**
     * Detect the changes every hour for flashback mode.
     */
    public void detectTimeChanges(){
        setCurrentUserMNEAndDay();
        Calendar calendar = Calendar.getInstance();
        int min = calendar.get(Calendar.MINUTE);
        //time it needs to delay. Ex: if the music is played at 12:05pm  then the time will
        //be delayed and time will be detected at 1pm
        int timeDelay = 1000*60*(60 - min);
        Timer timerDelay = new Timer();
        timerDelay.schedule(new TimerTask() {
            public void run() {
                Timer hourlyTime = new Timer ();
                TimerTask hourlyTask = new TimerTask () {
                    @Override
                    public void run () {
                        setCurrentUserMNEAndDay();
                        score.score(currentUserlocation,currentUserDayOfWeek,currentUserMNEIndex);
                        playlist_FB.sorter();
                        //playList_flashback.sorter();
                        playTracksOrder();
                    }
                };
                hourlyTime.schedule (hourlyTask, 0l, 1000*60*60);

            }

        }, timeDelay);
    }

    public void setListeners(){
        musicLocator = new MusicLocator(locationManager, MainActivity.this);

        //initialize location of user
        currentUserlocation = musicLocator.getCurrentLocation();

        //set up flashback switch listener
        flashback = findViewById(R.id.switch1);
        flashback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    //update flags
                    flashOn = true;
                    playingAlbumFlag = false;

                    nextSong.setClickable(false);
                    previousSong.setClickable(false);
                    listView.setEnabled(false);
                    playButton.setClickable(false);
                    player.changeToPauseButton();

                    Location location = currentUserlocation;
                    int time = currentUserMNEIndex;

                    Log.d("(flashback) time",Integer.toString(time));
                    Log.d("flashback: ", "toggled flashback mode on");

                    int day = currentUserDayOfWeek;

                    //get score to list out song to be played
                    score.score(location, day, time);

                    //playList_flashback.sorter();
                    playlist_FB.sorter();

                    sortingList = playlist_FB.getSortingList();

                    //play the song according to the score order
                    playTracksOrder();

                    Toast.makeText(MainActivity.this,
                            "Flashback mode on", Toast.LENGTH_SHORT).show();
                }
                else {
                    flashOn = false;

                    nextSong.setClickable(true);
                    previousSong.setClickable(true);
                    playButton.setClickable(true);
                    listView.setEnabled(true);

                    player.getMediaPlayer().stop();

                    player.changeToPlayButton();


                    Toast.makeText(MainActivity.this,
                            "Flashback mode off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Play or Pause, Previous, Next button
        player.changeToPlayButton();

        adapter = new MyAdapter((ArrayList<String>) songTitleList, this, songListObj,
                playlist_FB, this, flashOn, currentIndex, songList, currentUserMNEIndex,
                currentUserDayOfWeek, currentUserlocation);

        //handle listview and assign adapter
        listView = findViewById(R.id.songList);
        listView.setAdapter(adapter);


        //set up listener for list of tracks list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("listView: ", "a song is clicked");

                // Disable album queue
                playingAlbumFlag = false;
                currentIndex = i;

                if (!flashOn) {
                    Log.d("listView: ", "playing a song pressed by user");

                    int resID = getResources().getIdentifier(songList.get(i), "raw", getPackageName());
                    player.playMusicId(resID);

                    //set the current playing song in the text view
                    setNowPlayingView(songTitleList.get(i));
                    showCurrentLocation(songListObj.get(i));
                    showDateAndTime(songListObj.get(i));
                    storeDateAndTime(songListObj.get(i));

                    try {
                        storeLocation(songListObj.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {

                    Log.d("listView: ", "user pressed a track in flashback mode");
                    //TODO: add pop up message where the user need to press ok
                    Toast.makeText(MainActivity.this, "You are in flashback mode!\n" +
                            "Please go to normal mode to select music", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //set listener for next song button
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashOn == false) {
                    if(currentIndex == songList.size()-1){
                        currentIndex = songList.size()-1;
                        Toast lastSong = Toast.makeText(getApplicationContext(),
                                "No next song available", Toast.LENGTH_SHORT);

                        lastSong.show();
                        return;
                    }
                    next(songList);
                }
            }
        });

        //set listener for previous song button
        previousSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashOn == false) {

                    if(playingAlbumFlag)
                    {
                        Toast.makeText(getApplicationContext(),
                                "No previous song available." +
                                        " You are playing an album", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(currentIndex == 0){
                            Toast firstSong = Toast.makeText(getApplicationContext(),
                                    "No previous song available", Toast.LENGTH_LONG);
                            firstSong.show();
                            return;
                        }
                        previous(songList);
                    }
                }
            }
        });

        //show list of albums
        tempListAlbum = new ArrayList<>(albumList.values());
        albumAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, tempListAlbum);
        albumView.setAdapter(albumAdapter);


        //set on item click listener for album list
        albumView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("album:", "album is clicked!");
                // Enable album queue
                playingAlbumFlag = true;

                if (!flashOn) {
                    Log.d("album:", "playing the whole album");

                    player.releaseMusicPlayer();

                    //get album to play
                    albumToPlay = tempListAlbum.get(i);

                    //prepare album to be played
                    albumToPlay.setupAlbum();

                    player.setAlbumToPlay(albumToPlay);
                    player.setMusicLocator(musicLocator);

                    ArrayList<Song> albumTracks = albumToPlay.getListOfTracks();
                    for(int j = 0; j < albumTracks.size(); j++)
                    {
                        Log.d("album track list:", albumTracks.get(j).getTitle());
                    }

                    player.playAlbum();

                }
                else {
                    Log.d("album:", "user pressed album in flashback mode");

                    Toast.makeText(MainActivity.this, "You are in flashback mode!\n" +
                            "Please go to normal mode to select music", Toast.LENGTH_SHORT).show();
                }
            }

        });

        //set listener for the play button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.playingAndPausing();
            }
        });

        // Set listener for show current playlist_FB button
        showCurrentPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("show current list:", "clicked!");
                CharSequence trackList[];
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                //check if it is in vibe mode
                if(flashOn){ //TODO: need to change to vibe boolean

                    //get the neutral and favorited song
                    ArrayList<String> listSong = new ArrayList<>();
                    for(int i = 0; i < sortingList.size(); i++)
                    {
                        Song currSong = songListObj.get(
                                indexTosong.get(playlist_FB.getSortingList().get(i)));
                        if (currSong.getStatus() != -1) {
                            String display = currSong.getTitle() + " - " + currSong.getArtist();
                            listSong.add(display);
                        }
                    }

                    //display the song that is going to be played
                    trackList = new CharSequence[listSong.size()];
                    for(int i = 0; i < listSong.size(); i++)
                    {
                        // Put song to playlist_FB if not disliked
                        trackList[i] = listSong.get(i);
                    }
                    builder.setTitle("Vibe Mode Queue");

                }
                else if(playingAlbumFlag) {
                    trackList = new CharSequence[albumToPlay.getListOfTracks().size()];
                    ArrayList<Song> albumTracks = albumToPlay.getListOfTracks();
                    for (int i = 0; i < albumTracks.size(); i++) {
                        String display = albumTracks.get(i).getTitle() +
                                " - " + albumTracks.get(i).getArtist();
                        trackList[i] = display;
                    }
                    builder.setTitle(albumToPlay.getName());
                }
                else
                {
                    trackList = new CharSequence[]{ "Empty" };
                    builder.setTitle("Normal Mode - No Track List Displayed");
                }

                builder.setItems(trackList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });

        CharSequence sorts[] = new CharSequence[] {"Sort by Title", "Sort by Album", "Sort by Artist", "Sort by Status"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a sorting method");
        builder.setItems(sorts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    Sort sortTile = new SortTitle();
                    sortTile.sortPlayList(songListObj,songTitleList,songList);
                    adapter.list = (ArrayList<String>)songTitleList;
                    adapter.notifyDataSetChanged();
                }

                else if(which == 1){
                    Sort sortAlbum = new SortAlbum();
                    sortAlbum.sortPlayList(songListObj, songTitleList,songList);
                    adapter.list = (ArrayList<String>)songTitleList;
                    adapter.notifyDataSetChanged();
                }
                else if(which ==2){
                    Sort sortArtist = new SortArtist();
                    sortArtist.sortPlayList(songListObj,songTitleList,songList);
                    adapter.list = (ArrayList<String>)songTitleList;
                    adapter.notifyDataSetChanged();
                }
                else{
                    Sort sortStatus = new SortStatus();
                    sortStatus.sortPlayList(songListObj,songTitleList,songList);
                    adapter.list = (ArrayList<String>)songTitleList;
                    adapter.notifyDataSetChanged();
                }

            }
        });

        sortButton = (Button)findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!flashOn) {
                    builder.show();
                }
            }
        });
    }
}