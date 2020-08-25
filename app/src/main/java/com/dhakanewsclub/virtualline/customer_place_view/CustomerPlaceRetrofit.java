package com.dhakanewsclub.virtualline.customer_place_view;

import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface CustomerPlaceRetrofit {
    @POST("add_user_in_line/")
    Call<PlaceLine> checkInLine(@Body PlaceLine placeLine);

}
