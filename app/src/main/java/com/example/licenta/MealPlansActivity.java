package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.licenta.data.MealPlan;
import com.example.licenta.data.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MealPlansActivity extends AppCompatActivity {
    RadioGroup filters;
    RadioButton all,favorities,lowCarbs,highProtein,plantBased,muscleGrowth,started;
    ListView lv_mealPlans;
    ArrayList<MealPlan> mealList =new ArrayList<>();
    FirebaseDatabase fDatabase=FirebaseDatabase.getInstance();


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plans);
        getSupportActionBar().hide();


        filters=findViewById(R.id.filters);
        all=findViewById(R.id.all);
        favorities=findViewById(R.id.favorities);
        lowCarbs=findViewById(R.id.lowCarbs);
        highProtein=findViewById(R.id.highProtein);
        plantBased=findViewById(R.id.plantBased);
        muscleGrowth=findViewById(R.id.muscleGrowth);
        lv_mealPlans = findViewById(R.id.lv_mealPlans);
        started=findViewById(R.id.startedPlans);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.plans);

        MyAdapter adapter = new MyAdapter(this, mealList);
        lv_mealPlans.setAdapter(adapter);

        filters.check(R.id.all);
        filters.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(all.isChecked())
                {
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mealList.clear();
                            for(DataSnapshot snap:snapshot.getChildren())
                            {
                                MealPlan meal =snap.getValue(MealPlan.class);
                                mealList.add(meal);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(plantBased.isChecked())
                {
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mealList.clear();

                            for(DataSnapshot snap:snapshot.getChildren())
                            {
                                MealPlan meal =snap.getValue(MealPlan.class);
                                if(meal.getCategory().equals("Plant Based"))
                                mealList.add(meal);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(lowCarbs.isChecked())
                {
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mealList.clear();

                            for(DataSnapshot snap:snapshot.getChildren())
                            {
                                MealPlan meal =snap.getValue(MealPlan.class);
                                if(meal.getCategory().equals("Low Carbs"))
                                    mealList.add(meal);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(highProtein.isChecked())
                {
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mealList.clear();

                            for(DataSnapshot snap:snapshot.getChildren())
                            {
                                MealPlan meal =snap.getValue(MealPlan.class);
                                if(meal.getCategory().equals("High Protein"))
                                    mealList.add(meal);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(muscleGrowth.isChecked())
                {
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mealList.clear();

                            for(DataSnapshot snap:snapshot.getChildren())
                            {
                                MealPlan meal =snap.getValue(MealPlan.class);
                                if(meal.getCategory().equals("Growth Muscle"))
                                    mealList.add(meal);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(started.isChecked())
                {
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mealList.clear();

                            for(DataSnapshot snap:snapshot.getChildren())
                            {
                                MealPlan meal =snap.getValue(MealPlan.class);
                                if(meal.isStarted())
                                    mealList.add(meal);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if(favorities.isChecked())
                {
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mealList.clear();

                            for(DataSnapshot snap:snapshot.getChildren())
                            {
                                MealPlan meal =snap.getValue(MealPlan.class);
                                if(meal.isFavorite()==true)
                                    mealList.add(meal);
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        if(all.isChecked())
        {
            fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mealList.clear();
                    for(DataSnapshot snap:snapshot.getChildren())
                    {
                        MealPlan meal =snap.getValue(MealPlan.class);
                        mealList.add(meal);
                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.diary:
                        startActivity(new Intent(getApplicationContext(),DiaryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercies:
                        startActivity(new Intent(getApplicationContext(),ExercisesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.more:
                        startActivity(new Intent(getApplicationContext(),MoreActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return true;
                }
            }
        });
        lv_mealPlans.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View child, int pos, long _pos){
                Intent intent=new Intent(getApplicationContext(),DescriptionPlanActivity.class);
                //TODO: SET MEAL PLAN
                MealPlan mealPlan= mealList.get(pos);
                intent.putExtra("plan",mealPlan);
                intent.putExtra("index",pos);
                startActivity(intent);
             }
        });





        lv_mealPlans.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (mealList.get(position).isFavorite()==false)
                {

                    mealList.get(position).setFavorite(true);
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(position)).child("favorite").setValue(true);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    mealList.get(position).setFavorite(false);
                    fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(position)).child("favorite").setValue(false);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }


    public class MyAdapter extends ArrayAdapter<MealPlan>
    {
        public MyAdapter(Context context, ArrayList<MealPlan> string_list)
        {
            super(context, 0, string_list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item2, parent, false);
            }
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            TextView namePlan = (TextView) convertView.findViewById(R.id.tv_namePlan);
            TextView categoryPlan=(TextView)convertView.findViewById(R.id.tv_mealCategory);
            ImageView imgview = (ImageView) convertView.findViewById(R.id.mealImage);
            ImageView favorite= convertView.findViewById(R.id.favoritePlan);
            namePlan.setText(mealList.get(position).getName());
            categoryPlan.setText(mealList.get(position).getCategory());
            Glide.with(convertView).load(mealList.get(position).getImageUrl()).into(imgview);
            if (mealList.get(position).isFavorite()==true)
                favorite.setImageResource(R.drawable.ic_fav1);
            else
                favorite.setImageResource(R.drawable.fav2);


            return convertView;
        }
    }
}


