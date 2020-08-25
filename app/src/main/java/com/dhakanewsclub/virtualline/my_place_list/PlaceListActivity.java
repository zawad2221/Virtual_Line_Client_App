package com.dhakanewsclub.virtualline.my_place_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.adapter.StoreListRecyclerViewAdapter;
import com.dhakanewsclub.virtualline.add_place.PlaceSelectionActivity;
import com.dhakanewsclub.virtualline.home.MainActivity;
import com.dhakanewsclub.virtualline.login.LoginUserData;
import com.dhakanewsclub.virtualline.models.PlaceInformation;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PlaceListActivity extends AppCompatActivity {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";
    RecyclerView mRecyclerView;
    ArrayList<PlaceInfo> storeList=new ArrayList<>();
    FloatingActionButton mFloatingActionButtonAddPlace;
    PlaceListViewModel mPlaceListViewModel;
    StoreListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        mFloatingActionButtonAddPlace=findViewById(R.id.add_place_floatingButton);
        getSupportActionBar().setTitle("Place List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setCustomView(R.drawable.close_activity);
        mPlaceListViewModel= ViewModelProviders.of(this).get(PlaceListViewModel.class);
        mPlaceListViewModel.init(PlaceListActivity.this, LoginUserData.loginUserInfo);

        
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_activity);
        initRecyclerView();
//        mPlaceListViewModel.getOwnerPlace().observe(PlaceListActivity.this, new Observer<List<PlaceInfo>>() {
//            @Override
//            public void onChanged(List<PlaceInfo> placeInfos) {
//                Log.d(DIBAGING_TAG,"on chagne on place list");
//                storeList.addAll(mPlaceListViewModel.getOwnerPlace().getValue());
//                adapter.notifyDataSetChanged();
//            }
//        });
        placeListObserver();
        addPlaceFloatingButtonOnClick();
        //mPlaceListViewModel.init(PlaceListActivity.this, LoginUserData.loginUserInfo);
    }

    void placeListObserver(){
        final Observer<List<PlaceInfo>> observer=new Observer<List<PlaceInfo>>() {
            @Override
            public void onChanged(List<PlaceInfo> placeInfos) {
                Log.d(DIBAGING_TAG,"on chagne on place list");
                storeList.addAll(mPlaceListViewModel.getOwnerPlace().getValue());
                adapter.notifyDataSetChanged();
            }
        };
        mPlaceListViewModel.getOwnerPlace().observe(this,observer);

    }

    @Override
    public void onBackPressed() {
        Log.d(DIBAGING_TAG,"on back listactivity");
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
        super.onBackPressed();
    }

    private void addPlaceFloatingButtonOnClick(){
        mFloatingActionButtonAddPlace.setOnClickListener(view -> {
            Intent addPlace = new Intent(getApplicationContext(), PlaceSelectionActivity.class);
            startActivity(addPlace);
            /*mPlaceListViewModel.init(getApplicationContext(),LoginUserData.loginUserInfo);
            //Log.d(DIBAGING_TAG,"owner place size: "+mPlaceListViewModel.ownerPlace.getValue().size());
            placeListObserver();*/

        });
    }

    private void recycleViewOnClick(){
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void initRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.store_list_recyclerView);

        adapter = new StoreListRecyclerViewAdapter(storeList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Log.d(DIBAGING_TAG,"actionbar back"+item.getItemId());

        switch (item.getItemId()){
            //close activity button id
            case 16908332:
                Log.d(DIBAGING_TAG,"actionbar back");
                onBackPressed();
                break;
        }
        return true;
    }

//
}