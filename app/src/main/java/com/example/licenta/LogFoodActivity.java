package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.licenta.data.FoodReportModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class LogFoodActivity extends AppCompatActivity {

    TextView carbs,protein,fat,servingSize,kcals,name;
    Button logFood;
    SeekBar seekBar;
    FoodReportModel food;
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_food);
        getSupportActionBar().hide();

        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);

        Intent extras=getIntent();
        food=extras.getParcelableExtra("food");

        seekBar = findViewById(R.id.seekBar);
        logFood=findViewById(R.id.bt_logFood);
        name=findViewById(R.id.tvNameValue);
        carbs=findViewById(R.id.tvCarbsValue);
        protein=findViewById(R.id.tvProteinValue);
        fat=findViewById(R.id.tvFatValue);
        servingSize=findViewById(R.id.tvServingValue);
        kcals=findViewById(R.id.tvKcalValue);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);


        carbs.setText(String.valueOf(twoDForm.format(food.carbs))+" g");
        protein.setText(String.valueOf(twoDForm.format(food.protein))+" g");
        fat.setText(String.valueOf(twoDForm.format(food.fat))+" g");
        servingSize.setText(String.valueOf(food.serving_weight_grams)+" g");
        kcals.setText(String.valueOf(twoDForm.format(food.calories)) +" kcal");
        name.setText(food.name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(0);
            seekBar.setMax(1200);
        }

        logFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodReportModel foodLog=new FoodReportModel();
                String arr[]=carbs.getText().toString().split(" ");
                foodLog.setCarbs(Double.valueOf(arr[0]));
                arr=kcals.getText().toString().split(" ");
                foodLog.setCalories(Double.valueOf(arr[0]));
                arr=fat.getText().toString().split(" ");
                foodLog.setFat(Double.valueOf(arr[0]));
                arr=servingSize.getText().toString().split(" ");
                foodLog.setServing_weight_grams(Double.valueOf(arr[0]));
                foodLog.setSugars(food.sugars*Double.valueOf(arr[0])/100);
                foodLog.setFiber(food.fiber*Double.valueOf(arr[0])/100);
                arr=protein.getText().toString().split(" ");
                foodLog.setProtein(Double.valueOf(arr[0]));
                foodLog.setName(name.getText().toString());
                Intent resultIntent=new Intent();
                resultIntent.putExtra("result",foodLog);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            carbs.setText(String.valueOf(twoDForm.format(progress*food.carbs/100))+" g");
            protein.setText(String.valueOf(twoDForm.format(progress*food.protein/100))+" g");
            fat.setText(String.valueOf(twoDForm.format(progress*food.fat/100))+" g");
            servingSize.setText(String.valueOf(progress)+" g");
            kcals.setText(String.valueOf(twoDForm.format(progress*food.calories/100)) +" kcal");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };
}