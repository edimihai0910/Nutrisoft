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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.licenta.data.OnGetDataListener;
import com.example.licenta.data.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


public class NutrientsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static NutrientsFragment newInstance(String param1, String param2) {
        NutrientsFragment fragment = new NutrientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    public NutrientsFragment() {
        // Required empty public constructor
    }
    public synchronized void readData(DatabaseReference ref , final OnGetDataListener listener) {
        listener.onStart();
        ValueEventListener lv =new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();

            }
        };
        ref.addListenerForSingleValueEvent(lv);
        //ref.removeEventListener(lv);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    DecimalFormat df=new DecimalFormat("0.00");
    User user;
    PieChartView pieChartView;
    ListView listView;
    MyAdapter adapter;
    TextView totalCarbs,goalCarbs,totalProtein,goalProtein,totalFat,goalFat,todayNutrientsTv;
    FirebaseDatabase fDatabase=FirebaseDatabase.getInstance();
    ArrayList<Nutrient> nutrients;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)

    {
        View rootView = inflater.inflate(R.layout.fragment_nutrients, container, false);

        totalCarbs=rootView.findViewById(R.id.carbsTotalNutrients);
        goalCarbs=rootView.findViewById(R.id.carbsGoalNutrients);
        totalFat=rootView.findViewById(R.id.fatTotalNutrients);
        goalFat=rootView.findViewById(R.id.fatGoalNutrients);
        totalProtein=rootView.findViewById(R.id.proteinTotalNutrients);
        goalProtein=rootView.findViewById(R.id.proteinGoalNutrients);
        todayNutrientsTv=rootView.findViewById(R.id.todayNutrientsTv);
        listView = rootView.findViewById(R.id.listHistory);
        pieChartView = rootView.findViewById(R.id.chart);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Timestamp today=getActivity().getIntent().getParcelableExtra("today");
        todayNutrientsTv.setText(format.format(today.toDate()));

        readData(fDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot)
            {
                user=dataSnapshot.getValue(User.class);

                List pieData = new ArrayList<>();
                pieData.add(new SliceValue((float) (user.getCurrentDiary().getCarbs_consumed()),Color.parseColor("#0FB153")));
                pieData.add(new SliceValue((float) (user.getCurrentDiary().getFat_consumed()),Color.parseColor("#A434DD")));
                pieData.add(new SliceValue((float) (user.getCurrentDiary().getProtein_consumed()),Color.parseColor("#FF9800")));
                PieChartData pieChartData = new PieChartData(pieData);
                pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                pieChartData.setHasCenterCircle(false).setCenterText1("").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
                pieChartView.setPieChartData(pieChartData);
                totalCarbs.setText(df.format(user.getCurrentDiary().carbs_consumed));
                goalCarbs.setText(df.format(user.getGoal().getCarbsFit()));
                totalFat.setText(df.format(user.getCurrentDiary().fat_consumed));
                goalFat.setText(df.format(user.getGoal().getFatFit()));
                totalProtein.setText(df.format(user.getCurrentDiary().protein_consumed));
                goalProtein.setText(df.format(user.getGoal().getProteinNeeded()));

                nutrients=new ArrayList<>(Arrays.asList(
                        new Nutrient("Protein",user.getCurrentDiary().protein_consumed,user.getGoal().getProteinNeeded()),
                        new Nutrient("Carbs",user.getCurrentDiary().carbs_consumed,user.getGoal().getCarbsFit()),
                        new Nutrient("Sugars",user.getCurrentDiary().sugars_consumed,user.getGoal().getKcalFit()*0.1/4),
                        new Nutrient("Fat",user.getCurrentDiary().fat_consumed,user.getGoal().getFatFit()),
                        new Nutrient("Fiber",user.getCurrentDiary().fiber_consumed,user.getGoal().getKcalFit()/1000*14)
                ));
                adapter = new MyAdapter(getContext(), nutrients);
                listView.setAdapter(adapter);

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        NutrientsFragment.MyAdapter adapter = new NutrientsFragment.MyAdapter(getActivity(), nutrients);
        adapter.notifyDataSetChanged();

        return rootView;
    }

    public class MyAdapter extends ArrayAdapter<Nutrient>
    {
        public MyAdapter(Context context, ArrayList<Nutrient> string_list)
        {
            super(context, 0, string_list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_item6, parent, false);
            }
            parent.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            TextView txtview1 = (TextView) convertView.findViewById(R.id.tv_lvFoodName);
            txtview1.setText(nutrients.get(position).getNutrient());

            TextView totalNutrient=convertView.findViewById(R.id.tv_TotalNutrient);
            totalNutrient.setText(df.format((int)nutrients.get(position).getValue()));

            TextView goalNutrient=convertView.findViewById(R.id.tv_GoalNutrient);
            goalNutrient.setText(df.format((int)nutrients.get(position).getNeeded()));

            TextView leftNutrient=convertView.findViewById(R.id.tv_LeftNutrient);
            leftNutrient.setText(df.format((int)nutrients.get(position).getLeft()));
            ProgressBar progressBar=convertView.findViewById(R.id.pb_nutrientsFragment);

            if(nutrients.get(position).getValue()>nutrients.get(position).getNeeded())
                progressBar.setMax((int) nutrients.get(position).getValue());
            else
                progressBar.setMax((int) nutrients.get(position).getNeeded());

            progressBar.setProgress((int) nutrients.get(position).getValue());

            return convertView;
        }
    }
}