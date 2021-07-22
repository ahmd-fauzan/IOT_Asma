package com.example.iot.models;

public class Kelembaban {
    private int minKadar;
    private int maxKadar;
    private String kondisi;

    public Kelembaban(){}

    public int getMinKadar() {
        return minKadar;
    }

    public void setMinKadar(int minKadar) {
        this.minKadar = minKadar;
    }

    public int getMaxKadar() {
        return maxKadar;
    }

    public void setMaxKadar(int maxKadar) {
        this.maxKadar = maxKadar;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }
}
