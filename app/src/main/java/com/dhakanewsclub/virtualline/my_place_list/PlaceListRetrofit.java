package com.dhakanewsclub.virtualline.my_place_list;

import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PlaceListRetrofit {

    @POST("get_place_by_owner/")
    Call<List<PlaceInfo>> getPlaceByOwner(@Body UserInfo owner);
}
