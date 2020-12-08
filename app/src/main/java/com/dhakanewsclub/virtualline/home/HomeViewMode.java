package com.dhakanewsclub.virtualline.home;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.add_place.AddPlaceInformationRepository;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceType;

import java.util.List;

class HomeViewMode extends ViewModel {
    private HomeRepository mHomeRepository;
    private MutableLiveData<List<PlaceInfo>> allPlace;

    private AddPlaceInformationRepository mAddPlaceInformationRepository;
    private MutableLiveData<List<PlaceType>> mListMutableLiveDataPlaceType;

    public HomeViewMode(Application application, String param) {
    }

    public void initAllPlace(Context context){
        if(mHomeRepository==null){
            mHomeRepository=HomeRepository.getInstance();
        }
        allPlace=mHomeRepository.getAllPlace(context);
    }


    public void initPlaceType(Context context){
        if(mAddPlaceInformationRepository==null){
            mAddPlaceInformationRepository=AddPlaceInformationRepository.getInstance();
        }
        if (mListMutableLiveDataPlaceType==null){
            mListMutableLiveDataPlaceType=new MutableLiveData<>();
        }
        mListMutableLiveDataPlaceType=mAddPlaceInformationRepository.getPlaceType(context);
    }


    public MutableLiveData<List<PlaceType>> getListMutableLiveDataPlaceType() {
        return mListMutableLiveDataPlaceType;
    }

    public MutableLiveData<List<PlaceInfo>> getAllPlace() {
        return allPlace;
    }
}
