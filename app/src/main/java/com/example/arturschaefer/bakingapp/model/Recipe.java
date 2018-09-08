package com.example.arturschaefer.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipe implements Parcelable{

    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("ingredients")
    private ArrayList<Ingredient> mIngredientList;
    @SerializedName("steps")
    private ArrayList<Step> mStepList;
    @SerializedName("servings")
    private int mServings;
    @SerializedName("image")
    private String mImage;

    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        if (in.readByte() == 0x01) {
            mIngredientList = new ArrayList<Ingredient>();
            in.readList(mIngredientList, Ingredient.class.getClassLoader());
        } else {
            mIngredientList = null;
        }
        if (in.readByte() == 0x01) {
            mStepList = new ArrayList<Step>();
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

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ArrayList<Ingredient> getmIngredientList() {
        return mIngredientList;
    }

    public void setmIngredientList(ArrayList<Ingredient> mIngredientList) {
        this.mIngredientList = mIngredientList;
    }

    public ArrayList<Step> getmStepList() {
        return mStepList;
    }

    public void setmStepList(ArrayList<Step> mStepList) {
        this.mStepList = mStepList;
    }

    public int getmServings() {
        return mServings;
    }

    public void setmServings(int mServings) {
        this.mServings = mServings;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
