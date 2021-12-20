package com.example.uts.model;

public class User {
    private int id;

    private String email;

    private String password;

    private String name;

    private String noTelp;

    private String foto;

    public User(String name, String email, String password, String noTelp, String foto) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.noTelp = noTelp;
        this.foto = foto;
    }

    public User(String name, String email, String noTelp, String foto) {
        this.email = email;
        this.name = name;
        this.noTelp = noTelp;
        this.foto = foto;
    }

    public User(int id,String name, String email, String password, String noTelp, String foto) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.noTelp = noTelp;
        this.foto = foto;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(){
        //nothing
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFoto() { return foto; }

    public void setFoto(String foto) { this.foto = foto; }
}
