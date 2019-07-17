package com.example.trey.registertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText userEmail, sqAnswer1, sqAnswer2;
    private Button submitButton;
    Spinner mySpinner, mySpinner1;
    private String questionOne;
    private String questionTwo;
    private Boolean q1Check, q2Check, a1Check, a2Check;
    private static String url = "http://cgi.soic.indiana.edu/~team33/forgotPassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userEmail = findViewById(R.id.userEmail);
        sqAnswer1 = findViewById(R.id.sqAnswer1);
        sqAnswer2 = findViewById(R.id.sqAnswer2);
        submitButton = findViewById(R.id.submitButton);
        mySpinner = findViewById(R.id.securityQ1dropdown);
        mySpinner1 = findViewById(R.id.securityQ2dropdown);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ForgotPasswordActivity.this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.questions));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(ForgotPasswordActivity.this,
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


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEmail.getText().toString().isEmpty()){
                    userEmail.setError("Please insert your email address");

                }else{
                    forgotPasswordEmailCheck();
                }

            }
        });

    }

    private void forgotPasswordEmailCheck(){
        final String emailCheck = this.userEmail.getText().toString().trim();
        final String answerOne = this.sqAnswer1.getText().toString().trim();
        final String answerTwo = this.sqAnswer2.getText().toString().trim();
        q1Check = false;
        q2Check = false;
        a1Check = false;
        a2Check = false;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("emailCheck");

                            if (success.equals("1")){

                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);


                                    String q1 = object.getString("questionOne").trim();
                                    String a1 = object.getString("answerOne").trim();
                                    String q2 = object.getString("questionTwo").trim();
                                    String a2 = object.getString("answerTwo").trim();
                                    String id = object.getString("id").trim();


                                    if (!q1.equals(questionOne)){
                                        Toast.makeText(ForgotPasswordActivity.this, "Change question one", Toast.LENGTH_LONG).show();
                                    }else{
                                        q1Check = true;
                                    }

                                    if (!q2.equals(questionTwo)){
                                        Toast.makeText(ForgotPasswordActivity.this, "Change question two", Toast.LENGTH_LONG).show();
                                    }else{
                                        q2Check = true;
                                    }

                                    if (!a1.equals(sqAnswer1.getText().toString())){
                                        sqAnswer1.setError("Incorrect answer");
                                    }else{
                                        a1Check = true;
                                    }

                                    if (!a2.equals(sqAnswer2.getText().toString())){
                                        sqAnswer2.setError("Incorrect answer");
                                    }else{
                                        a2Check = true;
                                    }

                                    if (q1Check && q2Check && a1Check && a2Check){
                                        Intent intent = new Intent(ForgotPasswordActivity.this, NewPasswordActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(ForgotPasswordActivity.this, "Correct your questions or answers", Toast.LENGTH_LONG).show();
                                    }

                                    SharedPreferences.Editor editor = getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                                    editor.putString("userid", id);
                                    editor.commit();

                                }

                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Error", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email not found", Toast.LENGTH_LONG).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to connect to the database", Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", emailCheck);
                params.put("questionOne", questionOne);
                params.put("answerOne", answerOne);
                params.put("questionTwo", questionTwo);
                params.put("answerTwo", answerTwo);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}