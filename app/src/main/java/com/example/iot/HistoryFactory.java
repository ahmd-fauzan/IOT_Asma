package com.example.iot;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedInputStream;

public class HistoryFactory {
    private  List<History> historyList = new ArrayList<>();

    private static HistoryFactory instance;

    public List<History> getHistories(){
        return historyList;
    }

    public void addHistory(History history){
        historyList.add(history);
    }

    public static HistoryFactory getInstance(){
        if(instance == null){
            instance = new HistoryFactory();
        }

        return instance;
    }
}
