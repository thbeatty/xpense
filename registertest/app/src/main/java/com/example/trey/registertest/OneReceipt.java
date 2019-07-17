package com.example.trey.registertest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.trey.registertest.LoginActivity.globalPreferenceName;

public class OneReceipt extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView date1, storeName1, price1, categoryName1, notes1;
    String receiptidnum;
    private static String url = "http://cgi.soic.indiana.edu/~team33/onereceiptdisplay.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_receipt);

        date1 = findViewById(R.id.receiptDate);
        storeName1 = findViewById(R.id.storeName);
        price1 = findViewById(R.id.price);
        categoryName1 = findViewById(R.id.categoryName);
        notes1 = findViewById(R.id.notes);

        bottomNavigationView = findViewById(R.id.navBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case (R.id.homeNav):
                        Intent intent0 = new Intent(OneReceipt.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case (R.id.homeBudget):
                        Intent intent1 = new Intent(OneReceipt.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(OneReceipt.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(OneReceipt.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
        String receiptid = sharedPreferences.getString("receiptID", "No Value");
        receiptidnum = receiptid;

        ReceiptRetriever();

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
                                String notes = object.optString("notes").trim();

                                String initialMonthFinal = "";
                                String initialDayFinal = "";
                                String s = date;
                                String[] parts = s.split("-");
                                String initialMonth = parts[1];
                                String initialDay = parts[2];

                                if (initialMonth.contains("0")){
                                    String[] monthParts = initialMonth.split("0");
                                    initialMonthFinal = monthParts[1];
                                } else {
                                    initialMonthFinal = initialMonth;
                                }

                                if (initialDay.contains("0")){
                                    String[] dayParts = initialDay.split("0");
                                    initialDayFinal = dayParts[1];
                                } else {
                                    initialDayFinal = initialDay;
                                }

                                String correctDisplay = initialMonthFinal + "/" + initialDayFinal + "/" + parts[0];


                                date1.setText(correctDisplay);
                                storeName1.setText(storeName);
                                categoryName1.setText(categoryName);
                                price1.setText(price);
                                notes1.setText(notes);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OneReceipt.this, "Failed to add data to database", Toast.LENGTH_LONG).show();
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
                params.put("receiptID", receiptidnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
