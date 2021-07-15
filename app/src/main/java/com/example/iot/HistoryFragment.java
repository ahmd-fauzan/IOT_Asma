package com.example.iot;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iot.Model.History;
import com.example.iot.ViewModel.DataListener;
import com.example.iot.ViewModel.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView list;

    Adapter adapter;
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
        list = view.findViewById(R.id.list);

        helper = FirebaseHelper.getInstance();

        helper.readHistory(new DataListener() {
            @Override
            public void onCompleteListener() {
                List<History> historyList = new ArrayList<>();
                historyList = helper.getHistory();

                adapter = new Adapter(context, historyList);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                list.setAdapter(adapter);
                list.setLayoutManager(gridLayoutManager);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}