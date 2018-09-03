package com.example.aparn.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.aparn.bakingapp.Fragments.DescriptionFragment;
import com.example.aparn.bakingapp.Fragments.InstructionFragment;
import com.example.aparn.bakingapp.Model.Ingredient;
import com.example.aparn.bakingapp.Model.Recipe;
import com.example.aparn.bakingapp.Model.RecipeSteps;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private String recipe_Name;
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<RecipeSteps> recipeStepList;
    private int pos;

    private DescriptionFragment descriptionFragment;
    private InstructionFragment instructionFragment;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.recipe_object))) {
            Recipe recipe = intent.getParcelableExtra(getString(R.string.recipe_object));
            recipe_Name = recipe.getRecipeName();
            getSupportActionBar().setTitle(recipe_Name);

            ingredientList = recipe.getIngredientsList();
            recipeStepList = recipe.getRecipeStepList();
        }

        instructionFragment = new InstructionFragment();
        descriptionFragment = new DescriptionFragment();
        isTablet = getResources().getBoolean(R.bool.isTablet);//tablet mode

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.recipe_frame, descriptionFragment,getString(R.string.description_list_fragment))
                    .commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.position_value), pos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        pos = savedInstanceState.getInt(getString(R.string.position_value));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
        return true;
    }

    public void openInstructionFragment() {
        if (isTablet) {
            InstructionFragment instruction_FragmentValue = (InstructionFragment)getSupportFragmentManager()
                            .findFragmentByTag(getString(R.string.instruction_fragment));
            if (instruction_FragmentValue != null && instruction_FragmentValue.isVisible()) {
                instruction_FragmentValue.onPositionChanged(pos);
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.instruction_frame, instructionFragment,getString(R.string.instruction_fragment))
                        .commit();
            }
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.recipe_frame, instructionFragment,getString(R.string.instruction_fragment))
                    .addToBackStack(null)
                    .commit();
        }
    }

    public String getRecipeName() {
        return recipe_Name;
    }

    public void setRecipeName(String recipe_Name) {
        this.recipe_Name = recipe_Name;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public ArrayList<RecipeSteps> getRecipeStepList() {
        return recipeStepList;
    }

    public void setRecipeStepList(ArrayList<RecipeSteps> recipeStepList) {
        this.recipeStepList = recipeStepList;
    }

    public int getCurrentPosition() {
        return pos;
    }

    public void setCurrentPosition(int pos) {
        this.pos = pos;
    }

    public DescriptionFragment getDescriptionFragment() {
        return descriptionFragment;
    }

    public void setDescriptionFragment(DescriptionFragment descriptionFragment) {
        this.descriptionFragment = descriptionFragment;
    }

    public InstructionFragment getInstructionFragment() {
        return instructionFragment;
    }

    public void setInstructionFragment(InstructionFragment instructionFragment) {
        this.instructionFragment = instructionFragment;
    }
}

