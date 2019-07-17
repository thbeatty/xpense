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

import android.widget.CompoundButton;
import android.widget.Switch;
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

public class SettingsActivity extends AppCompatActivity {

    public static String globalPreferenceName = "com.trey.profile";
    String useridnum;
    TextView userEmailSettings;
    BottomNavigationView bottomNavigationView;
    private Button updatePassword, logOutButton;
//    private static final String PREFS_NAME = "prefs";
//    private static final String PREF_DARK_THEME = "dark_theme";
    private static String url = "http://cgi.soic.indiana.edu/~team33/emailretrieve.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SharedPreferences darkTheme = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        boolean useDarkTheme = darkTheme.getBoolean(PREF_DARK_THEME, false);
//
//        if (useDarkTheme) {
//            setTheme(R.style.AppTheme_Dark_NoActionBar);
//        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bottomNavigationView = findViewById(R.id.navBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        userEmailSettings = findViewById(R.id.userEmailSettings);
        logOutButton = findViewById(R.id.logOutButton);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.homeNav):
                        Intent intent0 = new Intent(SettingsActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case (R.id.homeBudget):
                        Intent intent1 = new Intent(SettingsActivity.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(SettingsActivity.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(SettingsActivity.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });
        updatePassword = findViewById(R.id.updatePasswordSettings);
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, NewPasswordActivity.class));
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });

//        Switch toggle = (Switch) findViewById(R.id.switch1);
//        toggle.setChecked(useDarkTheme);
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
//                toggleTheme(isChecked);
//            }
//        });

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.globalPreferenceName, MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "No Value");
        useridnum = userid;

        emailRetrieve();
    }

//    private void toggleTheme(boolean darkTheme) {
//        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
//        editor.putBoolean(PREF_DARK_THEME, darkTheme);
//        editor.apply();
//
//        Intent intent = getIntent();
//        finish();
//
//        startActivity(intent);
//    }

    private void emailRetrieve() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")){

                                for (int i = 0; i < jsonArray.length(); i=i+1){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String email = object.getString("email").trim();

                                    userEmailSettings.setText(email);

                                }

                            }else {
                                Toast.makeText(SettingsActivity.this, "Hello", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SettingsActivity.this, "Error"+e.toString(), Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SettingsActivity.this, "Error"+error.toString(), Toast.LENGTH_LONG).show();

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
