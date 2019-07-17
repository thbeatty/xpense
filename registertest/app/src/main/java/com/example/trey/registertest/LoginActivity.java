package com.example.trey.registertest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static String globalPreferenceName = "com.trey.profile";
    private EditText email1, password1;
    private Button loginButton, registerButton, forgotPasswordButton;
    private int CAMERA_PERMISSION_CODE = 1;
    private int STORAGE_PERMISSION_CODE = 1;
    private static String url = "https://cgi.soic.indiana.edu/~team33/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email1 = findViewById(R.id.email1);
        password1 = findViewById(R.id.password1);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email1.getText().toString().trim();
                String mPassword = password1.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPassword.isEmpty()){
                    Login(mEmail, mPassword);
                } else{
                    email1.setError("Please insert email");
                    password1.setError("Please insert password");
                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        SharedPreferences.Editor editor = getSharedPreferences(BudgetActivity.globalPreferenceName, MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        SharedPreferences.Editor editor2 = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
        editor2.clear();
        editor2.commit();

        requestCameraPermission();



    }

    private void Login(final String email, final String password) {

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

                                    String firstName = object.getString("firstName").trim();
                                    /*String lastName = object.getString("lastName").trim();*/
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id").trim();

                                    /*Toast.makeText(LoginActivity.this, "Login Successful \nWelcome "
                                            +firstName+" "+/*lastName+"\nEmail: "+email, Toast
                                            .LENGTH_LONG).show();*/

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("firstName", firstName);
                                    intent.putExtra("email", email);
                                    intent.putExtra("id", id);
                                    startActivity(intent);

                                    SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                                    editor.putString("userid", id);
                                    editor.commit();


                                }

                            }else {
                                Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginButton.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginButton.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Failed to connect to database", Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Camera access is needed for some features")
                    .setMessage("This permission is needed for the scanning function")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }


    }
}
