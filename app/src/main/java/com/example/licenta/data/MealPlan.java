package com.example.licenta.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealPlan implements Parcelable {

    private String name;
    private String description;
    private String category;
    private int noDays;
    private String timePerWeeks;
    private int currentDay;
    private String difficulty;
    private List<Task> tasksPerDay;
    private List<Task> proteinsTasks;
    private List<Task> lowCarbTasks;
    private List<Task> plantyBasedTasks;
    private List<Task> muscleGrowthTasks;
    private List<String> descriptionPerDay;
    private String imageUrl;
    private boolean isStarted;
    private boolean isFavorite;

    public List<Task> getTasksPerDay() {
        return tasksPerDay;
    }

    public List<Task> getProteinsTasks() {
        return proteinsTasks;
    }

    public void setProteinsTasks(List<Task> proteinsTasks) {
        this.proteinsTasks = proteinsTasks;
    }

    public List<Task> getLowCarbTasks() {
        return lowCarbTasks;
    }

    public void setLowCarbTasks(List<Task> lowCarbTasks) {
        this.lowCarbTasks = lowCarbTasks;
    }

    public List<Task> getPlantyBasedTasks() {
        return plantyBasedTasks;
    }

    public void setPlantyBasedTasks(List<Task> plantyBasedTasks) {
        this.plantyBasedTasks = plantyBasedTasks;
    }

    public List<Task> getMuscleGrowthTasks() {
        return muscleGrowthTasks;
    }

    public void setMuscleGrowthTasks(List<Task> muscleGrowthTasks) {
        this.muscleGrowthTasks = muscleGrowthTasks;
    }

    public MealPlan(){}

    public MealPlan(String name, String description, String category, int noDays, String timePerWeeks, int currentDay, String difficulty, List<Task> tasksPerDay, List<String> descriptionPerDay, String imageUrl, boolean isStarted, boolean isFavorite) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.noDays = noDays;
        this.timePerWeeks = timePerWeeks;
        this.currentDay = currentDay;
        this.difficulty = difficulty;
        this.tasksPerDay = tasksPerDay;
        this.descriptionPerDay = descriptionPerDay;
        this.imageUrl = imageUrl;
        this.isStarted = isStarted;
        this.isFavorite = isFavorite;
    }

    protected MealPlan(Parcel in) {
        name = in.readString();
        description = in.readString();
        category = in.readString();
        noDays = in.readInt();
        timePerWeeks = in.readString();
        currentDay = in.readInt();
        difficulty = in.readString();
        tasksPerDay = in.createTypedArrayList(Task.CREATOR);
        descriptionPerDay = in.createStringArrayList();
        imageUrl = in.readString();
        isStarted = in.readByte() != 0;
        isFavorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeInt(noDays);
        dest.writeString(timePerWeeks);
        dest.writeInt(currentDay);
        dest.writeString(difficulty);
        dest.writeTypedList(tasksPerDay);
        dest.writeStringList(descriptionPerDay);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isStarted ? 1 : 0));
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MealPlan> CREATOR = new Creator<MealPlan>() {
        @Override
        public MealPlan createFromParcel(Parcel in) {
            return new MealPlan(in);
        }

        @Override
        public MealPlan[] newArray(int size) {
            return new MealPlan[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNoDays() {
        return noDays;
    }

    public void setNoDays(int noDays) {
        this.noDays = noDays;
    }

    public String getTimePerWeeks() {
        return timePerWeeks;
    }

    public void setTimePerWeeks(String timePerWeeks) {
        this.timePerWeeks = timePerWeeks;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Task> getTasksPerDay(int currentDay) {
        List<Task>tasksPerDayGet= new ArrayList<>() ;
        for(int i =0 ; i<tasksPerDay.size();i++)
            if(tasksPerDay.get(i).getCategory().equals(category))
                tasksPerDayGet.add(tasksPerDay.get(i));
        if (currentDay==1)
        {
            return tasksPerDayGet.subList(0,3);
        }
        else if(currentDay==2)
        {
            return tasksPerDayGet.subList(4,7);
        }
        else if(currentDay==3)
        {
            return tasksPerDayGet.subList(6,9);
        }
        else if (currentDay==4)
        {
            return tasksPerDayGet.subList(9,12);
        }
        return tasksPerDay;
    }

    public void setTasksPerDay(List<Task> tasksPerDay) {
        this.tasksPerDay = tasksPerDay;
    }

    public List<String> getDescriptionPerDay() {
        return descriptionPerDay;
    }

    public String getDescriptionPerDay(int index)
    {
        if (index>-1&&index<descriptionPerDay.size())
        {
            return descriptionPerDay.get(index);
        }
        return "Description is null";
    }


    public void setDescriptionPerDay(List<String> descriptionPerDay) {
        this.descriptionPerDay = descriptionPerDay;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
