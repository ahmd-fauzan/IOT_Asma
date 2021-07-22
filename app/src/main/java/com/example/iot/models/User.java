package com.example.iot.models;

public class User{
    private String username;
    private String dateOfBirth;
    private String deviceName;

    public User(String username, String dateOfBirth, String deviceName){
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.deviceName = deviceName;
    }

    public User(){}
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
