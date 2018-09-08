package com.example.arturschaefer.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Ingredients {

    @SerializedName("ingredients")
    private ArrayList<Ingredient> mIngredientList;

    public ArrayList<Ingredient> getmIngredientList() {
        return mIngredientList;
    }
}
