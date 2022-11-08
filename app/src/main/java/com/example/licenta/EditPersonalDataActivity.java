package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.licenta.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPersonalDataActivity extends AppCompatActivity {
    EditText age,userName,height;
    Button save;
    RadioGroup gender;
    RadioButton female,male;
    FirebaseDatabase fDatabase=FirebaseDatabase.getInstance();
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_data);
        getSupportActionBar().hide();


        age=findViewById(R.id.et_agePersonalData);
        userName=findViewById(R.id.et_UserNamePersonalData);
        height=findViewById(R.id.et_HeightPersonalData);
        gender=findViewById(R.id.rgSexPersonalData);
        female=findViewById(R.id.User_Female_RadioButtonPersonalData);
        male=findViewById(R.id.User_Male_RadioButtonPersonalData);
        save=findViewById(R.id.SavePersonalData);

        fDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser=snapshot.getValue(User.class);
                age.setText(String.valueOf(currentUser.getAge()));
                userName.setText(currentUser.getUsername());
                height.setText(String.valueOf(currentUser.getGoal().getHeight()));
                if(currentUser.getSex().equals("Female"))
                    gender.check(female.getId());
                else
                    gender.check(male.getId());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()==true) {
                    int Age=Integer.parseInt(age.getText().toString());
                    String Username=userName.getText().toString();
                    int Height= Integer.parseInt(height.getText().toString());
                    int Gender= gender.getCheckedRadioButtonId();
                    currentUser.setAge(Age);
                    currentUser.setUsername(Username);
                    currentUser.getGoal().setHeight(Height);
                    Log.i("gender", String.valueOf(Gender));
                    if (Gender==female.getId()) {
                        currentUser.setSex("Female");
                    } else currentUser.setSex("Male");

                    fDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(currentUser);

                    startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                }
            }
        });
    }
    public boolean validate()
    {
        if (age.getText().toString().contains(",")||age.getText().toString().contains(".")|| age.getText().toString().contains("-")||age.getText().toString().contains(" "))
        {
            age.setError("Invalid character ");
            return false;
        }
        if (height.getText().toString().contains(",")||height.getText().toString().contains(".")|| height.getText().toString().contains("-")||height.getText().toString().contains(" "))
        {
            height.setError("Invalid character ");
            return false;
        }
        int  h=0;
        int intAge=-1;
        if(!height.getText().toString().isEmpty())
        h=Integer.parseInt(height.getText().toString());


        if(!age.getText().toString().isEmpty())
        intAge=Integer.parseInt(age.getText().toString());
        if (userName.getText().length()<3)
        {
            userName.setError("User name can't be less then 4 charachers");
            return false;
        }
         if (h>350)
        {
            height.setError("You are too tall to have a \"mini-phone\"");
            return false;
        }
        else if (h<80|| height.getText().toString().isEmpty()){
            height.setError("I'm sorry. I can't hear you. You are too small");
            return false;
        }
        if (intAge>110) {
            age.setError("You are too old for your age");
            return false;
        }
        else if (intAge<0 ||age.getText().toString().isEmpty()) {
            age.setError("You must be born to change the age value");
            return false;
        }
        if (!male.isChecked()&&!female.isChecked())
        {
            male.setError("You need to select something here");
            return false;
        }
        return true;
    }
}