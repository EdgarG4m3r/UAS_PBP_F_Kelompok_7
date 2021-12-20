package com.example.uts.fragment;

import static com.android.volley.Request.Method.GET;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uts.R;
import com.example.uts.adapter.ReservationAdapter;
import com.example.uts.api.ReservationApi;
import com.example.uts.model.Reservation;
import com.example.uts.model.ReservationResponse;
import com.example.uts.preferences.UserPreferences;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentDetail extends Fragment {
    private RecyclerView rv_reservasi;
    private UserPreferences userPreferences;
    private Context context;

    private List<Reservation> reservationList;
    private ReservationAdapter reservationAdapter = null;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_reservation_detail, container, false);
        rv_reservasi = root.findViewById(R.id.rv_reservasi);

        userPreferences = new UserPreferences(getContext());

        queue = Volley.newRequestQueue(getActivity());

        rv_reservasi.setLayoutManager(new LinearLayoutManager(getContext()));

        getReservations();

        reservationList = new ArrayList<>();

        return root;
    }

    private void getReservations() {

        final StringRequest stringRequest = new StringRequest(GET, ReservationApi.GET_ALL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ReservationResponse reservationResponse = gson.fromJson(response, ReservationResponse.class);

                        reservationAdapter = new ReservationAdapter(reservationResponse.getReservationList(), getContext());

                        Toast.makeText(getActivity(), reservationResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String responseBody =
                            new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(getActivity(), errors.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        queue.add(stringRequest);
        rv_reservasi.setAdapter(reservationAdapter);
    }

}
