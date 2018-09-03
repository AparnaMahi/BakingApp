package com.example.aparn.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);


    @Test
    public void checkMainScreen() {
        onView(withId(R.id.main_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeList_rv)).perform().check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeCount() {
        onView(withId(R.id.recipeList_rv)).check(new RecyclerViewItemCountAssertion(4));
    }

    @Test
    public void testItemClick() {
        onView(withRecyclerView(R.id.recipeList_rv).atPosition(1)).perform(longClick());
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

}
