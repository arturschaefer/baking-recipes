package com.example.arturschaefer.bakingapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.arturschaefer.bakingapp.adapter.DetailPagerAdapter;
import com.example.arturschaefer.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    public static final String FRAGMENT_INGREDIENTS = "fragment_ingredients";
    public static final String FRAGMENT_STEPS = "fragment_steps";

    @BindView(R.id.toolbar_details)
    Toolbar mToolbar;
    @BindView(R.id.tl_details)
    TabLayout mTabLayout;
    @BindView(R.id.vp_container)
    ViewPager mViewPager;

    private Recipe mRecipe;
    private IngredientFragment mIngredientFragment;
    private StepFragment mStepFragment;
    private DetailPagerAdapter mSectionsPagerAdapter;
    private boolean mTwoPane;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        this.mBundle = savedInstanceState;

        if (getIntent() != null && getIntent().hasExtra(RecipeListActivity.RECIPES_DETAILS)) {
            mRecipe = getIntent().getParcelableExtra(RecipeListActivity.RECIPES_DETAILS);
        }

        if(findViewById(R.id.fl_detail_step_container) != null)
            mTwoPane = true;

        setupFragment(mBundle);
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
         FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            savedInstanceState = new Bundle();

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
        super.onBackPressed();
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
