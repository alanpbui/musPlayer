package team20.flashbackmusic;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LocationAndLastPlayEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void locationAndLastPlayEspressoTest() {
        DataInteraction relativeLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.songList),
                        childAtPosition(
                                withId(R.id.layoutList),
                                1)))
                .atPosition(1);
        relativeLayout.perform(click());

        DataInteraction relativeLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.songList),
                        childAtPosition(
                                withId(R.id.layoutList),
                                1)))
                .atPosition(3);
        relativeLayout2.perform(click());

        DataInteraction relativeLayout3 = onData(anything())
                .inAdapterView(allOf(withId(R.id.songList),
                        childAtPosition(
                                withId(R.id.layoutList),
                                1)))
                .atPosition(1);
        relativeLayout3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.playingLoc), withText("6112-6398 La Jolla Colony Drive, California"),
                        childAtPosition(
                                allOf(withId(R.id.nowPlayingLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                1),
                        isDisplayed()));


        textView.check(matches(not(withText("No Last Current location is available"))));

        /* THIS PART IS TESTING THE TIME, WHICH CHANGES ALL THE TIME,
         * SINCE WE DIDN'T MOCK THE TIME */
//
//        ViewInteraction textView2 = onView(
//                allOf(withId(R.id.playingTime), withText("07:15:48   19 Feb 2018"),
//                        childAtPosition(
//                                allOf(withId(R.id.nowPlayingLayout),
//                                        childAtPosition(
//                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
//                                                1)),
//                                2),
//                        isDisplayed()));
//        textView2.check(matches(not(withText("No Last Current Time and Date are available"))));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
