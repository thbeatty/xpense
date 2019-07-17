package com.example.trey.registertest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstName, lastName, email, password, confirmPassword, answerOne, answerTwo;
    private Button registerButton, loginButton;
    Spinner mySpinner, mySpinner1;
    private static String url = "http://cgi.soic.indiana.edu/~team33/register.php";
    private String questionOne;
    private String questionTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        confirmPassword = findViewById(R.id.userConfirmPassword);
        registerButton = findViewById(R.id.registerButton);
        answerOne = findViewById(R.id.answerSecurityQ1);
        answerTwo = findViewById(R.id.answerSecurityQ2);
        mySpinner = findViewById(R.id.securityDropdownQ1);
        mySpinner1 = findViewById(R.id.securityDropdownQ2);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())){
                    register();
                } else {
                    confirmPassword.setError("Passwords do not match");
                }
            }
        });




        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.questions));
        myAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionOne = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.questions));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner1.setAdapter(myAdapter1);
        mySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionTwo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void register() {
        final String firstName = this.firstName.getText().toString().trim();
        final String lastName = this.lastName.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String answerOne = this.answerOne.getText().toString().trim();
        final String answerTwo = this.answerTwo.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Failed to connect to the database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("email", email);
                params.put("password", password);
                params.put("questionOne", questionOne);
                params.put("answerOne", answerOne);
                params.put("questionTwo", questionTwo);
                params.put("answerTwo", answerTwo);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}