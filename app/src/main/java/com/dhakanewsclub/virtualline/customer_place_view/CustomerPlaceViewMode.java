package com.dhakanewsclub.virtualline.customer_place_view;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;
import com.dhakanewsclub.virtualline.place_line.PlaceLineRepository;

import java.util.List;

class CustomerPlaceViewMode extends ViewModel {
    private PlaceLineRepository mPlaceLineRepository;
    private CustomerPlaceRepository mCustomerPlaceRepository;
    private MutableLiveData<List<PlaceInfo>> mListMutableLiveData;
    private MutableLiveData<Boolean> uncheckedUserStatus;
    private MutableLiveData<Boolean> checkedUserStatus;


    public CustomerPlaceViewMode(Application application, String param) {
    }

    public void initializePlaceInfo(Context context, PlaceInfo placeInfo){
        if(mPlaceLineRepository==null){
            mPlaceLineRepository=PlaceLineRepository.getInstance();
        }
        mListMutableLiveData=mPlaceLineRepository.getPlaceById(context,placeInfo);

    }
    public void uncheckedUser(Context context, CheckedUser checkedUser){
        uncheckedUserStatus=mPlaceLineRepository.removeUserFromLine(context,checkedUser);
    }

    public void checkedUser(Context context, PlaceLine placeLine){
        if(mCustomerPlaceRepository==null){
            mCustomerPlaceRepository=CustomerPlaceRepository.getInstance();
        }
        checkedUserStatus=mCustomerPlaceRepository.checkedPlace(context,placeLine);
    }

    public MutableLiveData<List<PlaceInfo>> getListMutableLiveData() {
        return mListMutableLiveData;
    }

    public MutableLiveData<Boolean> getUncheckedUserStatus() {
        return uncheckedUserStatus;
    }

    public MutableLiveData<Boolean> getCheckedUserStatus() {
        return checkedUserStatus;
    }
}
