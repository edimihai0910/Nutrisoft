package com.example.licenta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.example.licenta.ml.LiteModelAiyVisionClassifierFoodV11;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFoodActivity extends AppCompatActivity {

    Button addFood;
    ImageView barcode, imageRecognition,searchButton;
    EditText searchBar;
    ListView listAddedFood;
    TextView total;
    ArrayList<FoodReportModel> listArray = new ArrayList<>();
    RequestQueue queue;
    StringRequest request;
    DecimalFormat df = new DecimalFormat("0.00");

    float totalKcalAdded=0;
    //bar code section
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    //image recognition section
    ImageView picture;
    int imageSize = 192;
    AddFoodActivity.CustomAdapter adapter;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    ListView suggestionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().hide();

        queue = Volley.newRequestQueue(this);
        barcode = findViewById(R.id.barcode_bt);
        imageRecognition = findViewById(R.id.imageRec_bt);

        addFood = findViewById(R.id.bt_addFood);
        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.searchButton);
        listAddedFood = findViewById(R.id.lv_addFood);
        adapter= new AddFoodActivity.CustomAdapter(getApplicationContext(),R.layout.row_item9,listArray);
        listAddedFood.setAdapter(adapter);
        total = findViewById(R.id.totalKcalAddedInAF);

        addFood.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
            if(listArray.size()>0) {
                Intent resultIntent = new Intent();
                Bundle args = new Bundle();
                args.putSerializable("result", listArray);
                resultIntent.putExtra("resultBundle", args);
                setResult(RESULT_OK, resultIntent);
//                request.cancel();
                queue.cancelAll("searchByImage");
                queue.cancelAll("searchByName");
                queue.cancelAll("searchByBarcode");
                finish();
            }
            else
            {
                addFood.setError("You need to add some food before to press on the button");
            }
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             try {
                 if (searchBar.getText().length() > 0) {
                     SearchFoodByName(searchBar.getText().toString());
                     Log.i("ATENTIE", String.valueOf(listAddedFood.getCount()));
                 }
                 else
                 {
                     searchBar.setError("You need to search something");
                 }
             }
             catch (Exception ex)
             {
                 ex.printStackTrace();
             }

            }
        });
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator= new IntentIntegrator(AddFoodActivity.this);
                intentIntegrator.setPrompt("For flash use volume up key");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.initiateScan();
            }

        });

        imageRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    //REST API ADD FOOD USING SEARCH BAR
    private FoodReportModel SearchFoodByName(String query) {
        // url to post our data
        String url = "https://trackapi.nutritionix.com/v2/natural/nutrients";
        FoodReportModel food = new FoodReportModel();

        // creating a new variable for our request queue

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.i("tag", response);
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray ja_data=null;
                    if(!jsonObj.isNull("foods")) {
                        ja_data = jsonObj.getJSONArray("foods");
                        int length = jsonObj.length();
                        if (length > 0) {
                            for (int i = 0; i < length; i++) {
                                JSONObject json = ja_data.getJSONObject(i);
                                if (json.has("food_name"))
                                    food.setName(json.getString("food_name"));
                                if (json.has("brand_name")) ;
                                food.setManufacturer(json.getString("brand_name"));
                                if (json.has("serving_qty"))
                                    food.setServing_qty(json.getInt("serving_qty"));
                                if (json.has("serving_unit"))
                                    food.setServing_unit(json.getString("serving_unit"));
                                if (json.has("serving_weight_grams"))
                                    food.setServing_weight_grams(json.getDouble("serving_weight_grams"));
                                if (json.has("nf_calories"))
                                    food.setCalories((float) json.getDouble("nf_calories"));
                                if (json.has("nf_total_fat"))
                                    food.setFat((float) json.getDouble("nf_total_fat"));
                                if (json.has("nf_total_carbohydrate"))
                                    food.setCarbs((float) json.getDouble("nf_total_carbohydrate"));
                                if (json.has("nf_protein"))
                                    food.setProtein((float) json.getDouble("nf_protein"));
                                if (json.has("nf_dietary_fiber"))
                                    food.setFiber(json.getDouble("nf_dietary_fiber"));
                                if (json.has("nf_sugars"))
                                    food.setSugars(json.getDouble("nf_sugars"));

                                Intent newintent = new Intent(getApplicationContext(), LogFoodActivity.class);
                                newintent.putExtra("food", food);
                                startActivityForResult(newintent, 1);
                            }
                        }
                    }
                        else
                            searchBar.setError("Nothing was found.");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getApplicationContext(), "Product not found", Toast.LENGTH_SHORT).show();
                return ;
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
        request.setTag("searchByName");
        queue.add(request);
        return food;
    }

    //METHOD GETDATA BY BARCODE
    private void getDataByBarcode(String barcode) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String name = "";
                String brand = "";
                int serving_qty = 1;
                String serving_unit = "g";
                Double weight_grams = 100.0;
                Double kcal = 0.0;
                Double carbs = 0.0;
                Double fat = 0.0;
                Double sugars=0.0;
                Double fibers=0.0;
                Double protein = 0.0;
                String imageUrl = "";
                try {
                    JSONObject productJson=response.getJSONObject("product");
                    JSONObject nutrimentsJson=productJson.getJSONObject("nutriments");
                    if(productJson.has("product_name"))
                    name = response.getJSONObject("product").getString("product_name");
                    if(nutrimentsJson.has("energy_kcal_100g"))
                        kcal = response.getJSONObject("product").getJSONObject("nutriments").getDouble("energy-kcal_100g");
                    else
                        kcal=response.getJSONObject("product").getJSONObject("nutriments").getDouble("energy-kcal");
                    if(nutrimentsJson.has("carbohydrates_100g"))
                        carbs = response.getJSONObject("product").getJSONObject("nutriments").getDouble("carbohydrates_100g");
                    else
                        carbs=response.getJSONObject("product").getJSONObject("nutriments").getDouble("carbohydrates");
                    if(nutrimentsJson.has("fat_100g"))
                        fat = response.getJSONObject("product").getJSONObject("nutriments").getDouble("fat_100g");
                    else
                        fat = response.getJSONObject("product").getJSONObject("nutriments").getDouble("fat");
                    if(nutrimentsJson.has("proteins_100g"))
                        protein = response.getJSONObject("product").getJSONObject("nutriments").getDouble("proteins_100g");
                    else
                        protein = response.getJSONObject("product").getJSONObject("nutriments").getDouble("proteins");
                    if(nutrimentsJson.has("sugars_100g"))
                        sugars= response.getJSONObject("product").getJSONObject("nutriments").getDouble("sugars_100g");
                    else
                        sugars= response.getJSONObject("product").getJSONObject("nutriments").getDouble("sugars");
                    if(nutrimentsJson.has("fiber_100g"))
                        fibers= response.getJSONObject("product").getJSONObject("nutriments").getDouble("fiber_100g");
                    else
                        fibers= response.getJSONObject("product").getJSONObject("nutriments").getDouble("fiber");

                    if (productJson.has("image_front_small_url,"))
                        imageUrl=productJson.getString("image_front_small_url");
                    FoodReportModel barcodeFood = new FoodReportModel(name, brand, serving_qty, serving_unit, weight_grams, kcal, fat, carbs, protein,sugars,fibers, imageUrl);

                    Intent newintent = new Intent(AddFoodActivity.this, LogFoodActivity.class);
                    newintent.putExtra("food", barcodeFood);

                    startActivityForResult(newintent, 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setTag("searchByBarcode");
        queue.add(request);
        // queue.cancelAll(request);
    }

    private void SearchFoodByImage(String query) {
        // url to post our data
        String url = "https://trackapi.nutritionix.com/v2/natural/nutrients";
        FoodReportModel food = new FoodReportModel();

         request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
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
        request.setTag("searchByImage");
        queue.add(request);

    }

    public void classifyImage(Bitmap image) {

        try {
            LiteModelAiyVisionClassifierFoodV11 model = LiteModelAiyVisionClassifierFoodV11.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorImage tfimage = TensorImage.fromBitmap(image);

            // Runs model inference and gets result.
            LiteModelAiyVisionClassifierFoodV11.Outputs outputs = model.process(tfimage);
            List<Category> probability = outputs.getProbabilityAsCategoryList();
            int maxPos=0;
            float maxConfidence=0;
            for(int i =0 ; i<probability.size();i++)
            {
                if(probability.get(i).getScore()>maxConfidence)
                {
                    maxConfidence=probability.get(i).getScore();
                    maxPos=i;
                }
            }

            ArrayList<String> topLabels=new ArrayList<>();
            for(int i = maxPos-5;i<maxPos;i++) topLabels.add(probability.get(i).getLabel());
            SearchFoodByImage("100g "+probability.get(maxPos));
            
            model.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(resultCode != RESULT_CANCELED){
            if (requestCode==1)
            {
                if(resultCode==RESULT_OK)
                {
                    FoodReportModel result=data.getParcelableExtra("result");
                    listArray.add(result);
                    adapter.notifyDataSetChanged();
                    totalKcalAdded+=result.calories;
                    total.setText(String.valueOf(df.format(totalKcalAdded))+" kcal");
                }
            }
            else if(requestCode==2)
            {
                Uri uri = data.getData();
                Bitmap image= null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int dimension = Math.min(image.getWidth(),image.getHeight());
                image= ThumbnailUtils.extractThumbnail(image,dimension,dimension);
                image=Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
                classifyImage(image);
            }
            else {
                {
                    if (intentResult.getContents() != null) {
                        getDataByBarcode(intentResult.getContents());
                    } else {
                        Toast.makeText(getApplicationContext(), "You did not scan anything", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
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
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag



            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            TextView txtName = convertView.findViewById(R.id.tv_lvFoodName);
            TextView txtType = convertView.findViewById(R.id.dateText);

            txtName.setText(dataModel.name);
            txtType.setText(String.valueOf(dataModel.calories));

            // Return the completed view to render on screen
            return convertView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onStop()
    {
        super.onStop();

    }
}
