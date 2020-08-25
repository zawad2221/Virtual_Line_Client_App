package com.dhakanewsclub.virtualline.customer_place_view;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;




public class CustomerPlaceViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public CustomerPlaceViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new CustomerPlaceViewMode(mApplication, mParam);
    }
}
