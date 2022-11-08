package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.data.Diary;
import com.example.licenta.data.Exercice;
import com.example.licenta.data.Goal;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

    private ListView listHistory;
    private ArrayList<Diary> diaries=new ArrayList<>();
    private FirebaseDatabase fDatabase=FirebaseDatabase.getInstance();
    double maxProgress=0;
    private Spinner spinner;
    HistoryActivity.CustomAdapter1 adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        listHistory = findViewById(R.id.listHistory);
        spinner=findViewById(R.id.historySpinner);
        String[] stringArray = getResources().getStringArray(R.array.Months_Spinner);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,stringArray));
        adapter = new HistoryActivity.CustomAdapter1(this,R.layout.row_item3,diaries);
        listHistory.setAdapter(adapter);
        spinner.setSelection(Timestamp.now().toDate().getMonth());
        fDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("goal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Goal goal=snapshot.getValue(Goal.class);
                    maxProgress=goal.getKcalFit();
                    fDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("historyDiaries").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren())
                        {
                            Diary diary= snap.getValue(Diary.class);
                            if(new Date(diary.getCurrentDate()).getMonth() == Timestamp.now().toDate().getMonth()) {
                                diaries.add(diary);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectedHistory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getSelectedHistory(int position) {

            fDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("historyDiaries").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    diaries.clear();
                    for (DataSnapshot snap : snapshot.getChildren())
                    {
                        Diary diary= snap.getValue(Diary.class);
                        if(new Date(diary.getCurrentDate()).getMonth()==position)
                        diaries.add(diary);
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    public class CustomAdapter1 extends ArrayAdapter<Diary>{

            private ArrayList<Diary> dataSet;
            Context mContext;
            int mResource;

            // View lookup cache
            private  class ViewHolder {
                TextView txtName;
                TextView txtType;
            }


            public CustomAdapter1(Context context,int resource,ArrayList<Diary> data) {
                super(context, resource, data);
                this.mContext=context;
                mResource=resource;
            }

            private int lastPosition = -1;

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the data item for this position
                Diary dataModel = getItem(position);
                // Check if an existing view is being reused, otherwise inflate the view
               // HistoryActivity.CustomAdapter1.ViewHolder viewHolder; // view lookup cache stored in tag
                DecimalFormat format= new DecimalFormat("0.00");
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                TextView txtview = (TextView) convertView.findViewById(R.id.dateText);
                TextView txtview2= convertView.findViewById(R.id.proteinHistory);
                TextView txtview3= convertView.findViewById(R.id.carbsHistroy);
                TextView txtview4= convertView.findViewById(R.id.fatHistory);
                TextView txtView5=convertView.findViewById(R.id.tvSetKcalProgress);
                ProgressBar progressBar2 = convertView.findViewById(R.id.progressBarKcalLeft);

                txtview.setText(dateformat.format(diaries.get(position).getCurrentDate()));
                txtview2.setText(format.format(diaries.get(position).getProtein_consumed()));
                txtview3.setText(format.format(diaries.get(position).getCarbs_consumed()));
                txtview4.setText(format.format(diaries.get(position).getFat_consumed()));
                txtView5.setText(format.format(diaries.get(position).getKcal_consumed()));

                progressBar2.setMax((int) maxProgress);
                progressBar2.setProgress((int) diaries.get(position).getKcal_consumed());
                // Return the completed view to render on screen
                return convertView;
            }
    }
}