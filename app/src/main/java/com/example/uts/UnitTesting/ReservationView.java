package com.example.uts.UnitTesting;

public interface ReservationView {
    String getPaket();

    String getTanggal();
    void showTanggalError(String message);

    String getNote();
    void showNoteError(String message);

    void startMainReservation();

    void showReservationError(String message);
    void showErrorResponse(String message);
}
