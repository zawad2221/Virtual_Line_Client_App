package com.dhakanewsclub.virtualline.add_place;

import androidx.lifecycle.LiveData;

import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceType;
import com.dhakanewsclub.virtualline.models.retrofit.UserType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
interface AddPlaceRetrofit {

    @GET("get_place_type/")
    Call<List<PlaceType>> getPlaceType();
    @POST("add_place/")
    Call<PlaceInfo> addPlace(@Body PlaceInfo placeInfo);

}
