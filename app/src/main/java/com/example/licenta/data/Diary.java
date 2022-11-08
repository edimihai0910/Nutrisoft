package com.example.licenta.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;
import com.google.firebase.database.Exclude;
import com.google.type.DateTime;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Diary implements Parcelable {
    int id;
    Map<String, Object> diary;
    public double kcal_consumed;
    public double kcal_burned;
    public double kcal_left;
    public double fat_consumed;
    public double carbs_consumed;
    public double protein_consumed;
    public double sugars_consumed;
    public double potassium_consumed;
    public double sodium_consumed;
    public double fiber_consumed;
    public long currentDate;
    public long startDate;
    public long endDate;

    public Diary() {
    }

    public Diary(Date startDate, Date now,Date endDate) {
        this.startDate = startDate.getTime();
        this.currentDate = now.getTime();
        this.endDate = endDate.getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Object> getDiary() {
        return diary;
    }

    public void setDiary(Map<String, Object> diary) {
        this.diary = diary;
    }

    public double getKcal_consumed() {
        return kcal_consumed;
    }

    public void setKcal_consumed(double kcal_consumed) {
        this.kcal_consumed = kcal_consumed;
    }

    public double getKcal_burned() {
        return kcal_burned;
    }

    public void setKcal_burned(double kcal_burned) {
        this.kcal_burned = kcal_burned;
    }

    public double getKcal_left() {
        return kcal_left;
    }

    public void setKcal_left(double kcal_left) {
        this.kcal_left = kcal_left;
    }

    public double getFat_consumed() {
        return fat_consumed;
    }

    public void setFat_consumed(double fat_consumed) {
        this.fat_consumed = fat_consumed;
    }

    public double getCarbs_consumed() {
        return carbs_consumed;
    }

    public void setCarbs_consumed(double carbs_consumed) {
        this.carbs_consumed = carbs_consumed;
    }

    public double getProtein_consumed() {
        return protein_consumed;
    }

    public void setProtein_consumed(double protein_consumed) {
        this.protein_consumed = protein_consumed;
    }

    public double getSugars_consumed() {
        return sugars_consumed;
    }

    public void setSugars_consumed(double sugars_consumed) {
        this.sugars_consumed = sugars_consumed;
    }

    public double getPotassium_consumed() {
        return potassium_consumed;
    }

    public void setPotassium_consumed(double potassium_consumed) {
        this.potassium_consumed = potassium_consumed;
    }

    public double getSodium_consumed() {
        return sodium_consumed;
    }

    public void setSodium_consumed(double sodium_consumed) {
        this.sodium_consumed = sodium_consumed;
    }

    public double getFiber_consumed() {
        return fiber_consumed;
    }

    public void setFiber_consumed(double fiber_consumed) {
        this.fiber_consumed = fiber_consumed;
    }

    public long getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(long currentDate) {
        this.currentDate = currentDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public Diary(Parcel in) {
        id = in.readInt();
        kcal_consumed = in.readDouble();
        kcal_burned = in.readDouble();
        kcal_left = in.readDouble();
        fat_consumed = in.readDouble();
        carbs_consumed = in.readDouble();
        protein_consumed = in.readDouble();
        sugars_consumed = in.readDouble();
        potassium_consumed = in.readDouble();
        sodium_consumed = in.readDouble();
        fiber_consumed = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(kcal_consumed);
        dest.writeDouble(kcal_burned);
        dest.writeDouble(kcal_left);
        dest.writeDouble(fat_consumed);
        dest.writeDouble(carbs_consumed);
        dest.writeDouble(protein_consumed);
        dest.writeDouble(sugars_consumed);
        dest.writeDouble(potassium_consumed);
        dest.writeDouble(sodium_consumed);
        dest.writeDouble(fiber_consumed);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Diary> CREATOR = new Creator<Diary>() {
        @Override
        public Diary createFromParcel(Parcel in) {
            return new Diary(in);
        }

        @Override
        public Diary[] newArray(int size) {
            return new Diary[size];
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }
    @Exclude
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }
    @Exclude
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
   @Exclude
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
