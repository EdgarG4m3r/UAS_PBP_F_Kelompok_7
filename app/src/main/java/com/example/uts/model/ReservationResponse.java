package com.example.uts.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReservationResponse
{
    private String message;

    @SerializedName("reservation")
    private List<Reservation> reservationList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
}
