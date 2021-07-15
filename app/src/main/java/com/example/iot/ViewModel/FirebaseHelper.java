package com.example.iot.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.iot.Model.Debu;
import com.example.iot.Model.DetakJantung;
import com.example.iot.Model.History;
import com.example.iot.Model.Kelembaban;
import com.example.iot.Model.Kontak;
import com.example.iot.Model.User;
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

    public static FirebaseHelper getInstance(){
        if(instance == null)
            instance = new FirebaseHelper();

        return instance;
    }

    public void initializeAuth(){
        this.accountRef = database.getReference("Account").child(auth.getCurrentUser().getUid());
        this.asmaRef = database.getReference("Asma");
    }

    public void readHistory(DataListener dataListener){
        historyList = new ArrayList<>();
        accountRef.child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FIREBASE", "Data Changed");

                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    dataListener.onProcess();
                    History history = snapshot.child(String.valueOf(i)).getValue(History.class);

                    historyList.add(history);
                }
                dataListener.onCompleteListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readUser(DataListener dataListener){
        accountRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataListener.onProcess();
                user = snapshot.getValue(User.class);
                dataListener.onCompleteListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readKontak(DataListener dataListener){
        kontakList = new ArrayList<>();
        accountRef.child("Kontak").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    dataListener.onProcess();
                    kontakList.add(snapshot.child(String.valueOf(i)).getValue(Kontak.class));
                }
                dataListener.onCompleteListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readLatestHistory(DataListener dataListener){
        accountRef.child("History").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull   DataSnapshot snapshot) {
                dataListener.onProcess();
                long size = snapshot.getChildrenCount();
                history = snapshot.child(String.valueOf(size - 1)).getValue(History.class);
                dataListener.onCompleteListener();
            }

            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
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
    public List<History> getHistory(){
        return historyList;
    }

    public User getUser(){
        return user;
    }

    public List<Kontak> getKontak(){
        return kontakList;
    }

    public History getLatestHistory(){
        return history;
    }

    public FirebaseUser getAccountUser(){
        return  auth.getCurrentUser();
    }

    public void logOut(){
        auth.signOut();
    }

    public void readKadarDebu(DataListener dataListener){
        asmaRef.child("Kadar Debu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull   DataSnapshot snapshot) {
                for (int i = 0; i < snapshot.getChildrenCount(); i++){
                    debus.add(snapshot.child(String.valueOf(i)).getValue(Debu.class));
                }
                dataListener.onCompleteListener();
            }

            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
    }

    public void readDetak(DataListener dataListener){
        asmaRef.child("Detak Jantung").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull   DataSnapshot snapshot) {
                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    detakJantungs.add(snapshot.child(String.valueOf(i)).getValue(DetakJantung.class));
                }
                dataListener.onCompleteListener();
            }

            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
    }

    public void readKelembaban(DataListener dataListener){
        asmaRef.child("Kelembaban Ruangan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull   DataSnapshot snapshot) {
                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    kelembabans.add(snapshot.child(String.valueOf(i)).getValue(Kelembaban.class));
                }
                dataListener.onCompleteListener();
            }

            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
    }

    public List<Debu> getDebus(){
        return debus;
    }

    public List<DetakJantung> getDetakJantungs(){
        return detakJantungs;
    }

    public List<Kelembaban> getKelembabans(){
        return kelembabans;
    }
}
