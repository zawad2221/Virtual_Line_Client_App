package com.dhakanewsclub.virtualline.qr_code_scanner;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.place_line.PlaceLineRepository;

class ScannerViewModel extends ViewModel {
    private MutableLiveData<Boolean> userDeleteResponse;
    private PlaceLineRepository mPlaceLineRepository;

    public ScannerViewModel(Application application, String param) {
    }

    public void removeUserFromLine(Context context, CheckedUser checkedUser){
        if(mPlaceLineRepository==null){
            mPlaceLineRepository= PlaceLineRepository.getInstance();
        }
        userDeleteResponse=mPlaceLineRepository.removeUserFromLine(context,checkedUser);

    }

    public MutableLiveData<Boolean> getUserDeleteResponse() {
        return userDeleteResponse;
    }
}
