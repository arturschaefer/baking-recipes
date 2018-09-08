package com.example.arturschaefer.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Steps {
    @SerializedName("steps")
    private ArrayList<Ingredient> mStepList;

    public ArrayList<Ingredient> getmStepList() {
        return mStepList;
    }
}
