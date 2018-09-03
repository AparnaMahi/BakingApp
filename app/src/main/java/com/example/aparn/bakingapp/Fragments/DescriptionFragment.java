package com.example.aparn.bakingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aparn.bakingapp.Data.RecipeStepsAdapter;
import com.example.aparn.bakingapp.DetailActivity;
import com.example.aparn.bakingapp.Model.Ingredient;
import com.example.aparn.bakingapp.Model.RecipeSteps;
import com.example.aparn.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionFragment extends Fragment {

    @BindView(R.id.ingredients_text)
    TextView IngredientsText;
    @BindView(R.id.description_list_rv)
    RecyclerView RecipeStepRecyclerView;
    private RecipeStepsAdapter RecipeStepAdapter;
    private RecyclerView.LayoutManager StepCardLayoutManager;

    public DescriptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.description_list, container, false);
        ButterKnife.bind(this, view);
        populateIngredientsValues();
        RecipeStepRecyclerView.setHasFixedSize(true);
        ArrayList<RecipeSteps> recipeStepList =((DetailActivity) getActivity()).getRecipeStepList();
        String recipeName = ((DetailActivity) getActivity()).getRecipeName();
        RecipeStepAdapter = new RecipeStepsAdapter(getContext(), recipeName, recipeStepList);
        RecipeStepRecyclerView.setAdapter(RecipeStepAdapter);
        StepCardLayoutManager = new LinearLayoutManager(getContext());
        RecipeStepRecyclerView.setLayoutManager(StepCardLayoutManager);
        return view;
    }

    public void populateIngredientsValues() {
        ArrayList<Ingredient> ingredients = ((DetailActivity) getActivity()).getIngredientList();
        IngredientsText.setText(getString(R.string.ingredients_text) + "\n");
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient appendIngredient = ingredients.get(i);
            String ingredient_result = "\n(" + appendIngredient.getQuantity() + " "+ appendIngredient.getMeasure() + ") "
                            + appendIngredient.getIngredientName();
            IngredientsText.append(ingredient_result);
            IngredientsText.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}
