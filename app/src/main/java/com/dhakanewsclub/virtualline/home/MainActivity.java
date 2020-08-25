package com.dhakanewsclub.virtualline.home;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.add_place.PlaceSelectionActivity;
import com.dhakanewsclub.virtualline.customer_place_view.PlaceActivity;
import com.dhakanewsclub.virtualline.customer_place_view.SelectedPlaceData;
import com.dhakanewsclub.virtualline.login.LoginActivity;
import com.dhakanewsclub.virtualline.login.LoginUserData;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.UserType;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;
import com.dhakanewsclub.virtualline.my_place_list.PlaceListActivity;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//
//import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.mapboxsdk.maps.SupportMapFragment;
//import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
//import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String DIBAGING_TAG = "DIBAGING_TAG";

    private TextView mDrawerHerderUserName;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

//    private MapView mapView;
//    private MarkerView markerView;
//    private MarkerViewManager markerViewManager;
//    private MapboxMap mMapboxMap;

    private HomeViewMode mHomeViewMode;

    GoogleMap gMap;
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(DIBAGING_TAG,"on create main");

        //mHomeViewMode= ViewModelProviders.of(this).get(HomeViewMode.class);
        mHomeViewMode=new ViewModelProvider(this,new HomeViewModelFactory(this.getApplication(),"test")).get(HomeViewMode.class);
        mHomeViewMode.initAllPlace(this);
        placeListOnChange();

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        //Mapbox.getInstance(this, getString(R.string.access_token));
        //google map
        //initializerMap();



//        mapView = findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);

