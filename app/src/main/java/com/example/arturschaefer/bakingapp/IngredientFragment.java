package com.example.arturschaefer.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arturschaefer.bakingapp.adapter.IngredientAdapter;
import com.example.arturschaefer.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFragment extends Fragment {
    private final String LOG_TAG = IngredientFragment.class.getSimpleName();

    @BindView(R.id.rv_ingredients)
    RecyclerView mRecyclerView;

    private IngredientAdapter mIngredientAdapter;
    private ArrayList<Ingredient> mIngredientArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients_list, container, false);
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));

        try{
            mIngredientArrayList = getArguments().getParcelableArrayList(RecipeDetailActivity.FRAGMENT_INGREDIENTS);
            mIngredientAdapter = new IngredientAdapter(mIngredientArrayList);
            mRecyclerView.setAdapter(mIngredientAdapter);
        } catch (Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }


        return view;
    }
}
