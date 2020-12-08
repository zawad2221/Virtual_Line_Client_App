package com.dhakanewsclub.virtualline.home;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.adapter.PlaceTypeOptionRecyclerViewAdapter;
import com.dhakanewsclub.virtualline.add_place.PlaceSelectionActivity;
import com.dhakanewsclub.virtualline.customer_place_view.PlaceActivity;
import com.dhakanewsclub.virtualline.customer_place_view.SelectedPlaceData;
import com.dhakanewsclub.virtualline.login.LoginActivity;
import com.dhakanewsclub.virtualline.login.LoginUserData;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceType;
import com.dhakanewsclub.virtualline.models.retrofit.UserType;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;
import com.dhakanewsclub.virtualline.my_place_list.PlaceListActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private static final int REQUEST_CODE_FINE_LOCATION = 1234;

    //
    private static final int REQUEST_CODE_GOOGLE_PLAY_SERVECES_ERROR = -1;
    private static final double EARTH_RADIOUS = 3958.75; // Earth radius;
    private static final int METER_CONVERSION = 1609;

    private TextView mDrawerHerderUserName;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private PlaceTypeOptionRecyclerViewAdapter mPlaceTypeOptionRecyclerViewAdapter;

    //location info
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;

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


        Log.d(DIBAGING_TAG, "on create main");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermission();


        //mHomeViewMode= ViewModelProviders.of(this).get(HomeViewMode.class);
        mHomeViewMode = new ViewModelProvider(this, new HomeViewModelFactory(this.getApplication(), "test")).get(HomeViewMode.class);
        mHomeViewMode.initAllPlace(this);
        mHomeViewMode.initPlaceType(this);
        placeListOnChange();
        placeTypeListOnChange();
        initRecyclerView();

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        //Mapbox.getInstance(this, getString(R.string.access_token));
        //google map
        initializerMap();


//        mapView = findViewById(R.id.mapView);
//        mapView.onCreate(savedInstanceState);

