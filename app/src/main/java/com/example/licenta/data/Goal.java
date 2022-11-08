package com.example.licenta.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Goal implements Parcelable {

    int current_weight;
    int height;
    int target_weight;
    int i_want_to;
    float weekly_goal;
    Date goal_date;
    int activity_level;
    double energy_bmr;
    double proteins_bmr;
    double carbs_bmr ;
    double fat_bmr;
    double energy_diet;
    double proteins_diet;
    double carbs_diet;
    double fat_diet;
    double energy_with_activity;
    double proteins_with_activity;
    double carbs_with_activity;
    double fat_with_activity;
    double energy_with_activity_and_diet;
    double proteins_with_activity_and_diet;
    double carbs_with_activity_and_diet;
    double fat_with_activity_and_diet;
    double kcalFit;
    double fatFit;
    double carbsFit;
    double proteinsFit;

    public Goal(){}

    public Goal(int current_weight, int height, int target_weight, int i_want_to, float weekly_goal, Date goal_date, int activity_level, double energy_bmr, double proteins_bmr, double carbs_bmr, double fat_bmr, double energy_diet, double proteins_diet, double carbs_diet, double fat_diet, double energy_with_activity, double proteins_with_activity, double carbs_with_activity, double fat_with_activity, double energy_with_activity_and_diet, double proteins_with_activity_and_diet, double carbs_with_activity_and_diet, double fat_with_activity_and_diet) {
        this.current_weight = current_weight;
        this.height = height;
        this.target_weight = target_weight;
        this.i_want_to = i_want_to;
        this.weekly_goal = weekly_goal;
        this.goal_date = goal_date;
        this.activity_level = activity_level;
        this.energy_bmr = energy_bmr;
        this.proteins_bmr = proteins_bmr;
        this.carbs_bmr = carbs_bmr;
        this.fat_bmr = fat_bmr;
        this.energy_diet = energy_diet;
        this.proteins_diet = proteins_diet;
        this.carbs_diet = carbs_diet;
        this.fat_diet = fat_diet;
        this.energy_with_activity = energy_with_activity;
        this.proteins_with_activity = proteins_with_activity;
        this.carbs_with_activity = carbs_with_activity;
        this.fat_with_activity = fat_with_activity;
        this.energy_with_activity_and_diet = energy_with_activity_and_diet;
        this.proteins_with_activity_and_diet = proteins_with_activity_and_diet;
        this.carbs_with_activity_and_diet = carbs_with_activity_and_diet;
        this.fat_with_activity_and_diet = fat_with_activity_and_diet;
    }


    protected Goal(Parcel in) {
        current_weight = in.readInt();
        height = in.readInt();
        target_weight = in.readInt();
        i_want_to = in.readInt();
        weekly_goal = in.readFloat();
        activity_level = in.readInt();
        energy_bmr = in.readDouble();
        proteins_bmr = in.readDouble();
        carbs_bmr = in.readDouble();
        fat_bmr = in.readDouble();
        energy_diet = in.readDouble();
        proteins_diet = in.readDouble();
        carbs_diet = in.readDouble();
        fat_diet = in.readDouble();
        energy_with_activity = in.readDouble();
        proteins_with_activity = in.readDouble();
        carbs_with_activity = in.readDouble();
        fat_with_activity = in.readDouble();
        energy_with_activity_and_diet = in.readDouble();
        proteins_with_activity_and_diet = in.readDouble();
        carbs_with_activity_and_diet = in.readDouble();
        fat_with_activity_and_diet = in.readDouble();
    }

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };


    public int getCurrent_weight() {
        return current_weight;
    }

    public void setCurrent_weight(int current_weight) {
        this.current_weight = current_weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTarget_weight() {
        return target_weight;
    }

    public void setTarget_weight(int target_weight) {
        this.target_weight = target_weight;
    }

    public int getI_want_to() {
        return i_want_to;
    }

    public void setI_want_to(int i_want_to) {
        this.i_want_to = i_want_to;
    }

    public float getWeekly_goal() {
        return weekly_goal;
    }

    public void setWeekly_goal(float weekly_goal) {
        this.weekly_goal = weekly_goal;
    }

    public Date getGoal_date() {
        return goal_date;
    }

    public void setGoal_date(Date goal_date) {
        this.goal_date = goal_date;
    }

    public int getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(int activity_level) {
        this.activity_level = activity_level;
    }

    public double getEnergy_bmr() {
        return energy_bmr;
    }

    public void setEnergy_bmr(double energy_bmr) {
        this.energy_bmr = energy_bmr;
    }

    public double getProteins_bmr() {
        return proteins_bmr;
    }

    public void setProteins_bmr(double proteins_bmr) {
        this.proteins_bmr = proteins_bmr;
    }

    public double getCarbs_bmr() {
        return carbs_bmr;
    }

    public void setCarbs_bmr(double carbs_bmr) {
        this.carbs_bmr = carbs_bmr;
    }

    public double getFat_bmr() {
        return fat_bmr;
    }

    public void setFat_bmr(double fat_bmr) {
        this.fat_bmr = fat_bmr;
    }

    public double getEnergy_diet() {
        return energy_diet;
    }

    public void setEnergy_diet(double energy_diet) {
        this.energy_diet = energy_diet;
    }

    public double getProteins_diet() {
        return proteins_diet;
    }

    public void setProteins_diet(double proteins_diet) {
        this.proteins_diet = proteins_diet;
    }

    public double getCarbs_diet() {
        return carbs_diet;
    }

    public void setCarbs_diet(double carbs_diet) {
        this.carbs_diet = carbs_diet;
    }

    public double getFat_diet() {
        return fat_diet;
    }

    public void setFat_diet(double fat_diet) {
        this.fat_diet = fat_diet;
    }

    public double getEnergy_with_activity() {
        return energy_with_activity;
    }

    public void setEnergy_with_activity(double energy_with_activity) {
        this.energy_with_activity = energy_with_activity;
    }

    public double getProteins_with_activity() {
        return proteins_with_activity;
    }

    public void setProteins_with_activity(double proteins_with_activity) {
        this.proteins_with_activity = proteins_with_activity;
    }

    public double getCarbs_with_activity() {
        return carbs_with_activity;
    }

    public void setCarbs_with_activity(double carbs_with_activity) {
        this.carbs_with_activity = carbs_with_activity;
    }

    public double getFat_with_activity() {
        return fat_with_activity;
    }

    public void setFat_with_activity(double fat_with_activity) {
        this.fat_with_activity = fat_with_activity;
    }

    public double getEnergy_with_activity_and_diet() {
        return energy_with_activity_and_diet;
    }

    public void setEnergy_with_activity_and_diet(double energy_with_activity_and_diet) {
        this.energy_with_activity_and_diet = energy_with_activity_and_diet;
    }

    public double getProteins_with_activity_and_diet() {
        return proteins_with_activity_and_diet;
    }

    public void setProteins_with_activity_and_diet(double proteins_with_activity_and_diet) {
        this.proteins_with_activity_and_diet = proteins_with_activity_and_diet;
    }

    public double getCarbs_with_activity_and_diet() {
        return carbs_with_activity_and_diet;
    }

    public void setCarbs_with_activity_and_diet(double carbs_with_activity_and_diet) {
        this.carbs_with_activity_and_diet = carbs_with_activity_and_diet;
    }

    public double getFat_with_activity_and_diet() {
        return fat_with_activity_and_diet;
    }

    public void setFat_with_activity_and_diet(double fat_with_activity_and_diet) {
        this.fat_with_activity_and_diet = fat_with_activity_and_diet;
    }

    public double getKcalFit() {
        return kcalFit;
    }

    public void setKcalFit(double kcalFit) {
        this.kcalFit = kcalFit;
    }

    public double getFatFit() {
        return fatFit;
    }

    public void setFatFit(double fatFit) {
        this.fatFit = fatFit;
    }

    public double getCarbsFit() {
        return carbsFit;
    }

    public void setCarbsFit(double carbsFit) {
        this.carbsFit = carbsFit;
    }

    public double getProteinsFit() {
        return proteinsFit;
    }

    public void setProteinsFit(double proteinsFit) {
        this.proteinsFit = proteinsFit;
    }

    @Exclude
    public double getFatNeededBMR() { return fat_bmr/9;}
    @Exclude
    public double getFatNeededWithActivity( ){ return fat_with_activity/9;}
    @Exclude
    public double getFatNeededWithActivityAndDiet()
    {
        return fat_with_activity_and_diet/9;
    }
    @Exclude
    public double getFatNeededWithDiet(){return fat_diet/9;}
    @Exclude
    public double getCarbsNeededWithActivityAndDiet()
    {
        return energy_with_activity_and_diet/8;
    }
    @Exclude
    public double getCarbsNeededBMR() { return carbs_bmr/8;}
    @Exclude
    public double getCarbsNeededWithActivity() { return carbs_with_activity/8;}
    @Exclude
    public double getCarbsNeededWithDiet(){return carbs_diet/8;}
    @Exclude
    public double getProteinNeeded()
    {
        if (activity_level==0)
        {
            return 0.8*current_weight;
        }
        else if( activity_level==1)
        {
            return current_weight;
        }
        else if(activity_level==2)
        {
            return 1.15*current_weight;
        }
        else if(activity_level==3)
        {
            return 1.3*current_weight;
        }
        else if(activity_level==4)
        {
            return current_weight*1.55;
        }
        else return current_weight*1.8;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(current_weight);
        dest.writeInt(height);
        dest.writeInt(target_weight);
        dest.writeInt(i_want_to);
        dest.writeFloat(weekly_goal);
        dest.writeInt(activity_level);
        dest.writeDouble(energy_bmr);
        dest.writeDouble(proteins_bmr);
        dest.writeDouble(carbs_bmr);
        dest.writeDouble(fat_bmr);
        dest.writeDouble(energy_diet);
        dest.writeDouble(proteins_diet);
        dest.writeDouble(carbs_diet);
        dest.writeDouble(fat_diet);
        dest.writeDouble(energy_with_activity);
        dest.writeDouble(proteins_with_activity);
        dest.writeDouble(carbs_with_activity);
        dest.writeDouble(fat_with_activity);
        dest.writeDouble(energy_with_activity_and_diet);
        dest.writeDouble(proteins_with_activity_and_diet);
        dest.writeDouble(carbs_with_activity_and_diet);
        dest.writeDouble(fat_with_activity_and_diet);
    }
}
