package com.dhakanewsclub.virtualline.qr_code_scanner;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;




public class ScannerViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public ScannerViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ScannerViewModel(mApplication, mParam);
    }
}