//        initializerMap();

        //getSupportActionBar().hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = findViewById(R.id.navigation_view);


        //initiate header view
        View drawerHerderView = mNavigationView.getHeaderView(0);
        mDrawerHerderUserName = drawerHerderView.findViewById(R.id.drawer_header_user_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        //Intent picklocation = new Intent(this, LoginActivity.class);
        menuItemSelectListener();
        boolean login = ifLogin();
        if (login) {
            hideLoginOption();
            setUserNameInDrawer();
        } else {
            hideLogoutOption();
        }

        //startActivity(picklocation);

    }

    private void initRecyclerView() {
        mRecyclerView=findViewById(R.id.place_type_option_recycler_view);
        List<PlaceType> placeTypes = new ArrayList<>();
        PlaceType placeType=new PlaceType(1,"Pharmacy");
        PlaceType placeType1=new PlaceType(1,"Pharmacy");
        PlaceType placeType2=new PlaceType(1,"Pharmacy");
        PlaceType placeType3=new PlaceType(1,"Pharmacy");
        PlaceType placeType4=new PlaceType(1,"Pharmacy");
        PlaceType placeType5=new PlaceType(1,"Pharmacy");

        placeTypes.add(placeType);
        placeTypes.add(placeType1);
        placeTypes.add(placeType2);
        placeTypes.add(placeType3);
        placeTypes.add(placeType4);
        placeTypes.add(placeType5);
        mPlaceTypeOptionRecyclerViewAdapter=new PlaceTypeOptionRecyclerViewAdapter(placeTypes, new PlaceTypeOptionRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(DIBAGING_TAG,"place type recycler view item click: "+position);

                LatLng cameraLocation = getCameraPositionLocation();


            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView.setAdapter(mPlaceTypeOptionRecyclerViewAdapter);

    }

    private LatLng getCameraPositionLocation(){
        //get camera position location
        CameraPosition cameraPosition=gMap.getCameraPosition();
        float zoomLevel = cameraPosition.zoom;
        VisibleRegion visibleRegion = gMap.getProjection().getVisibleRegion();
        LatLng nearLeft = visibleRegion.nearLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng farRight = visibleRegion.farRight;
        Log.d(DIBAGING_TAG,"camera move, \nnearLeft:"+nearLeft+" \nnearRigth: "+nearRight+" \nfarLeft: "
                +farLeft+" \nfarRight: "+farRight);
        LatLng latLng = new LatLng(nearLeft.latitude,farRight.longitude);
        return latLng;
    }

    private void placeTypeRecyclerViewOnClick(){

    }

    private void placeTypeListOnChange() {
        mHomeViewMode.getListMutableLiveDataPlaceType().observe(this, new Observer<List<PlaceType>>() {
            @Override
            public void onChanged(List<PlaceType> placeTypeList) {
                mPlaceTypeOptionRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initializerMap() {
        supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

    }

    private void placeListOnChange() {
        mHomeViewMode.getAllPlace().observe(this, new Observer<List<PlaceInfo>>() {
            @Override
            public void onChanged(List<PlaceInfo> placeInfos) {
                Log.d(DIBAGING_TAG, "place list loaded");
                initializerMap();

            }
        });
    }


    private void setUserNameInDrawer() {
        try {

            mDrawerHerderUserName.setText(LoginUserData.loginUserInfo.getName());
        } catch (Exception e) {
            Log.d(DIBAGING_TAG, "error adding user name in header error :" + e.getMessage());
        }

    }


    private void hideLoginOption() {
        Menu nav_Menu = mNavigationView.getMenu();
        nav_Menu.findItem(R.id.login).setVisible(false);
    }

    private void hideLogoutOption() {
        Menu nav_Menu = mNavigationView.getMenu();
        nav_Menu.findItem(R.id.logout).setVisible(false);
    }

    private void clearLoginData() {
        LoginUserData.login = false;
        LoginUserData.loginUserType = null;
        LoginUserData.loginUserInfo = null;
        SharedPreferences sharedPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

    }


    private boolean ifLogin() {
        UserInfo userInfo = new UserInfo();
        ArrayList<UserType> userTypeList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE);
        try {
            String userId = sharedPreferences.getString("loginUserId", "0");
            if (!userId.equals("0")) {


                userInfo.setUserId(Integer.parseInt(userId));
                userInfo.setPhoneNumber(sharedPreferences.getString("loginUserPhoneNumber", "0"));
                userInfo.setName(sharedPreferences.getString("loginUserName", "0"));

                for (int index = 1; index <= 3; index++) {
                    UserType userType = new UserType();
                    String typeName = sharedPreferences.getString("loginUserPermission" + index, "0");
                    if (typeName.equals("0")) {
                        break;
                    }
                    userType.setTypeName(typeName);

                    userTypeList.add(userType);
                }
                LoginUserData.login = true;
                LoginUserData.loginUserInfo = userInfo;
                LoginUserData.loginUserType = userTypeList;
                Log.d(DIBAGING_TAG, "main activity getShared data username " + LoginUserData.loginUserInfo.getName());
                return true;

            }
        } catch (Exception e) {
            Log.d(DIBAGING_TAG, "exception in getting shared pref data");
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

    private void markerOnClick() {
//        mMapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(@NonNull Marker marker) {
//                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
    }

    private void starLoginActivity() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);

        startActivity(loginIntent);
    }

    private void menuItemSelectListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.d("MAIN_ACTIVITY", "menu on select");
                switch (menuItem.getItemId()) {
                    case R.id.checked_place:
                        mDrawerLayout.closeDrawer(GravityCompat.START, true);

                        break;
                    case R.id.login:

                        mDrawerLayout.closeDrawer(GravityCompat.START, true);
                        starLoginActivity();
                        break;
                    case R.id.my_place:
                        mDrawerLayout.closeDrawer(GravityCompat.START, true);
                        if (!LoginUserData.login) {
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


    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void checkGpsStatus() {
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (!hasGPSDevice(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(DIBAGING_TAG, "Gps not enabled");
            Toast.makeText(MainActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Log.d(DIBAGING_TAG, "Gps already enabled");
            Toast.makeText(MainActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
            //LatLng latLng=getLastLocation();
        }

    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);

                                //finish();
                                Log.d(DIBAGING_TAG, "gps on on result, ");
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION) {
            Log.d(DIBAGING_TAG, "result for gps on dialog " + resultCode);
            switch (resultCode) {
                case -1:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LatLng latLng = getLastLocation();
                        }
                    }, 3000);

                    return;
                default:
                    return;
            }
        }
    }

    private LatLng getLastLocation() {

        final LatLng[] latLng = new LatLng[1];
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Task<Location> locationTask = mFusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                try {
                    latLng[0] =new LatLng(location.getLatitude(),location.getLongitude());
                    Log.d(DIBAGING_TAG,"last location got: "+ latLng[0]);

                    //move and zoom the map in last location
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng[0]));
                    gMap.animateCamera(CameraUpdateFactory.zoomTo(12),2000,null);
                }
                catch (Exception e){
                    latLng[0]=null;
                    Log.d(DIBAGING_TAG,"exception to get last location");
                }

            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(DIBAGING_TAG,"failed to get last location");
                latLng[0]=null;
            }
        });
        return latLng[0];
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(DIBAGING_TAG, "map initialized");
        gMap=googleMap;


        LatLng zoomLocation = new LatLng(23.732817, 90.384439);

