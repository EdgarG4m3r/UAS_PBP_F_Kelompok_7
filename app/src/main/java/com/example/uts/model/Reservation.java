package com.example.uts.model;

public class Reservation
{
    private String paket;

    private String tanggal;

    private String note;

    private double harga;

    private int user_id;

    int id;

    public Reservation(String paket, String tanggal, String note, double harga, int user_id)
    {
        this.paket = paket;
        this.tanggal = tanggal;
        this.note = note;
        this.harga = harga;
        this.user_id = user_id;
    }

    public Reservation() {}

    public int getId() {
        return id;
    }

    public String getPaket() {
        return paket;
    }

    public void setPaket(String paket) {
        this.paket = paket;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
