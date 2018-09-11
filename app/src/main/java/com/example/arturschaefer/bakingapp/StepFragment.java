package com.example.arturschaefer.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


    @BindView(R.id.rv_step)
    RecyclerView mRecyclerView;

    private StepAdapter mStepAdapter;
    private ArrayList<Step> mStepArrayList;
    private int mCurrentStep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);
        ButterKnife.bind(this, view);

        if(getArguments() != null){
            mStepArrayList = getArguments().getParcelableArrayList(RecipeDetailActivity.FRAGMENT_STEPS);
            //TODO Tablet check
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
        mCurrentStep = mStepArrayList.indexOf(step);
        //TODO implement tablet check
        Intent intent = new Intent(getActivity(), StepDetailActivity.class);
        intent.putParcelableArrayListExtra(STEP_LIST, mStepArrayList);
        intent.putExtra(CURRENT_INDEX, mCurrentStep);
        startActivity(intent);
    }
}
