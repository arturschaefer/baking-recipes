package com.example.arturschaefer.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.arturschaefer.bakingapp.R;
import com.example.arturschaefer.bakingapp.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{

    private ArrayList<Ingredient> mIngredientArrayList;
    private Context mContext;

    public IngredientAdapter(ArrayList<Ingredient> ingredientArrayList){
        this.mIngredientArrayList = ingredientArrayList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View itemView = LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_igredients, parent, false);

        return new IngredientAdapter.IngredientViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = mIngredientArrayList.get(position);
        holder.ingredientTextView.setText(ingredient.getmIngredient().toUpperCase());
        String colorString = (position % 2 == 1) ? "#82b1ff" : "#e3f2fd";
        holder.itemView.setBackgroundColor(Color.parseColor(colorString));
        holder.quantityTextView.setText(String.valueOf(ingredient.getmQuantity()).toUpperCase()
                + " "
                + ingredient.getmMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredientArrayList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_ingredient)
        LinearLayout linearLayout;
        @BindView(R.id.tv_ingredient)
        TextView ingredientTextView;
        @BindView(R.id.tv_quantity)
        TextView quantityTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
