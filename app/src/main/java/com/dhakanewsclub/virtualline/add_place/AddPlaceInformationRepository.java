package com.dhakanewsclub.virtualline.add_place;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceType;
import com.dhakanewsclub.virtualline.my_place_list.PlaceListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPlaceInformationRepository {

    private static final String DIBAGING_TAG = "DIBAGING_TAG";

    private static AddPlaceInformationRepository instance;


    public static AddPlaceInformationRepository getInstance() {
        if (instance == null) {
            instance = new AddPlaceInformationRepository();

        }
        return instance;
    }

    public MutableLiveData<List<PlaceType>> getPlaceType(Context applicationContext){
        MutableLiveData<List<PlaceType>> mPlaceTypes=new MutableLiveData<>();
        Log.d(DIBAGING_TAG,"data fetch  repo");
        Retrofit.Builder builder= new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/place/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AddPlaceRetrofit addPlaceRetrofit=retrofit.create(AddPlaceRetrofit.class);
        Call<List<PlaceType>> call = addPlaceRetrofit.getPlaceType();
        call.enqueue(new Callback<List<PlaceType>>() {
            @Override
            public void onResponse(Call<List<PlaceType>> call, Response<List<PlaceType>> response) {
                if(response.isSuccessful()){
                    mPlaceTypes.setValue(response.body());
                    Log.d(DIBAGING_TAG,"data fetch add repo"+mPlaceTypes.getValue().get(0).getPlaceTypeName());
                }
                Log.d(DIBAGING_TAG,"data fetch not success repo "+response.body().toString());
            }

            @Override
            public void onFailure(Call<List<PlaceType>> call, Throwable t) {
                Log.d(DIBAGING_TAG,"failed to load data: "+t.getMessage());
                Toast.makeText(applicationContext,"failed to load data",Toast.LENGTH_LONG).show();
            }
        });

        return mPlaceTypes;
    }
    public ProgressDialog showDialog(Context applicationContext){
        final ProgressDialog progressDialog = new ProgressDialog(applicationContext,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding new place...");
        progressDialog.show();
        return progressDialog;
    }

    public void addNewPlace(Context applicationContext, PlaceInfo placeInfo){
        final ProgressDialog progressDialog=showDialog(applicationContext);
        Log.d(DIBAGING_TAG,"add place reppo start");
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/place/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        AddPlaceRetrofit addPlaceRetrofit=retrofit.create(AddPlaceRetrofit.class);
        Call<PlaceInfo> call = addPlaceRetrofit.addPlace(placeInfo);
        call.enqueue(new Callback<PlaceInfo>() {
            @Override
            public void onResponse(Call<PlaceInfo> call, Response<PlaceInfo> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    failedMessage();
                    return;
                }
                Log.d(DIBAGING_TAG,"new place added");
                Toast.makeText(applicationContext,"new place added",Toast.LENGTH_LONG).show();
                startMainActivity();
            }
            private void failedMessage(){
                Toast.makeText(applicationContext,"failed to add new place",Toast.LENGTH_LONG).show();
            }
            private void startMainActivity(){
                Intent mainActivity=new Intent(applicationContext, PlaceListActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //finish all previous activity
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                try {
//                    ((SavePlaceInformationActivity)applicationContext).finish();
//                    ((PlaceSelectionActivity)applicationContext).finish();
//                }
//                catch (Exception e){
//                    Log.d(DIBAGING_TAG,"exception in add place start main activity");
//                }

                applicationContext.startActivity(mainActivity);
            }

            @Override
            public void onFailure(Call<PlaceInfo> call, Throwable t) {
                progressDialog.dismiss();
                failedMessage();
                Log.d(DIBAGING_TAG,"place add failed"+t.getMessage());
                t.getMessage();
            }
        });

    }
}
