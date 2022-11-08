package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.licenta.data.Goal;
import com.example.licenta.data.User;

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class Sign_Up_Continue_Part2_Activity extends AppCompatActivity {

    Spinner spinnerCountry;
    RadioGroup rgSex;
    RadioButton rb1,rb2;
    EditText Age;
    Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_continue_part2);
        getSupportActionBar().hide();

        spinnerCountry=findViewById(R.id.User_Country_Spinner);
        rgSex=findViewById(R.id.rgSex);
        rb1=findViewById(R.id.User_Female_RadioButton);
        rb2=findViewById(R.id.User_Male_RadioButton);
        Age=findViewById(R.id.User_Age_EditText);
//        Age.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
//        Age.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        Register=findViewById(R.id.OkButton);


        SortedSet<String> countries = new TreeSet<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            if (!TextUtils.isEmpty(locale.getDisplayCountry())) {
                countries.add(locale.getDisplayCountry());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries.toArray(new String[0]));
        spinnerCountry.setAdapter(adapter);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rgSex.getCheckedRadioButtonId();
                Bundle extras=getIntent().getExtras();
                Goal goal =extras.getParcelable("goal");
                User user= new User();

                if(rb1.isChecked())
                    user.setSex("Female");
                if(rb2.isChecked())
                    user.setSex("Male");
                if(validate()){

                    user.setAge(Integer.parseInt(Age.getText().toString().trim()));
                user.setCountry(spinnerCountry.getSelectedItem().toString());

                double goalEnergyBMR = 0;

                //calculate energy
                //Step 1: Calculate Your BMR
                if(selectedId==0){
                    //if is female
                    goalEnergyBMR = 655+(9.563*goal.getCurrent_weight())+(1.850*goal.getHeight())-(4.676*user.getAge());
                }
                else {
                    //male
                    goalEnergyBMR = 66.5+(13.75*goal.getCurrent_weight())+(5.003*goal.getHeight())-(6.755*user.getAge());
                }
                goalEnergyBMR = Math.round(goalEnergyBMR);
                goal.setEnergy_bmr(goalEnergyBMR);

                double proteinsBmr = Math.round(goalEnergyBMR*25/100);
                double carbsBmr = Math.round(goalEnergyBMR*50/100);
                double fatBmr = Math.round(goalEnergyBMR*25/100);
                goal.setProteins_bmr(proteinsBmr);
                goal.setCarbs_bmr(carbsBmr);
                goal.setFat_bmr(fatBmr);

                /* 2: Diet */
                // If you want to loose weight
                // without activity (Little to no exercise)
                // Loose or gain weight?
                double WeeklyGoal = 0;
                try {
                    WeeklyGoal = goal.getWeekly_goal();
                }
                catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                int intIWantTo=goal.getI_want_to();
                // 1 kg fat = 7700 kcal
                double kcal = 0;
                double energyDiet = 0;
                kcal = 7700*goal.getWeekly_goal();
                if(intIWantTo == 0){
                    // Loose weight
                    energyDiet = Math.round((goalEnergyBMR - (kcal/7)) * 1.2);

                }
                else{
                    // Gain weight
                    energyDiet = Math.round((goalEnergyBMR + (kcal/7)) * 1.2);
                }
                goal.setEnergy_diet(energyDiet);


                // Proteins, carbs and fat diet
                // 20-25 % protein
                // 40-50 % carbs
                // 25-35 % fat
                double proteinsDiet = Math.round(energyDiet*25/100);
                double carbsDiet = Math.round(energyDiet*50/100);
                double fatDiet = Math.round(energyDiet*25/100);

                goal.setProteins_diet(proteinsDiet);
                goal.setCarbs_diet(carbsDiet);
                goal.setFat_diet(fatDiet);

                /* 3: With activity */
                // Taking in to account activity
                double energyWithActivity = 0;
                if(goal.getActivity_level()==0) {
                    energyWithActivity = goalEnergyBMR * 1.2;
                }
                else if(goal.getActivity_level()==1) {
                    energyWithActivity = goalEnergyBMR * 1.375; // slightly_active
                }
                else if(goal.getActivity_level()==2) {
                    energyWithActivity = goalEnergyBMR*1.55; // moderately_active
                }
                else if(goal.getActivity_level()==3) {
                    energyWithActivity = goalEnergyBMR*1.725; // active_lifestyle
                }
                else if(goal.getActivity_level()==4) {
                    energyWithActivity = goalEnergyBMR * 1.9; // very_active
                }
                energyWithActivity = Math.round(energyWithActivity);
                goal.setEnergy_with_activity(energyWithActivity);

                // Proteins, carbs and fat diet
                // 20-25 % protein
                // 40-50 % carbs
                // 25-35 % fat
                double proteinsWithActivity = Math.round(energyWithActivity*25/100);
                double carbsWithActivity = Math.round(energyWithActivity*50/100);
                double fatWithActivity = Math.round(energyWithActivity*25/100);

               goal.setProteins_with_activity(proteinsWithActivity);
               goal.setCarbs_with_activity(carbsWithActivity);
               goal.setFat_with_activity(fatWithActivity);

                /* 4: With_activity_and_diet */
                // If you want to loose your weight
                // With activity
                // 1 kg fat = 7700 kcal
                kcal = 0;
                double energyWithActivityAndDiet = 0;
                kcal = 7700*goal.getWeekly_goal();
                if(intIWantTo == 0){
                    // Loose weight
                    energyWithActivityAndDiet = goalEnergyBMR - (kcal/7);
                }
                else{
                    // Gain weight
                    energyWithActivityAndDiet = goalEnergyBMR + (kcal/7);
                }

                if(goal.getActivity_level()==0) {
                    energyWithActivityAndDiet= energyWithActivityAndDiet* 1.2;
                }
                else if(goal.getActivity_level()==1) {
                    energyWithActivityAndDiet= energyWithActivityAndDiet* 1.375; // slightly_active
                }
                else if(goal.getActivity_level()==2) {
                    energyWithActivityAndDiet= energyWithActivityAndDiet*1.55; // moderately_active
                }
                else if(goal.getActivity_level()==3) {
                    energyWithActivityAndDiet= energyWithActivityAndDiet*1.725; // active_lifestyle
                }
                else if(goal.getActivity_level()==4) {
                    energyWithActivityAndDiet = energyWithActivityAndDiet* 1.9; // very_active
                }
                energyWithActivityAndDiet = Math.round(energyWithActivityAndDiet);

                goal.setEnergy_with_activity_and_diet(energyWithActivityAndDiet);

                // Calcualte proteins
                // 20-25 % protein
                // 40-50 % carbs
                // 25-35 % fat
                double proteins = Math.round(energyWithActivityAndDiet*25/100);
                double carbs = Math.round(energyWithActivityAndDiet*50/100);
                double fat = Math.round(energyWithActivityAndDiet*25/100);

               goal.setProteins_with_activity_and_diet(proteins);
               goal.setCarbs_with_activity_and_diet(carbs);
               goal.setFat_with_activity_and_diet(fat);

               Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
               user.setGoal(goal);
               intent.putExtra("user",user);
               startActivity(intent);
           }
            }
        });

    }
    private Boolean validate(){
        if(!rb1.isChecked()&& !rb2.isChecked())
        {
            rb1.setError("Please check your genre");
            return false;
        }
        if (Age.getText().toString().contains(",")||Age.getText().toString().contains(".")|| Age.getText().toString().contains("-")||Age.getText().toString().contains(" "))
        {
            Age.setError("Invalid character ");
            return false;
        }
        if (Age.getText().toString().isEmpty()||Integer.parseInt(Age.getText().toString())<1||Integer.parseInt(Age.getText().toString())>130)
        {
            Age.setError("Please enter your correct age");
            return false;
        }
        return true;
    }

}