package com.example.licenta;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.licenta.data.FoodReportModel;
import com.example.licenta.data.OnGetDataListener;
import com.example.licenta.data.User;
import com.google.firebase.FirebaseError;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class CaloriesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CaloriesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CaloriesFragment newInstance(String param1, String param2) {
        CaloriesFragment fragment = new CaloriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static synchronized void mReadDataOnce(DatabaseReference child, ValueEventListener ok) {
        child.addListenerForSingleValueEvent(ok);
    }

    public static synchronized void readData(DatabaseReference ref , final OnGetDataListener listener) {
        listener.onStart();
        ValueEventListener l = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();

            }
        };
        mReadDataOnce(ref,l);
        ref.removeEventListener(l);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    ArrayList pieData = new ArrayList<>();
    PieChartView pieChartView;
    TextView todayTv,totalKcals,netKcals,goalKcals,breakfastKcal,lunchKcal,dinnerKcal,snacksKcal;
    FirebaseDatabase fDatabase=FirebaseDatabase.getInstance();
    User user=new User();
    double BreakfastCalories=0,LunchCalories=0,DinnerCalories=0,SnacksCalories=0;

    DecimalFormat df = new DecimalFormat("0.00");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calories, container, false);
        pieChartView = rootView.findViewById(R.id.chart);
        todayTv = rootView.findViewById(R.id.caloriesToday);
        totalKcals = rootView.findViewById(R.id.total_calories);
        netKcals = rootView.findViewById(R.id.net_calories);
        goalKcals = rootView.findViewById(R.id.goalCalories);
        breakfastKcal = rootView.findViewById(R.id.breakfastKcal);
        lunchKcal = rootView.findViewById(R.id.lunchKcal);
        dinnerKcal = rootView.findViewById(R.id.dinnerKcal);
        snacksKcal = rootView.findViewById(R.id.snacksKcal);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Timestamp today = getActivity().getIntent().getParcelableExtra("today");
        todayTv.setText(format.format(today.toDate()));
        readData(fDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()), new OnGetDataListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
        readData(fDatabase.getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Breakfast"), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    FoodReportModel post = child.getValue(FoodReportModel.class);
                    BreakfastCalories += post.calories;
                }

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
        readData(fDatabase.getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Lunch"), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    FoodReportModel post = child.getValue(FoodReportModel.class);
                    LunchCalories += post.calories;
                }

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
        readData(fDatabase.getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Dinner"), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    FoodReportModel post = child.getValue(FoodReportModel.class);
                    DinnerCalories += post.calories;
                }
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure() {

            }
        });
        readData(fDatabase.getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Snacks"), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    FoodReportModel post = child.getValue(FoodReportModel.class);
                    SnacksCalories += post.calories;

                }
                pieData.add(new SliceValue((float) BreakfastCalories, Color.parseColor("#ffa600")).setLabel((df.format(BreakfastCalories * 100 / user.getCurrentDiary().getKcal_consumed())) + "%"));
                pieData.add(new SliceValue((float) LunchCalories, Color.parseColor("#003f5c")).setLabel((df.format(LunchCalories * 100 / user.getCurrentDiary().getKcal_consumed())) + "%"));
                pieData.add(new SliceValue((float) DinnerCalories, Color.parseColor("#7a5195")).setLabel((df.format(DinnerCalories * 100 / user.getCurrentDiary().getKcal_consumed())) + "%"));
                pieData.add(new SliceValue((float) SnacksCalories, Color.parseColor("#ef5675")).setLabel((df.format(SnacksCalories * 100 / user.getCurrentDiary().getKcal_consumed())) + "%"));
                PieChartData pieChartData = new PieChartData(pieData);
                pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                pieChartData.setHasCenterCircle(false).setCenterText1("").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));

                pieChartView.setPieChartData(pieChartData);
                totalKcals.setText((df.format(user.getCurrentDiary().getKcal_consumed() - user.getCurrentDiary().getKcal_burned())) + "");
                netKcals.setText(df.format(user.getCurrentDiary().getKcal_consumed()) + "");
                goalKcals.setText(df.format(user.getGoal().getKcalFit()) + " ");
                breakfastKcal.setText(df.format(BreakfastCalories) + " kcal");
                lunchKcal.setText(df.format(LunchCalories) + " kcal");
                dinnerKcal.setText(df.format(DinnerCalories) + " kcal");
                snacksKcal.setText(df.format(SnacksCalories) + " kcal");
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });

        return rootView;
    }
}
