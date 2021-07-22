package com.example.iot.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iot.R;
import com.example.iot.adapters.Adapter;
import com.example.iot.models.History;
import com.example.iot.viewmodels.AsmaViewModel;
import com.example.iot.viewmodels.AsmaViewModelFactory;
import com.example.iot.viewmodels.DataListener;
import com.example.iot.repository.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    Context context;

    FirebaseHelper helper;

    public HistoryFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        helper = FirebaseHelper.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.list);

        AsmaViewModelFactory factory = new AsmaViewModelFactory();
        AsmaViewModel viewModel = new ViewModelProvider(getActivity(), factory).get(AsmaViewModel.class);

        DatabaseReference reference = helper.getAccountRef();
        reference.child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewModel.readHistory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewModel.getHistory().observe(getActivity(), new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                Adapter adapter = new Adapter(context, histories);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}