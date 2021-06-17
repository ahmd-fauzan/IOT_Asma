package com.example.iot;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        CallAdapter callAdapter = new CallAdapter(context, CreateKontak());
        recyclerView.setAdapter(callAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    private List<Kontak> CreateKontak(){
        List<Kontak> kontaks = new ArrayList<>();
        kontaks.add(new Kontak("0832-4398-4938", "Rumah Sakit Hasanah"));
        kontaks.add(new Kontak("0848-4938-4378", "Rumah Sakit Hasanudin"));
        kontaks.add(new Kontak("0298-3493-4398", "Rumah Sakit Rancamaya"));
        return kontaks;
    }
}