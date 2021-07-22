package com.example.iot.models;

public class DetakJantung {
    private int minUmur;
    private int maxUmur;
    private int minDetak;
    private int maxDetak;

    public DetakJantung(){}

    public int getMinUmur() {
        return minUmur;
    }

    public void setMinUmur(int minUmur) {
        this.minUmur = minUmur;
    }

    public int getMaxUmur() {
        return maxUmur;
    }

    public void setMaxUmur(int maxUmur) {
        this.maxUmur = maxUmur;
    }

    public int getMinDetak() {
        return minDetak;
    }

    public void setMinDetak(int minDetak) {
        this.minDetak = minDetak;
    }

    public int getMaxDetak() {
        return maxDetak;
    }

    public void setMaxDetak(int maxDetak) {
        this.maxDetak = maxDetak;
    }
}
