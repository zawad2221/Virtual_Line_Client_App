package com.dhakanewsclub.virtualline.home;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;

import java.util.List;

class HomeViewMode extends ViewModel {
    private HomeRepository mHomeRepository;
    private MutableLiveData<List<PlaceInfo>> allPlace;

    public HomeViewMode(Application application, String param) {
    }

    public void initAllPlace(Context context){
        if(mHomeRepository==null){
            mHomeRepository=HomeRepository.getInstance();
        }
        allPlace=mHomeRepository.getAllPlace(context);
    }

    public MutableLiveData<List<PlaceInfo>> getAllPlace() {
        return allPlace;
    }
}
