package com.dhakanewsclub.virtualline.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class HomeRepository {
    final String DIBAGING_TAG="DIBAGING_TAG";

    public static HomeRepository instance;

    public static HomeRepository getInstance(){
        if (instance==null){
            instance=new HomeRepository();
        }
        return instance;
    }

    MutableLiveData<List<PlaceInfo>> getAllPlace(Context context){
        MutableLiveData<List<PlaceInfo>> placeList=new MutableLiveData<>();
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/place/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        HomeRetrofit homeRetrofit=retrofit.create(HomeRetrofit.class);
        Call<List<PlaceInfo>> call=homeRetrofit.getAllPlace();
        call.enqueue(new Callback<List<PlaceInfo>>() {
            @Override
            public void onResponse(Call<List<PlaceInfo>> call, Response<List<PlaceInfo>> response) {
                if(response.isSuccessful()){
                    Log.d(DIBAGING_TAG,"successfully get all place");
                    placeList.setValue(response.body());
                }
                else {
                    Log.d(DIBAGING_TAG,"failed get all place");
                }

            }

            @Override
            public void onFailure(Call<List<PlaceInfo>> call, Throwable t) {
                Log.d(DIBAGING_TAG,"failed get all place :"+t.getMessage());

            }
        });
        return placeList;
    }
}
