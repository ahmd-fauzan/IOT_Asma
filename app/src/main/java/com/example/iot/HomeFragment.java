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
import java.util.List;

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