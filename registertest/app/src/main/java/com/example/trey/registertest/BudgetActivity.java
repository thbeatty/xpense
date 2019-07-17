package com.example.trey.registertest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class BudgetActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public static String globalPreferenceName = "com.trey.profile";
    private static String urlbills = "https://cgi.soic.indiana.edu/~team33/billsset.php";
    private static String urlenter = "https://cgi.soic.indiana.edu/~team33/entertainmentset.php";
    private static String urlgrocery = "https://cgi.soic.indiana.edu/~team33/groceryset.php";
    private static String urlmisc = "https://cgi.soic.indiana.edu/~team33/miscset.php";
    private static String urlpersonal = "https://cgi.soic.indiana.edu/~team33/personalset.php";
    private static String urlbudget = "http://cgi.soic.indiana.edu/~team33/budgetupdate.php";
    private static String urlmonthly = "http://cgi.soic.indiana.edu/~team33/monthlyset.php";
    String useridnum, budgetAmount1, categoryNameVariable;
    private Button monthlyCategoryButton;
    private Button billsCategoryButton;
    private Button entertainmentCategoryButton;
    private Button groceriesCategoryButton;
    private Button miscellaneousCategoryButton;
    private Button personalCategoryButton;
    private Button previousCategoryButton;
    ProgressBar monthlyProgressBar;
    ProgressBar billsProgressBar;
    ProgressBar entertainmentProgressBar;
    ProgressBar groceriesProgressBar;
    ProgressBar miscellaneousProgressBar;
    ProgressBar personalProgressBar;
    private static String monthlyPHP = "http://cgi.soic.indiana.edu/~team33/monthlyCurrentValue.php";
    private static String billsPHP = "http://cgi.soic.indiana.edu/~team33/billsCurrentValue.php";
    private static String entertainmentPHP = "http://cgi.soic.indiana.edu/~team33/entertainmentCurrentValue.php";
    private static String groceryPHP = "http://cgi.soic.indiana.edu/~team33/groceryCurrentValue.php";
    private static String miscellaneousPHP = "http://cgi.soic.indiana.edu/~team33/miscellaneousCurrentValue.php";
    private static String personalPHP = "http://cgi.soic.indiana.edu/~team33/personalCurrentValue.php";
    private static String monthlyPHP2 = "http://cgi.soic.indiana.edu/~team33/selectMonthlyTotal.php";
    private static String billsPHP2 = "http://cgi.soic.indiana.edu/~team33/selectBillsTotal.php";
    private static String entertainmentPHP2 = "http://cgi.soic.indiana.edu/~team33/selectEntertainmentTotal.php";
    private static String groceryPHP2 = "http://cgi.soic.indiana.edu/~team33/selectGroceriesTotal.php";
    private static String miscellaneousPHP2 = "http://cgi.soic.indiana.edu/~team33/selectMiscellaneousTotal.php";
    private static String personalPHP2 = "http://cgi.soic.indiana.edu/~team33/selectPersonalTotal.php";
    Button billsAmountEditButton, entertainmentAmountEditButton, groceriesAmountEditButton,
            miscAmountEditButton, personalAmountEditButton, monthlyEditButton;
    TextView monthCurrentTotal, billsCurrentTotal, entertainmentCurrentTotal, groceriesCurrentTotal,
            miscCurrentTotal, personalCurrentTotal;
    TextView monthBudgetAmt, billsBudgetAmt, entertainmentBudgetAmt, groceriesBudgetAmt, miscBudgetAmt, personalBudgetAmt;
    TextView monthAmtLeft, billsAmtLeft, entertainmentAmtLeft, groceriesAmtLeft, miscAmtLeft, personalAmtLeft;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        billsAmountEditButton = findViewById(R.id.billsAmountEditButton);
        monthlyEditButton = findViewById(R.id.monthlyEditButton);
        entertainmentAmountEditButton = findViewById(R.id.entertainmentAmountEditButton);
        groceriesAmountEditButton = findViewById(R.id.groceriesAmountEditButton);
        miscAmountEditButton = findViewById(R.id.miscAmountEditButton);
        personalAmountEditButton = findViewById(R.id.personalAmountEditButton);
        bottomNavigationView = findViewById(R.id.navBar);
        monthlyProgressBar = findViewById(R.id.progressBarMonth);
        billsProgressBar = findViewById(R.id.progressBarBills);
        entertainmentProgressBar = findViewById(R.id.progressBarEntertainment);
        groceriesProgressBar = findViewById(R.id.progressBarGroceries);
        miscellaneousProgressBar = findViewById(R.id.progressBarMisc);
        personalProgressBar = findViewById(R.id.progressBarPersonal);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        monthCurrentTotal = findViewById(R.id.monthCurrentTotal);
        billsCurrentTotal = findViewById(R.id.billsCurrentTotal);
        entertainmentCurrentTotal = findViewById(R.id.entertainmentCurrentTotal);
        groceriesCurrentTotal = findViewById(R.id.groceriesCurrentTotal);
        miscCurrentTotal = findViewById(R.id.miscCurrentTotal);
        personalCurrentTotal = findViewById(R.id.personalCurrentTotal);
        monthBudgetAmt = findViewById(R.id.monthBudgetAmt);
        billsBudgetAmt = findViewById(R.id.billsBudgetAmt);
        entertainmentBudgetAmt = findViewById(R.id.entertainmentBudgetAmt);
        groceriesBudgetAmt = findViewById(R.id.groceriesBudgetAmt);
        miscBudgetAmt = findViewById(R.id.miscBudgetAmt);
        personalBudgetAmt = findViewById(R.id.personalBudgetAmt);
        monthAmtLeft = findViewById(R.id.monthAmtLeft);
        billsAmtLeft = findViewById(R.id.billsAmtLeft);
        entertainmentAmtLeft = findViewById(R.id.entertainmentAmtLeft);
        groceriesAmtLeft = findViewById(R.id.groceriesAmtLeft);
        miscAmtLeft = findViewById(R.id.miscAmtLeft);
        personalAmtLeft = findViewById(R.id.personalAmtLeft);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case (R.id.homeNav):
                        Intent intent0 = new Intent(BudgetActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case (R.id.homeBudget): ;
                        Intent intent1 = new Intent(BudgetActivity.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(BudgetActivity.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(BudgetActivity.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });

        monthlyCategoryButton = findViewById(R.id.monthlyCategory);
        monthlyCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String monthly = "Monthly";
//                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
//                editor.putString("monthlyCategory", monthly);
//                editor.commit();
                startActivity(new Intent(BudgetActivity.this, MonthlyCategoryActivity.class));
            }
        });


        billsCategoryButton = (Button) findViewById(R.id.billsCategory);
        billsCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bills = "Bills";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("billsCategory", bills);
                editor.commit();
                startActivity(new Intent(BudgetActivity.this, BillsCategoryActivity.class));
            }
        });

        entertainmentCategoryButton = findViewById(R.id.entertainmentCategory);
        entertainmentCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entertainment = "Entertainment";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("entCategory", entertainment);
                editor.commit();

                startActivity(new Intent(BudgetActivity.this, EntertainmentCategoryActivity.class));
            }
        });

        groceriesCategoryButton = findViewById(R.id.groceriesCategory);
        groceriesCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groceries = "Grocery";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("groceriesCategory", groceries);
                editor.commit();
                startActivity(new Intent(BudgetActivity.this, GroceriesCategoryActivity.class));
            }
        });

        miscellaneousCategoryButton = findViewById(R.id.miscCategory);
        miscellaneousCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String miscellaneous = "Miscellaneous";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("miscCategory", miscellaneous);
                editor.commit();
                startActivity(new Intent(BudgetActivity.this, MiscellaneousCategoryActivity.class));
            }
        });
        personalCategoryButton = findViewById(R.id.personalCategory);
        personalCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personal = "Personal";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("personalCategory", personal);
                editor.commit();
                startActivity(new Intent(BudgetActivity.this, PersonalCategoryActivity.class));
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "No Value");
        useridnum = userid;


        billsAmountEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = "Bills";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("categoryName", categoryName);
                editor.commit();
                popUpEditText();
            }
        });

        entertainmentAmountEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = "Entertainment";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("categoryName", categoryName);
                editor.commit();
                popUpEditText();
            }
        });

        groceriesAmountEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = "Grocery";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("categoryName", categoryName);
                editor.commit();
                popUpEditText();
            }
        });

        miscAmountEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = "Miscellaneous";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("categoryName", categoryName);
                editor.commit();
                popUpEditText();
            }
        });

        personalAmountEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = "Personal";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("categoryName", categoryName);
                editor.commit();
                SharedPreferences sharedPreferences = getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
                categoryNameVariable = sharedPreferences.getString("categoryName", "No Value");
                popUpEditText();
            }
        });

        monthlyEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = "Monthly";
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.putString("categoryName", categoryName);
                editor.commit();
                SharedPreferences sharedPreferences = getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
                categoryNameVariable = sharedPreferences.getString("categoryName", "No Value");
                popUpEditText();
            }
        });

        previousCategoryButton = findViewById(R.id.previousMonth);
        previousCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BudgetActivity.this, PreviousCategoryActivity.class));
            }
        });

        ReceiptRetrieverMonthly();
        ReceiptRetrieverBills();
        ReceiptRetrieverEntertainment();
        ReceiptRetrieverGrocery();
        ReceiptRetrieverMiscellaneous();
        ReceiptRetrieverPersonal();

        ValueRetrieverMonthly();
        ValueRetrieverBills();
        ValueRetrieverEntertainment();
        ValueRetrieverGrocery();
        ValueRetrieverMiscellaneous();
        ValueRetrieverPersonal();

        billsSet();
        entertainmentSet();
        grocerySet();
        miscSet();
        personalSet();
        monthlySet();

    }

    private void popUpEditText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Amount");

        SharedPreferences sharedPreferences = getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
        String categoryName = sharedPreferences.getString("categoryName", "No Value");
        categoryNameVariable = categoryName;

        final EditText budgetAmount = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        budgetAmount.setLayoutParams(lp);
        budgetAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(budgetAmount);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                budgetAmount1 = budgetAmount.getText().toString().trim();
                updateAmount();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void personalSet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlpersonal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            //if (success.equals("1")) {
                            //Toast.makeText(BudgetActivity.this, "success", Toast.LENGTH_LONG).show();
                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BudgetActivity.this, "Failed to add data to database", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BudgetActivity.this, "Failed to connect to database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("valueTotal", budgetAmount1);
                //params.put("categoryName", categoryNameVariable);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void updateAmount() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlbudget,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(BudgetActivity.this, "Category total updated successfully", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BudgetActivity.this, "Category total could not be updated", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BudgetActivity.this, "Failed to connect to database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("valueTotal", budgetAmount1);
                params.put("categoryName", categoryNameVariable);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void billsSet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlbills,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            //if (success.equals("1")) {
                            //    Toast.makeText(BudgetActivity.this, "success", Toast.LENGTH_LONG).show();
                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BudgetActivity.this, "Failed to add data to database", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BudgetActivity.this, "Failed to connect to database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("valueTotal", budgetAmount1);
                //params.put("categoryName", categoryNameVariable);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void entertainmentSet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlenter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            //if (success.equals("1")) {
                            //    Toast.makeText(BudgetActivity.this, "success", Toast.LENGTH_LONG).show();
                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BudgetActivity.this, "Failed to add data to database", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BudgetActivity.this, "Failed to connect to database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("valueTotal", budgetAmount1);
                //params.put("categoryName", categoryNameVariable);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void grocerySet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgrocery,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            //if (success.equals("1")) {
                            //    Toast.makeText(BudgetActivity.this, "success", Toast.LENGTH_LONG).show();
                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BudgetActivity.this, "Failed to add data to database", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BudgetActivity.this, "Failed to connect to database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("valueTotal", budgetAmount1);
                //params.put("categoryName", categoryNameVariable);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void miscSet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlmisc,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            //if (success.equals("1")) {
                            //    Toast.makeText(BudgetActivity.this, "success", Toast.LENGTH_LONG).show();
                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BudgetActivity.this, "Failed to add data to database", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BudgetActivity.this, "Failed to connect to database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("valueTotal", budgetAmount1);
                //params.put("categoryName", categoryNameVariable);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void monthlySet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlmonthly,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            //if (success.equals("1")) {
                            //    Toast.makeText(BudgetActivity.this, "success", Toast.LENGTH_LONG).show();
                            //}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BudgetActivity.this, "Failed to add data to database", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BudgetActivity.this, "Failed to connect to database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("valueTotal", budgetAmount1);
                //params.put("categoryName", categoryNameVariable);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ReceiptRetrieverMonthly() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, monthlyPHP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int currentMonthlyTotal = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String price = object.optString("price").trim();
                                Float receiptPrice2 = Float.valueOf(price);
                                int receiptPrice = Math.round(receiptPrice2);
                                currentMonthlyTotal += receiptPrice;

                            }
                            Log.e("Monthly Total", String.valueOf(currentMonthlyTotal));
                            SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                            editor.putInt("currentMonthlyTotal", currentMonthlyTotal);
                            editor.commit();
                            monthCurrentTotal.setText("$" + currentMonthlyTotal);

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
                            billsCurrentTotal.setText("$" + currentBillsTotal);

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ReceiptRetrieverEntertainment() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, entertainmentPHP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int currentEntertainmentTotal = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String price = object.optString("price").trim();
                                Float receiptPrice2 = Float.valueOf(price);
                                int receiptPrice = Math.round(receiptPrice2);
                                currentEntertainmentTotal += receiptPrice;

                            }
                            Log.e("Ent Total", String.valueOf(currentEntertainmentTotal));
                            SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                            editor.putInt("currentEntertainmentTotal", currentEntertainmentTotal);
                            editor.commit();
                            entertainmentCurrentTotal.setText("$" + currentEntertainmentTotal);

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void ReceiptRetrieverGrocery() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, groceryPHP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int currentGroceryTotal = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String price = object.optString("price").trim();
                                Float receiptPrice2 = Float.valueOf(price);
                                int receiptPrice = Math.round(receiptPrice2);
                                currentGroceryTotal += receiptPrice;

                            }
                            Log.e("Grocery Total", String.valueOf(currentGroceryTotal));
                            SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                            editor.putInt("currentGroceryTotal", currentGroceryTotal);
                            editor.commit();
                            groceriesCurrentTotal.setText("$" + currentGroceryTotal);

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ReceiptRetrieverMiscellaneous() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, miscellaneousPHP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int currentMiscellaneousTotal = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String price = object.optString("price").trim();

                                Float receiptPrice2 = Float.valueOf(price);
                                int receiptPrice = Math.round(receiptPrice2);
                                currentMiscellaneousTotal += receiptPrice;

                            }
                            Log.e("Misc Total", String.valueOf(currentMiscellaneousTotal));
                            SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                            editor.putInt("currentMiscellaneousTotal", currentMiscellaneousTotal);
                            editor.commit();
                            miscCurrentTotal.setText("$" + currentMiscellaneousTotal);

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
                                String price = object.optString("price").trim();
                                Float receiptPrice2 = Float.valueOf(price);
                                int receiptPrice = Math.round(receiptPrice2);
                                currentPersonalTotal += receiptPrice;

                            }
                            Log.e("Personal Total", String.valueOf(currentPersonalTotal));
                            SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                            editor.putInt("currentPersonalTotal", currentPersonalTotal);
                            editor.commit();
                            personalCurrentTotal.setText("$" + currentPersonalTotal);

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void ValueRetrieverMonthly() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, monthlyPHP2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            float monthlyTotalValue = 0;
                            int monthlyTotalValue2 = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String valueTotal = object.optString("valueTotal").trim();


                                Float monthlyTotal = Float.valueOf(valueTotal);
                                monthlyTotalValue = monthlyTotal;
                                monthlyTotalValue2 = Math.round(monthlyTotal);


                            }
                            Log.e("Monthly Total Value", String.valueOf(monthlyTotalValue));

                            SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
                            int currentMonthlyTotal = sharedPreferences1.getInt("currentMonthlyTotal", 0);
                            float progress1 = (currentMonthlyTotal / monthlyTotalValue) * 100;
                            int remaining = monthlyTotalValue2 - currentMonthlyTotal;
                            int progress = Math.round(progress1);
                            monthlyProgressBar.setProgress(progress);
                            monthBudgetAmt.setText("$" + String.valueOf(monthlyTotalValue2));
                            if (remaining < 0) {
                                int remaining2 = remaining * -1;
                                monthAmtLeft.setText("$" + String.valueOf(remaining2) + " Over");
                            } else {
                                monthAmtLeft.setText("$" + String.valueOf(remaining) + " Left");
                            }


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
                            int billsTotalValue2 = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String valueTotal = object.optString("valueTotal").trim();

                                Float monthlyTotal = Float.valueOf(valueTotal);
                                billsTotalValue = monthlyTotal;
                                billsTotalValue2 = Math.round(monthlyTotal);


                            }
                            Log.e("Bills Total Value", String.valueOf(billsTotalValue));

                            SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
                            int currentBillsTotal = sharedPreferences1.getInt("currentBillsTotal", 0);
                            Log.e("Current Bills Total", String.valueOf(currentBillsTotal));
                            float progress1 = (currentBillsTotal / billsTotalValue) * 100;
                            int remaining = billsTotalValue2 - currentBillsTotal;
                            int progress = Math.round(progress1);
                            billsProgressBar.setProgress(progress);
                            billsBudgetAmt.setText("$" + String.valueOf(billsTotalValue2));
                            if (remaining < 0) {
                                int remaining2 = remaining * -1;
                                billsAmtLeft.setText("$" + String.valueOf(remaining2) + " Over");
                            } else {
                                billsAmtLeft.setText("$" + String.valueOf(remaining) + " Left");
                            }


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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ValueRetrieverEntertainment() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, entertainmentPHP2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            float entertainmentTotalValue = 0;
                            int entertainmentTotalValue2 = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String valueTotal = object.optString("valueTotal").trim();


                                Float monthlyTotal = Float.valueOf(valueTotal);
                                entertainmentTotalValue = monthlyTotal;
                                entertainmentTotalValue2 = Math.round(entertainmentTotalValue);


                            }
                            Log.e("Entertainment Value", String.valueOf(entertainmentTotalValue));

                            SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
                            int currentEntertainmentTotal = sharedPreferences1.getInt("currentEntertainmentTotal", 0);
                            float progress1 = (currentEntertainmentTotal / entertainmentTotalValue) * 100;
                            int progress = Math.round(progress1);
                            int remaining = entertainmentTotalValue2 - currentEntertainmentTotal;
                            entertainmentProgressBar.setProgress(progress);
                            entertainmentBudgetAmt.setText("$" + String.valueOf(entertainmentTotalValue2));

                            if (remaining < 0) {
                                int remaining2 = remaining * -1;
                                entertainmentAmtLeft.setText("$" + String.valueOf(remaining2) + " Over");
                            } else {
                                entertainmentAmtLeft.setText("$" + String.valueOf(remaining) + " Left");
                            }



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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ValueRetrieverGrocery() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, groceryPHP2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            float groceryTotalValue = 0;
                            int groceryTotalValue2 = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String valueTotal = object.optString("valueTotal").trim();


                                Float monthlyTotal = Float.valueOf(valueTotal);
                                groceryTotalValue = monthlyTotal;
                                groceryTotalValue2 = Math.round(groceryTotalValue);


                            }
                            Log.e("Grocery Value", String.valueOf(groceryTotalValue));

                            SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
                            int currentGroceryTotal = sharedPreferences1.getInt("currentGroceryTotal", 0);
                            float progress1 = (currentGroceryTotal / groceryTotalValue) * 100;
                            int progress = Math.round(progress1);
                            int remaining = groceryTotalValue2 - currentGroceryTotal;
                            groceriesProgressBar.setProgress(progress);
                            groceriesBudgetAmt.setText("$" + String.valueOf(groceryTotalValue2));

                            if (remaining < 0) {
                                int remaining2 = remaining * -1;
                                groceriesAmtLeft.setText("$" + String.valueOf(remaining2) + " Over");
                            } else {
                                groceriesAmtLeft.setText("$" + String.valueOf(remaining) + " Left");
                            }

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ValueRetrieverMiscellaneous() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, miscellaneousPHP2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            float miscellaneousTotalValue = 0;
                            int miscellaneousTotalValue2 = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String valueTotal = object.optString("valueTotal").trim();


                                Float monthlyTotal = Float.valueOf(valueTotal);
                                miscellaneousTotalValue = monthlyTotal;
                                miscellaneousTotalValue2 = Math.round(monthlyTotal);


                            }
                            Log.e("Miscellaneous Value", String.valueOf(miscellaneousTotalValue));

                            SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
                            int currentMiscellaneousTotal = sharedPreferences1.getInt("currentMiscellaneousTotal", 0);
                            float progress1 = (currentMiscellaneousTotal / miscellaneousTotalValue) * 100;
                            int progress = Math.round(progress1);
                            int remaining = miscellaneousTotalValue2 - currentMiscellaneousTotal;
                            miscellaneousProgressBar.setProgress(progress);
                            miscBudgetAmt.setText("$" + String.valueOf(miscellaneousTotalValue2));

                            if (remaining < 0) {
                                int remaining2 = remaining * -1;
                                miscAmtLeft.setText("$" + String.valueOf(remaining2) + " Over");
                            } else {
                                miscAmtLeft.setText("$" + String.valueOf(remaining) + " Left");
                            }


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
                            int personalTotalValue2 = 0;
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String valueTotal = object.optString("valueTotal").trim();


                                Float monthlyTotal = Float.valueOf(valueTotal);
                                personalTotalValue = monthlyTotal;
                                personalTotalValue2 = Math.round(monthlyTotal);


                            }
                            Log.e("Personal Value", String.valueOf(personalTotalValue));
                            SharedPreferences sharedPreferences1 = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE);
                            int currentPersonalTotal = sharedPreferences1.getInt("currentPersonalTotal", 0);
                            float progress1 = (currentPersonalTotal / personalTotalValue) * 100;
                            int progress = Math.round(progress1);
                            int remaining = personalTotalValue2 - currentPersonalTotal;
                            personalProgressBar.setProgress(progress);
                            personalBudgetAmt.setText("$" + String.valueOf(personalTotalValue2));

                            if (remaining < 0) {
                                int remaining2 = remaining * -1;
                                personalAmtLeft.setText("$" + String.valueOf(remaining2) + " Over");
                            } else {
                                personalAmtLeft.setText("$" + String.valueOf(remaining) + " Left");
                            }



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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}