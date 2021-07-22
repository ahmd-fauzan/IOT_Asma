package com.example.iot.network;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseClient {
    private static FirebaseDatabase instance;

    public static FirebaseDatabase getFirebase(){
        if (instance == null){
            instance = FirebaseDatabase.getInstance();
        }

        return instance;
    }
}