//        initializerMap();

        //getSupportActionBar().hide();
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,0,0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = findViewById(R.id.navigation_view);


        //initiate header view
        View drawerHerderView=mNavigationView.getHeaderView(0);
        mDrawerHerderUserName=drawerHerderView.findViewById(R.id.drawer_header_user_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        //Intent picklocation = new Intent(this, LoginActivity.class);
        menuItemSelectListener();
        boolean login=ifLogin();
        if(login){
            hideLoginOption();
            setUserNameInDrawer();
        }
        else {
            hideLogoutOption();
        }

        //startActivity(picklocation);

    }
    private void initializerMap(){
        supportMapFragment=(SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
    }

    private void placeListOnChange(){
        mHomeViewMode.getAllPlace().observe(this, new Observer<List<PlaceInfo>>() {
            @Override
            public void onChanged(List<PlaceInfo> placeInfos) {
                Log.d(DIBAGING_TAG,"place list loaded");
                initializerMap();

            }
        });
    }


    private void setUserNameInDrawer(){
        try{

            mDrawerHerderUserName.setText(LoginUserData.loginUserInfo.getName());
        }
        catch (Exception e){
            Log.d(DIBAGING_TAG,"error adding user name in header error :"+e.getMessage());
        }

    }


    private void hideLoginOption(){
        Menu nav_Menu = mNavigationView.getMenu();
        nav_Menu.findItem(R.id.login).setVisible(false);
    }
    private void hideLogoutOption(){
        Menu nav_Menu = mNavigationView.getMenu();
        nav_Menu.findItem(R.id.logout).setVisible(false);
    }

    private void clearLoginData(){
        LoginUserData.login=false;
        LoginUserData.loginUserType=null;
        LoginUserData.loginUserInfo=null;
        SharedPreferences sharedPreferences=getSharedPreferences("login_data",Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

    }


    private boolean ifLogin(){
        UserInfo userInfo= new UserInfo();
        ArrayList<UserType> userTypeList= new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE);
        try{
            String userId=sharedPreferences.getString("loginUserId","0");
            if(!userId.equals("0")){


                userInfo.setUserId(Integer.parseInt(userId));
                userInfo.setPhoneNumber(sharedPreferences.getString("loginUserPhoneNumber","0"));
                userInfo.setName(sharedPreferences.getString("loginUserName","0"));

                for(int index=1;index<=3;index++){
                    UserType userType=new UserType();
                    String typeName=sharedPreferences.getString("loginUserPermission"+index,"0");
                    if(typeName.equals("0")){
                        break;
                    }
                    userType.setTypeName(typeName);

                    userTypeList.add(userType);
                }
                LoginUserData.login=true;
                LoginUserData.loginUserInfo=userInfo;
                LoginUserData.loginUserType=userTypeList;
                Log.d(DIBAGING_TAG,"main activity getShared data username "+LoginUserData.loginUserInfo.getName());
                return true;

            }
        }
        catch (Exception e){
            Log.d(DIBAGING_TAG,"exception in getting shared pref data");
            return false;
        }

        return false;

    }

//    private void initializerMap(){
//
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
//
//
//
//                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
//                    @Override
//                    public boolean onMapClick(@NonNull LatLng point) {
//                        Log.d("MAIN_ACTIVITY","marker clicked"+point.getLatitude()+"-"+point.getLongitude());
//                        return false;
//                    }
//                });
//                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//
//                        // Initialize the MarkerViewManager
//                        markerViewManager = new MarkerViewManager(mapView, mapboxMap);
//
//                        //add place
//                        // Use an XML layout to create a View object
//                        View customView = LayoutInflater.from(MainActivity.this).inflate(
//                                R.layout.marker_view_bubble, null);
//                        //set marker title
//                        TextView markerTitle = customView.findViewById(R.id.marker_title);
//                        markerTitle.setText("This is a pharmacy");
//
//                        LinearLayout markerLinearLayout=customView.findViewById(R.id.marker_layout);
//
//                        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
//                            @Override
//                            public boolean onMarkerClick(@NonNull Marker marker) {
//                                Log.d(DIBAGING_TAG,"marker click");
//                                return true;
//                            }
//                        });
//
////                        markerLinearLayout.setOnClickListener(new View.OnClickListener() {
////
////                            @RequiresApi(api = Build.VERSION_CODES.O)
////                            @Override
////                            public void onClick(View view) {
////                                String toolTipText= null;
////                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////                                    toolTipText = markerLinearLayout.getTooltipText().toString();
////                                }
////                                Log.d("DIBAGING_TAG","liniar on select :"+toolTipText);
////                                Intent placeDetail = new Intent(customView.getContext(), PlaceActivity.class);
////                                startActivity(placeDetail);
////                            }
////                        });
//
//                        //set marker icon
//                        ImageView markerIcon = customView.findViewById(R.id.marker_icon);
//                        markerIcon.setImageResource(R.drawable.pharmacy);
//
//                        customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
//                        markerView = new MarkerView(new LatLng(23.73195, 90.378106), customView);
//                        markerViewManager.addMarker(markerView);
//
//
//
//
//
//                    }
//                });
//            }
//        });
//        mapView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("MAIN_ACTIVITY","clicked");
//            }
//        });
//
//    }

    private void markerOnClick(){
//        mMapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(@NonNull Marker marker) {
//                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
    }

    private void starLoginActivity(){
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(loginIntent);
    }

    private void menuItemSelectListener(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d("MAIN_ACTIVITY","menu on select");
                switch (menuItem.getItemId()){
                    case R.id.checked_place:
                        mDrawerLayout.closeDrawer(GravityCompat.START,true);

                        break;
                    case R.id.login:

                        mDrawerLayout.closeDrawer(GravityCompat.START,true);
                        starLoginActivity();
                        break;
                    case R.id.my_place:
                        mDrawerLayout.closeDrawer(GravityCompat.START,true);
                        if(!LoginUserData.login){
                            starLoginActivity();
                            break;
                        }
                        Intent myPlaceIntent = new Intent(getApplicationContext(), PlaceListActivity.class);
                        finish();
                        startActivity(myPlaceIntent);
                        break;
                    case R.id.logout:
                        clearLoginData();
                        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivityIntent);
                        finish();
                        break;


                }
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(DIBAGING_TAG, "map initialized");
        gMap=googleMap;


        LatLng dhaka = new LatLng(23.732817, 90.384439);

//        MarkerOptions markerDhaka= new MarkerOptions()
//                .position(dhaka)
//                .title("Marker in Sydney");


//        Marker marker1=gMap.addMarker(marker);
//        marker1.setTag("1");
//        Marker marker2=gMap.addMarker(markerDhaka);
//        marker2.setTag("2");
        try {
            addMarker(googleMap);
        }
        catch (Exception e){
            Log.d(DIBAGING_TAG,"exception in adding marker"+e.getMessage());
        }


        gMap.moveCamera(CameraUpdateFactory.newLatLng(dhaka));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(12),2000,null);
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!LoginUserData.login){
                    Intent login=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(login);
                }
                Log.d(DIBAGING_TAG,"marker clicked id: "+marker.getId());
                int index = Integer.parseInt(marker.getId().substring(1));
                Log.d(DIBAGING_TAG,"marker clicked index: "+index);
                SelectedPlaceData.customerSelectedPlaceInfo=mHomeViewMode.getAllPlace().getValue().get(index);
                Intent placeView=new Intent(getApplicationContext(),PlaceActivity.class);
                startActivity(placeView);
                return true;
            }
        });
    }

    private void addMarker(GoogleMap googleMap){

        for(PlaceInfo placeInfo: mHomeViewMode.getAllPlace().getValue()){
            LatLng latLng=new LatLng(Double.parseDouble(placeInfo.getPlaceLatitude()),Double.parseDouble(placeInfo.getPlaceLongitude()));
            MarkerOptions markerOptions= new MarkerOptions()
                    .position(latLng)
                    .title(placeInfo.getPlaceName());
            if(placeInfo.getPlaceType().getPlaceTypeName().equals("Pharmacy")){
                markerOptions.icon(BitmapDescriptorFactory
                        .fromBitmap(createCustomMarker(MainActivity.this,R.drawable.pharmacy,placeInfo.getPlaceName(),placeInfo.getPlaceLine().getLineStatus())));
            }
            else if(placeInfo.getPlaceType().getPlaceTypeName().equals("Super Shop")){
                markerOptions.icon(BitmapDescriptorFactory
                        .fromBitmap(createCustomMarker(MainActivity.this,R.drawable.grocery_store,placeInfo.getPlaceName(),placeInfo.getPlaceLine().getLineStatus())));
            }
            Marker marker=googleMap.addMarker(markerOptions);
            marker.setTag(placeInfo.getPlaceId());



        }


    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }


