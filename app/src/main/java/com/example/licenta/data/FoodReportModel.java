package com.example.licenta.data;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodReportModel implements Parcelable {
    public String name;
    public String manufacturer;
    public int serving_qty;
    public String serving_unit;
    public double serving_weight_grams;
    public double calories;
    public double fat;
    public double carbs;
    public double protein;
    public double sugars;
    public double potassium;
    public double sodium;
    public double fiber;
    public String imageUrl;

    public FoodReportModel()
    {

    }

    public FoodReportModel(String name, String manufacturer, int serving_qty, String serving_unit, double serving_weight_grams, double calories, double fat, double carbs, double protein, double sugars, double fiber, String imageUrl) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.serving_qty = serving_qty;
        this.serving_unit = serving_unit;
        this.serving_weight_grams = serving_weight_grams;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
        this.sugars = sugars;
        this.fiber = fiber;
        this.imageUrl = imageUrl;
    }

    public FoodReportModel(String name, String manufacturer, int serving_qty, String serving_unit, double serving_weight_grams, Double calories, Double fat, Double carbs, Double protein, String imageUrl) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.serving_qty = serving_qty;
        this.serving_unit = serving_unit;
        this.serving_weight_grams = serving_weight_grams;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
        this.imageUrl = imageUrl;
    }


    protected FoodReportModel(Parcel in) {
        name = in.readString();
        manufacturer = in.readString();
        serving_qty = in.readInt();
        serving_unit = in.readString();
        serving_weight_grams = in.readDouble();
        calories = in.readDouble();
        fat = in.readDouble();
        carbs = in.readDouble();
        protein = in.readDouble();
        sugars = in.readDouble();
        potassium = in.readDouble();
        sodium = in.readDouble();
        fiber = in.readDouble();
        imageUrl = in.readString();
    }

    public static final Creator<FoodReportModel> CREATOR = new Creator<FoodReportModel>() {
        @Override
        public FoodReportModel createFromParcel(Parcel in) {
            return new FoodReportModel(in);
        }

        @Override
        public FoodReportModel[] newArray(int size) {
            return new FoodReportModel[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setServing_qty(int serving_qty) {
        this.serving_qty = serving_qty;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }

    public void setServing_weight_grams(double serving_weight_grams) {
        this.serving_weight_grams = serving_weight_grams;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(manufacturer);
        dest.writeInt(serving_qty);
        dest.writeString(serving_unit);
        dest.writeDouble(serving_weight_grams);
        dest.writeDouble(calories);
        dest.writeDouble(fat);
        dest.writeDouble(carbs);
        dest.writeDouble(protein);
        dest.writeDouble(sugars);
        dest.writeDouble(potassium);
        dest.writeDouble(sodium);
        dest.writeDouble(fiber);
        dest.writeString(imageUrl);
    }
}