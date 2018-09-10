package com.example.arturschaefer.bakingapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.arturschaefer.bakingapp.IngredientFragment;
import com.example.arturschaefer.bakingapp.StepFragment;

public class DetailPagerAdapter extends FragmentPagerAdapter {
    private IngredientFragment mIngredientFragment;
    private StepFragment mStepFragment;

    public DetailPagerAdapter (FragmentManager fragmentManager,
                                        IngredientFragment ingredientFragment,
                                        StepFragment stepFragment) {
        super(fragmentManager);
        this.mIngredientFragment = ingredientFragment;
        this.mStepFragment = stepFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mIngredientFragment;
            case 1:
                return mStepFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Ingredients";
            case 1:
                return "Steps";
        }
        return null;
    }
}
