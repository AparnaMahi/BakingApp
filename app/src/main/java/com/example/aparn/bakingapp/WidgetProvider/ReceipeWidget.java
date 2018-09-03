package com.example.aparn.bakingapp.WidgetProvider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.aparn.bakingapp.Model.Ingredient;
import com.example.aparn.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;

public class ReceipeWidget extends AppWidgetProvider {

    @BindView(R.id.widget_recipe_text)
    TextView RecipeNameText;
    @BindView(R.id.widget_ingredients_text)
    TextView IngredientsText;

    private static String RecipeName = "";
    private static ArrayList<Ingredient> IngredientsList = new ArrayList<Ingredient>();

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        CharSequence widgetRecipeText = RecipeName;
        String ingredientsList = "\n";
        for (Ingredient i : IngredientsList) {
            ingredientsList += "\n(" + i.getQuantity() + " " + i.getMeasure()+ ")\t\t" + i.getIngredientName();
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.receipe_widget);
        if (!RecipeName.equals("") && !IngredientsList.isEmpty()) {
            remoteViews.setTextViewText(R.id.widget_recipe_text, widgetRecipeText);
            remoteViews.setTextViewText(R.id.widget_ingredients_text, ingredientsList);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String recipeListValues= context.getString(R.string.recipe_name);
        String ingredientsValues= context.getString(R.string.ingredients_text);
        if (intent.hasExtra(recipeListValues) && intent.hasExtra(ingredientsValues)) {
            RecipeName = intent.getStringExtra(recipeListValues);
            IngredientsList = intent.getParcelableArrayListExtra(ingredientsValues);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context.getPackageName(),
            ReceipeWidget.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

