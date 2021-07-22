package com.example.iot.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iot.models.Debu;
import com.example.iot.models.DetakJantung;
import com.example.iot.models.History;
import com.example.iot.models.Kelembaban;
import com.example.iot.models.Kontak;
import com.example.iot.models.User;
import com.example.iot.repository.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AsmaViewModel extends ViewModel {
    private User user = new User();

    private List<DetakJantung> detakJantungs = new ArrayList<>();
    private List<Kelembaban> kelembabans = new ArrayList<>();
    private List<Debu> debus = new ArrayList<>();

    private MutableLiveData<List<History>> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> mutableUser = new MutableLiveData<>();
    private MutableLiveData<List<Kontak>> mutableKontak = new MutableLiveData<>();

    private MutableLiveData<List<DetakJantung>> mutableDetak = new MutableLiveData<>();
    private MutableLiveData<List<Kelembaban>> mutableKelembaban = new MutableLiveData<>();
    private MutableLiveData<List<Debu>> mutableDebu = new MutableLiveData<>();

    private DatabaseReference accountRef;
    private DatabaseReference asmaRef;

    public AsmaViewModel(){
        FirebaseHelper helper = FirebaseHelper.getInstance();
        accountRef = helper.getAccountRef();
        asmaRef = helper.getAsmaRef();

        readDetak();
        readKelembaban();
        readKadarDebu();
    }

    public void readHistory(){
        List<History> historyList = new ArrayList<>();
        accountRef.child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    History history = snapshot.child(String.valueOf(i)).getValue(History.class);

                    historyList.add(history);
                }
                mutableLiveData.setValue(historyList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readUser(){
        accountRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                mutableUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readKontak(){
        List<Kontak> kontakList = new ArrayList<>();
        accountRef.child("Kontak").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    kontakList.add(snapshot.child(String.valueOf(i)).getValue(Kontak.class));
                }
                mutableKontak.setValue(kontakList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readKadarDebu(){
        asmaRef.child("Kadar Debu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull   DataSnapshot snapshot) {
                for (int i = 0; i < snapshot.getChildrenCount(); i++){
                    debus.add(snapshot.child(String.valueOf(i)).getValue(Debu.class));
                }
                mutableDebu.setValue(debus);
            }

            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
    }

    public void readDetak(){
        asmaRef.child("Detak Jantung").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull   DataSnapshot snapshot) {
                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    detakJantungs.add(snapshot.child(String.valueOf(i)).getValue(DetakJantung.class));
                }
                mutableDetak.setValue(detakJantungs);
            }

            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
    }

    public void readKelembaban(){
        asmaRef.child("Kelembaban Ruangan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull   DataSnapshot snapshot) {
                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                    kelembabans.add(snapshot.child(String.valueOf(i)).getValue(Kelembaban.class));
                }
                mutableKelembaban.setValue(kelembabans);
            }

            @Override
            public void onCancelled(@NonNull   DatabaseError error) {

            }
        });
    }

    public LiveData<List<History>> getHistory(){
        return mutableLiveData;
    }

    public LiveData<User> getUser(){
        return mutableUser;
    }

    public LiveData<List<Kontak>> getKontak(){
        return mutableKontak;
    }

    public LiveData<List<DetakJantung>> getDetak(){
        return mutableDetak;
    }

    public LiveData<List<Debu>> getDebu(){
        return mutableDebu;
    }

    public LiveData<List<Kelembaban>> getKelembaban(){
        return mutableKelembaban;
    }
}
