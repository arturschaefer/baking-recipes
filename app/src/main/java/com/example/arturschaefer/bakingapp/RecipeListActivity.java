package com.example.arturschaefer.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arturschaefer.bakingapp.adapter.RecipeAdapter;
import com.example.arturschaefer.bakingapp.dummy.DummyContent;
import com.example.arturschaefer.bakingapp.model.Recipe;
import com.example.arturschaefer.bakingapp.utils.RetrofitBuilder;
import com.example.arturschaefer.bakingapp.utils.RetrofitService;
import com.google.gson.annotations.Expose;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private FastItemAdapter<Recipe> mFastAdapter;
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
        } else {
            mRecipeList = savedInstanceState.getParcelableArrayList(RECIPES_LIST);
        }

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
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

        assert  mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView, mCallback);
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

//        recyclerView.setAdapter(mFastAdapter);
        recyclerView.setAdapter(mRecipeAdapter);
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }


    @Override
    public void onRecipeItemClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RECIPES_DETAILS, recipe);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!mRecipeList.isEmpty()){
            outState.putParcelableArrayList(RECIPES_LIST, mRecipeList);
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, item.id);
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
//            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
//            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.textview_recipe_name);
//                mContentView = view.findViewById(R.id.content);
            }
        }
    }

    public interface CallbackInterface{
        void onSuccess(boolean value);
        void onError();
    }
}
