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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PlayAlbumEspressoTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

  @Test
  public void playAlbumEspressoTest() {
    DataInteraction appCompatTextView = onData(anything())
            .inAdapterView(allOf(withId(R.id.albumList),
                    childAtPosition(
                            withId(R.id.albumHorizontalView),
                            0)))
            .atPosition(0);
    appCompatTextView.perform(scrollTo(), click());

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

    ViewInteraction imageButton = onView(
            allOf(withId(R.id.playButton),
                    childAtPosition(
                            childAtPosition(
                                    IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                    1),
                            3),
                    isDisplayed()));
    imageButton.check(matches(isDisplayed()));

    DataInteraction appCompatTextView2 = onData(anything())
            .inAdapterView(allOf(withId(R.id.albumList),
                    childAtPosition(
                            withId(R.id.albumHorizontalView),
                            0)))
            .atPosition(1);
    appCompatTextView2.perform(scrollTo(), click());

    ViewInteraction textView2 = onView(
            allOf(withId(R.id.nowPlaying), withText(" Jazz In Paris - Media Right Productions "),
                    childAtPosition(
                            allOf(withId(R.id.nowPlayingLayout),
                                    childAtPosition(
                                            IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                            1)),
                            0),
                    isDisplayed()));
    textView2.check(matches(withText(" Jazz In Paris - Media Right Productions ")));

    ViewInteraction imageButton2 = onView(
            allOf(withId(R.id.playButton),
                    childAtPosition(
                            childAtPosition(
                                    IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                    1),
                            3),
                    isDisplayed()));
    imageButton2.check(matches(isDisplayed()));

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