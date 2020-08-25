package com.dhakanewsclub.virtualline.add_place;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.login.LoginUserData;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceType;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

public class SavePlaceInformationActivity extends AppCompatActivity {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";

    List<PlaceType> mPlaceTypeList=new ArrayList<>();
    //drop down item list
    ArrayList<String > dropDownItem = new ArrayList<>();

    private EditText mEditTextPlaceName;
    private Spinner mSpinnerPlaceType;
    private EditText mEditTextPlaceAddress;

    private AddPlaceViewModel mAddPlaceViewModel;
    ArrayAdapter<String> mDropDownAdapter;

    //store intent data
    String longitude=null,latitude=null, country =null, region =null, place =null;
    private String placeName;
    private String placeAddress;
    private PlaceType placeType;


//    ArrayList<PlaceType> dayFromDropDownTypeList;
//    ArrayList<PlaceType> dayToDropDownTypeList;
//    ArrayList<PlaceType> timeOpenDropDownTypeList;
//    ArrayList<PlaceType> timeCloseDropDownTypeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location_information);
        mEditTextPlaceName=findViewById(R.id.place_name_editText);
        mSpinnerPlaceType=findViewById(R.id.place_type_drop);
        mEditTextPlaceAddress =findViewById(R.id.place_address_edit_text);
        mAddPlaceViewModel= ViewModelProviders.of(this).get(AddPlaceViewModel.class);


        getIntentData();
        getSupportActionBar().setTitle("Location Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setCustomView(R.drawable.close_activity);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_activity);

        initiateViewModel(getApplicationContext());


        placeTypeDropDownAdapter(mPlaceTypeList);
        mAddPlaceViewModel.getPlaceTypeLiveData().observe(this, new Observer<List<PlaceType>>() {
            @Override
            public void onChanged(List<PlaceType> placeTypes) {
                Log.d(DIBAGING_TAG,"mutable data change");
                mPlaceTypeList.addAll(mAddPlaceViewModel.getPlaceTypeLiveData().getValue());
                addPlaceNameToDropDownList(mPlaceTypeList);
                Log.d(DIBAGING_TAG,"mutable data change"+mPlaceTypeList.get(0).getPlaceTypeName());
                mDropDownAdapter.notifyDataSetChanged();
            }
        });





    }

    private void initiateViewModel(Context context) {
        mAddPlaceViewModel.init(context);
    }

    private void getIntentData(){
        try{
            Intent intent=getIntent();
            longitude=intent.getStringExtra("LONGITUDE");
            latitude = intent.getStringExtra("LATITUDE");
            country = intent.getStringExtra("COUNTRY");
            region = intent.getStringExtra("REGION");
            place = intent.getStringExtra("PLACE");

        }
        catch (Exception e){
            Log.d("location_info_activity","error getting intent from placeSelectionActivity, Exception: "+e);
            Intent intent=new Intent(this,PlaceSelectionActivity.class);
            Toast.makeText(this,"Can't get location",Toast.LENGTH_LONG).show();
            startActivity(intent);

        }


    }

    private void addPlaceNameToDropDownList(List<PlaceType> placeTypeList){
        for(PlaceType item : placeTypeList){
            Log.d(DIBAGING_TAG,"adding in dropdown"+item.getPlaceTypeName());
            dropDownItem.add(item.getPlaceTypeName());
        }
    }

    private void placeTypeDropDownAdapter(List<PlaceType> placeTypeList){
        Log.d(DIBAGING_TAG,"adapter dropdown");

        addPlaceNameToDropDownList(placeTypeList);

        mDropDownAdapter = new ArrayAdapter<>(this,R.layout.place_type_dropdown_item,dropDownItem);
        // Drop down layout style - list view with radio button
        mDropDownAdapter.setDropDownViewResource(R.layout.place_type_dropdown_item);

        // attaching data adapter to spinner
        mSpinnerPlaceType.setAdapter(mDropDownAdapter);

    }
//    private void dayFromDropDownAdapter(){
//        dayFromDropDownTypeList= new ArrayList<>();
//        dayFromDropDownTypeList.add("")
//    }
//    private void dayToDropDownAdapter(){
//
//    }
//    private void timeOpenDropDownAdapter(){
//
//    }
//    private void timeCloseDropDownAdapter(){
//
//    }

//    private void dropDownOnSelect(){
//        mSpinnerPlaceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

    public void getDataFromView(){
        try{
            placeName =mEditTextPlaceName.getText().toString();
            placeAddress = mEditTextPlaceAddress.getText().toString();
            placeType = mPlaceTypeList.get(mSpinnerPlaceType.getSelectedItemPosition());
        }
        catch (Exception e){
            Toast.makeText(this,"invalid input",Toast.LENGTH_LONG).show();
            return;
        }

    }

    private boolean validateFields(){
        if(placeName.isEmpty()){
            mEditTextPlaceName.setError("invalid input");
            return false;
        }
        else if(placeAddress.isEmpty()){
            mEditTextPlaceAddress.setError("invalid input");
            return false;
        }

        return true;
    }
    
    private void saveInfo(){
        getDataFromView();
        if(!validateFields()){
            Toast.makeText(this,"invalid input",Toast.LENGTH_LONG).show();

        }
        else {
            UserInfo owner= LoginUserData.loginUserInfo;
            PlaceInfo newPlaceInfo=new PlaceInfo();
            newPlaceInfo.setPlaceName(placeName);
            newPlaceInfo.setPlaceAddress(placeAddress);
            newPlaceInfo.setPlaceLongitude(longitude);
            newPlaceInfo.setPlaceLatitude(latitude);
            newPlaceInfo.setPlaceType(placeType);
            newPlaceInfo.setOwner(owner.getUserId());
            mAddPlaceViewModel.addPlace(SavePlaceInformationActivity.this,newPlaceInfo);
        }
        




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.location_info_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_info:
                saveInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}