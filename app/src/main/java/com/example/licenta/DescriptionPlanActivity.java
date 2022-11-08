package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.licenta.data.MealPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DescriptionPlanActivity extends AppCompatActivity {

    ImageView imagePlan;
    TextView namePlan, description,duration,timesPerWeek,difficulty;
    ProgressBar daysProgress;
    Button startPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_plan);
        getSupportActionBar().hide();

        imagePlan=findViewById(R.id.image_Plan);
        namePlan=findViewById(R.id.tv_NamePlan);
        description=findViewById(R.id.tv_planDescription);
        duration=findViewById(R.id.total_calories);
        timesPerWeek=findViewById(R.id.net_calories);
        difficulty=findViewById(R.id.goalCalories);
        daysProgress=findViewById(R.id.pb_nutrientsFragment);
        startPlan=findViewById(R.id.bt_StartPlan);

        Bundle extras=getIntent().getExtras();
        MealPlan plan =extras.getParcelable("plan");

        if(plan.isStarted()==true)
        {
            startPlan.setText("Continue");
        }

        Glide.with(this).load(plan.getImageUrl()).into(imagePlan);
        namePlan.setText(plan.getName());
        description.setText(plan.getDescription());
        duration.setText(String.valueOf(plan.getNoDays())+" days");
        timesPerWeek.setText(plan.getTimePerWeeks());
        difficulty.setText(plan.getDifficulty());
        daysProgress.setMax(plan.getNoDays());
        daysProgress.setProgress(plan.getCurrentDay());

        startPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plan.isStarted()==false) {
                    plan.setStarted(true);
                    plan.setCurrentDay(1);
                    FirebaseDatabase.getInstance().getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(extras.get("index"))).setValue(plan);

                }
                Intent startIntent=new Intent(getApplicationContext(),StartedPlanActivity.class);
                startIntent.putExtra("plan",plan);
                startIntent.putExtra("index", (Integer) extras.get("index"));
                startActivity(startIntent);
            }
        });
    }
}