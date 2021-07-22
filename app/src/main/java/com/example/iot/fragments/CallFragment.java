package com.example.iot.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iot.R;
import com.example.iot.adapters.Adapter;
import com.example.iot.adapters.CallAdapter;
import com.example.iot.models.Kontak;
import com.example.iot.viewmodels.AsmaViewModel;
import com.example.iot.viewmodels.AsmaViewModelFactory;
import com.example.iot.viewmodels.DataListener;
import com.example.iot.repository.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CallFragment extends Fragment {

    Context context;
    FirebaseHelper helper;
    CallAdapter adapter;

    public CallFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.callList);

        AsmaViewModelFactory factory = new AsmaViewModelFactory();
        AsmaViewModel viewModel = new ViewModelProvider(getActivity(), factory).get(AsmaViewModel.class);

        viewModel.getKontak().observe(getActivity(), new Observer<List<Kontak>>() {
            @Override
            public void onChanged(List<Kontak> kontaks) {
                adapter = new CallAdapter(context, kontaks);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        });

        setupRecycleview(view);

        return view;
    }

    private void setupRecycleview(View view){


    }
}