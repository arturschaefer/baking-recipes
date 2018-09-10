package com.example.arturschaefer.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.arturschaefer.bakingapp.adapter.DetailPagerAdapter;
import com.example.arturschaefer.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String FRAGMENT_INGREDIENTS = "fragment_ingredients";
    private static final String FRAGMENT_STEPS = "fragment_steps";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.vp_container)
    ViewPager mViewPager;

    private Recipe mRecipe;
    private IngredientFragment mIngredientFragment;
    private StepFragment mStepFragment;
    private DetailPagerAdapter mSectionsPagerAdapter;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra(RecipeListActivity.RECIPES_DETAILS)) {
            mRecipe = getIntent().getParcelableExtra(RecipeListActivity.RECIPES_DETAILS);
        }

        if(findViewById(R.id.fl_detail_step_container) != null)
            mTwoPane = true;

        setupFragment(savedInstanceState);
        setupToolbar();
    }

    public void setupToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRecipe.getmName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void setupFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(RecipeDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_ID));
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mIngredientFragment = new IngredientFragment();
            mStepFragment = new StepFragment();
            savedInstanceState.putParcelableArrayList(FRAGMENT_INGREDIENTS, mRecipe.getmIngredientList());
            mIngredientFragment.setArguments(savedInstanceState);

            savedInstanceState.putParcelableArrayList(FRAGMENT_STEPS, mRecipe.getmStepList());
            mStepFragment.setArguments(savedInstanceState);

        } else {
            mIngredientFragment = (IngredientFragment) fragmentManager
                    .getFragment(savedInstanceState, FRAGMENT_INGREDIENTS);
            mStepFragment = (StepFragment) fragmentManager
                    .getFragment(savedInstanceState, FRAGMENT_STEPS);
        }

        mSectionsPagerAdapter = new DetailPagerAdapter(
                fragmentManager,
                mIngredientFragment,
                mStepFragment);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
