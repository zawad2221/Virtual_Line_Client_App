package com.dhakanewsclub.virtualline.add_place;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceType;

import java.util.List;

public class AddPlaceViewModel extends ViewModel {


    private MutableLiveData<List<PlaceType>> mPlaceTypeLiveData;
    AddPlaceInformationRepository addPlaceInformationRepository;

    public void init(Context context){
        addPlaceInformationRepository=AddPlaceInformationRepository.getInstance();
        if(mPlaceTypeLiveData!=null){
            return;
        }
        mPlaceTypeLiveData=addPlaceInformationRepository.getPlaceType(context);

    }
    public MutableLiveData<List<PlaceType>> getPlaceTypeLiveData() {
        return mPlaceTypeLiveData;
    }

    public void addPlace(Context context, PlaceInfo placeInfo){
        addPlaceInformationRepository.addNewPlace(context,placeInfo);
    }
}
