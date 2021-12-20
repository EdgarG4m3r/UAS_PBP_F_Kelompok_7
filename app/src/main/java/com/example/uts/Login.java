package com.example.uts;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uts.api.UserApi;
import com.example.uts.model.User;
import com.example.uts.model.UserResponse;
import com.example.uts.preferences.UserPreferences;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText etEmail, etPass;
    private MaterialButton btnLogin;
    private TextView tvSignup;
    private UserPreferences userPreferences;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userPreferences = new UserPreferences(Login.this);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);

        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        queue = Volley.newRequestQueue(this);

        /* Apps will check the login first from shared preferences */
        checkLogin();

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    checkLogin();
                }
        });

    }

    private void checkLogin(){
        login();
        if(userPreferences.checkLogin()){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    private void login() {

        User user = new User(
                etEmail.getText().toString(),
                etPass.getText().toString());

        final StringRequest stringRequest = new StringRequest(POST, UserApi.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        UserResponse userResponse =
                                gson.fromJson(response, UserResponse.class);

                        Toast.makeText(Login.this, userResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        User responseUser = userResponse.getUser();
                        userPreferences.setUser(responseUser.getId(),responseUser.getName(),responseUser.getEmail(),etPass.getText().toString(),responseUser.getNoTelp(),responseUser.getFoto());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String responseBody =
                            new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(Login.this, errors.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(Login.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            //TODO jgn lupa hapus kalau error
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", etEmail.getText().toString());
                params.put("password", etPass.getText().toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                String requestBody = gson.toJson(user);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        queue.add(stringRequest);
    }

}
