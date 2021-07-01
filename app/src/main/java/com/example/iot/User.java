package com.example.iot;

public class User {
    private String username;
    private String dateOfBirth;

    public User(String username, String dateOfBirth){
        this.username = username;
        this.dateOfBirth = dateOfBirth;
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
}
