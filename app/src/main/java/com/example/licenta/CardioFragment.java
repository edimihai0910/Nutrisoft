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
import com.example.licenta.data.FoodReportModel;
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
 * Use the {@link CardioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardioFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static CardioFragment newInstance(String param1, String param2) {
        CardioFragment fragment = new CardioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static ArrayList<String> getCardio()
    {
        ArrayList<String> cardio = new ArrayList<>();
        cardio.add("Aerobic general");
        cardio.add("Aerobics high impact");
        cardio.add("Aerobics low impact");
        cardio.add("Aerobics step");
        cardio.add("Aerobics step");
        cardio.add("Apple Health App Workout");
        cardio.add("Archery non-hunting");
        cardio.add("Automobile repair");
        cardio.add("Backpacking general");
        cardio.add("Badminton competitive");
        cardio.add("Badminton general");
        cardio.add("Baseball");
        cardio.add("Basketbull game");
        cardio.add("Basketball nongame");
        cardio.add("Basketball officiating");
        cardio.add("Basketball shooting baskets");
        cardio.add("Basketball wheelchair");
        cardio.add("Belly dancing");
        cardio.add("Bicycling light effort ");
        cardio.add("Bicycling moderate effort ");
        cardio.add("Bicycling vigorous effort ");
        cardio.add("Bicycling very fast effort ");
        cardio.add("Bicycling leisure effort ");
        cardio.add("Bicycling racing");
        cardio.add("Bicycling BMX or mountain");
        cardio.add("Biliards");
        cardio.add("Bowling");
        cardio.add("Boxing in ring");
        cardio.add("Boxing punching bag");
        cardio.add("Boxing spparing");
        cardio.add("Brazilian Jiu Jitsu");
        cardio.add("Broomball");
        cardio.add("Calithenics vigorous effort ");
        cardio.add("Calithenics light effort ");
        cardio.add("Canoeing on camping trip");
        cardio.add("Canoeing vigorous effort ");
        cardio.add("Canoeing crewing");
        cardio.add("Canoeing light effort ");
        cardio.add("Canoeing moderate effort ");
        cardio.add("Carpentry");
        cardio.add("Chopping wood");
        cardio.add("Circuit training");
        cardio.add("Cleaning vigorous effort ");
        cardio.add(" Cleaning moderate effort");
        cardio.add("Cooking or food preparation");
        cardio.add("Crewing");
        cardio.add("Cricket");
        cardio.add("Croquet");
        cardio.add("Curling");
        cardio.add("Curves Circuit Training");
        cardio.add("Dancing");
        cardio.add("Ballet");
        cardio.add("Darts");
        cardio.add("Diving");
        cardio.add("Fencing");
        cardio.add("Fishing");
        cardio.add("Football general");
        cardio.add("Football competitive");
        cardio.add("Frisbee ultimate");
        cardio.add("Frisbee general");
        cardio.add("Gardening");
        cardio.add("Golf");
        cardio.add("Gymnastics");
        cardio.add("Hacky sack");
        cardio.add("Handball general");
        cardio.add("Handball team");
        cardio.add("Hiking");
        cardio.add("Hochey");
        cardio.add("Horse grooming");
        cardio.add("Horseback riding");
        cardio.add("Hunting");
        cardio.add("Jai alai");
        cardio.add("Jet skiing");
        cardio.add("Judo");
        cardio.add("Karate");
        cardio.add("Kickboxing");
        cardio.add("Tae kwan do");
        cardio.add("Jumping jacks");
        cardio.add("Kayaking");
        cardio.add("Kickball");
        cardio.add("Lacrosse");
        cardio.add("Les Mills BODYATTACK");
        cardio.add("Les Mills BODYBALANCE");
        cardio.add("Les Mills BODYCOMBAT");
        cardio.add("Les Mills BODYSTEP");
        cardio.add("Les Mills GRIT");
        cardio.add("Les Mills RPM");
        cardio.add("Line dancing");
        cardio.add("Marching band");
        cardio.add("Marching rapidly");
        cardio.add("Martial arts");
        cardio.add("Mild stretching");
        cardio.add("Motor-cross");
        cardio.add("Moving furniture");
        cardio.add("Moving household items");
        cardio.add("Mowimg lawn");
        cardio.add("Music palying");
        cardio.add("Paddleboat");
        cardio.add("Pilates");
        cardio.add("Polo");
        cardio.add("Pull-ups vigorous");
        cardio.add("Punching bag");
        cardio.add("Push-ups vigorous");
        cardio.add("Race walking");
        cardio.add("Racquetball general");
        cardio.add("Racquetball competitive");
        cardio.add("Raking lawn");
        cardio.add("Rappelling");
        cardio.add("Rock climbing");
        cardio.add("Roller blading");
        cardio.add("Rope jumping fast");
        cardio.add("Rope jumping moderate");
        cardio.add("Rope jumping slow");
        cardio.add("Rowing stationary light effort");
        cardio.add("Rowing stationary moderate effort");
        cardio.add("Rowing stationary very vigorous effort");
        cardio.add("Rowing stationary vigorous effort");
        cardio.add("Rugby");
        cardio.add("Running 1 km");
        cardio.add("Running 2 km");
        cardio.add("Running 3 km ");
        cardio.add("Running 4 km ");
        cardio.add("Running 5 km ");
        cardio.add("Running 6 km ");
        cardio.add("Running 7 km ");
        cardio.add("Running 8 km ");
        cardio.add("Running 9 km ");
        cardio.add("Running 10 km");
        cardio.add("Sailing");
        cardio.add("Shoveling snow");
        cardio.add("Shufflleboard");
        cardio.add("Skateboarding");
        cardio.add("Skating");
        cardio.add("Ski jumping");
        cardio.add("Ski machine");
        cardio.add("Skiing downhill light effort");
        cardio.add("Skiing downhill moderate effort");
        cardio.add("Skiing downhill vigorous effort");
        cardio.add("Scuba diving");
        cardio.add("Sledding");
        cardio.add("Slimnastics");
        cardio.add("Snorkeling");
        cardio.add("Snow shoeing");
        cardio.add("Snowboarding");
        cardio.add("Soccer");
        cardio.add("Softball");
        cardio.add("Spinning");
        cardio.add("Squash");
        cardio.add("Stair-treadmill ergometer");
        cardio.add("Stationary bike light effort");
        cardio.add("Stationary bike moderate effort");
        cardio.add("Stationary bike very light effort");
        cardio.add("Stationary bike very vigorous effort");
        cardio.add("Stationary bike vigorous effort ");
        cardio.add("Strength training");
        cardio.add("Stretching");
        cardio.add("Surfing");
        cardio.add("Swimming light effort");
        cardio.add("Swimming moderate effort");
        cardio.add("Swimming vigorous effort");
        cardio.add("Swimming butterfly");
        cardio.add("Swimming leisurely");
        cardio.add("Swimming sidestroke");
        cardio.add("Tae Bo");
        cardio.add("Tai Chi");
        cardio.add("Tennis");
        cardio.add("Unicycling");
        cardio.add("Volleyball");
        cardio.add("Walking 1 km");
        cardio.add("Walking 2 km");
        cardio.add("Walking 3 km");
        cardio.add("Walking 4 km");
        cardio.add("Walking 5 km");
        cardio.add("Walking 6 km");
        cardio.add("Walking 7 km");
        cardio.add("Walking 8 km");
        cardio.add("Walking 9 km");
        cardio.add("Walking 10 km");
        cardio.add("Walking upstairs");
        cardio.add("Wallyball");
        cardio.add("Water jogging");
        cardio.add("Water polo");
        cardio.add("Water volleyball");
        cardio.add("Wii baseball");
        cardio.add("Wii bowling");
        cardio.add("Wii boxing");
        cardio.add("Wii Fit Free Run");
        cardio.add("Wii golf");
        cardio.add("Wrestling");
        cardio.add("Yoga");
        return cardio;
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
    EditText et_minPerformed;
    Button cancel,logExercise;
    public final static int REQUEST_CODE_E = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cardio, container, false);

        ListView lvCardio = v.findViewById(R.id.lv_ListCardio);

        ArrayList<String> array = CardioFragment.getCardio();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array);
        lvCardio.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvCardio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addNewExercise(array.get(position));
            }
        });



        SearchView sv_Cardio = v.findViewById(R.id.sv_Cardio);
        sv_Cardio.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        final View exercisePopupView=getLayoutInflater().inflate(R.layout.popupexercise,null);

        et_minPerformed=exercisePopupView.findViewById(R.id.et_MinPerformed);
        logExercise=exercisePopupView.findViewById(R.id.bt_addExercise);
        dialogBuilder.setView(exercisePopupView);
        dialog=dialogBuilder.create();
        dialog.show();
        et_minPerformed.setInputType(InputType.TYPE_CLASS_NUMBER );

        logExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validate input
                if(validate()){
                if(et_minPerformed.getText().length()>0) {
                    String sMins = String.valueOf(et_minPerformed.getText());
                    int mins = Integer.parseInt(sMins);
                    try {
                        AddExerciseInDiary(name, mins);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                }
            }
        });


    }
    private void AddExerciseInDiary(String name,int totalMins) throws InterruptedException {
        String url = "https://trackapi.nutritionix.com/v2/natural/exercise";
        Exercice exercice=new Exercice();
        exercice.setReps(0);
        exercice.setName(name);
        exercice.setDuration(totalMins);
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
                    // on below line we are setting this string s to our text view.
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
                params.put("query", totalMins+" min "+name);

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
        if(et_minPerformed.getText().toString().isEmpty())
        {
            et_minPerformed.setError("You need to set a value");
            return false;
        }
        if (Integer.parseInt(String.valueOf(et_minPerformed.getText().toString())) < 0 || Integer.parseInt(et_minPerformed.getText().toString()) > 1000) {
            et_minPerformed.setError("Please enter a valid value");
            return false;
        }
        return true;
    }
}