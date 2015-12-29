package com.jp.smartgpsapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.jp.smartgpsapp.helpers.HandleProgressDialog;
import com.jp.smartgpsapp.helpers.SQLiteHandler;
import com.jp.smartgpsapp.helpers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button loginBtn, registerBtn;
    private EditText emailTxt, passwordTxt;
    private HandleProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Progress dialog
        String msg = getResources().getText(R.string.loggingIn).toString();
        pDialog = new HandleProgressDialog(this, msg);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }

        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        emailTxt = (EditText) findViewById(R.id.emailEditText);
        passwordTxt = (EditText) findViewById(R.id.passwordEditText);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void login(){
        final String email = emailTxt.getText().toString().trim();
        final String password = passwordTxt.getText().toString().trim();
        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {
            // login user
            // Tag used to cancel the request
            String tag_string_req = "req_login";
            pDialog.showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Register Response: " + response.toString());
                            pDialog.hideDialog();
                            try {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean("error");

                                // Check for error node in json
                                if (!error) {
                                    // user successfully logged in
                                    // Create login session
                                    session.setLogin(true);

                                    JSONObject user = jObj.getJSONObject("user");
                                    String callsign = user.getString("callsign");
                                    String email = user.getString("email");
                                    String username = user.getString("username");
                                    String password = user.getString("password");
                                    String location = user.getString("location");
                                    String created_at = user.getString("created_at");
                                    User userObj = new User(callsign, email, username,
                                                        password, location, created_at );
                                    // Inserting row in users table
                                    db.addUser(userObj);
                                    // Launch main activity
                                    Intent intent = new Intent(LoginActivity.this,
                                            MainMenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Error in login. Get the error message
                                    pDialog.hideDialog();
                                    String errorMsg = jObj.getString("error_msg");
                                    Toast.makeText(getApplicationContext(),
                                            errorMsg, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                // JSON error
                                pDialog.hideDialog();
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
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
            AppControllor.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Email or password cannot be empty!", Toast.LENGTH_LONG)
                    .show();
        }
    }// end login()
}
