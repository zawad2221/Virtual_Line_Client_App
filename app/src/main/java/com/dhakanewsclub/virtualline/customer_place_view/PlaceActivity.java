package com.dhakanewsclub.virtualline.customer_place_view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dhakanewsclub.virtualline.R;

import com.dhakanewsclub.virtualline.login.LoginUserData;
import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceLine;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class PlaceActivity extends AppCompatActivity {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";
    private ImageView mImageViewQrCode;
    private LinearLayout mLinearLayoutQrCode;
    TextView mPlaceNameTextVeiw,mPlaceAddressTextView,mPlaceLineStatusTextView,mPlaceLineTotalCheckedCustomerTextView,mPlaceLineCustomerPositionTextView;

    private LinearLayout mLineInfoLinearLayout,mCustomerPositionInLineLayout;

    private CustomerPlaceViewMode mCustomerPlaceViewMode;

    Switch checkedSwitch =null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        getSupportActionBar().setTitle("AB store");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_activity);

        mImageViewQrCode=findViewById(R.id.qr_code_image_view);
        mLinearLayoutQrCode=findViewById(R.id.qr_code_layout);


        mPlaceNameTextVeiw=findViewById(R.id.plece_name_textView);
        mPlaceAddressTextView=findViewById(R.id.place_address_textView);
        mPlaceLineStatusTextView=findViewById(R.id.line_status);
        mPlaceLineTotalCheckedCustomerTextView=findViewById(R.id.number_of_customer_in_line_textView);
        mPlaceLineCustomerPositionTextView=findViewById(R.id.customer_position_in_line_textView);
        mLineInfoLinearLayout=findViewById(R.id.line_info);
        mCustomerPositionInLineLayout=findViewById(R.id.customer_position_in_line_linear_layout);


        mCustomerPlaceViewMode=new ViewModelProvider(this,new CustomerPlaceViewModelFactory(this.getApplication(),"test")).get(CustomerPlaceViewMode.class);

        loadPlaceInfo();
        placeInfoOnChange();


    }

    private void loadPlaceInfo(){
        mCustomerPlaceViewMode.initializePlaceInfo(this,SelectedPlaceData.customerSelectedPlaceInfo);
    }

    private void placeInfoOnChange(){
        mCustomerPlaceViewMode.getListMutableLiveData().observe(this, new Observer<List<PlaceInfo>>() {
            @Override
            public void onChanged(List<PlaceInfo> placeInfos) {
                Log.d(DIBAGING_TAG,"place data loaded: "+mCustomerPlaceViewMode.getListMutableLiveData().getValue().get(0).getPlaceName());
                SelectedPlaceData.customerSelectedPlaceInfo=mCustomerPlaceViewMode.getListMutableLiveData().getValue().get(0);
                setPlaceDataToView();
            }
        });
    }

    private void setPlaceDataToView() {
        try{
            getSupportActionBar().setTitle(SelectedPlaceData.customerSelectedPlaceInfo.getPlaceType().getPlaceTypeName());
            mPlaceNameTextVeiw.setText(SelectedPlaceData.customerSelectedPlaceInfo.getPlaceName());
            mPlaceAddressTextView.setText(SelectedPlaceData.customerSelectedPlaceInfo.getPlaceAddress());
            String lineStatus=SelectedPlaceData.customerSelectedPlaceInfo.getPlaceLine().getLineStatus();
            mPlaceLineStatusTextView.setText(lineStatus);
            if(lineStatus.equals("close")){
                SelectedPlaceData.lineStatus=false;
                setLineInfoVisibility(false);
            }
            else {
                SelectedPlaceData.lineStatus=true;
                setLineInfoVisibility(true);
                Log.d(DIBAGING_TAG,"login user"+LoginUserData.loginUserInfo.getUserId());
                try{
                    int numberOfUserInLIne=(SelectedPlaceData.customerSelectedPlaceInfo.getPlaceLine().getCheckedUser().size());
                    Log.d(DIBAGING_TAG,"number of user in line: "+numberOfUserInLIne);
                    mPlaceLineTotalCheckedCustomerTextView.setText(Integer.toString(numberOfUserInLIne));
                }
                catch (Exception e){
                    mPlaceLineTotalCheckedCustomerTextView.setText("0");
                }
                int index=0;
                SelectedPlaceData.isChecked=false;
                for(CheckedUser checkedUser: SelectedPlaceData.customerSelectedPlaceInfo.getPlaceLine().getCheckedUser()){
                    Log.d(DIBAGING_TAG,"line user"+LoginUserData.loginUserInfo.getUserId());
                    if(checkedUser.getUser().getUserId()== LoginUserData.loginUserInfo.getUserId()){
                        Log.d(DIBAGING_TAG,"line user checked"+checkedUser.getUser().getUserId());
                        SelectedPlaceData.isChecked=true;
                        setCustomerPositionLayoutVisibility(true);
                        mPlaceLineCustomerPositionTextView.setText(Integer.toString(index+1));
                        showQrCode();
                        break;
                    }
                    Log.d(DIBAGING_TAG,"line user is not checked");
                    SelectedPlaceData.isChecked=false;
                    index++;
                }
                Log.d(DIBAGING_TAG,"user checked status: "+SelectedPlaceData.isChecked);

            }

        }
        catch (Exception e){
            Log.d(DIBAGING_TAG,"exception in adding store info in view :"+e.getMessage());
        }

    }

    private void showQrCode(){
        Bitmap qrCodeBitmap=generateQRCode(Integer.toString(LoginUserData.loginUserInfo.getUserId()));
        setLineCheckedQrLayoutVisibility(true);
        setCheckedSwitchStatus(true);
        showQrCodeInImageView(qrCodeBitmap);
    }



    private void setLineInfoVisibility(boolean status){
        if(status)
        mLineInfoLinearLayout.setVisibility(View.VISIBLE);
        else mLineInfoLinearLayout.setVisibility(View.INVISIBLE);
    }

    private void setLineCheckedQrLayoutVisibility(boolean status){
        if (status)mLinearLayoutQrCode.setVisibility(View.VISIBLE);
        else mLinearLayoutQrCode.setVisibility(View.INVISIBLE);
    }

    private void setCustomerPositionLayoutVisibility(boolean status){
        if (status)mCustomerPositionInLineLayout.setVisibility(View.VISIBLE);
        else mCustomerPositionInLineLayout.setVisibility(View.INVISIBLE);
    }

    private void setCheckedSwitchStatus(boolean status){
        checkedSwitch.setChecked(status);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.d(DIBAGING_TAG,"menu creat");
        getMenuInflater().inflate(R.menu.customer_place_view_menu,menu);
        MenuItem itemSwitch = menu.findItem(R.id.switch_checked);
        itemSwitch.setActionView(R.layout.switch_item);
        checkedSwitch = (Switch) menu.findItem(R.id.switch_checked).getActionView().findViewById(R.id.action_switch_checked);
        checkedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButtonView, boolean isChecked) {
                if(isChecked){
                    if(!SelectedPlaceData.lineStatus){
                        checkedSwitch.setChecked(false);
                        Toast.makeText(getApplicationContext(),"there is no line",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Log.d(DIBAGING_TAG,"checked switch");
                    if(!SelectedPlaceData.isChecked){
                        Log.d(DIBAGING_TAG,"checked switch send data");
                        checkedInLine();
                    }



                    setLineCheckedQrLayoutVisibility(true);
                    showQrCode();
                    //showQrCodeInImageView(qrCodeBitmap);
                }
                else {

                    CheckedUser checkedUser=new CheckedUser();
                    checkedUser=(SelectedPlaceData.customerSelectedPlaceInfo.getPlaceLine().getCheckedUser().get(Integer.parseInt(mPlaceLineCustomerPositionTextView.getText().toString())-1));
                    mCustomerPlaceViewMode.uncheckedUser(getApplicationContext(),checkedUser);
                    mCustomerPlaceViewMode.getUncheckedUserStatus().observe(PlaceActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            Log.d(DIBAGING_TAG,"unchecked data chnage");
                            loadPlaceInfo();
                            placeInfoOnChange();
                            SelectedPlaceData.isChecked=false;

                        }
                    });

                    mImageViewQrCode.setImageBitmap(null);
                    mLinearLayoutQrCode.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"unchecked",Toast.LENGTH_LONG).show();
                }

            }
        });
        return true;
    }

    private void checkedInLine(){
        PlaceLine placeLine=SelectedPlaceData.customerSelectedPlaceInfo.getPlaceLine();
        UserInfo userInfo=new UserInfo();
        userInfo.setUserId(LoginUserData.loginUserInfo.getUserId());
        CheckedUser checkedUser=new CheckedUser();
        checkedUser.setUser(userInfo);
        List<CheckedUser> checkedUserList=new ArrayList<>();
        checkedUserList.add(checkedUser);

        placeLine.setCheckedUser(checkedUserList);
        mCustomerPlaceViewMode.checkedUser(getApplicationContext(),placeLine);
        mCustomerPlaceViewMode.getCheckedUserStatus().observe(PlaceActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(DIBAGING_TAG,"customer checked updated");
                loadPlaceInfo();
                placeInfoOnChange();
            }
        });

    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.switch_checked:
//                Toast.makeText(getApplicationContext(),"checked/unchecked",Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

    private Bitmap generateQRCode(String qrValue){
        Bitmap bitmap = null;
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        QRGEncoder qrgEncoder = new QRGEncoder(qrValue, null, QRGContents.Type.TEXT, 900);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView

        } catch (Exception e) {
            Log.d(DIBAGING_TAG,"error in generation qr code");
        }
        return bitmap;
    }

    private void showQrCodeInImageView(Bitmap qrCodeBitmap){
        mImageViewQrCode.setImageBitmap(qrCodeBitmap);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(DIBAGING_TAG,"actionbar back"+item.getItemId());

        switch (item.getItemId()){
            //close activity button id
            case 16908332:
                Log.d(DIBAGING_TAG,"actionbar back");
                onBackPressed();
                break;
        }
        return true;
    }
}
