package com.example.licenta.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Exercice implements Parcelable {

    public String name ;
    public int duration;
    public int reps;
    public double kcalBurned;

    public Exercice(){}

    protected Exercice(Parcel in) {
        name = in.readString();
        duration = in.readInt();
        kcalBurned = in.readDouble();
        reps=in.readInt();
    }

    public static final Creator<Exercice> CREATOR = new Creator<Exercice>() {
        @Override
        public Exercice createFromParcel(Parcel in) {
            return new Exercice(in);
        }

        @Override
        public Exercice[] newArray(int size) {
            return new Exercice[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getKcalBurned() {
        return kcalBurned;
    }

    public void setKcalBurned(double kcalBurned) {
        this.kcalBurned = kcalBurned;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(duration);
        dest.writeDouble(kcalBurned);
    }
}
