package com.dhakanewsclub.virtualline.place_line;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.login.LoginUserData;
import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlaceLineViewModel extends ViewModel {
    public MutableLiveData<List<PlaceInfo>> mPlaceInfoMutableLiveData=new MutableLiveData<>();

//    private MutableLiveData<List<PlaceInfo>> mListMutableLiveDataPlaceInfo;

    private MutableLiveData<Boolean> userDeleteResponse=new MutableLiveData<>();
    private MutableLiveData<PlaceLine> lineStatusChangeResponse=new MutableLiveData<>();

    PlaceLineRepository mPlaceLineRepository;

    public void init(Context context,PlaceInfo placeInfo){
        if(mPlaceLineRepository==null){
            mPlaceLineRepository= PlaceLineRepository.getInstance();
        }
        //List<PlaceInfo> placeInfos=new ArrayList<>();
        //placeInfos.add(mPlaceLineRepository.getPlaceById(context,placeInfo).getValue());


        //mPlaceInfoMutableLiveData=mPlaceLineRepository.getPlaceById(context,placeInfo);
        mPlaceInfoMutableLiveData=mPlaceLineRepository.getPlaceById(context,placeInfo);


    }

    public MutableLiveData<List<PlaceInfo>> getPlaceInfoMutableLiveData() {
        return mPlaceInfoMutableLiveData;
    }

    public void removeUserFromLine(Context context, CheckedUser checkedUser){
        if(mPlaceLineRepository==null){
            mPlaceLineRepository= PlaceLineRepository.getInstance();
        }
        userDeleteResponse=mPlaceLineRepository.removeUserFromLine(context,checkedUser);

    }

    public void changeLineStatus(Context context, PlaceLine placeLine){
        if(mPlaceLineRepository==null){
            mPlaceLineRepository= PlaceLineRepository.getInstance();
        }
        lineStatusChangeResponse=mPlaceLineRepository.changeLineStatus(context,placeLine);
        //Log.d("DIBAGING_TAG","line statu in view model"+lineStatusChangeResponse.getValue().getLineStatus());
    }

    public MutableLiveData<Boolean> getUserDeleteResponse() {
        return userDeleteResponse;
    }

    public MutableLiveData<PlaceLine> getLineStatusChangeResponse() {
        return lineStatusChangeResponse;
    }

    public void setUserDeleteResponse(MutableLiveData<Boolean> userDeleteResponse) {
        this.userDeleteResponse = userDeleteResponse;
    }

    public void setLineStatusChangeResponse(MutableLiveData<PlaceLine> lineStatusChangeResponse) {
        this.lineStatusChangeResponse = lineStatusChangeResponse;
    }
}
