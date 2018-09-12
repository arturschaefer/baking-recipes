package com.example.arturschaefer.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class BakingAppViews {
    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);
    @Test
    public void clickRecyclerViewItem_OpensActivityWithIngredients() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_ingredients)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecyclerViewItem_OpensActivityWithRecipeSteps() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.vp_container)).check(matches(isDisplayed()));
        onView(withId(R.id.vp_container)).perform(swipeLeft());
        onView(withId(R.id.rv_step)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_step)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
    }
}
