package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.data.OnGetDataListener;
import com.example.licenta.data.User;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditGoalsActivity extends AppCompatActivity {

    EditText weight,targetWeight;
    Spinner spinnerIWantTo,spinnerWeeklyGoal,spinnerActivityLevel;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    Button save;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goals);
        getSupportActionBar().hide();

        weight=findViewById(R.id.editTextGoalCurrentWeight);
        targetWeight=findViewById(R.id.editTextGoalTargetWeight);
        spinnerIWantTo=findViewById(R.id.spinnerIWantTo);
        spinnerWeeklyGoal=findViewById(R.id.spinnerWeeklyGoal);
        spinnerActivityLevel=findViewById(R.id.spinnerActivityLevel);
        save=findViewById(R.id.buttonGoalSubmit);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGoalSubmitOnClick();

            }
        });
    }
    public void editGoalSubmitOnClick(){
        if (validate()) {
            int intIWantTo = spinnerIWantTo.getSelectedItemPosition();
            double doubleTargetWeight = Double.parseDouble(targetWeight.getText().toString());
            double doubleWeeklyGoal = Double.parseDouble(spinnerWeeklyGoal.getSelectedItem().toString());
            int activityLevel = spinnerActivityLevel.getSelectedItemPosition();
            double doubleCurrentWeight = Double.parseDouble(weight.getText().toString());

            firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    double goalEnergyBMR = 0;
                    /* 1. BMR: Energy */
                    if (user.getSex().equals("Male")) {
                        goalEnergyBMR = 66.5 + (13.75 * doubleCurrentWeight) + (5.003 * user.getGoal().getHeight()) - (6.755 * user.getAge());

                    } else {
                        goalEnergyBMR = 655 + (9.563 * doubleCurrentWeight) + (1.850 * user.getGoal().getHeight()) - (4.676 * user.getAge());

                    }
                    goalEnergyBMR = Math.round(goalEnergyBMR);
                    double proteinsBMR = Math.round(goalEnergyBMR * 25 / 100);
                    double carbsBMR = Math.round(goalEnergyBMR * 50 / 100);
                    double fatBMR = Math.round(goalEnergyBMR * 25 / 100);
                    /* 2: Diet */
                    // If you want to loose weight
                    // without activity (Little to no exercise)
                    // Loose or gain weight?

                    double kcal = 0;
                    double energyDiet = 0;
                    kcal = 7700 * doubleWeeklyGoal;
                    if (intIWantTo == 0) {
                        // Loose weight
                        energyDiet = Math.round((goalEnergyBMR - (kcal / 7)) * 1.2);
                    } else {
                        // Gain weight
                        energyDiet = Math.round((goalEnergyBMR + (kcal / 7)) * 1.2);
                    }
                    double proteinsDiet = Math.round(energyDiet * 25 / 100);
                    double carbsDiet = Math.round(energyDiet * 50 / 100);
                    double fatDiet = Math.round(energyDiet * 25 / 100);

                    /* 3: With activity */
                    // If you want to keep your weight
                    //
                    // Taking in to account activity
                    double energyWithActivity = 0;

                    if (activityLevel == 0) {
                        energyWithActivity = goalEnergyBMR * 1.2;
                    } else if (activityLevel == 1) {
                        energyWithActivity = goalEnergyBMR * 1.375; // slightly_active

                    } else if (activityLevel == 2) {
                        energyWithActivity = goalEnergyBMR * 1.55; // moderately_active

                    } else if (activityLevel == 3) {
                        energyWithActivity = goalEnergyBMR * 1.725; // active_lifestyle

                    } else if (activityLevel == 4) {
                        energyWithActivity = goalEnergyBMR * 1.9; // very_active

                    }
                    energyWithActivity = Math.round(energyWithActivity);
                    double proteinsWithActivity = Math.round(energyWithActivity * 25 / 100);
                    double carbsWithActivity = Math.round(energyWithActivity * 50 / 100);
                    double fatWithActivity = Math.round(energyWithActivity * 25 / 100);

                    /* 4: With_activity_and_diet */
                    // If you want to loose your weight
                    // With activity
                    // 1 kg fat = 7700 kcal
                    kcal = 0;
                    double energyWithActivityAndDiet = 0;
                    kcal = 7700 * doubleWeeklyGoal;
                    if (intIWantTo == 0) {
                        // Loose weight
                        energyWithActivityAndDiet = goalEnergyBMR - (kcal / 7);
                    } else {
                        // Gain weight
                        energyWithActivityAndDiet = goalEnergyBMR + (kcal / 7);
                    }
                    if (activityLevel == 0) {
                        energyWithActivityAndDiet = energyWithActivityAndDiet * 1.2;
                    } else if (activityLevel == 1) {
                        energyWithActivityAndDiet = energyWithActivityAndDiet * 1.375;

                    } else if (activityLevel == 2) {
                        energyWithActivityAndDiet = energyWithActivityAndDiet * 1.55; // moderately_active

                    } else if (activityLevel == 3) {
                        energyWithActivityAndDiet = energyWithActivityAndDiet * 1.725; // active_lifestyle

                    } else if (activityLevel == 4) {
                        energyWithActivityAndDiet = energyWithActivityAndDiet * 1.9; // very_active
                    }
                    energyWithActivityAndDiet = Math.round(energyWithActivityAndDiet);
                    double proteinsWithActivityAndDiet = Math.round(energyWithActivityAndDiet * 25 / 100);
                    double carbsWithActivityAndDiet = Math.round(energyWithActivityAndDiet * 50 / 100);
                    double fatWithActivityAndDiet = Math.round(energyWithActivityAndDiet * 25 / 100);

                    user.getGoal().setCurrent_weight((int) doubleCurrentWeight);
                    user.getGoal().setTarget_weight((int) doubleTargetWeight);
                    user.getGoal().setI_want_to(intIWantTo);
                    user.getGoal().setWeekly_goal((float) doubleWeeklyGoal);
                    user.getGoal().setActivity_level(activityLevel);
                    user.getGoal().setEnergy_bmr(goalEnergyBMR);
                    user.getGoal().setEnergy_diet(energyDiet);
                    user.getGoal().setEnergy_with_activity(energyWithActivity);
                    user.getGoal().setEnergy_with_activity_and_diet(energyWithActivityAndDiet);
                    user.getGoal().setProteins_bmr(proteinsBMR);
                    user.getGoal().setProteins_diet(proteinsDiet);
                    user.getGoal().setProteins_with_activity(proteinsWithActivity);
                    user.getGoal().setProteins_with_activity_and_diet(proteinsWithActivityAndDiet);
                    user.getGoal().setFat_bmr(fatBMR);
                    user.getGoal().setFat_diet(fatDiet);
                    user.getGoal().setFat_with_activity(fatWithActivity);
                    user.getGoal().setFat_with_activity_and_diet(fatWithActivityAndDiet);
                    user.getGoal().setCarbs_bmr(carbsBMR);
                    user.getGoal().setCarbs_diet(carbsDiet);
                    user.getGoal().setCarbs_with_activity(carbsWithActivity);
                    user.getGoal().setCarbs_with_activity_and_diet(carbsWithActivityAndDiet);

                    if (user.getGoal().getActivity_level()==0&& user.getGoal().getI_want_to()==0)
                    {
                        user.getGoal().setKcalFit(user.getGoal().getEnergy_diet());
                        user.getGoal().setProteinsFit(user.getGoal().getProteinNeeded());
                        user.getGoal().setCarbsFit(user.getGoal().getCarbsNeededWithDiet());
                        user.getGoal().setFatFit(user.getGoal().getFatNeededWithDiet());
                    }
                    else if (user.getGoal().getActivity_level()==0&user.getGoal().getI_want_to()==1) {
                        user.getGoal().setKcalFit(user.getGoal().getEnergy_bmr());
                        user.getGoal().setProteinsFit(user.getGoal().getProteinNeeded());
                        user.getGoal().setCarbsFit(user.getGoal().getCarbsNeededBMR());
                        user.getGoal().setFatFit(user.getGoal().getFatNeededBMR());
                    }
                    else if (user.getGoal().getActivity_level()>0 &&user.getGoal().getI_want_to()==0)
                    {
                        user.getGoal().setKcalFit(user.getGoal().getEnergy_with_activity_and_diet());
                        user.getGoal().setProteinsFit(user.getGoal().getProteinNeeded());
                        user.getGoal().setCarbsFit(user.getGoal().getCarbsNeededWithActivityAndDiet());
                        user.getGoal().setFatFit(user.getGoal().getFatNeededWithActivityAndDiet());

                    }
                    else if(user.getGoal().getActivity_level()>0 && user.getGoal().getI_want_to()==1) {
                        user.getGoal().setKcalFit(user.getGoal().getEnergy_with_activity());
                        user.getGoal().setProteinsFit(user.getGoal().getProteinNeeded());
                        user.getGoal().setCarbsFit(user.getGoal().getCarbsNeededWithActivity());
                        user.getGoal().setFatFit(user.getGoal().getFatNeededWithActivity());
                    }

                    firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(user);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        }
    }

    public boolean validate()
    {
        if (weight.getText().toString().contains(",")||weight.getText().toString().contains(".")|| weight.getText().toString().contains("-")||weight.getText().toString().contains(" "))
        {
            weight.setError("Invalid character ");
            return false;
        }
        if (targetWeight.getText().toString().contains(",")||targetWeight.getText().toString().contains(".")|| targetWeight.getText().toString().contains("-")||targetWeight.getText().toString().contains(" "))
        {
            targetWeight.setError("Invalid character");
            return false;
        }
        if (weight.getText().toString().isEmpty()) {
            weight.setError("You need to set a value");
            return false;
        }
        if( targetWeight.getText().toString().isEmpty())
        {
            targetWeight.setError("You need to set a value");
            return false;
        }
        int intWeight=Integer.parseInt(weight.getText().toString());
        double doubleTarget=Double.parseDouble(targetWeight.getText().toString());
        int intIWantTo = spinnerIWantTo.getSelectedItemPosition();
        if(intWeight<15 || intWeight>300)
        {
            weight.setError("You need to set a valid weight value");
            return false;
        }
        else if (doubleTarget<15 || doubleTarget>300)
        {
            targetWeight.setError("You need to set a valid value");
            return false;
        }
        else if (intIWantTo==0 && doubleTarget>intWeight || intIWantTo==1 && doubleTarget<intWeight)
        {
            targetWeight.setError("Please select a valid target");
            return false;
        }
        return true;
    }
}