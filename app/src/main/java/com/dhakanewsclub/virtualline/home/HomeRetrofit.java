package com.dhakanewsclub.virtualline.home;

import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface HomeRetrofit {

    @GET("get_all_place/")
    Call<List<PlaceInfo>> getAllPlace();
}
