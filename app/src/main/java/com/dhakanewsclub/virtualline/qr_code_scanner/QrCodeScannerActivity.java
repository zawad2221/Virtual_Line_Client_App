package com.dhakanewsclub.virtualline.qr_code_scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.place_line.PlaceLineActivity;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Scanner;

public class QrCodeScannerActivity extends AppCompatActivity {
    //will store data of first person in the line
    public String firstUserId ="this is a qr code";
    private String firstCheckedUserId=null;
    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView resultData;

    private CheckedUser mCheckedUser;

    ScannerViewModel mScannerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scannView);
        resultData = findViewById(R.id.resultsOfQr);
        getSupportActionBar().setTitle("Scanner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_activity);

        mScannerViewModel= new ViewModelProvider(this,new ScannerViewModelFactory(this.getApplication(),"test")).get(ScannerViewModel.class);

        getIntentData();
        Log.d("DIBAGING_TAG"," scanner activity user id :"+firstUserId+" checked id: "+firstCheckedUserId);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result.getText().equals(firstUserId)){
                            resultData.setBackgroundColor(getResources().getColor(R.color.accessGranted));
                            resultData.setText("Access Granted");
                            mScannerViewModel.removeUserFromLine(getApplicationContext(),mCheckedUser);
                            mScannerViewModel.getUserDeleteResponse().observe(QrCodeScannerActivity.this, new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean aBoolean) {
                                    if(!mScannerViewModel.getUserDeleteResponse().getValue()){
                                        Toast.makeText(QrCodeScannerActivity.this,"failed to push first user from line",Toast.LENGTH_LONG).show();
                                    }
                                    startLineActivity();
                                }
                            });
                        }
                        else{
                            resultData.setBackgroundColor(getResources().getColor(R.color.accessDenied));
                            resultData.setText("Access Denied");
                        }

                    }
                });

            }
        });


        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultData.setText(null);
                resultData.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                codeScanner.startPreview();
            }
        });
    }

    private void getIntentData(){
        try{
            Intent intent=getIntent();
            firstCheckedUserId=intent.getStringExtra("checked_user_id");
            firstUserId=intent.getStringExtra("user_id");
            mCheckedUser=new CheckedUser();
            mCheckedUser.setCheckedId(Integer.parseInt(firstCheckedUserId));

        }
        catch (Exception e){
            Log.d("DIBAGING_TAG"," scanner activity exception in getting intent data: "+e.getMessage());
            Intent placeLine = new Intent(this,PlaceLineActivity.class);
            Toast.makeText(this,"failed to open scanner try again",Toast.LENGTH_LONG).show();
            startActivity(placeLine);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();

    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(), "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

            }

//
        }).check();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Log.d(DIBAGING_TAG,"actionbar back"+item.getItemId());
        Log.d("DIBAGING_TAG","actionbar back close activity clicked, id: "+item.getItemId());
        switch (item.getItemId()){

            //close activity button id
            case 16908332:
                Log.d("DIBAGING_TAG","actionbar back close activity clicked, id: "+item.getItemId());
                onBackPressed();
                break;
        }
        return true;
    }
    private void startLineActivity(){
        Intent line=new Intent(QrCodeScannerActivity.this,PlaceLineActivity.class);
        finish();
        startActivity(line);
    }

    @Override
    public void onBackPressed() {
        startLineActivity();
        //super.onBackPressed();
    }
}