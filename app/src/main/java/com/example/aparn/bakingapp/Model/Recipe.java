package com.example.aparn.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    private String recipeName;
    private int servingSize;
    private ArrayList<Ingredient> ingredientsList;
    private ArrayList<RecipeSteps> recipeStepList;
    private String imageUrl;

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe(String recipe_Name, int serving_Size, ArrayList<Ingredient> ingredients_List,
                  ArrayList<RecipeSteps> recipe_StepList, String image_Url) {
        recipeName = recipe_Name;
        servingSize = serving_Size;
        ingredientsList = ingredients_List;
        recipeStepList = recipe_StepList;
        imageUrl = image_Url;
    }

    private Recipe(Parcel in) {
        recipeName = in.readString();
        servingSize = in.readInt();
        imageUrl = in.readString();
        if (in.readByte() == 0x01) {
            ingredientsList = new ArrayList<>();
            in.readList(ingredientsList, Ingredient.class.getClassLoader());
        } else {
            ingredientsList = null;
        }
        if (in.readByte() == 0x01) {
            recipeStepList = new ArrayList<>();
            in.readList(recipeStepList, RecipeSteps.class.getClassLoader());
        } else {
            recipeStepList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeName);
        dest.writeInt(servingSize);
        dest.writeString(imageUrl);

        if (ingredientsList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredientsList);
        }
        if (recipeStepList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recipeStepList);
        }
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public ArrayList<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public ArrayList<RecipeSteps> getRecipeStepList() {
        return recipeStepList;
    }

    public void setRecipeStepList(ArrayList<RecipeSteps> receipeStepList) {
        this.recipeStepList = receipeStepList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}