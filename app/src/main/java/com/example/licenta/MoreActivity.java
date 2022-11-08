package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.licenta.data.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MoreActivity extends AppCompatActivity {


    private ListView listview1;
    private ImageView profile;
    private TextView username;
    private ArrayList<String> mylist = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        getSupportActionBar().hide();

        listview1 = findViewById(R.id.listHistory);
        username=findViewById(R.id.tv_profileUserName);
        profile=findViewById(R.id.profileMoreImage);
        StorageReference profileRef=storageReference.child(FirebaseAuth.getInstance().getUid()+".jpg");
        if(profileRef!=null)
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into(profile);
                }
            });
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User currentUser=snapshot.getValue(User.class);
                username.setText(currentUser.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View child, int pos, long _pos){
                // Toast.makeText(getApplicationContext(), mylist.get(pos), Toast.LENGTH_LONG).show();
            }
        });

        mylist.add("Personal Data");
        mylist.add("Change Goals");
        mylist.add("History");
        mylist.add("Sign out");

        MoreActivity.MyAdapter adapter = new MoreActivity.MyAdapter(this, mylist);
        listview1.setAdapter(adapter);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                {
                    startActivity(new Intent(getApplicationContext(),EditPersonalDataActivity.class));
                }
                if(position==1)
                {
                    startActivity(new Intent(getApplicationContext(),ChangeGoalsActivity.class));
                }
                if(position==2)
                {
                    startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                }
                if(position==3)
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }
            }
        });
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.more);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.diary:
                        startActivity(new Intent(getApplicationContext(),DiaryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.exercies:
                        startActivity(new Intent(getApplicationContext(),ExercisesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.plans:
                        startActivity(new Intent(getApplicationContext(),MealPlansActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    public class MyAdapter extends ArrayAdapter<String>
    {
        public MyAdapter(Context context, ArrayList<String> string_list)
        {
            super(context, 0, string_list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item8, parent, false);
            }
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            TextView txtview = (TextView) convertView.findViewById(R.id.dateText);


            txtview.setText(mylist.get(position));


            return convertView;
        }
    }

}