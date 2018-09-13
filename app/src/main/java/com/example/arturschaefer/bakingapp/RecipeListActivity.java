package com.example.arturschaefer.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.arturschaefer.bakingapp.adapter.RecipeAdapter;
import com.example.arturschaefer.bakingapp.model.Recipe;
import com.example.arturschaefer.bakingapp.utils.RetrofitBuilder;
import com.example.arturschaefer.bakingapp.utils.RetrofitService;
import com.example.arturschaefer.bakingapp.widget.WidgetService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity implements RecipeAdapter.RecipeListItemClickListener {

    private final String LOG_TAG = RecipeListActivity.class.getSimpleName();
    public static final String RECIPES_LIST = "recipes";
    public static final String RECIPES_DETAILS = "recipes_details";
    public static final String FRAGMENT_INGREDIENTS = "fragment_ingredients";
    public static final String FRAGMENT_STEPS = "fragment_steps";
    public static final String TWO_PANE = "two_pane";

    public static final String SHARED_PREF = "shared_pref_baking";
    public static final String JSON_RESULT = "json_result";

    private boolean mTwoPane;
    private ArrayList<Recipe> mRecipeList;
    private RecipeAdapter mRecipeAdapter;
    private CallbackInterface mCallback;
    private boolean isFragmentDisplayed;
    private Bundle mBundle;
    private String mJsonRecipe;
    private String mJsonResult;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recipe_list)
    View mRecyclerView;
    @BindView(R.id.pb_recipe_list)
    ProgressBar mProgressBar;

    StepFragment mStepFragment;
    IngredientFragment mIngredientFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        mProgressBar.setVisibility(View.VISIBLE);

        if (savedInstanceState == null || !savedInstanceState.containsKey(RECIPES_LIST)){
            mRecipeList = new ArrayList<>();
        } else {
            mRecipeList = savedInstanceState.getParcelableArrayList(RECIPES_LIST);
        }
        mRecipeAdapter = new RecipeAdapter(mRecipeList,this);

        mCallback = new CallbackInterface() {
            @Override
            public void onSuccess(boolean value) {
                mProgressBar.setVisibility(View.GONE);
                RecyclerView recyclerView = (RecyclerView) mRecyclerView;
                recyclerView.setAdapter(mRecipeAdapter);
                mRecipeAdapter.swapData(mRecipeList);
            }

            @Override
            public void onError() {
                Log.e(LOG_TAG, "Error with recipelist");
            }
        };
        mBundle = savedInstanceState;

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

        setupRecyclerView((RecyclerView) mRecyclerView, mCallback);
        assert  mRecyclerView != null;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, final CallbackInterface callbackInterface) {
        try {
            RetrofitService iRecipe = RetrofitBuilder.Retrieve();
            Call<ArrayList<Recipe>> recipe = iRecipe.getRecipe();

            recipe.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                    Integer statusCode = response.code();
                    Log.i(LOG_TAG, statusCode.toString());
                    mRecipeList = response.body();
                    mJsonResult = new Gson().toJson(response.body());
                    callbackInterface.onSuccess(true);
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                    Log.i(LOG_TAG, "Error with Retrofit call");
                    callbackInterface.onError();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(mRecipeAdapter);
    }


    @Override
    public void onRecipeItemClick(Recipe recipe) {
        if(!mTwoPane){
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RECIPES_DETAILS, recipe);
            intent.putExtra(JSON_RESULT, mJsonRecipe);
            startActivity(intent);

            mJsonRecipe = jsonResultToOneRecipe(mJsonResult, recipe.getmId() - 1);
            editor.putString(JSON_RESULT, mJsonRecipe);
            editor.apply();
            WidgetService.startActionOpenRecipe(getApplicationContext());
        } else {
            if(!mRecipeList.isEmpty()) {
                displayFragments(recipe);
                SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
                mJsonRecipe = jsonResultToOneRecipe(mJsonResult, recipe.getmId() - 1);
                editor.putString(JSON_RESULT, mJsonRecipe);
                editor.apply();
                WidgetService.startActionOpenRecipe(getApplicationContext());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!mRecipeList.isEmpty()){
            outState.putParcelableArrayList(RECIPES_LIST, mRecipeList);
        }
    }

    public interface CallbackInterface {
        void onSuccess(boolean value);
        void onError();
    }

    public void displayFragments(Recipe recipe){
        mStepFragment = new StepFragment();
        mIngredientFragment = new IngredientFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        if (mBundle == null)
            mBundle = new Bundle();

        mBundle.putParcelableArrayList(FRAGMENT_INGREDIENTS, recipe.getmIngredientList());
        mBundle.putParcelableArrayList(FRAGMENT_STEPS, recipe.getmStepList());
        mBundle.putBoolean(TWO_PANE, mTwoPane);

        mIngredientFragment.setArguments(mBundle);
        fragmentTransaction.replace(R.id.recipe_detail_container,
                mIngredientFragment);

        mStepFragment.setArguments(mBundle);
        fragmentTransaction.replace(R.id.recipe_step_container,
                mStepFragment).commit();

        isFragmentDisplayed = true;
    }

    private String jsonResultToOneRecipe(String jsonResult, int position){
        JsonElement jsonElement = new JsonParser().parse(jsonResult);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonElement recipeElement = jsonArray.get(position);
        return recipeElement.toString();
    }
}
