package com.example.uts.UnitTesting;

import com.example.uts.model.Reservation;

public class ReservationPresenter {
    private ReservationView view;
    private ReservationService service;
    private ReservationCallback callback;
    private Reservation reservationl;
    public ReservationPresenter(ReservationView view, ReservationService service) {
        this.view = view; this.service = service;
    }
}
