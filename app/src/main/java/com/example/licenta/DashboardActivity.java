package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.licenta.data.Diary;
import com.example.licenta.data.MealPlan;
import com.example.licenta.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

// Proteins, carbs and fat diet
// 20-25 % protein
// 40-50 % carbs
// 25-35 % fat

//                2. No more than 30% of calories should be from fat, so take the total calories and multiply by 30%.
//
//                ____calories per day x 0.30 = ____ calories from fat per day.
//
//                3.  Because there are 9 calories in each gram of fat, take calories from fat per day and divide by 9.
//
//                ____calories from fat per day divided by 9 = ____fat grams per day.

public class DashboardActivity extends AppCompatActivity {
    TextView progressText,usernameMessage,tv_date;
    Button nutrients,exercises;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth fAuth;
    FirebaseDatabase fDatabase;
    DatabaseReference ref;
    ProgressBar kcalLeft,proteinProgress,carbsProgress,fatProgress;
    DecimalFormat df = new DecimalFormat("0.00");
    ImageView imageProfile;
    StorageReference storageReference;

    private List<MealPlan> movieList = new ArrayList<>();
    private DashboardActivity.MealAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

        nutrients=findViewById(R.id.bt_dashNutrients);
        exercises=findViewById(R.id.bt_exercices);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        usernameMessage=findViewById(R.id.tv_userName);
        progressText=findViewById(R.id.progress_text);
        kcalLeft=findViewById(R.id.progressBarKcalLeft);
        proteinProgress=findViewById(R.id.pb_dash_protein);
        fatProgress=findViewById(R.id.pb_fat_dash);
        carbsProgress=findViewById(R.id.pb_dash_carbs);
        tv_date=findViewById(R.id.tv_today);
        imageProfile=findViewById(R.id.iv_imageProfile);

        fAuth=FirebaseAuth.getInstance();
        fDatabase=FirebaseDatabase.getInstance();
        ref=FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference profileRef=storageReference.child(fAuth.getUid()+".jpg");
        if(profileRef!=null)
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(imageProfile);

            }

        });
        FirebaseUser user=fAuth.getCurrentUser();
        String userKey=user.getUid();
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new MealAdapter(movieList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fDatabase.getReference().child("Users").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Timestamp timestamp=Timestamp.now();
                if (Timestamp.now().toDate().after(new Date(String.valueOf(user.getCurrentDiary().atEndOfDay(new Date(user.getCurrentDiary().getCurrentDate()))))))
                {
                    if(user.getHistoryDiaries()==null)
                    {
                        user.setHistoryDiaries(new ArrayList<Diary>());
                    }
                    user.getHistoryDiaries().add(user.getCurrentDiary());
                    user.setCurrentDiary(new Diary(Diary.atStartOfDay(Timestamp.now().toDate()), Timestamp.now().toDate(),Diary.atEndOfDay(Timestamp.now().toDate())));

                }
                ref.child("Users").child(userKey).setValue(user);
                ValueEventListener l1= new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user =snapshot.getValue(User.class);
                        if((user.getGoal().getKcalFit()-user.getCurrentDiary().getKcal_consumed())<0)
                            progressText.setText("0");
                        else
                            progressText.setText(df.format(user.getGoal().getKcalFit()-user.getCurrentDiary().getKcal_consumed())+ "");

                        proteinProgress.setMax((int) user.getGoal().getProteinNeeded());
                        carbsProgress.setMax((int) user.getGoal().getCarbsFit());
                        fatProgress.setMax((int) user.getGoal().getFatFit());
                        kcalLeft.setMax((int)user.getGoal().getKcalFit());

                        kcalLeft.setProgress(((int) user.getCurrentDiary().getKcal_consumed()));
                        proteinProgress.setProgress((int) user.getCurrentDiary().protein_consumed);
                        fatProgress.setProgress((int) user.getCurrentDiary().fat_consumed);
                        carbsProgress.setProgress((int)user.getCurrentDiary().carbs_consumed);
                        usernameMessage.setText("Welcome "+user.getUsername());
                        tv_date.setText(Timestamp.now().toDate().toString().substring(0,10));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                fDatabase.getReference().child("Users").child(userKey).addListenerForSingleValueEvent(l1);
                fDatabase.getReference().removeEventListener(l1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ValueEventListener l2=new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movieList.clear();
                for(DataSnapshot snap:snapshot.getChildren())
                {
                    MealPlan meal =snap.getValue(MealPlan.class);
                    if(meal.isFavorite()==true)
                        movieList.add(meal);
                }
                mAdapter.notifyDataSetChanged();

            }  @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        fDatabase.getReference().child("MealPlan").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(l2);
        fDatabase.getReference().removeEventListener(l2);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.diary:
                        startActivity(new Intent(getApplicationContext(),DiaryActivity.class));
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

        nutrients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),NutrientsActivity.class);
                intent.putExtra("today",Timestamp.now());
                startActivity(intent);
            }
        });
        exercises.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),ExercisesActivity.class);
                startActivityForResult(intent,1);
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Uri imageUri=data.getData();
                StorageReference fileRef=storageReference.child(fAuth.getUid()+".jpg");
                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext()).load(uri).into(imageProfile);
                            }
                        });
                    }
                });
            }
        }
    }

    public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> {
        private List<MealPlan> moviesList;

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title, year;
            ImageView image;

            MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.favoritiesTextDash);
                year = view.findViewById(R.id.favoritiesTextDash2);
                image = view.findViewById(R.id.iv_Card1);
            }
        }

        public MealAdapter(List<MealPlan> moviesList) {
            this.moviesList = moviesList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item10, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            MealPlan movie = moviesList.get(position);
            holder.title.setText(movie.getName());
            holder.year.setText(movie.getCategory());
            Glide.with(holder.itemView).load(movieList.get(position).getImageUrl()).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }
}