package com.dhakanewsclub.virtualline.my_place_list;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dhakanewsclub.virtualline.login.LoginUserData;
import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;
import com.dhakanewsclub.virtualline.place_line.PlaceLineRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceLIstRepository {

    final String DIBAGING_TAG="DIBAGING_TAG";

    private static PlaceLIstRepository instance;


    public static PlaceLIstRepository getInstance() {
        if (instance == null) {
            instance = new PlaceLIstRepository();

        }
        return instance;
    }

    public MutableLiveData<List<PlaceInfo>> getPlaceByOwner(Context applicationContext, UserInfo owner){
        if(owner==null){
            owner= LoginUserData.loginUserInfo;
            Log.d(DIBAGING_TAG,"owner was null"+LoginUserData.loginUserInfo.getName());
        }
        MutableLiveData<List<PlaceInfo>> mutableLiveDataPlaceList= new MutableLiveData<>();
        Retrofit.Builder builder= new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/place/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        PlaceListRetrofit placeListRetrofit=retrofit.create(PlaceListRetrofit.class);
        Call<List<PlaceInfo>> call=placeListRetrofit.getPlaceByOwner(owner);
        call.enqueue(new Callback<List<PlaceInfo>>() {
            @Override
            public void onResponse(Call<List<PlaceInfo>> call, Response<List<PlaceInfo>> response) {
                if(response.isSuccessful()){
                    mutableLiveDataPlaceList.setValue(response.body());
                    Log.d(DIBAGING_TAG,"data fetch owner place");
                    Log.d(DIBAGING_TAG,"place list last data: "+response.body().get(response.body().size()-1).getPlaceName());
                }
                else {
                    Log.d(DIBAGING_TAG, "data fetch not success owner place " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<PlaceInfo>> call, Throwable t) {
                Log.d(DIBAGING_TAG,"failed to load owner place: "+t.getMessage());
                Toast.makeText(applicationContext,"failed to load data",Toast.LENGTH_LONG).show();
            }
        });

        return mutableLiveDataPlaceList;

    }




}
