package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.licenta.data.OnGetDataListener;
import com.example.licenta.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class ChangeGoalsActivity extends AppCompatActivity {
    ImageButton edit;
    User user =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_change_goals);
        DecimalFormat df=new DecimalFormat("0.00");
        edit=findViewById(R.id.edit_goalsButton);
     readData(FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()), new OnGetDataListener() {
      @Override
      public void onSuccess(DataSnapshot dataSnapshot) {
          user =dataSnapshot.getValue(User.class);
          //  Status
          TextView textViewGoalCurrentWeightNumber = (TextView)findViewById(R.id.textViewGoalCurrentWeightNumber);
          textViewGoalCurrentWeightNumber.setText(user.getGoal().getCurrent_weight() + " kg");
//  Target
          TextView textViewGoalCurrentTargetNumber = findViewById(R.id.textViewGoalCurrentTargetNumber);
          textViewGoalCurrentTargetNumber.setText(user.getGoal().getTarget_weight() + " kg");
          TextView textViewGoalMethodText = (TextView)findViewById(R.id.textViewGoalMethodText);
          String method = "";
          if(user.getGoal().getI_want_to()==0){
              method = "Loose "  + user.getGoal().getWeekly_goal();
          }
          else{
              method = "Gain "  + user.getGoal().getWeekly_goal();
          }
          method = method + " kg/week";
          textViewGoalMethodText.setText(method);
          TextView textViewActivityLevel = (TextView)findViewById(R.id.textViewActivityLevel);
          if(user.getGoal().getActivity_level()==0)
          {
              textViewActivityLevel.setText("Little to no exercise");
          }
          else if(user.getGoal().getActivity_level()==1)
          {
              textViewActivityLevel.setText("Light exercise (1–3 days per week)");
          }
          else if(user.getGoal().getActivity_level()==2)
          {
              textViewActivityLevel.setText("Moderate exercise (3–5 days per week)");
          }
          else if(user.getGoal().getActivity_level()==3)
          {
              textViewActivityLevel.setText("Heavy exercise (6–7 days per week)");
          }
          else if(user.getGoal().getActivity_level()==4)
          {
              textViewActivityLevel.setText("Very heavy exercise (twice per day, extra heavy workouts)");
          }
          /* Numbers */
          // 1 Diet
          TextView textViewGoalEnergyDiet = (TextView)findViewById(R.id.textViewGoalEnergyDiet);
          textViewGoalEnergyDiet.setText(df.format(user.getGoal().getEnergy_diet()));
          TextView textViewGoalProteinsDiet = (TextView)findViewById(R.id.textViewGoalProteinsDiet);
          textViewGoalProteinsDiet.setText(df.format(user.getGoal().getCurrent_weight()*0.8));
          TextView textViewGoalCarbsDiet = (TextView)findViewById(R.id.textViewGoalCarbsDiet);
          textViewGoalCarbsDiet.setText(df.format(user.getGoal().getCarbsNeededBMR()));
          TextView textViewGoalFatDiet = (TextView)findViewById(R.id.textViewGoalFatDiet);
          textViewGoalFatDiet.setText(df.format(user.getGoal().getFatNeededBMR()));

          // 2 WithActivityAndDiet
          TextView textViewGoalEnergyWithActivityAndDiet = (TextView)findViewById(R.id.textViewGoalEnergyWithActivityAndDiet);
          textViewGoalEnergyWithActivityAndDiet.setText(df.format(user.getGoal().getEnergy_with_activity_and_diet()));
          TextView textViewGoalProteinsWithActivityAndDiet = (TextView)findViewById(R.id.textViewGoalProteinsWithActivityAndDiet);
          textViewGoalProteinsWithActivityAndDiet.setText(df.format(user.getGoal().getCurrent_weight()*1.3));
          TextView textViewGoalCarbsWithActivityAndDiet = (TextView)findViewById(R.id.textViewGoalCarbsWithActivityAndDiet);
          textViewGoalCarbsWithActivityAndDiet.setText(df.format(user.getGoal().getCarbsNeededWithActivityAndDiet()));
          TextView textViewGoalFatWithActivityAndDiet = (TextView)findViewById(R.id.textViewGoalFatWithActivityAndDiet);
          textViewGoalFatWithActivityAndDiet.setText(df.format(user.getGoal().getFatNeededWithActivityAndDiet()));

          // 3 BMR
          TextView textViewGoalEnergyBMR = (TextView)findViewById(R.id.textViewGoalEnergyBMR);
          textViewGoalEnergyBMR.setText(df.format(user.getGoal().getEnergy_bmr()));
          TextView textViewGoalProteinsBMR = (TextView)findViewById(R.id.textViewGoalProteinsBMR);
          textViewGoalProteinsBMR.setText(df.format(user.getGoal().getCurrent_weight()*1.15));
          TextView textViewGoalCarbsBMR = (TextView)findViewById(R.id.textViewGoalCarbsBMR);
          textViewGoalCarbsBMR.setText(df.format(user.getGoal().getCarbsNeededBMR()));
          TextView textViewGoalFatBMR = (TextView)findViewById(R.id.textViewGoalFatBMR);
          textViewGoalFatBMR.setText(df.format(user.getGoal().getFatNeededBMR()));


          // 4 WithActivity
          TextView textViewGoalEnergyWithActivity = (TextView)findViewById(R.id.textViewGoalEnergyWithActivity);
          textViewGoalEnergyWithActivity.setText(df.format(user.getGoal().getEnergy_with_activity()));
          TextView textViewGoalProteinsWithActivity = (TextView)findViewById(R.id.textViewGoalProteinsWithActivity);
          textViewGoalProteinsWithActivity.setText(df.format(user.getGoal().getProteinNeeded()*1.5));
          TextView textViewGoalCarbsWithActivity = (TextView)findViewById(R.id.textViewGoalCarbsWithActivity);
          textViewGoalCarbsWithActivity.setText(df.format(user.getGoal().getCarbsNeededWithActivity()));
          TextView textViewGoalFatWithActivity = (TextView)findViewById(R.id.textViewGoalFatWithActivity);
          textViewGoalFatWithActivity.setText(df.format(user.getGoal().getFatNeededWithActivity()));
          // Hide fields
          toggleNumbersViewGoal(false);

          // Checkbox toggle
          CheckBox checkBoxAdvanced = (CheckBox)findViewById(R.id.checkBoxGoalToggle);

          checkBoxAdvanced.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                  toggleNumbersViewGoal(isChecked);
              }
          });
      }

      @Override
      public void onStart() {

      }

      @Override
      public void onFailure() {

      }
  });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditGoalsActivity.class));
            }
        });
    }



    public void toggleNumbersViewGoal(boolean isChecked) {
        // Remove table rows
        TableRow textViewGoalMethodRowA = (TableRow) findViewById(R.id.textViewGoalMethodRowA);

        TableRow textViewGoalMethodRowB = (TableRow) findViewById(R.id.textViewGoalMethodRowB);

        // Hide fields
        TextView textViewGoalHeadcellEnergy = (TextView) findViewById(R.id.textViewGoalHeadcellEnergy);
        TextView textViewGoalHeadcellProteins = (TextView) findViewById(R.id.textViewGoalHeadcellProteins);
        TextView textViewGoalHeadcellCarbs = (TextView) findViewById(R.id.textViewGoalHeadcellCarbs);
        TextView textViewGoalHeadcellFat = (TextView) findViewById(R.id.textViewGoalHeadcellFat);

        TextView textViewGoalProteinsBMR = (TextView) findViewById(R.id.textViewGoalProteinsBMR);
        TextView textViewGoalCarbsBMR = (TextView) findViewById(R.id.textViewGoalCarbsBMR);
        TextView textViewGoalFatBMR = (TextView) findViewById(R.id.textViewGoalFatBMR);

        TextView textViewGoalProteinsDiet = (TextView) findViewById(R.id.textViewGoalProteinsDiet);
        TextView textViewGoalCarbsDiet = (TextView) findViewById(R.id.textViewGoalCarbsDiet);
        TextView textViewGoalFatDiet = (TextView) findViewById(R.id.textViewGoalFatDiet);

        TextView textViewGoalProteinsWithActivity = (TextView) findViewById(R.id.textViewGoalProteinsWithActivity);
        TextView textViewGoalCarbsWithActivity = (TextView) findViewById(R.id.textViewGoalCarbsWithActivity);
        TextView textViewGoalFatWithActivity = (TextView) findViewById(R.id.textViewGoalFatWithActivity);

        TextView textViewGoalProteinsWithActivityAndDiet = (TextView) findViewById(R.id.textViewGoalProteinsWithActivityAndDiet);
        TextView textViewGoalCarbsWithActivityAndDiet = (TextView) findViewById(R.id.textViewGoalCarbsWithActivityAndDiet);
        TextView textViewGoalFatWithActivityAndDiet = (TextView) findViewById(R.id.textViewGoalFatWithActivityAndDiet);

        if (isChecked == false) {
            textViewGoalMethodRowA.setVisibility(View.GONE);
            textViewGoalMethodRowB.setVisibility(View.GONE);
            textViewGoalHeadcellEnergy.setVisibility(View.GONE);
            textViewGoalHeadcellProteins.setVisibility(View.GONE);
            textViewGoalHeadcellCarbs.setVisibility(View.GONE);
            textViewGoalHeadcellFat.setVisibility(View.GONE);
            textViewGoalProteinsBMR.setVisibility(View.GONE);
            textViewGoalCarbsBMR.setVisibility(View.GONE);
            textViewGoalFatBMR.setVisibility(View.GONE);
            textViewGoalProteinsDiet.setVisibility(View.GONE);
            textViewGoalCarbsDiet.setVisibility(View.GONE);
            textViewGoalFatDiet.setVisibility(View.GONE);
            textViewGoalProteinsWithActivity.setVisibility(View.GONE);
            textViewGoalCarbsWithActivity.setVisibility(View.GONE);
            textViewGoalFatWithActivity.setVisibility(View.GONE);
            textViewGoalProteinsWithActivityAndDiet.setVisibility(View.GONE);
            textViewGoalCarbsWithActivityAndDiet.setVisibility(View.GONE);
            textViewGoalFatWithActivityAndDiet.setVisibility(View.GONE);
        } else {
            textViewGoalMethodRowA.setVisibility(View.VISIBLE);
            textViewGoalMethodRowB.setVisibility(View.VISIBLE);
            textViewGoalHeadcellEnergy.setVisibility(View.VISIBLE);
            textViewGoalHeadcellProteins.setVisibility(View.VISIBLE);
            textViewGoalHeadcellCarbs.setVisibility(View.VISIBLE);
            textViewGoalHeadcellFat.setVisibility(View.VISIBLE);
            textViewGoalProteinsBMR.setVisibility(View.VISIBLE);
            textViewGoalCarbsBMR.setVisibility(View.VISIBLE);
            textViewGoalFatBMR.setVisibility(View.VISIBLE);
            textViewGoalProteinsDiet.setVisibility(View.VISIBLE);
            textViewGoalCarbsDiet.setVisibility(View.VISIBLE);
            textViewGoalFatDiet.setVisibility(View.VISIBLE);
            textViewGoalProteinsWithActivity.setVisibility(View.VISIBLE);
            textViewGoalCarbsWithActivity.setVisibility(View.VISIBLE);
            textViewGoalFatWithActivity.setVisibility(View.VISIBLE);
            textViewGoalProteinsWithActivityAndDiet.setVisibility(View.VISIBLE);
            textViewGoalCarbsWithActivityAndDiet.setVisibility(View.VISIBLE);
            textViewGoalFatWithActivityAndDiet.setVisibility(View.VISIBLE);

        }
    }
    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }
        });

    }
}