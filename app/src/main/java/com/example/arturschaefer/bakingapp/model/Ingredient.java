package com.example.arturschaefer.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable{

    @SerializedName("quantity")
    @Expose
    private Double mQuantity;
    @SerializedName("measure")
    @Expose
    private String mMeasure;
    @SerializedName("ingredient")
    @Expose
    private String mIngredient;

    protected Ingredient(Parcel in) {
        mQuantity = in.readByte() == 0x00 ? null : in.readDouble();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mQuantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mQuantity);
        }
        dest.writeString(mMeasure);
        dest.writeString(mIngredient);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public Double getmQuantity() {
        return mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public String getmIngredient() {
        return mIngredient;
    }
}
