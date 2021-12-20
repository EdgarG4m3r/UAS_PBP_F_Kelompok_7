package com.example.uts;

import static android.widget.Toast.LENGTH_SHORT;
import static com.android.volley.Request.Method.POST;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uts.UnitTesting.ActivityUtil;
import com.example.uts.UnitTesting.ReservationPresenter;
import com.example.uts.api.ReservationApi;
//import com.example.uts.database.DatabaseClient;
import com.example.uts.model.Reservation;
import com.example.uts.model.ReservationResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.example.uts.preferences.UserPreferences;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ReservationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private Spinner spinnerPaket;
    private static final String[] paketGunting = {"Gunting", "Gunting + Cuci", "Gunting + Cuci + Pijat"};
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String paket;
    private String tanggal;
    private String note;
    private double harga;
    private UserPreferences userPreferences;
    TextView hargaPaket;
    EditText tglReservasi;
    TextInputEditText noteText;
    private Context context;
    private RequestQueue queue;
    private ReservationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation);

         hargaPaket = findViewById(R.id.hargaPaket);
         tglReservasi= findViewById(R.id.tglReservasi);
         noteText = findViewById(R.id.note);

        userPreferences = new UserPreferences(this);
        spinnerPaket = (Spinner)findViewById(R.id.spinnerPaket);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ReservationActivity.this, android.R.layout.simple_spinner_item, paketGunting);

        queue = Volley.newRequestQueue(this);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaket.setAdapter(adapter);
        spinnerPaket.setOnItemSelectedListener(this);
        dateFormatter = new SimpleDateFormat("yyyy/mm/dd", Locale.US);

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tglReservasi, myCalendar);
            }

        };

        tglReservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ReservationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        String newHarga;
        switch (position) {
            case 0:
                paket = spinnerPaket.getSelectedItem().toString();
                harga = 15000;
                newHarga = Double.toString(harga);
                hargaPaket.setText(newHarga);
                break;
            case 1:
                paket = spinnerPaket.getSelectedItem().toString();
                harga = 18000;
                newHarga = Double.toString(harga);
                hargaPaket.setText(newHarga);
                break;
            case 2:
                paket = spinnerPaket.getSelectedItem().toString();
                harga = 20000;
                newHarga = Double.toString(harga);
                hargaPaket.setText(newHarga);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateLabel(EditText tglReservasi, Calendar myCalendar) {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tglReservasi.setText(sdf.format(myCalendar.getTime()));
    }

    public void btnSave (View view) {
        tanggal = tglReservasi.getText().toString();
        note = noteText.getText().toString();
        saveReservation();
    };

    public void btnBack (View view) {
        finish();
    };

    private void saveReservation() {

        Reservation reservation = new Reservation(
                paket,
                tanggal,
                note,
                Double.parseDouble(hargaPaket.getText().toString()),
                userPreferences.getUserLogin().getId());

        final StringRequest stringRequest = new StringRequest(POST, ReservationApi.ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ReservationResponse reservationResponse =
                                gson.fromJson(response, ReservationResponse.class);

                        Toast.makeText(ReservationActivity.this, reservationResponse.getMessage(),
                                LENGTH_SHORT).show();

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

                    Toast.makeText(ReservationActivity.this, errors.getString("message"),
                            LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ReservationActivity.this, e.getMessage(),
                            LENGTH_SHORT).show();
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
                String requestBody = gson.toJson(reservation);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
        };

        queue.add(stringRequest);
    }

    /*@Override
    public String getPaket() {
        return spinnerPaket.getSelectedItem().toString();
    }
    @Override
    public String getTanggal() {
        return tglReservasi.getText().toString();
    }
    @Override
    public void showTanggalError(String message) {
        tglReservasi.setError(message);
    }
    @Override
    public String getNote() {
        return noteText.getText().toString();
    }
    @Override
    public void startMainActivity() {
        new ActivityUtil(this).startMainReservation();
    }
    @Override
    public void showProfilError(String message) {
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }
    @Override
    public void showErrorResponse(String message) {
        Toast.makeText(this, message, LENGTH_SHORT).show();
    }*/
}
