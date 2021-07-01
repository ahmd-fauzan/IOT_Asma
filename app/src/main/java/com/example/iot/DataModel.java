package com.example.iot;

import com.google.firebase.auth.FirebaseAuth;

public class DataModel {
    private static FirebaseAuth auth;

    public static FirebaseAuth getAuth(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }
}
