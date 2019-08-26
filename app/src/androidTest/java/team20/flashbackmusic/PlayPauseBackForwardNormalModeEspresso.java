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
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PlayPauseBackForwardNormalModeEspresso {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void playPauseBackForwardNormalModeEspresso() {
        DataInteraction relativeLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.songList),
                        childAtPosition(
                                withId(R.id.layoutList),
                                1)))
                .atPosition(0);
        relativeLayout.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.nowPlaying), withText(" Dead Dove Do Not Eat - Forum "),
                        childAtPosition(
                                allOf(withId(R.id.nowPlayingLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText(" Dead Dove Do Not Eat - Forum ")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.playingLoc), withText("No Last Current location is available"),
                        childAtPosition(
                                allOf(withId(R.id.nowPlayingLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("No Last Current location is available")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.playingTime), withText("No Last Current Time and Date are available"),
                        childAtPosition(
                                allOf(withId(R.id.nowPlayingLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("No Last Current Time and Date are available")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.songDuration), withText("Song duration"),
                        childAtPosition(
                                allOf(withId(R.id.nowPlayingLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                3),
                        isDisplayed()));
        textView4.check(matches(withText("Song duration")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.playButton),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        1),
                                3),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.playButton),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                3),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.playButton),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        1),
                                3),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.next),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                4),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.nowPlaying), withText(" Dreamatorium - Forum "),
                        childAtPosition(
                                allOf(withId(R.id.nowPlayingLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText(" Dreamatorium - Forum ")));

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.previous),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                5),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.nowPlaying), withText(" Dead Dove Do Not Eat - Forum "),
                        childAtPosition(
                                allOf(withId(R.id.nowPlayingLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText(" Dead Dove Do Not Eat - Forum ")));

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