//    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {
//
//        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_google_map_marker, null);
//
//        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
//        markerImage.setImageResource(resource);
//        TextView txt_name = (TextView)marker.findViewById(R.id.name);
//        txt_name.setText(_name);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
//        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
//        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
//        marker.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        marker.draw(canvas);
//
//        return bitmap;
//    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name,String placeStatus) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_view_bubble, null);

        ImageView markerImage = (ImageView) marker.findViewById(R.id.marker_icon);

        markerImage.setImageResource(resource);
        final RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams)markerImage.getLayoutParams();
        if(resource==R.drawable.pharmacy){
            Log.d(DIBAGING_TAG,"set margin");
            layoutparams.setMargins(36,27,0,0);
            markerImage.setLayoutParams(layoutparams);

        }
        else if(resource==R.drawable.grocery_store){
            layoutparams.setMargins(38,38,0,0);
            markerImage.setLayoutParams(layoutparams);
        }
        TextView txt_name = (TextView)marker.findViewById(R.id.marker_title);
        txt_name.setText(_name);

        TextView placeStatusTextView=(TextView)marker.findViewById(R.id.marker_status);
        placeStatusTextView.setText(placeStatus);
        if(placeStatus.equals("open"))
        placeStatusTextView.setTextColor(Color.GREEN);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }


}