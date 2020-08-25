package com.dhakanewsclub.virtualline.login;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.models.User;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;

public class LoginViewModel extends ViewModel {
    public void login(Context applicationContext, String phoneNumber, String password){
        //User loginUserData= new User(phoneNumber,password);
        UserInfo loginUserData= new UserInfo(phoneNumber,password);

        LoginRepository loginRepository=LoginRepository.getInstance();
        loginRepository.login(applicationContext,loginUserData);

    }
}
