package com.example.iot;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iot.Model.Kontak;
import com.example.iot.ViewModel.DataListener;
import com.example.iot.ViewModel.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CallFragment extends Fragment {

    Context context;
    FirebaseHelper helper;

    public CallFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.callList);

        helper = FirebaseHelper.getInstance();

        helper.readKontak(new DataListener() {
            @Override
            public void onCompleteListener() {
                List<Kontak> kontakList = new ArrayList<>();
                kontakList = helper.getKontak();

                CallAdapter callAdapter = new CallAdapter(context, kontakList);
                recyclerView.setAdapter(callAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        });

        return view;
    }
}