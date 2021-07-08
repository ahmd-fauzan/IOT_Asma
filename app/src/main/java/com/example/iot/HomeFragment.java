package com.example.iot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    String id;
    public HomeFragment(String id) {
        // Required empty public constructor
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView txtDetak = view.findViewById(R.id.txtDetakJantung);
        TextView txtKbb = view.findViewById(R.id.txtKelembaban);
        TextView txtDebu = view.findViewById(R.id.txtDebu);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;

        myRef = database.getReference("Account").child(auth.getCurrentUser().getUid());

        //Read data history terbaru dari firebase
        myRef.child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    long size = snapshot.getChildrenCount();

                    if(size > 0){
                        History history = snapshot.child(String.valueOf(size - 1)).getValue(History.class);

                        DatabaseReference dataRef = database.getReference("Asma");
                        dataRef.child("Kadar Debu").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(int i = 0; i < snapshot.getChildrenCount(); i++){
                                    Map<String, Long> min = new HashMap<>();
                                    Map<String, String> kondisi = new HashMap<>();

                                    min = (HashMap<String, Long>) snapshot.child(String.valueOf(i)).getValue();
                                    kondisi = (HashMap<String, String>) snapshot.child(String.valueOf(i)).getValue();

                                    if(Math.round(history.getDebu()) > min.get("Min Kadar").intValue() && Math.round(history.getDebu()) < min.get("Max Kadar").intValue()){
                                        Log.d("STATE", "Kondisi Debu : " + kondisi.get("Kondisi"));
                                    }
                                }

                                dataRef.child("Kelembaban Ruangan").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(int i = 0; i < snapshot.getChildrenCount(); i++){
                                            Map<String, Long> kadar = (HashMap<String, Long>) snapshot.child(String.valueOf(i)).getValue();
                                            Map<String, String> kondisi = (HashMap<String, String>) snapshot.child(String.valueOf(i)).getValue();

                                            if(Math.round(history.getKbb()) > kadar.get("Min Kadar").intValue() && Math.round(history.getKbb()) < kadar.get("Max Kadar").intValue()){
                                                Log.d("STATE", "Kondisi Kelembaban : " + kondisi.get("Kondisi"));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        txtDetak.setText(history.getDetak() +"");
                        txtKbb.setText(history.getKbb() + "");
                        txtDebu.setText((int)history.getDebu() + "");
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TextView username = view.findViewById(R.id.username);
        username.setText(id);
        return view;
    }
}