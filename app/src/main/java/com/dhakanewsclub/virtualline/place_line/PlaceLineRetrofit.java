package com.dhakanewsclub.virtualline.place_line;

import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PlaceLineRetrofit {
    @POST("get_place_by_id/")
    Call<PlaceInfo> getPlaceById(@Body PlaceInfo placeInfo);
    @POST("change_line_status/")
    Call<PlaceLine> changeLineStatus(@Body PlaceLine placeLine);

    @POST("remove_user_from_line/")
    Call<CheckedUser> removeUserFromLine(@Body CheckedUser checkedUser);
}
