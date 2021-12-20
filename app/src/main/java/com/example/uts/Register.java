package com.example.uts;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity
{
    private TextInputEditText etName, etEmail, etPassword,etTelp;
    private MaterialButton btnSignup;
    private UserPreferences userPreferences;
    private TextView tv_Login;
    private RequestQueue queue;
    private Bitmap bitmap = null;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        userPreferences = new UserPreferences(Register.this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tv_Login = findViewById(R.id.tv_login);
        btnSignup = findViewById(R.id.btnSignup);
        etTelp = findViewById(R.id.etTelp);

        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_baseline_account_circle_24);

        queue = Volley.newRequestQueue(this);

        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    register();
                }
            }
        });
    }

    private boolean validateForm(){
        /* Check username & password is empty or not */
        if(etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etName.getText().toString().isEmpty() || etTelp.getText().toString().isEmpty()){
            Toast.makeText(Register.this,"Field tidak boleh Kosong",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    private void register() {

        User user = new User(
                etName.getText().toString(),
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                etTelp.getText().toString(),
                bitmapToBase64(bitmap));

        final StringRequest stringRequest = new StringRequest(POST, UserApi.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        UserResponse userResponse =
                                gson.fromJson(response, UserResponse.class);

                        Toast.makeText(Register.this, userResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String responseBody =
                            new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(Register.this, errors.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(Register.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            //TODO jgn lupa hapus kalau error
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
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