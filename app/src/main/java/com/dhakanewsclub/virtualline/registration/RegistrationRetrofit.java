package com.dhakanewsclub.virtualline.registration;

import com.dhakanewsclub.virtualline.models.User;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistrationRetrofit {
    @POST("registration/")
    Call<UserInfo> createAccount(@Body UserInfo user);

}
