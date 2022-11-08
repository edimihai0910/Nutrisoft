package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.licenta.data.Goal;
import com.example.licenta.data.User;

import java.sql.Date;
import java.util.Calendar;

public class SignUp_Continue_Activity extends AppCompatActivity {
    NumberPicker Weight,Height;
    EditText TargetWeight ;
    Button Next;
    Spinner SpinnerActivityLevel, SpinnerWeeklyTarget,SpinnerWeeklyKg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_continue);
        getSupportActionBar().hide();
        //initialize objects
        Height=findViewById(R.id.User_Height_NumberPicker);
        Weight=findViewById(R.id.User_Weight_NumberPicker);
        SpinnerActivityLevel=findViewById(R.id.ActivityLevelSpinner);
        SpinnerWeeklyTarget =findViewById(R.id.TargetSpinner) ;
        SpinnerWeeklyKg=findViewById(R.id.TargetKgSpinner);
        TargetWeight=findViewById(R.id.TargetWeightEditText);
        TargetWeight.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        TargetWeight.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        Next=findViewById(R.id.User_SignIn_Button);

        Height.setMaxValue(300);
        Height.setMinValue(40);
        Height.setValue(150);

        Weight.setMaxValue(300);
        Weight.setMinValue(40);
        Weight.setValue(65);


        String stringActivity=SpinnerActivityLevel.getSelectedItem().toString();
        int intActivityLevel=SpinnerActivityLevel.getSelectedItemPosition();

        int WantTo= SpinnerWeeklyTarget.getSelectedItemPosition();
        float WeeklyGoal=Float.parseFloat(SpinnerWeeklyKg.getSelectedItem().toString());



        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {

                    //Datetime.Now
                    Calendar cc = Calendar.getInstance();
                    int year = cc.get(Calendar.YEAR);
                    int month = cc.get(Calendar.MONTH);
                    int day = cc.get(Calendar.DAY_OF_MONTH);
                    String goalDate = year + "-" + month + "-" + day;

                    Goal goalUser = new Goal();
                    goalUser.setCurrent_weight(Weight.getValue());
                    goalUser.setHeight(Height.getValue());
                    goalUser.setTarget_weight(Integer.valueOf(String.valueOf(TargetWeight.getText().toString().trim())));
                    goalUser.setGoal_date(Date.valueOf(goalDate));
                    goalUser.setI_want_to(WantTo);
                    goalUser.setWeekly_goal(WeeklyGoal);
                    goalUser.setActivity_level(intActivityLevel);

                    Intent newIntent = new Intent(getApplicationContext(), Sign_Up_Continue_Part2_Activity.class);
                    newIntent.putExtra("goal", goalUser);
                    startActivity(newIntent);
                }
            }
        });
    }
        public boolean validate() {

            if (TargetWeight.getText().toString().contains(",")||TargetWeight.getText().toString().contains(".")|| TargetWeight.getText().toString().contains("-")||TargetWeight.getText().toString().contains(" "))
            {
                TargetWeight.setError("Invalid character ");
                return false;
            }
            if (TargetWeight.getText().toString().isEmpty() || Integer.parseInt(TargetWeight.getText().toString()) < 30 || Integer.parseInt(TargetWeight.getText().toString()) > 250) {
                TargetWeight.setError("Please choose a real target!");
                return false ;
            }
            if ((SpinnerWeeklyTarget.getSelectedItemPosition() == 0) && Integer.parseInt(TargetWeight.getText().toString()) > Weight.getValue()) {
                TargetWeight.setError("Invalid Target !");
                return false;
            }
            if ((SpinnerWeeklyTarget.getSelectedItemPosition() == 1) && Integer.parseInt(TargetWeight.getText().toString()) < Weight.getValue()) {
                TargetWeight.setError("Invalid Target !");
                return false;
            }
            return true;
        }
}