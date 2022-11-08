package com.example.licenta.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private String category;
    private String description;
    private boolean state;


    public Task(){}
    public Task(String description, boolean state,String category) {
        this.description = description;
        this.state = state;
        this.category=category;
    }


    protected Task(Parcel in) {
        category = in.readString();
        description = in.readString();
        state = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(description);
        dest.writeByte((byte) (state ? 1 : 0));
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
