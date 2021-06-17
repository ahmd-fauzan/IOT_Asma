package com.example.iot;

public class Kontak {
    private String hp;
    private String rs;

    public Kontak(String hp, String rs){
        this.hp = hp;
        this.rs = rs;
    }
    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }
}
