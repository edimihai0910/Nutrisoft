package com.example.licenta.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class User implements Parcelable {
    private int Id;
    private String FirstName;
    private String LastName;
    private String Username;
    private int Age;
    private String Sex;
    private String Country;
    private String Note;
    private Goal goal;
    private List<Diary> historyDiaries;
    private Diary currentDiary;
    private Map<String, Object> mealPlans;

    public User() {
    }

    public User(int id, String firstName, String lastName, String email, String username, String password, int age, String sex, String country, String note, Goal goal, Diary diary, Map<String, Object> mealPlans) {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
        Username = username;
        Age = age;
        Sex = sex;
        Country = country;
        Note = note;
        this.goal = goal;
        this.currentDiary = diary;
        this.mealPlans = mealPlans;
    }

    protected User(Parcel in) {
        Id = in.readInt();
        FirstName = in.readString();
        LastName = in.readString();
        Username = in.readString();
        Age = in.readInt();
        Sex = in.readString();
        Country = in.readString();
        Note = in.readString();
        goal = in.readParcelable(Goal.class.getClassLoader());
        historyDiaries = in.createTypedArrayList(Diary.CREATOR);
        currentDiary = in.readParcelable(Diary.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(Username);
        dest.writeInt(Age);
        dest.writeString(Sex);
        dest.writeString(Country);
        dest.writeString(Note);
        dest.writeParcelable(goal, flags);
        dest.writeTypedList(historyDiaries);
        dest.writeParcelable(currentDiary, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirstName() {
        return FirstName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public List<Diary> getHistoryDiaries() {
        return historyDiaries;
    }

    public void setHistoryDiaries(List<Diary> historyDiaries) {
        this.historyDiaries = historyDiaries;
    }

    public Diary getCurrentDiary() {
        return currentDiary;
    }

    public void setCurrentDiary(Diary currentDiary) {
        this.currentDiary = currentDiary;
    }

    public Map<String, Object> getMealPlans() {
        return mealPlans;
    }

    public void setMealPlans(Map<String, Object> mealPlans) {
        this.mealPlans = mealPlans;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }
}


