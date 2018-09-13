package com.example.arturschaefer.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arturschaefer.bakingapp.R;
import com.example.arturschaefer.bakingapp.model.Recipe;
import com.example.arturschaefer.bakingapp.utils.ImageUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final RecipeListItemClickListener recipeListItemClickListener;
    private List<Recipe> recipes;

    public RecipeAdapter(ArrayList<Recipe> mRecipeList, RecipeListItemClickListener recipeListItemClickListener) {
        this.recipeListItemClickListener = recipeListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.item_recipes, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    public void swapData(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public List<Recipe> getData() {
        return recipes;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageview_receipt)
        AppCompatImageView image;

        @BindView(R.id.textview_recipe_name)
        TextView recipeName;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(@NonNull Recipe recipe) {
            if (!TextUtils.isEmpty(recipe.getmImage())) {
                Picasso.get().load(recipe.getmImage()).into(image);
            }
            else {
                Picasso.get().load(ImageUtil.getImageResId(recipe.getmName())).into(image);
            }
            recipeName.setText(recipe.getmName());
        }

        @Override
        public void onClick(View v) {
            recipeListItemClickListener.onRecipeItemClick(recipes.get(getAdapterPosition()));
        }
    }

    public interface RecipeListItemClickListener {
        void onRecipeItemClick(Recipe recipe);
    }
}