//        MarkerOptions markerDhaka= new MarkerOptions()
//                .position(zoomLocation)
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


        if(isLocationPermissionGranted()){
            LatLng lastLocation=getLastLocation();
            Log.d(DIBAGING_TAG,"last location got: "+ lastLocation);
            if(lastLocation!=null){
                zoomLocation=lastLocation;
            }
        }
        //gMap.moveCamera(CameraUpdateFactory.newLatLng(zoomLocation));
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
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(DIBAGING_TAG,"map clicked , location :"+latLng);
            }
        });
        gMap.setOnCameraChangeListener(cameraPosition -> {
            float zoomLevel = cameraPosition.zoom;
            VisibleRegion visibleRegion = gMap.getProjection().getVisibleRegion();
            LatLng nearLeft = visibleRegion.nearLeft;
            LatLng nearRight = visibleRegion.nearRight;
            LatLng farLeft = visibleRegion.farLeft;
            LatLng farRight = visibleRegion.farRight;
            Log.d(DIBAGING_TAG,"camera move, \nnearLeft:"+nearLeft+" \nnearRigth: "+nearRight+" \nfarLeft: "
                    +farLeft+" \nfarRight: "+farRight);


//            double dist_w = distanceFrom(nearLeft.latitude, nearLeft.longitude, nearRight.latitude, nearRight.longitude);
//            double dist_h = distanceFrom(farLeft.latitude, farLeft.longitude, farRight.latitude, farRight.longitude);
//            Log.d(DIBAGING_TAG, "DISTANCE WIDTH: " + dist_w + " DISTANCE HEIGHT: " + dist_h);
        });
    }
//    public double distanceFrom(double lat1, double lng1, double lat2, double lng2)
//    {
//        // Return distance between 2 points, stored as 2 pair location;
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLng = Math.toRadians(lng2 - lng1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
//                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double dist = EARTH_RADIOUS * c;
//        return new Double(dist * METER_CONVERSION).floatValue();
//    }

    private void locationPermissionDialog(){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_FINE_LOCATION);
    }



    private void askPermissionDialogForPerformance(){
        try{
            SharedPreferences sharedPreferences=getSharedPreferences("location_permission",MODE_PRIVATE);
            String showDialog=sharedPreferences.getString("show_permission_dialog","true");
            if(showDialog.equals("false")){
                Log.d(DIBAGING_TAG,"do not show location permission dialog");
                return;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("we need location permission for better experience")
                .setCancelable(false)
                .setMultiChoiceItems(new String[]{"Do not ask again"}, new boolean[]{false}, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                        if(isChecked){
                            SharedPreferences sharedPreferences=MainActivity.this.getApplicationContext().getSharedPreferences("location_permission",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("show_permission_dialog","false");
                            editor.apply();
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        locationPermissionDialog();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }
    boolean isLocationPermissionGranted(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else return false;
    }
    private void requestPermission(){


        if(!isLocationPermissionGranted()){


            //permission is not granted for location
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                //if denied first time
                askPermissionDialogForPerformance();
                //locationPermissionDialog();
            }
            else {

                //locationPermissionDialog();
                askPermissionDialogForPerformance();
            }
        }
        else {
            //permission is granted for location
//            turnGPSOn();
            checkGpsStatus();

        }
    }

    private void goToPermissionSetting(){
        Intent settingIntent=new Intent();
        settingIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri= Uri.fromParts("package",getPackageName(),null);
        settingIntent.setData(uri);
        startActivity(settingIntent);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_FINE_LOCATION){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //permission granted
                Log.d(DIBAGING_TAG,"permission granted");
                checkGpsStatus();
            }
            else {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    //askPermissionDialogForPerformance();
                    //permission denied permanently
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("You have denied this permission permanently, to grant this permission go to setting and enable this permission")
                            .setCancelable(false)
                            .setPositiveButton("Go to setting", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    goToPermissionSetting();
                                }
                            })
                            .setNegativeButton("Cancle",null)
                            .show();
                }
                else {
                    //permission not granted
                    //askPermissionDialogForPerformance();
                }
            }
        }
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