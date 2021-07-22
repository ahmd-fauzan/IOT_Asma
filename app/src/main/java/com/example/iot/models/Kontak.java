package com.example.iot.models;

public class Kontak {
    private String name;
    private String telp;

    public Kontak(){}

    public Kontak(String name, String telp){
        this.setName(name);
        this.setTelp(telp);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }
}
