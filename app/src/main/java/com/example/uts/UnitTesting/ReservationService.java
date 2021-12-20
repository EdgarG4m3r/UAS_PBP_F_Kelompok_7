package com.example.uts.UnitTesting;

import com.example.uts.model.Reservation;
import com.example.uts.model.ReservationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationService {
    /*public void reservation(final ReservationView view, Reservation reservation, final ReservationCallback callback) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReservationResponse> profilDAOCall = apiService.saveReservation(reservation);
        reservationDAOCall.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response) {
                if (response.body().getMessage().equalsIgnoreCase("berhasil reservasi")) {
                    callback.onSuccess(true, response.body().getReservationList().get(0));
                } else {
                    callback.onError();
                    view.showReservationError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {
                view.showErrorResponse(t.getMessage());
                callback.onError();
            }
        });
    }

    public Boolean getValid(final ReservationView view, Reservation reservation) {
        final Boolean[] bool = new Boolean[1];
        reservation(view, reservation, new ReservationCallback() {
            @Override
            public void onSuccess(boolean value, Reservation reservation) {
                bool[0] = true;
            }

            @Override
            public void onError() {
                bool[0] = false;
            }
        });
        return bool[0];
    }*/
}
