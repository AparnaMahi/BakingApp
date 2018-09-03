package com.example.aparn.bakingapp.Data;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aparn.bakingapp.DetailActivity;
import com.example.aparn.bakingapp.Model.Ingredient;
import com.example.aparn.bakingapp.Model.Recipe;
import com.example.aparn.bakingapp.Model.RecipeSteps;
import com.example.aparn.bakingapp.R;
import com.example.aparn.bakingapp.WidgetProvider.ReceipeWidget;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    public Context mContext;
    private ArrayList<Recipe> RecipeList;

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ViewLayout;
        public ImageView ImageView;
        public TextView TitleText;
        public RecipeAdapterViewHolder(LinearLayout view) {
            super(view);
            ViewLayout = view;
            TitleText = new TextView(mContext);
            ImageView = new ImageView(mContext);
            ViewLayout.addView(ImageView);
            ViewLayout.addView(TitleText);
        }
    }

    public RecipeAdapter(Context context, String jsonString) {
        mContext = context;
        RecipeList = new ArrayList<>();
        getAllRecipesFromJSON(jsonString);
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout recipeViewLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 25, 15, 25);
        recipeViewLayout.setLayoutParams(layoutParams);
        recipeViewLayout.setPadding(0, 60, 0, 60);
        recipeViewLayout.setBackgroundResource(R.color.cardBGColor);
        RecipeAdapterViewHolder recipeCards = new RecipeAdapterViewHolder(recipeViewLayout);
        return recipeCards;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, final int position) {
        final Recipe recipeAt = RecipeList.get(position);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageParams.weight = 1f;
        imageParams.gravity = Gravity.CENTER;
        holder.ImageView.setLayoutParams(imageParams);
        Picasso.get()
                .load(Uri.parse(recipeAt.getImageUrl()))
                .placeholder(R.drawable.ic_cake_black_24dp)
                .error(R.drawable.ic_cake_black_24dp)
                .into(holder.ImageView);

        LinearLayout.LayoutParams text_LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        text_LayoutParams.weight = 1f;
        text_LayoutParams.gravity = Gravity.CENTER;
        holder.TitleText.setLayoutParams(text_LayoutParams);
        holder.TitleText.setText(recipeAt.getRecipeName());
        holder.TitleText.append("\n" + Integer.toString(recipeAt.getServingSize()) + " servings");
        holder.TitleText.setTextAppearance(mContext, R.style.RecipeCardText);
        holder.TitleText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //hints from https://www.androidauthority.com/create-simple-android-widget-608975/ and other tutorials
        holder.ViewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent widgetIntent = new Intent(mContext,ReceipeWidget.class);
                widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                widgetIntent.putExtra
                        (mContext.getString(R.string.recipe_name), recipeAt.getRecipeName());
                widgetIntent.putExtra
                        (mContext.getString
                                (R.string.ingredients_text), recipeAt.getIngredientsList());
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra
                        (mContext.getString(R.string.recipe_object), recipeAt);
                mContext.sendBroadcast(widgetIntent);
                mContext.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RecipeList.size();
    }

    public void getAllRecipesFromJSON(String jsonResponse) {
        try {
            JSONArray recipeJson = new JSONArray(jsonResponse);
            for (int i = 0; i < recipeJson.length(); i++) {
                String JSONRecipeName = recipeJson.getJSONObject(i).getString(mContext.getString(R.string.name));
                int JSONServingSize = recipeJson.getJSONObject(i).getInt(mContext.getString(R.string.servings));

                JSONArray ingredientsJson = recipeJson.getJSONObject(i).getJSONArray(mContext.getString(R.string.ingredients));
                JSONArray recipeStepsJson = recipeJson.getJSONObject(i).getJSONArray(mContext.getString(R.string.steps));

                ArrayList<Ingredient> mIngredientList = new ArrayList<>();
                ArrayList<RecipeSteps> mRecipeStepList = new ArrayList<>();

                for (int j = 0; j < ingredientsJson.length(); j++) {
                    JSONObject objectAt = ingredientsJson.getJSONObject(j);

                    double JSONQuantity = objectAt.getDouble(mContext.getString(R.string.quantity));
                    String JSONMeasure = objectAt.getString(mContext.getString(R.string.measure));
                    String JSONIngredientName = objectAt.getString(mContext.getString(R.string.ingredient_val));
                    Ingredient JSONIngredient = new Ingredient(JSONQuantity, JSONMeasure, JSONIngredientName);
                    mIngredientList.add(JSONIngredient);
                }
                for (int k = 0; k < recipeStepsJson.length(); k++) {
                    JSONObject objectAt = recipeStepsJson.getJSONObject(k);
                    int JSONId = objectAt.getInt(mContext.getString(R.string.id));
                    String JSONShortDescription = objectAt.getString(mContext.getString(R.string.short_description));
                    String JSONDescription = objectAt.getString(mContext.getString(R.string.description));
                    String JSONVideoUrl = objectAt.getString(mContext.getString(R.string.video));
                    String JSONThumbnailUrl = objectAt.getString(mContext.getString(R.string.thumbnail));
                    RecipeSteps JSONRecipeStep = new RecipeSteps(JSONId, JSONShortDescription,JSONDescription, JSONVideoUrl, JSONThumbnailUrl);
                    mRecipeStepList.add(JSONRecipeStep);
                }
                String JSONImageUrl = recipeJson.getJSONObject(i).getString(mContext.getString(R.string.image));
                RecipeList.add(new Recipe(JSONRecipeName, JSONServingSize,mIngredientList, mRecipeStepList, JSONImageUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

