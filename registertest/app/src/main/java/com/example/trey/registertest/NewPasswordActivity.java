package com.example.trey.registertest;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class NewPasswordActivity extends AppCompatActivity {

    private EditText newPassword, newPasswordConfirm;
    private Button submitButton;
    String useridnum;
    private static String url = "http://cgi.soic.indiana.edu/~team33/newpassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        newPassword = findViewById(R.id.newPassword);
        newPasswordConfirm = findViewById(R.id.newPasswordConfirm);
        submitButton = findViewById(R.id.submitButton);


        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.globalPreferenceName, MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "No Value");
        useridnum = userid;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())){
                    updatePassword();
                }

                else{
                    newPasswordConfirm.setError("Passwords do not match");
                }
            }
        });
    }

    private void updatePassword() {
        final String password = this.newPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            Toast.makeText(NewPasswordActivity.this, success, Toast.LENGTH_LONG).show();

                            if (success.equals("1")) {
                                Toast.makeText(NewPasswordActivity.this, "Password Reset Successfully", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(NewPasswordActivity.this, "Password Reset Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewPasswordActivity.this, "Failed to connect to the database", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("id", useridnum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
