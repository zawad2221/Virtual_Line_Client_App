package com.dhakanewsclub.virtualline.login;

import com.dhakanewsclub.virtualline.models.User;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginRetrofit {
    @POST("login/")
    Call<UserInfo> createAccount(@Body UserInfo user);

}
