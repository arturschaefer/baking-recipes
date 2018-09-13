package com.example.arturschaefer.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arturschaefer.bakingapp.adapter.StepAdapter;
import com.example.arturschaefer.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment implements StepAdapter.StepListener{
    private static final String LOG_TAG = StepFragment.class.getSimpleName();
    private static final String STEP_LIST = "step_list";
    private static final String CURRENT_INDEX = "current_index";
    public static final String TWO_PANE = "two_pane";
    private static final String CURRENT_STEP = "current_step";


    @BindView(R.id.rv_step)
    RecyclerView mRecyclerView;

    private StepAdapter mStepAdapter;
    private ArrayList<Step> mStepArrayList;
    private int mCurrentStep;
    private boolean mTwoPane;
    private Bundle mBundle;
    private StepDetailFragment mStepFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);
        ButterKnife.bind(this, view);

        mBundle = savedInstanceState;

        if(getArguments() != null){
            mStepArrayList = getArguments().getParcelableArrayList(RecipeDetailActivity.FRAGMENT_STEPS);
            mTwoPane = getArguments().getBoolean(TWO_PANE);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mStepAdapter = new StepAdapter(mStepArrayList, this);
        mRecyclerView.setAdapter(mStepAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onStepClick(Step step) {
        try {
            mCurrentStep = mStepArrayList.indexOf(step);
            if (!mTwoPane) {
                Intent intent = new Intent(getActivity(), StepDetailActivity.class);
                intent.putParcelableArrayListExtra(STEP_LIST, mStepArrayList);
                intent.putExtra(CURRENT_INDEX, mCurrentStep);
                startActivity(intent);
            } else {
                mStepFragment = new StepDetailFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                if (mBundle == null)
                    mBundle = new Bundle();

                mBundle.putParcelable(CURRENT_STEP, step);
                mBundle.putBoolean(TWO_PANE, mTwoPane);

                mStepFragment.setArguments(mBundle);
                fragmentTransaction.replace(R.id.exo_player,
                        mStepFragment).commit();
            }
        } catch (Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }
}
