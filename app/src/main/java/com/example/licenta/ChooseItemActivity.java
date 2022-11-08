package com.example.licenta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.example.licenta.data.FoodReportModel;
import com.google.android.gms.ads.mediation.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseItemActivity extends AppCompatActivity {

   ArrayList<String> foodList=new ArrayList<>();
   ListView suggestionList;
   ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item);
        getSupportActionBar().hide();

        foodList=getIntent().getStringArrayListExtra("list");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, foodList);
        suggestionList.setAdapter(adapter);
        suggestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchFoodByImage(foodList.get(position));
            }
        });
    }
    private void SearchFoodByImage(String query) {
        // url to post our data
        String url = "https://trackapi.nutritionix.com/v2/natural/nutrients";
        FoodReportModel food = new FoodReportModel();

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.i("tag", response);
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray ja_data = jsonObj.getJSONArray("foods");
                    int length = jsonObj.length();
                    if(length>0) {
                        for (int i = 0; i < length; i++) {
                            JSONObject json = ja_data.getJSONObject(i);
                            if(json.has("food_name"))
                                food.setName(json.getString("food_name"));
                            if(json.has("brand_name"));
                                food.setManufacturer(json.getString("brand_name"));
                            if(json.has("serving_qty"))
                                food.setServing_qty(json.getInt("serving_qty"));
                            if(json.has("serving_unit"))
                                food.setServing_unit(json.getString("serving_unit"));
                            if(json.has("serving_weight_grams"))
                                food.setServing_weight_grams(json.getDouble("serving_weight_grams"));
                            if(json.has("nf_calories"))
                                food.setCalories((float) json.getDouble("nf_calories"));
                            if(json.has("nf_total_fat"))
                                food.setFat((float) json.getDouble("nf_total_fat"));
                            if(json.has("nf_total_carbohydate"))
                                food.setCarbs((float) json.getDouble("nf_total_carbohydrate"));
                            if(json.has("nf_protein"))
                                food.setProtein((float) json.getDouble("nf_protein"));
                            if(json.has("nf_dietary_fiber"))
                                food.setFiber(json.getDouble("nf_dietary_fiber"));
                            if(json.has("nf_sugars"))
                                food.setSugars(json.getDouble("nf_sugars"));

                            Intent newintent = new Intent(getApplicationContext(), LogFoodActivity.class);
                            newintent.putExtra("food", food);
                            startActivityForResult(newintent, 1);
                        }
                    }
                    // on below line we are setting this string s to our text view.
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getApplicationContext(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
                params.put("query", "100g "+query);

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

}