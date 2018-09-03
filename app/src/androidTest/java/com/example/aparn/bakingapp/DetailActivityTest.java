package com.example.aparn.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.aparn.bakingapp.Model.Ingredient;
import com.example.aparn.bakingapp.Model.Recipe;
import com.example.aparn.bakingapp.Model.RecipeSteps;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DetailActivityTest {

    Context mContext = InstrumentationRegistry.getTargetContext();

    Ingredient testIngredient = new Ingredient( 2,"CUP","Graham Cracker crumbs");
    RecipeSteps testStep = new RecipeSteps(1,
            "Test Short Description",
            "Test Instructions",
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
            "");

    ArrayList<Ingredient> testIngredientsList = new ArrayList<Ingredient>();
    ArrayList<RecipeSteps> testStepsList = new ArrayList<RecipeSteps>();

    @Rule
    public ActivityTestRule<DetailActivity> detailActivityRule =
            new ActivityTestRule(DetailActivity.class,false,false);

    @Test
    public void testIngredientsView() {
        testIngredientsList.add(testIngredient);
        testStepsList.add(testStep);
        Recipe testRecipe = new Recipe("Nutella Pie", 8, testIngredientsList, testStepsList, "");
        Intent testIntent = new Intent();
        testIntent.putExtra(mContext.getString(R.string.recipe_object), testRecipe);
        detailActivityRule.launchActivity(testIntent);
        onView(withId(R.id.ingredients_text)).perform().check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeSteps() {
        testIngredientsList.add(testIngredient);
        testStepsList.add(testStep);
        Recipe testRecipe = new Recipe("Nutella Pie", 8, testIngredientsList, testStepsList, "");
        Intent testIntent = new Intent();
        testIntent.putExtra(mContext.getString(R.string.recipe_object), testRecipe);
        detailActivityRule.launchActivity(testIntent);
        onView(withContentDescription(testStep.getShortDescription())).perform().check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeVideo() {
        testIngredientsList.add(testIngredient);
        testStepsList.add(testStep);
        Recipe testRecipe = new Recipe("Nutella Pie", 8, testIngredientsList, testStepsList, "");
        Intent testIntent = new Intent();
        testIntent.putExtra(mContext.getString(R.string.recipe_object), testRecipe);
        detailActivityRule.launchActivity(testIntent);
        onView(withContentDescription(testStep.getShortDescription())).perform(longClick());
        onView(withId(R.id.video_frame)).check(matches(isDisplayed()));
    }
}
