package com.dhakanewsclub.virtualline.customer_place_view;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerPlaceRepository {
    final String DIBAGING_TAG="DIBAGING_TAG";

    public static CustomerPlaceRepository instance;

    public static CustomerPlaceRepository getInstance(){
        if (instance==null){
            instance=new CustomerPlaceRepository();
        }
        return instance;
    }

    MutableLiveData<Boolean> checkedPlace(Context context, PlaceLine placeLine){
        MutableLiveData<Boolean> success =new MutableLiveData<>();
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/place/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        CustomerPlaceRetrofit customerPlaceRetrofit=retrofit.create(CustomerPlaceRetrofit.class);
        Call<PlaceLine> call=customerPlaceRetrofit.checkInLine(placeLine);
        call.enqueue(new Callback<PlaceLine>() {
            @Override
            public void onResponse(Call<PlaceLine> call, Response<PlaceLine> response) {
                if(response.isSuccessful()){
                    success.setValue(true);
                    Log.d(DIBAGING_TAG,"checked in line");
                }
                else {
                    success.setValue(false);
                    Log.d(DIBAGING_TAG,"faild to checked in line");
                }
            }

            @Override
            public void onFailure(Call<PlaceLine> call, Throwable t) {
                success.setValue(false);
                Log.d(DIBAGING_TAG,"faild to checked in line");
            }
        });
        return success;

    }
}
