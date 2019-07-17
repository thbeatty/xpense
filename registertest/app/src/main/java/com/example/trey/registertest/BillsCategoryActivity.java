package com.example.trey.registertest;

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

public class BillsCategoryActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    String[] title;
    String[] description;

    BottomNavigationView bottomNavigationView;
    String useridnum;
    ProgressBar billsProgressBar;
    private ListView mListView;
    private SearchView searchView;
    public static ArrayList<Receipt> receiptArrayList = new ArrayList<Receipt>();
    private ReceiptAdapter receiptAdapter;
    private ArrayList<String> receipts;
    private JSONArray result;
    private final static String TAG = "BillsDisplay";
    private static String url = "http://cgi.soic.indiana.edu/~team33/billsdisplay.php";
    private static String url1 = "http://cgi.soic.indiana.edu/~team33/homepagesearch.php";
    private static String billsPHP = "http://cgi.soic.indiana.edu/~team33/billsCurrentValue.php";
    private static String billsPHP2 = "http://cgi.soic.indiana.edu/~team33/selectBillsTotal.php";

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_category);
        searchView = findViewById(R.id.homeSearch);
        receipts = new ArrayList<String>();
        Log.d(TAG, "onCreate: Started");
        mListView = findViewById(R.id.listViewBills);
        receiptArrayList = new ArrayList<>();
        billsProgressBar = findViewById(R.id.progressBarBills);

        bottomNavigationView = findViewById(R.id.navBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case (R.id.homeNav):
                        Intent intent = new Intent(BillsCategoryActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;

                    case (R.id.homeBudget):
                        Intent intent1 = new Intent(BillsCategoryActivity.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(BillsCategoryActivity.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(BillsCategoryActivity.this, SettingsActivity.class);
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
        String billsCategory = sharedPreferences1.getString("billsCategory", "No Value");
        category = billsCategory;


        ReceiptRetrieverBills();

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Search Here");
        searchView.setFocusable(false);

        ValueRetrieverBills();
        ReceiptRetriever();


    }

    private void ReceiptRetriever() {
        Log.e("AA","ReceiptRetriever started");
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
                            Toast.makeText(BillsCategoryActivity.this, "No receipts under this category", Toast.LENGTH_LONG).show();
                        }
                        receiptAdapter = new ReceiptAdapter(BillsCategoryActivity.this, receiptArrayList);
                        mListView.setAdapter(receiptAdapter);

                        Log.e("AA","ReceiptRetriever done");

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
        Log.e("AA","Added to the queue");

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

    private void ReceiptRetrieverBills() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, billsPHP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int currentBillsTotal = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String price = object.optString("price").trim();

                                Float receiptPrice2 = Float.valueOf(price);
                                int receiptPrice = Math.round(receiptPrice2);
                                currentBillsTotal += receiptPrice;

                            }
                            Log.e("Bills Total", String.valueOf(currentBillsTotal));
                            SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                            editor.putInt("currentBillsTotal", currentBillsTotal);
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

    private void ValueRetrieverBills() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, billsPHP2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            float billsTotalValue = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String valueTotal = object.optString("valueTotal").trim();

                                Float monthlyTotal = Float.valueOf(valueTotal);
                                billsTotalValue = monthlyTotal;


                            }
                            Log.e("Bills Total Value", String.valueOf(billsTotalValue));

                            SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
                            int currentBillsTotal = sharedPreferences1.getInt("currentBillsTotal", 0);
                            Log.e("Current Bills Total", String.valueOf(currentBillsTotal));
                            float progress1 = (currentBillsTotal / billsTotalValue) * 100;
                            int progress = Math.round(progress1);
                            billsProgressBar.setProgress(progress);


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