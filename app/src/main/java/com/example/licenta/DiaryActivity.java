package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.licenta.data.Exercice;
import com.example.licenta.data.FoodReportModel;
import com.example.licenta.data.OnGetDataListener;
import com.example.licenta.data.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Value;
import com.google.protobuf.ValueOrBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {
    TextView btn_Breakfast,btn_Dinner,btn_Lunch,btn_snacks;
    ListView lv_Breakfast,lv_Dinner,lv_Lunch,lv_Snacks,lv_Exercises;
    TextView totalKcal,todayDate;
    ProgressBar progressTotal;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase fDatabase=FirebaseDatabase.getInstance();


    DecimalFormat df = new DecimalFormat("0.00");

    public final static int REQUEST_CODE_A = 1;
    public final static int REQUEST_CODE_B = 2;
    public final static int REQUEST_CODE_C = 3;
    public final static int REQUEST_CODE_D = 4;

    private ArrayList<FoodReportModel> listBreakfast = new ArrayList<>();
    private ArrayList<FoodReportModel> listLunch = new ArrayList<>();
    private ArrayList<FoodReportModel> listDinner = new ArrayList<>();
    private ArrayList<FoodReportModel> listSnacks = new ArrayList<>();
    private ArrayList<Exercice> listTraining = new ArrayList<>();

    DiaryActivity.CustomAdapter adapter;
    DiaryActivity.CustomAdapter adapter2;
    DiaryActivity.CustomAdapter adapter3;
    DiaryActivity.CustomAdapter adapter4;
    DiaryActivity.CustomAdapter1 adapter5;

    User user=new User();
    double totalKcalsConsumed=0;
    double totalFatConsumed=0;
    double totalCarbsConsumed=0;
    double totalProteinConsumed=0;
    double totalFiberConsumed=0;
    double totalSugarsConsumed=0;
    double totalKcalsBurned=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        getSupportActionBar().hide();

        btn_Breakfast=findViewById(R.id.btnBreakfast);
        btn_Dinner=findViewById(R.id.btnDinner);
        btn_Lunch=findViewById(R.id.btnLunch);
        btn_snacks=findViewById(R.id.btnSnacks);

        totalKcal=findViewById(R.id.tvTotal);
        todayDate=findViewById(R.id.tvCarbsText);
        progressTotal=findViewById(R.id.progressBarTotal);

        lv_Breakfast=findViewById(R.id.listBreakfast);
        adapter = new DiaryActivity.CustomAdapter(this,R.layout.row_item9,listBreakfast);
        lv_Breakfast.setAdapter(adapter);

        lv_Lunch=findViewById(R.id.listLunch);
        adapter2 = new DiaryActivity.CustomAdapter(this,R.layout.row_item9, listLunch);
        lv_Lunch.setAdapter(adapter2);

        lv_Dinner=findViewById(R.id.listDinner);
        adapter3 = new DiaryActivity.CustomAdapter(this,R.layout.row_item9,listDinner);
        lv_Dinner.setAdapter(adapter3);

        lv_Snacks=findViewById(R.id.listSnack);
        adapter4=new DiaryActivity.CustomAdapter(this, R.layout.row_item9,listSnacks);
        lv_Snacks.setAdapter(adapter4);

        lv_Exercises=findViewById(R.id.listExercices);
        adapter5=new DiaryActivity.CustomAdapter1(this,R.layout.row_item9,listTraining);
        lv_Exercises.setAdapter(adapter5);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.diary);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.plans:
                        startActivity(new Intent(getApplicationContext(),MealPlansActivity.class));
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
        ValueEventListener l1= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    FoodReportModel post = child.getValue(FoodReportModel.class);
                    listBreakfast.add(post);
                }
                adapter.notifyDataSetChanged();
                totalKcalsConsumed = totalKcals();
                totalKcalsBurned=totalKcalsBurned();
                totalFatConsumed=totalFatConsumed();
                totalProteinConsumed=totalProteinConsumed();
                totalCarbsConsumed=totalCarbsConsumed();
                progressTotal.setProgress((int) ( totalKcalsConsumed-totalKcalsBurned));

                if (totalKcalsConsumed-totalKcalsBurned<0)
                {
                    totalKcal.setText("0.0");
                }
                else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed-totalKcalsBurned)));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };
        fDatabase.getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Breakfast").addListenerForSingleValueEvent(l1);
        fDatabase.getReference().removeEventListener(l1);

        ValueEventListener l2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    FoodReportModel post = child.getValue(FoodReportModel.class);
                    listLunch.add(post);
                }
                adapter2.notifyDataSetChanged();
                totalKcalsConsumed = totalKcals();
                totalKcalsBurned=totalKcalsBurned();
                totalFatConsumed=totalFatConsumed();
                totalProteinConsumed=totalProteinConsumed();
                totalCarbsConsumed=totalCarbsConsumed();
                progressTotal.setProgress((int) ( totalKcalsConsumed-totalKcalsBurned));

                if (totalKcalsConsumed-totalKcalsBurned<0)
                {
                    totalKcal.setText("0.0");
                }
                else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed-totalKcalsBurned)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };
        fDatabase.getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Lunch").addListenerForSingleValueEvent(l2);
        fDatabase.getReference().removeEventListener(l2);
        ValueEventListener l3 =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    FoodReportModel post = child.getValue(FoodReportModel.class);
                    listDinner.add(post);
                }
                adapter3.notifyDataSetChanged();
                totalKcalsConsumed = totalKcals();
                totalKcalsBurned=totalKcalsBurned();
                totalFatConsumed=totalFatConsumed();
                totalProteinConsumed=totalProteinConsumed();
                totalCarbsConsumed=totalCarbsConsumed();
                progressTotal.setProgress((int) ( totalKcalsConsumed-totalKcalsBurned));

                if (totalKcalsConsumed-totalKcalsBurned<0)
                {
                    totalKcal.setText("0.0");
                }
                else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed-totalKcalsBurned)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };
        fDatabase.getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Dinner").addListenerForSingleValueEvent(l3);
        fDatabase.getReference().removeEventListener(l3);
        ValueEventListener l4=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    FoodReportModel post = child.getValue(FoodReportModel.class);
                    listSnacks.add(post);
                }
                adapter4.notifyDataSetChanged();
                totalKcalsConsumed = totalKcals();
                totalKcalsBurned=totalKcalsBurned();
                totalFatConsumed=totalFatConsumed();
                totalProteinConsumed=totalProteinConsumed();
                totalCarbsConsumed=totalCarbsConsumed();
                progressTotal.setProgress((int) ( totalKcalsConsumed-totalKcalsBurned));

                if (totalKcalsConsumed-totalKcalsBurned<0)
                {
                    totalKcal.setText("0.0");
                }
                else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed-totalKcalsBurned)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
            }
        };
        fDatabase.getReference()
                .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Snacks").addListenerForSingleValueEvent(l4);
        fDatabase.getReference().removeEventListener(l4);

        ValueEventListener l5 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Exercice post = child.getValue(Exercice.class);
                    listTraining.add(post);
                }
                adapter5.notifyDataSetChanged();
                totalKcalsBurned=totalKcalsBurned();
                if (listTraining.size()>0)
                {
                    fDatabase.getReference()
                            .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("kcal_burned").setValue(totalKcalsBurned);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        fDatabase.getReference()
                    .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Training").addListenerForSingleValueEvent(l5);
        fDatabase.getReference().removeEventListener(l5);

        ValueEventListener l6=new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user= snapshot.getValue(User.class);
                Timestamp timestamp=Timestamp.now();

                todayDate.setText(Timestamp.now().toDate().toString().substring(0,10));
                progressTotal.setMax((int) user.getGoal().getKcalFit());
                totalKcalsBurned=totalKcalsBurned();
                progressTotal.setProgress((int) ( totalKcalsConsumed-totalKcalsBurned));

                if (totalKcalsConsumed-totalKcalsBurned<0)
                    totalKcal.setText("0.0");
                else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed-totalKcalsBurned)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        fDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(l6);
        fDatabase.getReference().removeEventListener(l6);
        btn_Breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddFoodActivity.class);
                startActivityForResult(intent,REQUEST_CODE_A);
            }
        });

        btn_Lunch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddFoodActivity.class);
                startActivityForResult(intent,REQUEST_CODE_B);
            }
        });

        btn_Dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddFoodActivity.class);
                startActivityForResult(intent,REQUEST_CODE_C);
            }
        });

        btn_snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddFoodActivity.class);
                startActivityForResult(intent,REQUEST_CODE_D);
            }
        });
        lv_Breakfast.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fDatabase.getReference()
                        .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Breakfast").child(String.valueOf(position)).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                listBreakfast.remove(position);
                                adapter.notifyDataSetChanged();
                                totalKcalsConsumed = totalKcals();
                                totalKcalsBurned = totalKcalsBurned();
                                totalFatConsumed = totalFatConsumed();
                                totalProteinConsumed = totalProteinConsumed();
                                totalSugarsConsumed = totalSugarConsumed();
                                totalFiberConsumed = totalFiberConsumed();
                                totalCarbsConsumed = totalCarbsConsumed();

                                progressTotal.setProgress((int) (totalKcalsConsumed - totalKcalsBurned));

                                if (totalKcalsConsumed - totalKcalsBurned < 0) {
                                    totalKcal.setText("0.0");
                                } else
                                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed - totalKcalsBurned)));

                                adapter.notifyDataSetChanged();

                                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                                ValueEventListener l = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        user = snapshot.getValue(User.class);
                                        user.getCurrentDiary().setFat_consumed(totalFatConsumed);
                                        user.getCurrentDiary().setProtein_consumed(totalProteinConsumed);
                                        user.getCurrentDiary().setCarbs_consumed(totalCarbsConsumed);
                                        user.getCurrentDiary().setFiber_consumed(totalFiberConsumed);
                                        user.getCurrentDiary().setSugars_consumed(totalSugarsConsumed);
                                        user.getCurrentDiary().setKcal_consumed(totalKcalsConsumed);
                                        ref1.setValue(user);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                mReadDataOnce(ref1,l);
                                ref1.removeEventListener(l);
                            }
                        });
                return true;
            }
        });
        lv_Dinner.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fDatabase.getReference()
                        .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Dinner").child(String.valueOf(position)).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                listDinner.remove(position);
                                adapter3.notifyDataSetChanged();
                                totalKcalsConsumed = totalKcals();
                                totalKcalsBurned = totalKcalsBurned();
                                totalFatConsumed = totalFatConsumed();
                                totalProteinConsumed = totalProteinConsumed();
                                totalSugarsConsumed = totalSugarConsumed();
                                totalFiberConsumed = totalFiberConsumed();
                                totalCarbsConsumed = totalCarbsConsumed();

                                progressTotal.setProgress((int) (totalKcalsConsumed - totalKcalsBurned));

                                if (totalKcalsConsumed - totalKcalsBurned < 0) {
                                    totalKcal.setText("0.0");
                                } else
                                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed - totalKcalsBurned)));

                                adapter3.notifyDataSetChanged();

                                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                                ValueEventListener l =new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        user = snapshot.getValue(User.class);
                                        user.getCurrentDiary().setFat_consumed(totalFatConsumed);
                                        user.getCurrentDiary().setProtein_consumed(totalProteinConsumed);
                                        user.getCurrentDiary().setCarbs_consumed(totalCarbsConsumed);
                                        user.getCurrentDiary().setFiber_consumed(totalFiberConsumed);
                                        user.getCurrentDiary().setSugars_consumed(totalSugarsConsumed);
                                        user.getCurrentDiary().setKcal_consumed(totalKcalsConsumed);
                                        ref1.setValue(user);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                mReadDataOnce(ref1,l);
                                ref1.removeEventListener(l);
                            }
                        });
                return true;
            }
        });
        lv_Lunch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fDatabase.getReference()
                        .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Lunch").child(String.valueOf(position)).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                listLunch.remove(position);
                                adapter2.notifyDataSetChanged();
                                totalKcalsConsumed = totalKcals();
                                totalKcalsBurned = totalKcalsBurned();
                                totalFatConsumed = totalFatConsumed();
                                totalProteinConsumed = totalProteinConsumed();
                                totalSugarsConsumed = totalSugarConsumed();
                                totalFiberConsumed = totalFiberConsumed();
                                totalCarbsConsumed = totalCarbsConsumed();

                                progressTotal.setProgress((int) (totalKcalsConsumed - totalKcalsBurned));

                                if (totalKcalsConsumed - totalKcalsBurned < 0) {
                                    totalKcal.setText("0.0");
                                } else
                                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed - totalKcalsBurned)));

                                adapter2.notifyDataSetChanged();

                                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                                ValueEventListener l10= new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        user = snapshot.getValue(User.class);
                                        user.getCurrentDiary().setFat_consumed(totalFatConsumed);
                                        user.getCurrentDiary().setProtein_consumed(totalProteinConsumed);
                                        user.getCurrentDiary().setCarbs_consumed(totalCarbsConsumed);
                                        user.getCurrentDiary().setFiber_consumed(totalFiberConsumed);
                                        user.getCurrentDiary().setSugars_consumed(totalSugarsConsumed);
                                        user.getCurrentDiary().setKcal_consumed(totalKcalsConsumed);
                                        ref1.setValue(user);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                mReadDataOnce(ref1,l10);
                                ref1.removeEventListener(l10);
                            }
                        });
                return true;
            }
        });
        lv_Snacks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fDatabase.getReference()
                        .child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Snacks").child(String.valueOf(position)).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                listSnacks.remove(position);
                                adapter4.notifyDataSetChanged();
                                totalKcalsConsumed = totalKcals();
                                totalKcalsBurned = totalKcalsBurned();
                                totalFatConsumed = totalFatConsumed();
                                totalProteinConsumed = totalProteinConsumed();
                                totalSugarsConsumed = totalSugarConsumed();
                                totalFiberConsumed = totalFiberConsumed();
                                totalCarbsConsumed = totalCarbsConsumed();

                                progressTotal.setProgress((int) (totalKcalsConsumed - totalKcalsBurned));

                                if (totalKcalsConsumed - totalKcalsBurned < 0) {
                                    totalKcal.setText("0.0");
                                } else
                                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed - totalKcalsBurned)));

                                adapter4.notifyDataSetChanged();
                                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                                ValueEventListener l = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        user = snapshot.getValue(User.class);
                                        user.getCurrentDiary().setFat_consumed(totalFatConsumed);
                                        user.getCurrentDiary().setProtein_consumed(totalProteinConsumed);
                                        user.getCurrentDiary().setCarbs_consumed(totalCarbsConsumed);
                                        user.getCurrentDiary().setFiber_consumed(totalFiberConsumed);
                                        user.getCurrentDiary().setSugars_consumed(totalSugarsConsumed);
                                        user.getCurrentDiary().setKcal_consumed(totalKcalsConsumed);
                                        ref1.setValue(user);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                mReadDataOnce(ref1,l);
                                ref1.removeEventListener(l);
                            }
                        });
                return true;
            }
        });
    }
        public class CustomAdapter extends ArrayAdapter<FoodReportModel>{

            private ArrayList<FoodReportModel> dataSet;
            Context mContext;
            int mResource;

            // View lookup cache
            private  class ViewHolder {
                TextView txtName;
                TextView txtType;
            }


            public CustomAdapter(Context context,int resource,ArrayList<FoodReportModel> data) {
                super(context, resource, data);
                this.mContext=context;
                mResource=resource;
            }

            private int lastPosition = -1;

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the data item for this position
                FoodReportModel dataModel = getItem(position);
                if (dataModel!=null) {
                    // Check if an existing view is being reused, otherwise inflate the view
                    DiaryActivity.CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag


                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    convertView = inflater.inflate(mResource, parent, false);
                    TextView txtName = convertView.findViewById(R.id.tv_lvFoodName);
                    TextView txtType = convertView.findViewById(R.id.dateText);

                    txtName.setText(dataModel.name);
                    txtType.setText(String.valueOf(dataModel.calories));
                }
                    // Return the completed view to render on screen
                    return convertView;
            }
        }

    public class CustomAdapter1 extends ArrayAdapter<Exercice>{

        private ArrayList<Exercice> dataSet;
        Context mContext;
        int mResource;

        // View lookup cache
        private  class ViewHolder {
            TextView txtName;
            TextView txtType;
        }

        public CustomAdapter1(Context context,int resource,ArrayList<Exercice> data) {
            super(context, resource, data);
            this.mContext=context;
            mResource=resource;
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Exercice dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            DiaryActivity.CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag



            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            TextView txtName = convertView.findViewById(R.id.tv_lvFoodName);
            TextView txtType = convertView.findViewById(R.id.dateText);

            txtName.setText(dataModel.name);
            txtType.setText(String.valueOf(dataModel.kcalBurned));

            // Return the completed view to render on screen
            return convertView;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_CODE_A) {

                Bundle args = data.getBundleExtra("resultBundle");
                ArrayList<FoodReportModel> result = (ArrayList<FoodReportModel>) args.getSerializable("result");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Breakfast");
                List<FoodReportModel> alreadyInFirebase = new ArrayList<FoodReportModel>();
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        listBreakfast.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FoodReportModel data = snap.getValue(FoodReportModel.class);
                            alreadyInFirebase.add(data);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                fDatabase.getReference().removeEventListener(childEventListener);

                listBreakfast.addAll(alreadyInFirebase);
                listBreakfast.addAll(result);
                totalKcalsConsumed = totalKcals();
                totalKcalsBurned = totalKcalsBurned();
                totalFatConsumed = totalFatConsumed();
                totalProteinConsumed = totalProteinConsumed();
                totalCarbsConsumed = totalCarbsConsumed();
                totalSugarsConsumed=totalSugarConsumed();
                totalFiberConsumed=totalFiberConsumed();
                progressTotal.setProgress((int) (totalKcalsConsumed - totalKcalsBurned));

                if (totalKcalsConsumed - totalKcalsBurned < 0) {
                    totalKcal.setText("0.0");
                } else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed - totalKcalsBurned)));

                adapter.notifyDataSetChanged();
                ref.setValue(listBreakfast);
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                ValueEventListener l8=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        user.getCurrentDiary().setFat_consumed(totalFatConsumed);
                        user.getCurrentDiary().setProtein_consumed(totalProteinConsumed);
                        user.getCurrentDiary().setCarbs_consumed(totalCarbsConsumed);
                        user.getCurrentDiary().setFiber_consumed(totalFiberConsumed);
                        user.getCurrentDiary().setSugars_consumed(totalSugarsConsumed);
                        user.getCurrentDiary().setKcal_consumed(totalKcalsConsumed);
                        ref1.setValue(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                    mReadDataOnce(ref1,l8);
                    ref1.removeEventListener(l8);
            }
            if (requestCode == REQUEST_CODE_B) {
                Bundle args = data.getBundleExtra("resultBundle");
                ArrayList<FoodReportModel> result = (ArrayList<FoodReportModel>) args.getSerializable("result");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Lunch");
                List<FoodReportModel> alreadyInFirebase = new ArrayList<FoodReportModel>();
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        listLunch.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FoodReportModel data = snap.getValue(FoodReportModel.class);
                            alreadyInFirebase.add(data);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                fDatabase.getReference().removeEventListener(childEventListener);

                listLunch.addAll(alreadyInFirebase);
                listLunch.addAll(result);
                adapter2.notifyDataSetChanged();
                ref.setValue(listLunch);
                totalKcalsConsumed = totalKcals();
                totalKcalsBurned = totalKcalsBurned();
                totalFatConsumed = totalFatConsumed();
                totalSugarsConsumed=totalSugarConsumed();
                totalFiberConsumed=totalFiberConsumed();
                totalProteinConsumed = totalProteinConsumed();
                totalCarbsConsumed = totalCarbsConsumed();
                progressTotal.setProgress((int) (totalKcalsConsumed - totalKcalsBurned));

                if (totalKcalsConsumed - totalKcalsBurned < 0) {
                    totalKcal.setText("0.0");
                } else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed - totalKcalsBurned)));

                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                ValueEventListener l7=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        user.getCurrentDiary().setFat_consumed(totalFatConsumed);
                        user.getCurrentDiary().setProtein_consumed(totalProteinConsumed);
                        user.getCurrentDiary().setCarbs_consumed(totalCarbsConsumed);
                        user.getCurrentDiary().setFiber_consumed(totalFiberConsumed);
                        user.getCurrentDiary().setSugars_consumed(totalSugarsConsumed);
                        user.getCurrentDiary().setKcal_consumed(totalKcalsConsumed);

                        ref1.setValue(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };

                mReadDataOnce(ref1,l7);
                ref1.removeEventListener(l7);
            }
            if (requestCode == REQUEST_CODE_C) {
                Bundle args = data.getBundleExtra("resultBundle");
                ArrayList<FoodReportModel> result = (ArrayList<FoodReportModel>) args.getSerializable("result");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Dinner");
                List<FoodReportModel> alreadyInFirebase = new ArrayList<FoodReportModel>();
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        listDinner.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FoodReportModel data = snap.getValue(FoodReportModel.class);
                            alreadyInFirebase.add(data);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                fDatabase.getReference().removeEventListener(childEventListener);

                fDatabase.getReference().removeEventListener(childEventListener);
                listDinner.addAll(alreadyInFirebase);
                listDinner.addAll(result);
                adapter3.notifyDataSetChanged();
                ref.setValue(listDinner);

                totalKcalsConsumed = totalKcals();
                totalKcalsBurned = totalKcalsBurned();
                totalFatConsumed = totalFatConsumed();
                totalProteinConsumed = totalProteinConsumed();
                totalSugarsConsumed=totalSugarConsumed();
                totalFiberConsumed=totalFiberConsumed();
                totalCarbsConsumed = totalCarbsConsumed();
                progressTotal.setProgress((int) (totalKcalsConsumed - totalKcalsBurned));

                if (totalKcalsConsumed - totalKcalsBurned < 0) {
                    totalKcal.setText("0.0");
                } else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed - totalKcalsBurned)));
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                ValueEventListener l9=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        user.getCurrentDiary().setFat_consumed(totalFatConsumed);
                        user.getCurrentDiary().setProtein_consumed(totalProteinConsumed);
                        user.getCurrentDiary().setCarbs_consumed(totalCarbsConsumed);
                        user.getCurrentDiary().setFiber_consumed(totalFiberConsumed);
                        user.getCurrentDiary().setSugars_consumed(totalSugarsConsumed);
                        user.getCurrentDiary().setKcal_consumed(totalKcalsConsumed);

                        ref1.setValue(user);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                    mReadDataOnce(ref1,l9);
                    ref1.removeEventListener(l9);
            }

            if (requestCode == REQUEST_CODE_D) {
                Bundle args = data.getBundleExtra("resultBundle");
                ArrayList<FoodReportModel> result = (ArrayList<FoodReportModel>) args.getSerializable("result");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Snacks");
                List<FoodReportModel> alreadyInFirebase = new ArrayList<FoodReportModel>();
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        listSnacks.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FoodReportModel data = snap.getValue(FoodReportModel.class);
                            alreadyInFirebase.add(data);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                fDatabase.getReference().removeEventListener(childEventListener);

                listSnacks.addAll(alreadyInFirebase);
                listSnacks.addAll(result);
                adapter4.notifyDataSetChanged();
                ref.setValue(listSnacks);
                totalKcalsConsumed = totalKcals();
                totalKcalsBurned = totalKcalsBurned();
                totalFatConsumed = totalFatConsumed();
                totalProteinConsumed = totalProteinConsumed();
                totalSugarsConsumed=totalSugarConsumed();
                totalFiberConsumed=totalFiberConsumed();
                totalCarbsConsumed = totalCarbsConsumed();
                progressTotal.setProgress((int) (totalKcalsConsumed - totalKcalsBurned));

                if (totalKcalsConsumed - totalKcalsBurned < 0) {
                    totalKcal.setText("0.0");
                } else
                    totalKcal.setText(String.valueOf(df.format(totalKcalsConsumed - totalKcalsBurned)));

                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                ValueEventListener l6 =new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        user.getCurrentDiary().setFat_consumed(totalFatConsumed);
                        user.getCurrentDiary().setProtein_consumed(totalProteinConsumed);
                        user.getCurrentDiary().setCarbs_consumed(totalCarbsConsumed);
                        user.getCurrentDiary().setKcal_consumed(totalKcalsConsumed);
                        user.getCurrentDiary().setFiber_consumed(totalFiberConsumed);
                        user.getCurrentDiary().setSugars_consumed(totalSugarsConsumed);
                        user.getCurrentDiary().setKcal_burned(totalKcalsBurned());
                        ref1.setValue(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                    mReadDataOnce(ref1,l6);
                    ref1.removeEventListener(l6);
            }
        }
    }

    private double totalKcalsBurned() {
       double sum=0;
       for(Exercice x:listTraining)
       {
           sum+=x.kcalBurned;
       }
       return sum;
    }

    public double totalKcals()
        {
            double sum=0 ;
            for (FoodReportModel food: listBreakfast)
            {
                sum+=food.calories;
            }
            for (FoodReportModel food: listDinner)
            {
                sum+=food.calories;
            }
            for (FoodReportModel food: listLunch)
            {
                sum+=food.calories;
            }
            for (FoodReportModel food: listSnacks)
            {
                sum+=food.calories;
            }

            return sum;
        }

        public double totalCarbsConsumed()
        {
            double sum=0 ;
            for (FoodReportModel food: listBreakfast)
            {
                sum+=food.carbs;
            }
            for (FoodReportModel food: listDinner)
            {
                sum+=food.carbs;
            }
            for (FoodReportModel food: listLunch)
            {
                sum+=food.carbs;
            }
            for (FoodReportModel food: listSnacks)
            {
                sum+=food.carbs;
            }
            return sum ;
        }
    public double totalFatConsumed()
    {
        double sum=0 ;
        for (FoodReportModel food: listBreakfast)
        {
            sum+=food.fat;
        }
        for (FoodReportModel food: listDinner)
        {
            sum+=food.fat;
        }
        for (FoodReportModel food: listLunch)
        {
            sum+=food.fat;
        }
        for (FoodReportModel food: listSnacks)
        {
            sum+=food.fat;
        }
        return sum ;
    }
    public double totalProteinConsumed()
    {
        double sum=0 ;
        for (FoodReportModel food: listBreakfast)
        {
            sum+=food.protein;
        }
        for (FoodReportModel food: listDinner)
        {
            sum+=food.protein;
        }
        for (FoodReportModel food: listLunch)
        {
            sum+=food.protein;
        }
        for (FoodReportModel food: listSnacks)
        {
            sum+=food.protein;
        }

        return sum ;
    }
    public double totalSugarConsumed()
    {
        double sum=0 ;
        for (FoodReportModel food: listBreakfast)
        {
            sum+=food.sugars;
        }
        for (FoodReportModel food: listDinner)
        {
            sum+=food.sugars;
        }
        for (FoodReportModel food: listLunch)
        {
            sum+=food.sugars;
        }
        for (FoodReportModel food: listSnacks)
        {
            sum+=food.sugars;
        }

        return sum ;
    }
    public double totalFiberConsumed()
    {
        double sum=0 ;
        for (FoodReportModel food: listBreakfast)
        {
            sum+=food.fiber;
        }
        for (FoodReportModel food: listDinner)
        {
            sum+=food.fiber;
        }
        for (FoodReportModel food: listLunch)
        {
            sum+=food.fiber;
        }
        for (FoodReportModel food: listSnacks)
        {
            sum+=food.fiber;
        }

        return sum ;
    }
    public static synchronized void mReadDataOnce(DatabaseReference child, ValueEventListener ok) {
        child.addListenerForSingleValueEvent(ok);
    }
}

