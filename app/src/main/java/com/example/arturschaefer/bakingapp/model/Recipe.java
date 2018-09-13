package com.example.arturschaefer.bakingapp.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturschaefer.bakingapp.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Recipe extends AbstractItem<Recipe, Recipe.ViewHolder> implements Parcelable {

    @SerializedName("id")
    @Expose
    private int mId;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> mIngredientList;
    @SerializedName("steps")
    @Expose
    private ArrayList<Step> mStepList;
    @SerializedName("servings")
    @Expose
    private int mServings;
    @SerializedName("image")
    @Expose
    private String mImage;

    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        if (in.readByte() == 0x01) {
            mIngredientList = new ArrayList<>();
            in.readList(mIngredientList, Ingredient.class.getClassLoader());
        } else {
            mIngredientList = null;
        }
        if (in.readByte() == 0x01) {
            mStepList = new ArrayList<>();
            in.readList(mStepList, Step.class.getClassLoader());
        } else {
            mStepList = null;
        }
        mServings = in.readInt();
        mImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        if (mIngredientList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mIngredientList);
        }
        if (mStepList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mStepList);
        }
        dest.writeInt(mServings);
        dest.writeString(mImage);
    }

    @SuppressWarnings("unused")
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public ArrayList<Ingredient> getmIngredientList() {
        return mIngredientList;
    }

    public ArrayList<Step> getmStepList() {
        return mStepList;
    }


    public int getmServings() {
        return mServings;
    }

    public String getmImage() {
        return mImage;
    }

    @Override
    public int getType() {
        return R.id.recipe_detail_container;
    }

    @SuppressLint("ResourceType")
    @Override
    public int getLayoutRes() {
        return R.id.recipe_list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_receipt)
        ImageView imageView;
        @BindView(R.id.textview_recipe_name)
        TextView recipeName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Recipe recipe){
            recipeName.setText(recipe.getmName());
            if(recipe.getmImage().isEmpty()){
                Picasso.get().load(R.drawable.luck_biscuit).into(imageView);
            } else {
                Picasso.get().load(recipe.getmImage()).into(imageView);
            }
        }
    }
}
