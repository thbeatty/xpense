package com.example.trey.registertest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static com.example.trey.registertest.LoginActivity.globalPreferenceName;

public class PersonalCategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    String[] title;
    String[] description;

    BottomNavigationView bottomNavigationView;
    String useridnum;
    private ListView mListView;
    ProgressBar personalProgressBar;
    private SearchView searchView;
    public static ArrayList<Receipt> receiptArrayList = new ArrayList<Receipt>();
    private ReceiptAdapter receiptAdapter;
    private ArrayList<String> receipts;
    private JSONArray result;
    private final static String TAG = "PersonalDisplay";
    private static String url = "http://cgi.soic.indiana.edu/~team33/personaldisplay.php";
    private static String url1 = "http://cgi.soic.indiana.edu/~team33/homepagesearch.php";
    private static String personalPHP = "http://cgi.soic.indiana.edu/~team33/personalCurrentValue.php";
    private static String personalPHP2 = "http://cgi.soic.indiana.edu/~team33/selectPersonalTotal.php";

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_category);
        searchView = findViewById(R.id.homeSearch);
        receipts = new ArrayList<String>();
        Log.d(TAG, "onCreate: Started");
        mListView = findViewById(R.id.listViewPersonal);
        receiptArrayList = new ArrayList<>();

        personalProgressBar = findViewById(R.id.progressBarPersonal);

        bottomNavigationView = findViewById(R.id.navBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case (R.id.homeNav):
                        Intent intent = new Intent(PersonalCategoryActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;

                    case (R.id.homeBudget):
                        Intent intent1 = new Intent(PersonalCategoryActivity.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(PersonalCategoryActivity.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(PersonalCategoryActivity.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "No Value");
        useridnum = userid;

        SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
        String personalCategory = sharedPreferences1.getString("personalCategory", "No Value");
        category = personalCategory;


        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Search Here");
        searchView.setFocusable(false);

        //receiptAdapter.filter("");
        //mListView.clearTextFilter();
        ReceiptRetriever();

        ReceiptRetrieverPersonal();
        ValueRetrieverPersonal();

    }

    private void ReceiptRetriever() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String date = object.optString("date").trim();
                                String storeName = object.optString("storeName").trim();
                                String categoryName = object.optString("categoryName").trim();
                                String price = object.optString("price").trim();
                                String receiptID = object.optString("receiptID").trim();

                                Receipt a = new Receipt(storeName, "$"+price,categoryName, date, receiptID);
                                receiptArrayList.add(a);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PersonalCategoryActivity.this, "No receipts under this category", Toast.LENGTH_LONG).show();
                        }
                        receiptAdapter = new ReceiptAdapter(PersonalCategoryActivity.this, receiptArrayList);
                        mListView.setAdapter(receiptAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("aa","error: " + error.networkResponse.allHeaders);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", useridnum);
                params.put("categoryName", category);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ReceiptSearch(final String s) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String date = object.optString("date").trim();
                                String storeName = object.optString("storeName").trim();
                                String categoryName = object.optString("categoryName").trim();
                                String price = object.optString("price").trim();
                                String receiptID = object.optString("receiptID").trim();

                                Receipt a = new Receipt(storeName, price, date, categoryName, receiptID);
                                receiptArrayList.add(a);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("aa","error: "+error.networkResponse.allHeaders);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("storeName", s);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        ReceiptSearch(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)){
            receiptAdapter.filter("");
            mListView.clearTextFilter();
        }
        else {
            receiptAdapter.filter(s);
        }
        return true;
    }

    private void ReceiptRetrieverPersonal() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, personalPHP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int currentPersonalTotal = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
//                                String date = object.optString("date").trim();
//                                String storeName = object.optString("storeName").trim();
//                                String categoryName = object.optString("categoryName").trim();
                                String price = object.optString("price").trim();
//                                String receiptID = object.optString("receiptID").trim();


//                                Receipt a = new Receipt(storeName, "$"+price,categoryName, date, receiptID);
//                                receiptArrayList.add(a);

                                Float receiptPrice2 = Float.valueOf(price);
                                int receiptPrice = Math.round(receiptPrice2);
                                currentPersonalTotal += receiptPrice;

                            }
                            Log.e("Personal Total", String.valueOf(currentPersonalTotal));
                            SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                            editor.putInt("currentPersonalTotal", currentPersonalTotal);
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("aa","error: " + error.networkResponse.allHeaders);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", useridnum);
//                params.put("categoryName", category);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ValueRetrieverPersonal() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, personalPHP2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            float personalTotalValue = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String valueTotal = object.optString("valueTotal").trim();


                                Float monthlyTotal = Float.valueOf(valueTotal);
                                personalTotalValue = monthlyTotal;


                            }
                            Log.e("Personal Value", String.valueOf(personalTotalValue));
                            SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
                            int currentPersonalTotal = sharedPreferences1.getInt("currentPersonalTotal", 0);
                            float progress1 = (currentPersonalTotal / personalTotalValue) * 100;
                            int progress = Math.round(progress1);
                            personalProgressBar.setProgress(progress);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("aa","error: " + error.networkResponse.allHeaders);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", useridnum);
//                params.put("categoryName", category);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}