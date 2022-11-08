package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.licenta.data.Exercice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StrengthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StrengthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StrengthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StrengthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StrengthFragment newInstance(String param1, String param2) {
        StrengthFragment fragment = new StrengthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static ArrayList<String> getStrength()
    {
        ArrayList<String> str= new ArrayList<>();
        str.add("Abdominal Crunches");
        str.add("Abdominall Leg Raise");
        str.add("Abdominal Twist");
        str.add("Back Butterfly");
        str.add("Back Extesion");
        str.add("Bar Dip");
        str.add("Barbell Military Press");
        str.add("Barbell Row");
        str.add("Bench Press");
        str.add("Bent Arm Barbell Pullover");
        str.add("Bent-Arm Lateral");
        str.add("Bent-Knee Sit-up");
        str.add("Bent-Leg Kickbacks");
        str.add("Biceps Curl");
        str.add("Cable Crossover");
        str.add("Calf Raises");
        str.add("Chin-Ups");
        str.add("Deadlift");
        str.add("DeclineBench Press");
        str.add("Decline Dumbbell Fly");
        str.add("Dips");
        str.add("Dumbbell Press");
        str.add("Dumbbell Row");
        str.add("Flat Dumbbell Fly");
        str.add("Flat Dumbbell Press");
        str.add("Front Barbel Raise");
        str.add("Front Chin Up");
        str.add("Front Raises");
        str.add("Front Squats");
        str.add("Hack Squat");
        str.add("Heel Raise");
        str.add("Hip Abduction");
        str.add("Hip Flexor");
        str.add("Hyperextensions");
        str.add("Incline Bench Press");
        str.add("Incline Dumbbell Curl");
        str.add("Incline Dumbbell Fly");
        str.add("Incline Latera Dumbbells");
        str.add("Kickbacks Bent 1-Arm");
        str.add("Kickbacks Bent 2-Arm");
        str.add("Lat Pulldown");
        str.add("Lateral Arm Raise");
        str.add("Leg Curls");
        str.add("Leg Extension");
        str.add("Leg Press");
        str.add("Leg Pull-in");
        str.add("Leg Raises");
        str.add("Lunge");
        str.add("Machine Fly");
        str.add("Machine Squat");
        str.add("Mid Row Chest Supported");
        str.add("Overhead Press");
        str.add("Pelvic Lifts");
        str.add("Pull Ups");
        str.add("Pullover");
        str.add("Rear Deltoid Raise");
        str.add("Reverse Sit-Up");
        str.add("Reverse Trunk Twist");
        str.add("Seated Biceps Curl");
        str.add("Seated Calf Raise");
        str.add("Seated Row");
        str.add("Seated Low Lat Pull-In");
        str.add("Shoulder Press");
        str.add("Shoulder Shrug");
        str.add("Side Bends");
        str.add("Side Leg Raises");
        str.add("Standing Bar Curl");
        str.add("Standing Biceps Curl");
        str.add("Standing Calf Raises");
        str.add("Standing Medium-Grip Barbell Curl");
        str.add("Standing One-Arm Curl");
        str.add("Standin One-Arm Dumbbell Curl");
        str.add("Straight Arm Bumbbell Pullover");
        str.add("Triceps Extesion");
        str.add("Triceps Pull-down");
        str.add("Triceps Push Down");
        str.add("Upright Row");
        str.add("V-Bar Chin Up");
        str.add("Vertical Leg Lift");
        str.add("Wrist Curl");
        str.add("Wrist Roller");

        return str;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    EditText et_reps,et_sets;
    Button cancel,logExercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_strength, container, false);

        ListView lvStrength = v.findViewById(R.id.lv_ListStrength);

        ArrayList<String> array = StrengthFragment.getStrength();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array);
        lvStrength.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvStrength.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nameEx=array.get(position);
                addNewExercise(nameEx);
            }
        });



        SearchView sv_Strength = v.findViewById(R.id.sv_Strength);
        sv_Strength.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }

    private void addNewExercise(String name)
    {
        dialogBuilder=new AlertDialog.Builder(getActivity());
        final View exercisePopupView=getLayoutInflater().inflate(R.layout.popupexercisestrength,null);

        et_sets=exercisePopupView.findViewById(R.id.et_sets);
        et_reps=exercisePopupView.findViewById(R.id.et_reps);
        logExercise=exercisePopupView.findViewById(R.id.bt_addExercise);
        dialogBuilder.setView(exercisePopupView);
        dialog=dialogBuilder.create();
        dialog.show();
        et_reps.setInputType(InputType.TYPE_CLASS_NUMBER );
        et_sets.setInputType(InputType.TYPE_CLASS_NUMBER );

        logExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validate input
                if(validate()) {
                    int totalReps = Integer.parseInt(String.valueOf(et_reps.getText())) * Integer.parseInt(String.valueOf(et_sets.getText()));
                    AddExerciseInDiary(name, totalReps);
                }
            }
        });


    }

    private void AddExerciseInDiary(String name,int totalReps)
    {
        String url = "https://trackapi.nutritionix.com/v2/natural/exercise";
        Exercice exercice=new Exercice();
        exercice.duration=0;
        exercice.setName(name);
        exercice.setReps(totalReps);
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.i("tag", response);
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray ja_data = jsonObj.getJSONArray("exercises");
                    int length = jsonObj.length();
                    double kcals=0;
                    for (int i = 0; i < length; i++) {
                        JSONObject json = ja_data.getJSONObject(i);
                        kcals+= json.getDouble("nf_calories");

                    }
                    exercice.setKcalBurned(kcals);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("currentDiary").child("diary").child("Training");
                    List<Exercice> alreadyInFirebase=new ArrayList<>();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Exercice data = snap.getValue(Exercice.class);
                                alreadyInFirebase.add(data);
                            }
                            alreadyInFirebase.add(exercice);
                            ref.setValue(alreadyInFirebase);
                            Intent resultIntent=new Intent(getActivity(),DiaryActivity.class);
                            startActivity(resultIntent);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    } );
                    // on below line we are setting this string s to our text view.ne we are setting this string s to our text view.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                String er = error.getCause().getMessage();
                Log.i("ex", er);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-app-id", "9318b288");
                params.put("x-app-key", "e8c78b54e0a155e422e7a12c5cd2537a");

                // at last we are
                // returning our params.
                return params;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //   params.put("content", "application/json");
                //  params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("query", totalReps+" "+name);

                return params;

            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        // below line is to make
        // a json object request.
        Log.d("string", request.toString());
        queue.add(request);
    }

    public boolean validate() {
        if(et_reps.getText().toString().isEmpty())
        {
            et_reps.setError("You need to set a value");
            return false;
        }
        if(et_sets.getText().toString().isEmpty())
        {
            et_sets.setError("You need to set a value");
            return false;
        }
        if (Integer.parseInt(String.valueOf(et_reps.getText().toString())) < 0 || Integer.parseInt(et_reps.getText().toString()) > 5000) {
            et_reps.setError("Please enter a valid value");
            return false;
        }
        else if (Integer.parseInt(String.valueOf(et_sets.getText().toString())) < 0 || Integer.parseInt(et_sets.getText().toString()) >15 )
        {
            et_sets.setError("Please enter a valid value");
            return false;
        }
        return true;
    }

}
