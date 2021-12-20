package com.example.uts.adapter;

import static com.android.volley.Request.Method.DELETE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uts.Detail;
import com.example.uts.R;
import com.example.uts.api.ReservationApi;
//import com.example.uts.database.DatabaseClient;
import com.example.uts.fragment.FragmentDetail;
import com.example.uts.model.Reservation;
import com.example.uts.model.ReservationResponse;
import com.example.uts.preferences.UserPreferences;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Todo 11: ubah reservation Adapter
//Todo 12: tambah user adapter?
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder>
{
    private List<Reservation> reservationList;
    private Context context;
    //private DatabaseClient databaseClient;
    private UserPreferences userPreferences;
    private RequestQueue queue;

    public ReservationAdapter(List<Reservation> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
        this.userPreferences = new UserPreferences(context);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //init view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_detail,parent,false);
        queue = Volley.newRequestQueue(context.getApplicationContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.tv_tanggal.setText(reservation.getTanggal());
        holder.tv_paket.setText(reservation.getPaket());
        holder.tv_note.setText(reservation.getNote());
        holder.tv_harga.setText(Double.toString(reservation.getHarga()));
        //databaseClient = DatabaseClient.getInstance(context);
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelReservation(reservation.getId());
                Toast.makeText(context.getApplicationContext(), "Reservation Cancelled", Toast.LENGTH_SHORT).show();
                if(context instanceof Detail)
                {
                    Detail detail = (Detail) context;
                    detail.changeFragment(new FragmentDetail());
                }
            }
        });
        holder.ivPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Todo 4: masukan fungsi cetak PDF
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tanggal, tv_paket, tv_note, tv_harga;
        private Button btnCancel;
        private ImageView ivPdf;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_paket = itemView.findViewById(R.id.tv_paket);
            tv_note = itemView.findViewById(R.id.tv_note);
            tv_harga = itemView.findViewById(R.id.tv_harga);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            ivPdf = itemView.findViewById(R.id.ivPdf);
        }
    }

    public void cancelReservation( int id) {

        final StringRequest stringRequest = new StringRequest(DELETE, ReservationApi.DELETE_URL + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ReservationResponse reservationResponse =
                                gson.fromJson(response, ReservationResponse.class);

                        Toast.makeText(context.getApplicationContext(), reservationResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String responseBody =
                            new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(context.getApplicationContext(), errors.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(context.getApplicationContext(), e.getMessage(),
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
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
}

//Todo 13: harus buat seperti addedit activity di modul volley?
//Todo 14: harus buat seperti main activity di modul volley?