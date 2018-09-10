package com.example.arturschaefer.bakingapp.utils;

import com.example.arturschaefer.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitBuilder {
    static RetrofitService iRetrofit;

    private static RetrofitBuilder client = new RetrofitBuilder();

    public static RetrofitBuilder getInstance() {
        return client;
    }

    public Call<ArrayList<Recipe>> getRecipes() {
        return iRetrofit.getRecipe();
    }

    public static RetrofitService Retrieve(){

        Gson gson = new GsonBuilder().create();
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        iRetrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build().create(RetrofitService.class);

        return iRetrofit;
    }
}
