package com.dhakanewsclub.virtualline.registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.login.LoginActivity;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationRepository {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";
    private static RegistrationRepository instance;
    public static RegistrationRepository getInstance(){
        if(instance==null){
            instance = new RegistrationRepository();

        }
        return instance;
    }

    public void startLoginActivity(Context applicationContext){
        Intent login = new Intent(applicationContext, LoginActivity.class);
//                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        applicationContext.startActivity(login);
    }

    public ProgressDialog showDialog(Context applicationContext){
        final ProgressDialog progressDialog = new ProgressDialog(applicationContext,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account...");
        progressDialog.show();
        return progressDialog;
    }

    public void registration(Context applicationContext, UserInfo user){
        final ProgressDialog progressDialog=showDialog(applicationContext);
        Log.d(DIBAGING_TAG,"reg reppo start");
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/system_user/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        RegistrationRetrofit registrationRetrofit=retrofit.create(RegistrationRetrofit.class);
        Call<UserInfo> call = registrationRetrofit.createAccount(user);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                progressDialog.dismiss();
                if(response.code()==400){
                    Toast failToast=Toast.makeText(applicationContext,"phone number already used",Toast.LENGTH_LONG);

                    failToast.show();
                    return;
                }
                else if(new Gson().toJson(response.body()).equals("null")){
                    Log.d(DIBAGING_TAG,"reg repo on response code: "+response.code()+response.message());
                    Toast failToast=Toast.makeText(applicationContext,"registration failed",Toast.LENGTH_LONG);

                    failToast.show();
                    return;
                }




//                Log.d(DIBAGING_TAG,"reg repo on response");
//                Log.d(DIBAGING_TAG,"registration success"+response.body().getUserId());
//                Log.d(DIBAGING_TAG,"registration success"+new Gson().toJson(response.body()));

                if(user.getPhoneNumber().equals(response.body().getPassword())){
                    Toast successToast=Toast.makeText(applicationContext,"registration success",Toast.LENGTH_LONG);
                    successToast.show();
                    startLoginActivity(applicationContext);
                }

//                Log.d(DIBAGING_TAG,"login success"+response.body().getUserId());
//                Log.d(DIBAGING_TAG,"login success"+response.body().getUserId());
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(applicationContext,"registration failed",Toast.LENGTH_LONG).show();
                Log.d(DIBAGING_TAG,"registration failed"+t.getMessage());
                t.getMessage();
            }
        });

    }
}
