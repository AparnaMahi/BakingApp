package com.example.aparn.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aparn.bakingapp.Fragments.RecipeFragment;

public class MainActivity extends AppCompatActivity {

    public static final String json_URL="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*bool values setting derived from follwing links.
        : https://stackoverflow.com/questions/9279111/determine-if-the-device-is-a-smartphone-or-tablet
        : https://developer.android.com/guide/topics/resources/more-resources#Bool
       */
        isTablet = getResources().getBoolean(R.bool.isTablet);
        if (savedInstanceState == null) {
            RecipeFragment recipesList = new RecipeFragment();
            getSupportFragmentManager()
                    .beginTransaction().add(R.id.main_layout, recipesList).commit();
        }
        setContentView(R.layout.activity_main);
    }
}
