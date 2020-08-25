package com.dhakanewsclub.virtualline.place_line;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.adapter.StoreLineRecyclerViewAdapter;

import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;
import com.dhakanewsclub.virtualline.my_place_list.PlaceListData;
import com.dhakanewsclub.virtualline.qr_code_scanner.QrCodeScannerActivity;

import java.util.ArrayList;
import java.util.List;

public class PlaceLineActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    ArrayList<CheckedUser> lineUserList=new ArrayList<>();
    PlaceLineViewModel mPlaceLineViewModel;
    StoreLineRecyclerViewAdapter adapter;
    TextView mLineStatusTextView;

    private String lineStatusOpenMessage="Customers checked in line";
    private String lineStatusCloseMessage="No line, Check the menu to create line";
    final String DIBAGING_TAG="DIBAGING_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_line);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLineStatusTextView=findViewById(R.id.lineStatusTextView);
        mPlaceLineViewModel= ViewModelProviders.of(this).get(PlaceLineViewModel.class);
        Log.d(DIBAGING_TAG," place line acti place info static :"+PlaceListData.selectedPlace.getPlaceLine().getLineStatus());
        setTextInLineStatus();
        initRecyclerView();
        getStoreData();

        //lineStatusChangeListener();

        //getSupportActionBar().setCustomView(R.drawable.close_activity);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_activity);
        getSupportActionBar().setTitle(PlaceListData.selectedPlace.getPlaceName());


    }

    private void lineStatusChangeListener() {
        //mPlaceLineViewModel.changeLineStatus(PlaceLineActivity.this,PlaceListData.selectedPlace.getPlaceLine());
        mPlaceLineViewModel.getLineStatusChangeResponse().observe(this, new Observer<PlaceLine>() {
            @Override
            public void onChanged(PlaceLine placeLine) {
                Log.d(DIBAGING_TAG,"line status change");
                getStoreData();
                mPlaceLineViewModel.setLineStatusChangeResponse(null);
                //adapter.notifyDataSetChanged();


            }
        });
    }

    private void setTextInLineStatus(){
        Log.d(DIBAGING_TAG," store data "+PlaceListData.selectedPlace.toString());
        if(PlaceListData.selectedPlace.getPlaceLine().getLineStatus().equals("open")){
            mLineStatusTextView.setText(lineStatusOpenMessage);
        }
        else {
            mLineStatusTextView.setText(lineStatusCloseMessage);
        }
    }

    private void storeDataOnChangeListener(){
        mPlaceLineViewModel.getPlaceInfoMutableLiveData().observe(this, new Observer<List<PlaceInfo>>() {
            @Override
            public void onChanged(List<PlaceInfo> placeInfo) {
                PlaceListData.selectedPlace=mPlaceLineViewModel.getPlaceInfoMutableLiveData().getValue().get(0);
                lineUserList.clear();
                lineUserList.addAll(mPlaceLineViewModel.getPlaceInfoMutableLiveData().getValue().get(0).getPlaceLine().getCheckedUser());

                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"place live data change",Toast.LENGTH_LONG).show();
                Log.d(DIBAGING_TAG,"line data change line status: "+PlaceListData.selectedPlace.getPlaceLine().getLineStatus());
                setTextInLineStatus();
            }
        });

//        mPlaceLineViewModel.getListMutableLiveDataPlaceInfo().observe(this, new Observer<List<PlaceInfo>>() {
//            @Override
//            public void onChanged(List<PlaceInfo> placeInfos) {
//                PlaceListData.selectedPlace=mPlaceLineViewModel.getPlaceInfoMutableLiveData().getValue();
//                lineUserList.addAll(mPlaceLineViewModel.getPlaceInfoMutableLiveData().getValue().getPlaceLine().getCheckedUser());
//                adapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(),"place live data list change",Toast.LENGTH_LONG).show();
//                setTextInLineStatus();
//            }
//        });

    }

    private void getStoreData(){

        mPlaceLineViewModel.init(getApplicationContext(), PlaceListData.selectedPlace);
        storeDataOnChangeListener();
        //PlaceListData.selectedPlace=(mPlaceLineViewModel.getPlaceInfoMutableLiveData().getValue());
        Log.d(DIBAGING_TAG,"get line data "+PlaceListData.selectedPlace.toString());


    }

    void removeUserResponseObserver(){
        mPlaceLineViewModel.getUserDeleteResponse().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(DIBAGING_TAG,"line live data change");



                getStoreData();
                //mPlaceLineViewModel.setUserDeleteResponse(null);
                //adapter.notifyDataSetChanged();
            }
        });
    }

    private void removeUserFromLine(Integer position){
        mPlaceLineViewModel.removeUserFromLine(PlaceLineActivity.this,PlaceListData.selectedPlace.getPlaceLine().getCheckedUser().get(position));

        removeUserResponseObserver();
    }

    //will check if the new and old status is same, if same then return false
    private boolean checkNewStatus(String newStatus){
        if(newStatus.equals(PlaceListData.selectedPlace.getPlaceLine().getLineStatus())){
            return false;
        }
        return true;
    }

    private void changeLineStatus(String newStatus){

        //contain the new status and line id
        PlaceLine placeLine=new PlaceLine();
        placeLine.setLineId(PlaceListData.selectedPlace.getPlaceLine().getLineId());
        placeLine.setLineStatus(newStatus);
        if(newStatus.equals("open")){
            if(!checkNewStatus(newStatus)){
                Toast.makeText(this,"already there is a line",Toast.LENGTH_LONG).show();
                return;
            }
        }
        else {
            if(!checkNewStatus(newStatus)){
                Toast.makeText(this,"there is no line",Toast.LENGTH_LONG).show();
                return;
            }
        }
        mPlaceLineViewModel.changeLineStatus(PlaceLineActivity.this,placeLine);
        lineStatusChangeListener();
        //mPlaceLineViewModel.init(getApplicationContext(),PlaceListData.selectedPlace);
        //mPlaceLineViewModel.mPlaceInfoMutableLiveData.setValue(null);
        //Log.d(DIBAGING_TAG,mPlaceLineViewModel.mPlaceInfoMutableLiveData.getValue().getPlaceName());
    }


    private void initRecyclerView(){


        mRecyclerView = (RecyclerView) findViewById(R.id.store_line_recyclerView);
        adapter = new StoreLineRecyclerViewAdapter(lineUserList, new StoreLineRecyclerViewAdapter.OnItemClickListener() {
            //recycle view item on click from activity
            @Override
            public void onItemClick(View view, int position) {
                removeUserFromLine(position);
                //Toast.makeText(getApplicationContext(),"delete user : "+PlaceListData.selectedPlace.getPlaceLine().getCheckedUser().get(position).getUser().getName(),Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_line_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_line:
                changeLineStatus("open");
                return true;
            case R.id.delete_line:
                changeLineStatus("close");
                return true;
            case R.id.check_customer:
                startScanner();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void startScanner(){
        if(PlaceListData.selectedPlace.getPlaceLine().getCheckedUser().size()==0){
            Toast.makeText(this,"there is no user in the line",Toast.LENGTH_LONG).show();
            return;
        }

        String firstCheckedUserId=PlaceListData.selectedPlace.getPlaceLine().getCheckedUser().get(0).getCheckedId().toString();
        String firstUserId=PlaceListData.selectedPlace.getPlaceLine().getCheckedUser().get(0).getUser().getUserId().toString();
        Intent scanner = new Intent(this, QrCodeScannerActivity.class);
        scanner.putExtra("checked_user_id",firstCheckedUserId);
        scanner.putExtra("user_id",firstUserId);
        finish();
        startActivity(scanner);
    }

}