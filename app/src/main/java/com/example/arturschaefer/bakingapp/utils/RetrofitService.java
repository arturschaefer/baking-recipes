package com.example.arturschaefer.bakingapp.utils;

import com.example.arturschaefer.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}