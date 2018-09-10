package com.example.arturschaefer.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.arturschaefer.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepDetailActivity extends AppCompatActivity {
    public static final String LOG_TAG = StepDetailActivity.class.getSimpleName();
    private static final String STEPS_FRAGMENT = "step_fragment";
    private static final String CURRENT_STEP = "current_step";
    private static final String STEPS = "STEPS_DETAILS";
    private static final String CURRENT_INDEX = "CURRENT_STEP";

    @BindView(R.id.v_horizontal)
    View mView;
    @BindView(R.id.bt_prev)
    Button mPrevButton;
    @BindView(R.id.bt_next)
    Button mNextButton;
    @BindView(R.id.ll_step_navigation)
    LinearLayout mLinearLayout;

    private ArrayList<Step> mStepArrayList;
    private int mCurrentStep;
    private StepDetailFragment mStepDetailFragment;
    private Step mStep;
    private Bundle mBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);
        this.mBundle = savedInstanceState;

        if(getIntent().hasExtra(CURRENT_INDEX)
                && getIntent().hasExtra(STEPS)){
            mStepArrayList = getIntent().getParcelableArrayListExtra(STEPS);
            mCurrentStep = getIntent().getIntExtra(CURRENT_INDEX, 0);
            setupButtons(mCurrentStep);
        }

        setupActionBar();
        setupFragment(savedInstanceState);
    }



    public void setupFragment(Bundle bundle){
        if (bundle != null && bundle.containsKey(STEPS_FRAGMENT)) {
            mStepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(bundle, STEPS_FRAGMENT);
        } else {
            bundle = new Bundle();
            mStepDetailFragment = new StepDetailFragment();
            mStep = mStepArrayList.get(mCurrentStep);
            bundle.putParcelable(CURRENT_STEP, mStep);
            mStepDetailFragment.setArguments(bundle);
            mBundle = bundle;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rl_container, mStepDetailFragment)
                .commit();
    }

    public void setupActionBar(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mStepArrayList.get(mCurrentStep).getmShortDescription());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            if (getApplicationContext().getResources().getConfiguration().orientation
//                    == Configuration.ORIENTATION_LANDSCAPE) {
//                getSupportActionBar().hide();
//            }
        }
    }

    public void setupButtons(int position){
        String stepName;
        if(mStepArrayList.size() > 1 ){
            if (position == 0){
                mPrevButton.setVisibility(View.INVISIBLE);
                stepName = mStepArrayList.get(position + 1).getmShortDescription();
                mNextButton.setText(stepName);
            } else if (position == (mStepArrayList.size() - 1)){
                mNextButton.setVisibility(View.INVISIBLE);
                stepName = mStepArrayList.get(position -1).getmShortDescription();
                mPrevButton.setText(stepName);
            } else {
                mPrevButton.setVisibility(View.VISIBLE);
                stepName = mStepArrayList.get(position -1).getmShortDescription();
                mPrevButton.setText(stepName);
                mNextButton.setVisibility(View.VISIBLE);
                stepName = mStepArrayList.get(position + 1).getmShortDescription();
                mNextButton.setText(stepName);
            }
        } else {
            mPrevButton.setVisibility(View.GONE);
            mNextButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mStepDetailFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, STEPS_FRAGMENT, mStepDetailFragment);
        }
        outState.putInt(CURRENT_STEP, mCurrentStep);
        outState.putParcelable(CURRENT_STEP, mStep);
        mBundle = outState;
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.bt_next)
    public void setmNextButton(){
        mCurrentStep += 1;
        setupFragment(mBundle);
        setupButtons(mCurrentStep);
    }

    @OnClick(R.id.bt_prev)
    public void setmPrevButton(){
        mCurrentStep -= 1;
        setupFragment(mBundle);
        setupButtons(mCurrentStep);
    }

    //TODO Fix the Instacesave
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStep = savedInstanceState.getParcelable(CURRENT_STEP);
        mBundle = savedInstanceState;
        mCurrentStep = savedInstanceState.getInt(CURRENT_INDEX);
    }
}
