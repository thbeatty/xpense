package com.example.trey.registertest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PreviewScreenActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    EditText storeName, price, notes;
    Button save, delete, date;
    String useridnum;
    Spinner mySpinner;
    String categoryName, databaseDate;
    Calendar c;
    DatePickerDialog datePickerDialog;
    String url = "http://cgi.soic.indiana.edu/~team33/preview_screen.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_screen);

        storeName = findViewById(R.id.storeNameEntry);
        price = findViewById(R.id.priceEntry);
        notes = findViewById(R.id.notesEntry);
        save = findViewById(R.id.submitButton);
        delete = findViewById(R.id.deleteButton);
        mySpinner = findViewById(R.id.categoriesDropdown);
        date = findViewById(R.id.receiptDate);

        bottomNavigationView = findViewById(R.id.navBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        Intent intent1 = getIntent();
        storeName.setText(intent1.getStringExtra("storeName"));
        price.setText(intent1.getStringExtra("price1"));
        date.setText(intent1.getStringExtra("date1"));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case (R.id.homeNav):
                        Intent intent0 = new Intent(PreviewScreenActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case (R.id.homeBudget):
                        Intent intent1 = new Intent(PreviewScreenActivity.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(PreviewScreenActivity.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(PreviewScreenActivity.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });

        databaseDate = intent1.getStringExtra("date1");
        String s = databaseDate;
        String[] parts = s.split("/");
        databaseDate = parts[2] + "/" + parts[0] + "/" + parts[1];

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!storeName.getText().toString().isEmpty() && !date.getText().toString().isEmpty() &&
                        !price.getText().toString().isEmpty()){
                    manualEntry();
                }else{
                    storeName.setError("Please fill out all necessary fields");
                    price.setError("Please fill out all necessary fields");
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeName.getText().clear();
                price.getText().clear();
                notes.getText().clear();
                startActivity(new Intent(PreviewScreenActivity.this, AddActivity.class));
            }
        });

//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                c = Calendar.getInstance();
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//
//                try {
//                    datePickerDialog = new DatePickerDialog(PreviewScreenActivity.this, new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                            databaseDate = Integer.toString(year) + "/" + Integer.toString(month+1) + "/" + Integer.toString(dayOfMonth);
//                            date.setText((month+1) + "/" + dayOfMonth + "/" + year);
//                        }
//                    }, year, month, day);
//                    datePickerDialog.show();
//
//                    Log.e("PLEASE HELP!!!!!!!!!!", String.valueOf(databaseDate));
//                } catch (Exception e) {
//                    Log.e("PLEASE HELP!!!!!!!!!!", String.valueOf(databaseDate));
//                    e.printStackTrace();
//                }
//
//
//            }
//        });

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(PreviewScreenActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));
        myAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.globalPreferenceName, MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "No Value");
        useridnum = userid;



    }

    private void manualEntry() {
        final String storeName = this.storeName.getText().toString().trim();
        final String date = databaseDate;
        final String price = this.price.getText().toString().trim();
        final String notes = this.notes.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(PreviewScreenActivity.this, "Receipt added successfully", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PreviewScreenActivity.this, "Receipt failed to add", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PreviewScreenActivity.this, "Failed to connect to the database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("storeName", storeName);
                params.put("date", date);
                params.put("price", price);
                params.put("notes", notes);
                params.put("categoryName", categoryName);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        startActivity(new Intent(PreviewScreenActivity.this, HomeActivity.class));

    }

}
