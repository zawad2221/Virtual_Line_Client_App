package com.dhakanewsclub.virtualline.my_place_list;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;
import com.dhakanewsclub.virtualline.place_line.PlaceLineRepository;

import java.util.List;

public class PlaceListViewModel extends ViewModel {
    public MutableLiveData<List<PlaceInfo>> ownerPlace;
    private PlaceLIstRepository mPlaceLIstRepository;


    public void init(Context context, UserInfo ownerInfo){
        mPlaceLIstRepository=PlaceLIstRepository.getInstance();
        if(ownerInfo==null){
            ownerPlace=new MutableLiveData<>();
        }

        ownerPlace=mPlaceLIstRepository.getPlaceByOwner(context,ownerInfo);

    }






    public MutableLiveData<List<PlaceInfo>> getOwnerPlace() {
        return ownerPlace;
    }
}
