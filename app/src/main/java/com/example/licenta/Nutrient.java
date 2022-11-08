package com.example.licenta;

public class Nutrient{
    private String nutrient;
    private double value;
    private double needed;
    public Nutrient(String nutrient, double value,double needed) {
        this.nutrient = nutrient;
        this.value = value;
        this.needed=needed;
    }

    public String getNutrient() {
        return nutrient;
    }

    public double getNeeded() {
        return needed;
    }

    public void setNeeded(int needed) {
        this.needed = needed;
    }

    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getLeft()
    {
        if(needed>value)
        return needed-value;
        else return 0;
    }
}