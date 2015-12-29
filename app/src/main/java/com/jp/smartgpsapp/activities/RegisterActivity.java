package com.jp.smartgpsapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jp.smartgpsapp.Domain.User;
import com.jp.smartgpsapp.R;
import com.jp.smartgpsapp.controllers.AppControllor;
import com.jp.smartgpsapp.helpers.AppConfig;
import com.jp.smartgpsapp.helpers.SQLiteHandler;
import com.jp.smartgpsapp.helpers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button registerBtn;
    private Button linkToLoginBtn;
    private EditText callsignEditText;
    private EditText emailEditText;
    private EditText pwdEditText;
    private EditText rePwdEditText;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        callsignEditText = (EditText) findViewById(R.id.callsignEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        pwdEditText = (EditText) findViewById(R.id.psdEditText);
        rePwdEditText = (EditText) findViewById(R.id.rePsdEditText);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        linkToLoginBtn = (Button) findViewById(R.id.linkToLogin);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Register Button Click event
        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String callsign = callsignEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = pwdEditText.getText().toString().trim();
                String rePassword = rePwdEditText.getText().toString().trim();
                if (!callsign.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if(password.equals(rePassword)){
                        registerUser(callsign, email, password);
                    }else{
                        Toast.makeText(getApplicationContext(),
                                R.string.registerErr2, Toast.LENGTH_LONG)
                                .show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.registerErr1, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        linkToLoginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String callsign, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONObject user = jObj.getJSONObject("user");
                        String callsign = user.getString("callsign");
                        String email = user.getString("email");
                        String username = user.getString("username");

                        String password = user.getString("password");
                        String location = user.getString("location");
                        String created_at = user.getString("created_at");
                        User userObj = new User(callsign, email, username,
                                                password, location, created_at);
                        // Inserting row in users table
                        db.addUser(userObj);
                        Toast.makeText(getApplicationContext(), R.string.registerSuc,
                                        Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("callsign", callsign);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };

        // Adding request to request queue
        AppControllor.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}