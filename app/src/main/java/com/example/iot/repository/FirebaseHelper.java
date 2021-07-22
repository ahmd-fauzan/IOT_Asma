package com.example.iot.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.iot.models.Debu;
import com.example.iot.models.DetakJantung;
import com.example.iot.models.History;
import com.example.iot.models.Kelembaban;
import com.example.iot.models.Kontak;
import com.example.iot.models.User;
import com.example.iot.viewmodels.DataListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    static FirebaseHelper instance;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference asmaRef;
    DatabaseReference accountRef;

    List<History> historyList;
    User user;
    History history;
    List<Kontak> kontakList;

    List<DetakJantung> detakJantungs;
    List<Debu> debus;
    List<Kelembaban> kelembabans;


    public FirebaseHelper(){
        this.database = FirebaseDatabase.getInstance();
        this.auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            this.accountRef = database.getReference("Account").child(auth.getCurrentUser().getUid());
            this.asmaRef = database.getReference("Asma");
        }

        this.user = new User();
        this.history = new History();
        detakJantungs = new ArrayList<>();
        debus = new ArrayList<>();
        kelembabans = new ArrayList<>();
    }

    public DatabaseReference getAccountRef(){
        return accountRef;
    }

    public DatabaseReference getAsmaRef(){
        return asmaRef;
    }

    public static FirebaseHelper getInstance(){
        if(instance == null)
            instance = new FirebaseHelper();

        return instance;
    }

    public void initializeAuth(){
        this.accountRef = database.getReference("Account").child(auth.getCurrentUser().getUid());
        this.asmaRef = database.getReference("Asma");
    }

    public FirebaseUser getAccountUser(){
        return auth.getCurrentUser();
    }

    public void logOut(){
        auth.signOut();
    }

    public void insertHistory(History history, DataListener dataListener){
        accountRef.child("History").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long size = snapshot.getChildrenCount();
                accountRef.child(auth.getCurrentUser().getUid()).child("History").child(String.valueOf(size)).setValue(history);
                dataListener.onCompleteListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void insertUser(User user, DataListener dataListener){
        accountRef.child("User").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull   Task<Void> task) {
                if(task.isSuccessful())
                    dataListener.onCompleteListener();
            }
        });
    }

    public void insertKontak(List<Kontak> kontaks, DataListener dataListener){
        accountRef.child("Kontak").setValue(kontaks).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull   Task<Void> task) {
                if(task.isSuccessful()){
                    dataListener.onCompleteListener();
                }
            }
        });
    }
    public void loginAccount(String email, String password, DataListener dataListener){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull   Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dataListener.onCompleteListener();
                }
            }
        });
    }

    public void registerAccount(String email, String password, DataListener dataListener){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull   Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dataListener.onCompleteListener();
                }
            }
        });
    }
}
