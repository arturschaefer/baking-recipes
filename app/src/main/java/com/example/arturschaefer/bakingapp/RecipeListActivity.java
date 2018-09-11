package com.example.arturschaefer.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private boolean mTwoPane;
    private ArrayList<Recipe> mRecipeList;
    private RecipeAdapter mRecipeAdapter;
    private CallbackInterface mCallback;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recipe_list)
    View mRecyclerView;
    @BindView(R.id.pb_recipe_list)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());
        mProgressBar.setVisibility(View.VISIBLE);

        if (savedInstanceState == null || !savedInstanceState.containsKey(RECIPES_LIST)) {
            mRecipeList = new ArrayList<>();
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
            setupRecyclerView((RecyclerView) mRecyclerView, mCallback);
        } else {
            mRecipeList = savedInstanceState.getParcelableArrayList(RECIPES_LIST);
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

        mRecipeAdapter = new RecipeAdapter(mRecipeList,this);

        assert  mRecyclerView != null;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, final CallbackInterface callbackInterface) {
        try {
            RetrofitService iRecipe = RetrofitBuilder.Retrieve();
            Call<ArrayList<Recipe>> recipe = iRecipe.getRecipe();

            recipe.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    Integer statusCode = response.code();
                    Log.i(LOG_TAG, statusCode.toString());
                    mRecipeList = response.body();
                    callbackInterface.onSuccess(true);
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
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
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RECIPES_DETAILS, recipe);
            startActivity(intent);
        } else{
            //TODO configurar para tela de tablet
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
}
