package com.dhakanewsclub.virtualline.registration;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.dhakanewsclub.virtualline.login.LoginRepository;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;

public class RegistrationViewModel extends ViewModel {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";

    public void registration(Context applicationContext, String name, String phoneNumber, String password){
        Log.d(DIBAGING_TAG,"reg view model regi method");
        //User loginUserData= new User(phoneNumber,password);
        UserInfo registrationData= new UserInfo(name,phoneNumber,password);

        RegistrationRepository registrationRepository=RegistrationRepository.getInstance();
        registrationRepository.registration(applicationContext,registrationData);

    }
}
