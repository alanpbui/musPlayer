package team20.flashbackmusic;

/**
 * Created by yujingwen199756 on 2/18/18.
 */

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GetAddressTest
{

    @Rule
    public ActivityTestRule<MainActivity> mainActivity= new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void convertToMNEIndexTest(){
        assertEquals(3, mainActivity.getActivity().convertToMNEIndex(21));
        assertEquals(2, mainActivity.getActivity().convertToMNEIndex(12));
        assertEquals(1, mainActivity.getActivity().convertToMNEIndex(11));
        assertEquals(3, mainActivity.getActivity().convertToMNEIndex(24));
    }
    @Test
    public void testGetAddress() throws IOException {
        String address1 = mainActivity.getActivity().getAddress(32.881131, -117.237569);
        assertEquals(address1, "Library Walk, California");
        String address2 = mainActivity.getActivity().getAddress(36.299335, 43.149057);
        assertEquals(address2, "The Right Coast, Nineveh Governorate");

    }
}
