package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.licenta.data.Diary;
import com.example.licenta.data.MealPlan;
import com.example.licenta.data.Task;
import com.example.licenta.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    EditText FirstName,LastName,Username,Password,Email;
    Button Next;
    Intent intent;
    FirebaseAuth fAuth;
    String description = "These meals and snacks focus on protein and fiber to trim your midsection and lose weight in this easy high-protein diet plan.";
    List<String> descriptionsPerDay=new ArrayList<String>(Arrays.asList("Eat vegetables and grill",
            "Let's consume some fruits and noodles","Today we have a good soup"
            ,"Musli Time","Cook the Slow-Cooker Vegan Chili on Low for 8 hours so it's ready in time for dinner tonight."));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        intent = getIntent();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        FirstName = findViewById(R.id.User_FirstNameEditText);
        LastName = findViewById(R.id.User_LastNameEditText);
        Username = findViewById(R.id.User_UsernameEditText);
        Password = findViewById(R.id.User_PasswordEditText);
        Email = findViewById(R.id.User_EmailEditText);
        Next = findViewById(R.id.User_SignIn_Button);

        Next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String firstName = FirstName.getText().toString().trim();
                String lastName = LastName.getText().toString().trim();
                String userName = Username.getText().toString().trim();

                if (TextUtils.isEmpty(email) || !email.contains("@")) {
                    Email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(firstName)|| firstName.length()<2) {
                    FirstName.setError("First Name is required");
                    return;
                }
                if (TextUtils.isEmpty(lastName)||lastName.length()<2) {
                    LastName.setError("Last Name is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Password.setError("Password is required");
                    return;
                }
                if (password.length() < 8) {
                    Password.setError("Password must be greater then 8 Characters");
                    return;
                }
                if (TextUtils.isEmpty(userName)||userName.length()<2) {
                    Username.setError("Username is required");
                    return;
                }
                User user = getIntent().getParcelableExtra("user");
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUsername(userName);
                user.setCurrentDiary(new Diary(Diary.atStartOfDay(Timestamp.now().toDate()), Timestamp.now().toDate(), Diary.atEndOfDay(Timestamp.now().toDate())));

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
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(fAuth.getCurrentUser().getUid()).setValue(user);
                        FirebaseDatabase.getInstance().getReference().child("MealPlan").child(fAuth.getCurrentUser().getUid()).setValue(getMeals());
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public List<MealPlan> getMeals() {
        List<MealPlan> meals = new ArrayList<>();

        List<String> descriptionDays1=new ArrayList<>(Arrays.asList(
                "A good breakfast","You will eat very proteinful","A lot of food today","Let's see if you can eat all of this"
        ));
        List<String> descriptionsDays2=new ArrayList<>(Arrays.asList(
                "Let's eat some snacks","Egg Day","More, more, and much more water"));
        List<String> descriptionsDays3= new ArrayList<>(Arrays.asList(
                "Protein, protein and again protein","A good day for a shake with proteins and Greek yougrt","A Big Food challange"
        ));
        MealPlan meal1 = new MealPlan("Flat-Belly Foods", "These meals and snacks focus on protein and fiber to trim your midsection and lose weight in this easy high-protein diet plan.",
                "High Protein", 4, "Daily", 0, "Easy", PutTaskOnMeals(), descriptionDays1, "https://firebasestorage.googleapis.com/v0/b/qualeatsoft-29fa7.appspot.com/o/1.jpg?alt=media&token=b1cda06e-3623-4c55-86c1-0c4bf53756f1", false, false);
        MealPlan meal2 = new MealPlan("Steak diet", "These meals and snacks focus on protein and fiber to trim your midsection and lose weight in this easy high-protein diet plan.", "High Protein", 3, "Daily",
                0, "Easy", PutTaskOnMeals(), descriptionDays1, "https://firebasestorage.googleapis.com/v0/b/qualeatsoft-29fa7.appspot.com/o/6.jpg?alt=media&token=d1e4de94-e021-4c80-8d26-53b0a453067d", false, false);
        MealPlan meal3=new MealPlan("Low-Carb Eating — The Basics","Your food choices depend on a few things, including how healthy you are, how much you exercise and how much weight you have to lose.",
                "Low Carbs",3,"Daily",0,"Easy",PutTaskOnMeals(),descriptionsDays2,"https://firebasestorage.googleapis.com/v0/b/qualeatsoft-29fa7.appspot.com/o/5.jpg?alt=media&token=bd762cd3-717a-4b7b-85de-56e2cfdd0ba2",false,false);
        MealPlan meal4 =new MealPlan("Bodybuilder diet","These meals and snacks focus on protein and fiber to trim your midsection and to build your dream muscle body",
                "Growth Muscle",3,"Daily",0,"Easy", PutTaskOnMeals(),descriptionsPerDay,"https://firebasestorage.googleapis.com/v0/b/qualeatsoft-29fa7.appspot.com/o/1.jpg?alt=media&token=b1cda06e-3623-4c55-86c1-0c4bf53756f1",false,false);
        MealPlan meal5=new MealPlan("Without Skin Body","Your body is ready to build a new man, skinny body is gone. ",
                "Growth Muscle",3,"Daily",0,"Easy",PutTaskOnMeals(),descriptionsDays3,"https://firebasestorage.googleapis.com/v0/b/qualeatsoft-29fa7.appspot.com/o/3.jpg?alt=media&token=6116bead-19c2-4ad4-b399-0bc01c2e178a",false,false);
        MealPlan meal6=new MealPlan("Planty Healthy","A diet destinated to vegetarians and vegans. Let's eat some salad but no from Salad Box",
                "Plant Based",3,"Daily",0,"Easy",PutTaskOnMeals(),descriptionsDays3,"https://firebasestorage.googleapis.com/v0/b/qualeatsoft-29fa7.appspot.com/o/4.jpg?alt=media&token=1dda6b49-3515-4cbe-9022-56d38e5756de",false,false);
        
        meals.add(meal1);
        meals.add(meal2);
        meals.add(meal3);
        meals.add(meal4);
        meals.add(meal5);
        meals.add(meal6);

        return meals;
    }

    public List<Task> PutTaskOnMeals()
    {
            List<Task> allTasks = new ArrayList(Arrays.asList(
                    new Task("2 servings Berry-Mint Kefir Smoothies", false,"High Protein"),
                    new Task("Add 8 walnuts to A.M. snack and 2 Tbsp. almond butter to P.M. snack.", false,"High Protein"),
                    new Task("Add 1 cup cooked oatmeal prepared with water to breakfast, add 1/2 cup walnuts to A.M. snack, and add 3 Tbsp. almond butter to P.M. snack.", false,"High Protein"),
                    new Task("Eat a protein baton as snack", false,"High Protein"),
                    new Task("Replace cereal with eggs ", false,"High Protein"),
                    new Task("Top your food with chopped almonds", false,"High Protein"),
                    new Task("Choose as snack Greek yogurt", false,"High Protein"),
                    new Task("Have a protein shake for breakfast", false,"High Protein"),
                    new Task("Include a minimum 30 g protein food with every meal", false,"High Protein"),
                    new Task("Add peanut butter to your breakfast", false,"High Protein"),
                    new Task("Eat lean jerky", false,"High Protein"),
                    new Task("Eat canned fish to dinner", false,"High Protein"),
                    new Task("Enjoy more whole grains", false,"High Protein"),
                    new Task("Include whole grains for breakfast.", false,"Plant Based"),
                    new Task("Build a meal around a salad.", false,"Plant Based"),
                    new Task("Cook a vegetarian meal at least one night a week.", false,"Plant Based"),
                    new Task("Eat at least 5 portions of a variety of fruit and vegetables", false,"Plant Based"),
                    new Task("Eat fruit for dessert.", false,"Plant Based"),
                    new Task("Base meals on potatoes, bread, rice, pasta or other starchy carbohydrates", false,"Plant Based"),
                    new Task("Eat some beans, pulses and other proteins", false,"Plant Based"),
                    new Task("Choose unsaturated oils and spreads, and eat in small amounts", false,"Plant Based"),
                    new Task("Drink plenty of fluids (the government recommends 6 to 8 cups or glasses a day)", false,"Plant Based"),
                    new Task("Enjoy more whole grains", false,"Plant Based"),
                    new Task("Curb your intake of sugar-sweetened drinks", false,"Low Carbs"),
                    new Task("Cut back on refined grain bread", false,"Low Carbs"),
                    new Task("Think about fruit juice", false,"Low Carbs"),
                    new Task("Choose lower-carb snacks", false,"Low Carbs"),
                    new Task("Start your day with eggs or other lower-carb breakfast foods", false,"Low Carbs"),
                    new Task("Substitute alternative flours for white flour", false,"Low Carbs"),
                    new Task("Supplement with healthier fats", false,"Low Carbs"),
                    new Task("Count carbs with Qualeatsoft", false,"Low Carbs"),
                    new Task("Greek yogurt is a good snack anytime", false,"Low Carbs"),
                    new Task("Eggs contain high quality protein,eat 2 eggs per day", false,"Growth Muscle"),
                    new Task("Salmon is a great choice for muscle building and overall health.", false,"Growth Muscle"),
                    new Task("Chicken breasts also contain generous amounts of the B vitamins niacin and B6", false,"Growth Muscle"),
                    new Task("Greek yogurt is a good snack anytime", false,"Growth Muscle"),
                    new Task("Eat lots of fruit and veg", false,"Growth Muscle"),
                    new Task("Like shrimp, tilapia, and lean poultry, scallops provide protein with very little fat", false,"Growth Muscle"),
                    new Task("Tuna provides large amounts of omega-3 fatty acids", false,"Growth Muscle"),
                    new Task("Do not skip breakfast", false,"Growth Muscle"),
                    new Task("Choose nonfat or high-fat milk, yogurt and cottage cheese.", false,"Growth Muscle"),
                    new Task("Prepare all foods with few addition of butter and other fats.", false,"Growth Muscle"),
                    new Task("Eggs contain high quality protein,eat 2 eggs per day", false,"Growth Muscle"),
                    new Task("Salmon is a great choice for muscle building and overall health.", false,"Growth Muscle"),
                    new Task("Chicken breasts also contain generous amounts of the B vitamins niacin and B6", false,"Growth Muscle"),
                    new Task("Greek yogurt is a good snack anytime", false,"Growth Muscle"),
                    new Task("Eat lots of fruit and veg", false,"Growth Muscle"),
                    new Task("Like shrimp, tilapia, and lean poultry, scallops provide protein with very little fat", false,"Growth Muscle"),
                    new Task("Tuna provides large amounts of omega-3 fatty acids", false,"Growth Muscle"),
                    new Task("Do not skip breakfast", false,"Growth Muscle"),
                    new Task("Choose nonfat or high-fat milk, yogurt and cottage cheese.", false,"Growth Muscle"),
                    new Task("Prepare all foods with few addition of butter and other fats.", false,"Growth Muscle")

            ));
            return allTasks;
    }
}

