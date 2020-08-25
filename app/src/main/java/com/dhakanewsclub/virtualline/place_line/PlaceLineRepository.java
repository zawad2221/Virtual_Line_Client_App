package com.dhakanewsclub.virtualline.place_line;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceLineRepository {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";
    private static PlaceLineRepository instance;


    public static PlaceLineRepository getInstance() {
        if (instance == null) {
            instance = new PlaceLineRepository();

        }
        return instance;
    }

    public MutableLiveData<List<PlaceInfo>> getPlaceById(Context context, PlaceInfo placeInfo){
        Log.d(DIBAGING_TAG,"get place byid: place :"+placeInfo.getPlaceId());

        MutableLiveData<List<PlaceInfo>> placeInfoMutableLiveData=new MutableLiveData<>();
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/place/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        PlaceLineRetrofit placeLineRetrofit= retrofit.create(PlaceLineRetrofit.class);
        Call<PlaceInfo> call=placeLineRetrofit.getPlaceById(placeInfo);
        call.enqueue(new Callback<PlaceInfo>() {
            @Override
            public void onResponse(Call<PlaceInfo> call, Response<PlaceInfo> response) {
                if(response.isSuccessful()){
                    Log.d(DIBAGING_TAG," place response: "+response.body().toString());
                    List<PlaceInfo> re=new ArrayList<>();
                    re.add(response.body());
                    placeInfoMutableLiveData.setValue(re);

                    Log.d(DIBAGING_TAG,"success to load data: place line repo :"+placeInfoMutableLiveData.getValue().get(0).getPlaceName());

                }
            }

            @Override
            public void onFailure(Call<PlaceInfo> call, Throwable t) {
                Log.d(DIBAGING_TAG,"failed to load data: "+t.getMessage());
            }
        });
        return placeInfoMutableLiveData;

    }
    public MutableLiveData<PlaceLine> changeLineStatus(Context context, PlaceLine placeLine){
        //final boolean[] success = {false};
        MutableLiveData<PlaceLine> success=new MutableLiveData<PlaceLine>();

        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/place/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        PlaceLineRetrofit placeLineRetrofit=retrofit.create(PlaceLineRetrofit.class);
        Call<PlaceLine> placeLineCall=placeLineRetrofit.changeLineStatus(placeLine);
        placeLineCall.enqueue(new Callback<PlaceLine>() {
            @Override
            public void onResponse(Call<PlaceLine> call, Response<PlaceLine> response) {
                if(response.isSuccessful()){
                    success.setValue(response.body());
                    Log.d(DIBAGING_TAG,"successfully change status to :"+response.body().getLineStatus());
                    return;
                }
                Log.d(DIBAGING_TAG,"failed to change status :");
            }

            @Override
            public void onFailure(Call<PlaceLine> call, Throwable t) {
                Log.d(DIBAGING_TAG,"failed to change status :"+t.getMessage());
            }
        });
        return success;
    }

    public MutableLiveData<Boolean> removeUserFromLine(Context context, CheckedUser checkedUser){
        MutableLiveData<Boolean> success=new MutableLiveData<>();


        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/place/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        PlaceLineRetrofit placeLineRetrofit=retrofit.create(PlaceLineRetrofit.class);
        Call<CheckedUser> checkedUserCall=placeLineRetrofit.removeUserFromLine(checkedUser);
        checkedUserCall.enqueue(new Callback<CheckedUser>() {
            @Override
            public void onResponse(Call<CheckedUser> call, Response<CheckedUser> response) {
                if(response.isSuccessful()){
                    success.setValue(true);
                    Log.d(DIBAGING_TAG,"successfully removed user :"+response.body());
                    Toast.makeText(context,checkedUser.getUser().getName()+" removed from line",Toast.LENGTH_LONG).show();
                    return;
                }
                success.setValue(false);
                Log.d(DIBAGING_TAG,"failed to remove user :");
            }

            @Override
            public void onFailure(Call<CheckedUser> call, Throwable t) {
                success.setValue(false);
                Log.d(DIBAGING_TAG,"failed to remove user :"+t.getMessage());
                Toast.makeText(context,checkedUser.getUser().getName()+" failed to removed from line",Toast.LENGTH_LONG).show();
            }
        });
        return success;
    }

}
