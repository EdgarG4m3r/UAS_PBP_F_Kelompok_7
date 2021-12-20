package com.example.uts.UnitTesting;

import com.example.uts.model.Reservation;

public interface ReservationCallback {
    void onSuccess(boolean value, Reservation reservation);
    void onError();
}
