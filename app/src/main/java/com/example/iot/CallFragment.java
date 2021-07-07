package com.example.iot;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CallFragment extends Fragment {

    Context context;
    public CallFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.callList);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference("Account").child(auth.getCurrentUser().getUid());

        //Read data kontak di firebase
        myRef.child("Kontak").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0){
                    List<Kontak> kontaks = new ArrayList<>();
                    for(int i = 0; i < snapshot.getChildrenCount(); i++){
                        kontaks.add(snapshot.child(String.valueOf(i)).getValue(Kontak.class));
                    }

                    CallAdapter callAdapter = new CallAdapter(context, kontaks);
                    recyclerView.setAdapter(callAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}