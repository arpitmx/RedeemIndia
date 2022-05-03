package com.redeemindia.in.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    int count;
    MainViewModelFactory(int i){
        count = i;
    }

//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass == ViewModel_MainActivity.class) {
//            return (T) new ViewModel_MainActivity(count);
//        }
//        return null;
//    }
}

//        viewModel = new ViewModelProvider(this,new MainViewModelFactory(5)).get(ViewModel_MainActivity.class);
