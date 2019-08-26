package team20.flashbackmusic;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for adapting list view to put +,x, and checklist signs
 */
public class MyAdapter extends BaseAdapter implements ListAdapter {
    protected ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private List<Song> songListObj;
    private PlaylistFBM playlist_FB;
    private MainActivity mainActivity;
    private boolean flashOn;
    private int currentIndex;
    private List<String> songList;
    private int currentUserMNEIndex;
    private int currentUserDayOfWeek;
    private Location currentUserlocation;


    //        private Button addBtn;
    int buttonimage = 1;

    /**
     * Constructor of the adapter of list view
     * @param list
     * @param context
     */
    public MyAdapter(ArrayList<String> list, Context context, List<Song> songListObj,
                     PlaylistFBM playlist_FB, MainActivity mainActivity, boolean flashOn,
                     int currentIndex, List<String> songList, int currentUserMNEIndex,
                     int currentUserDayOfWeek, Location currentUserlocation) {

        this.list = list;
        this.context = context;
        this.songListObj = songListObj;
        this.playlist_FB = playlist_FB;
        this.mainActivity = mainActivity;
        this.flashOn = flashOn;
        this.currentIndex = currentIndex;
        this.songList = songList;
        this.currentUserMNEIndex = currentUserMNEIndex;
        this.currentUserDayOfWeek = currentUserDayOfWeek;
        this.currentUserlocation = currentUserlocation;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    //@Override
    public long getItemId(int pos) {
        //return list.get(pos).getiD();
        //just return 0 if your list items do not have an Id variable.
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listview, null);
        }
        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) v.findViewById(R.id.list_item_string);
        final Button addBtn = (Button) v.findViewById(R.id.add_btn);
        listItemText.setText(list.get(position));


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                if (songListObj.get(position).getStatus() == 0) {

                    //change status of song to favorite
                    addBtn.setBackgroundResource(R.drawable.check);
                    //playList_flashback.changeToFavorite(position);
                    playlist_FB.changeToFavorite(position);

                    Toast.makeText(mainActivity, "Added" +
                            " to favorite", Toast.LENGTH_SHORT).show();

                } else if (songListObj.get(position).getStatus() == 1) {

                    //change status of song to dislike
                    playlist_FB.changeToDislike(position);

                    addBtn.setBackgroundResource(R.drawable.cross);

                    Toast.makeText(mainActivity, "Added" +
                            " to dislike", Toast.LENGTH_SHORT).show();

                    //if (flashOn && playList_flashback.sortingList.get(currentIndex).equals(songList.get(position)))
                    if(flashOn && playlist_FB.getSortingList().get(currentIndex).equals(songList.get(position)))
                        mainActivity.next(playlist_FB.getSortingList());
                        //next(playList_flashback.sortingList);


                        //else if (!flashOn && currentIndex == position && mediaPlayer.isPlaying())
                    else if(!flashOn && currentIndex == position)
                        mainActivity.next(songList);

                } else {
                    Toast.makeText(mainActivity, "Back" +
                            " to neutral", Toast.LENGTH_SHORT).show();
                    songListObj.get(position).setStatus(0);
                    Location location = currentUserlocation;
                    int day = currentUserDayOfWeek;
                    int time = currentUserMNEIndex;

                    //change status of song to neutral
                    //playList_flashback.changeToNeutral(position,location,day, time);
                    playlist_FB.changeToNeutral(position, location, day, time);
                    addBtn.setBackgroundResource(R.drawable.add);
                }
            }
        });

        return v;
    }
}
