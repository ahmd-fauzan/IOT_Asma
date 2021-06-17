package com.example.iot;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView list;

    Adapter adapter;
    Context context;

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

        adapter = new Adapter(context, CreateData());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        list.setAdapter(adapter);
        list.setLayoutManager(gridLayoutManager);

        // Inflate the layout for this fragment
        return view;
    }

    private List<History> CreateData(){
        List<History> listData = new ArrayList<>();
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        listData.add(new History("22/12/34", 66, 23,0.7f));
        return listData;
    }
}