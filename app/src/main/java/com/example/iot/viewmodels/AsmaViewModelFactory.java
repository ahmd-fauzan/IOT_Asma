package com.example.iot.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AsmaViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(AsmaViewModel.class)){
            return (T) new AsmaViewModel();
        }

        throw new IllegalArgumentException("ViewModel yang diminta AsmaViewModel");
    }
}
