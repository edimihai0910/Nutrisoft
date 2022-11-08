package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.licenta.data.FoodReportModel;
import com.example.licenta.data.MealPlan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestActivityy extends AppCompatActivity {

    Button register,login;
    FirebaseAuth auth ;
    EditText userR,passR,userL,passL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activityy);
    register=findViewById(R.id.register);
    login=findViewById(R.id.login);
    auth=FirebaseAuth.getInstance();
    userR=findViewById(R.id.etemail);
    passR=findViewById(R.id.etemailpas);
    userL=findViewById(R.id.etEmailLog);
    passL=findViewById(R.id.etPassLog);
    register.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            String u=userR.getText().toString();
            String p=passR.getText().toString();
            auth.createUserWithEmailAndPassword("94599999989@pin23tech.com", "3213w312231321sadcv.").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registering successfull", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(TestActivityy.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    });

    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            auth.signInWithEmailAndPassword("9199999989@pintech.com", "qweqw312231321sadcv.").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseDatabase.getInstance().getReference().child("ProgrammingTest").child("Android").setValue("Ana are mere");
                    FirebaseDatabase.getInstance().getReference().child("ProgrammingTest").child("Tasks").setValue("Ziua1");
                    FirebaseDatabase.getInstance().getReference().child("ProgrammingTest").child("Tasks").setValue("Ziua2");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Ziua 1", Collections.singletonList("Mananca sanatos ca asa ajungi barbos"));;
                    map.put("Ziua 2", Collections.singletonList("Rupe mancarea ca sa ajungi marea"));
                     FirebaseDatabase.getInstance().getReference().child("ProgrammingTest").child("Tasks").setValue(map);
                    Toast.makeText(getApplicationContext(),"You are welcome",Toast.LENGTH_LONG).show();


                    FirebaseDatabase.getInstance().getReference().child("Partea 2").push().child("Name").push().setValue("Ratatoui");


                    //preluare date
                    final ArrayList<String> list= new ArrayList<>();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ProgrammingTest").child("Tasks");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot data:snapshot.getChildren())
                            {
                                list.add(snapshot.getValue().toString());
                            }
                            Log.i("LISTTTTT",list.toString());
                        }

                        //MealPlan
                           MealPlan info=new MealPlan();
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    FirebaseFirestore db=FirebaseFirestore.getInstance();

                    Map<String,Object> tasks=new HashMap<>();
                    tasks.put("Day 1","Branza");
                    tasks.put("Day 2","Lapte");
                    tasks.put("Day 3","Rosii");

                    db.collection("mealplaning").document("JSR").set(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {


                            }
                            if(task.isCanceled())
                            {
                                Toast.makeText(TestActivityy.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    });
    }
}