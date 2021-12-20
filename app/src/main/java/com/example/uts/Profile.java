package com.example.uts;

import static com.android.volley.Request.Method.PUT;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity
{
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;

    private TextView tv_email,tv_nama, tv_noTelp;
    UserPreferences userPreferences;
    private MaterialButton btnSave, btnEdit;
    private ImageView ivPicture;
    private RequestQueue queue;
    private String newName;
    private String newTelp;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        userPreferences = new UserPreferences(Profile.this);

        tv_email = findViewById(R.id.tv_email);
        tv_nama = findViewById(R.id.tv_nama);
        tv_noTelp = findViewById(R.id.tv_noTelp);

        tv_email.setText(userPreferences.getUserLogin().getEmail());
        tv_nama.setText(userPreferences.getUserLogin().getName());
        tv_noTelp.setText(userPreferences.getUserLogin().getNoTelp());

        queue = Volley.newRequestQueue(this);

        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_baseline_cancel_24);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                Toast.makeText(Profile.this, "Permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            Uri selectedImage = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(Profile.this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }

        bitmap = getResizedBitmap(bitmap, 512);
        ivPicture.setImageBitmap(bitmap);
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }*/

    private String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public void btn_Back (View view) {
        startActivity(new Intent(Profile.this, MainActivity.class));
        finish();
    }

    public void ivPicture (View view) {
        startActivity(new Intent(Profile.this, CameraActivity.class));
    }

    public void btnNama (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Nama");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newName = input.getText().toString();
                editNama(newName);
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

    private void editNama(String nama) {

        User user = new User(
                nama,
                userPreferences.getUserLogin().getEmail(),
                userPreferences.getUserLogin().getNoTelp(),
                "-");

        final StringRequest stringRequest = new StringRequest(PUT, UserApi.UPDATE_URL + userPreferences.getUserLogin().getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        UserResponse userResponse =
                                gson.fromJson(response, UserResponse.class);

                        Toast.makeText(Profile.this, userResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();

                        userPreferences.setNama(nama);
                        tv_nama.setText(nama);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String responseBody =
                            new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(Profile.this, errors.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(Profile.this, e.getMessage(),
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

    public void btnTelp (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit No. Telp");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newTelp = input.getText().toString();
                editNoTelp(newTelp);
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

    private void editNoTelp(String noTelp) {

        User user = new User(
                userPreferences.getUserLogin().getName(),
                userPreferences.getUserLogin().getEmail(),
                noTelp,
                "-");

        final StringRequest stringRequest = new StringRequest(PUT, UserApi.UPDATE_URL + userPreferences.getUserLogin().getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        UserResponse userResponse =
                                gson.fromJson(response, UserResponse.class);

                        Toast.makeText(Profile.this, userResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();

                        userPreferences.setTelp(noTelp);
                        tv_noTelp.setText(noTelp);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String responseBody =
                            new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(Profile.this, errors.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(Profile.this, e.getMessage(),
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

