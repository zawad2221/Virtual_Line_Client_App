package com.dhakanewsclub.virtualline.add_place;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dhakanewsclub.virtualline.R;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

/**
 * Use the place picker functionality inside of the Places Plugin, to show UI for
 * choosing a map location. Once selected, return to the previous location with a
 * CarmenFeature to extract information from for whatever use that you want.
 */
public class PlaceSelectionActivity extends AppCompatActivity {
    String DIBAGING_TAG="DIBAGING_TAG";

    private static final int REQUEST_CODE = 5678;
    private TextView selectedLocationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_place_selection);

        selectedLocationTextView = findViewById(R.id.selected_location_info_textview);
        goToPickerActivity();
        pickerButtonOnclickListener();
    }

    /**
     * Set up the PlacePickerOptions and startActivityForResult
     */
    private void goToPickerActivity() {
        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken(getString(R.string.access_token))
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(23.73195, 90.378106)).zoom(16).build())
                                .build())
                        .build(this), REQUEST_CODE);
    }

    /**
     * This fires after a location is selected in the Places Plugin's PlacePickerActivity.
     * @param requestCode code that is a part of the return to this activity
     * @param resultCode code that is a part of the return to this activity
     * @param data the data that is a part of the return to this activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("PLACE_SELECTION","on result"+requestCode);
        if (resultCode == RESULT_CANCELED) {
// Show the button and set the OnClickListener()
//            Button goToPickerActivityButton = findViewById(R.id.go_to_picker_button);
//            goToPickerActivityButton.setVisibility(View.VISIBLE);
//            goToPickerActivityButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    Log.d(DIBAGING_TAG,"Activity palce_selection: picker button onClick");
//                    goToPickerActivity();
//                }
//            });

        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
// Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);

// Set the TextView text to the entire CarmenFeature. The CarmenFeature
// also be parsed through to grab and display certain information such as
// its placeName, text, or coordinates.
            if (carmenFeature != null) {
                String placeLocationData = carmenFeature.toJson();

                selectedLocationTextView.setText(String.format(
                        getString(R.string.selected_place_info), placeLocationData));
                passLocationDataToLocationInfoActivity(carmenFeature.toJson());

            }

        }
        if(requestCode == REQUEST_CODE && resultCode != RESULT_OK){
//            Intent main = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(main);
            finish();
            Log.d(DIBAGING_TAG,"finish");
            //onBackPressed();
        }

    }

    //extract location json data and put in intent and start locationInfoActivity
    private void passLocationDataToLocationInfoActivity(String locationData){
        JSONObject placeLocationDataJson=null;
        String country= null,region= null,place= null,longitude = null,latitude= null;

        ArrayList<String> countryRegionPlace= new ArrayList<>();

        try {
            //parse location data string to json
            //sample data {"type":"Feature","id":"poi.51539668186","geometry":{"coordinates":[90.377698,23.746604],"type":"Point"},"properties":{"landmark":true,"address":"Dhanmondi. lake side. next to rd.7 bridge","category":"cafe, coffee, tea, tea house","maki":"cafe"},"text":"Dingii Cafe","place_name":"Dingii Cafe, Dhanmondi. lake side. next to rd.7 bridge, Dhaka, Dhaka, Bangladesh","place_type":["poi"],"center":[90.377698,23.746604],"context":[{"id":"locality.6055103572176890","text":"Kalabagan"},{"id":"place.1406769746840860","text":"Dhaka","wikidata":"Q1354"},{"id":"region.9831996884466970","text":"Dhaka","short_code":"BD-C","wikidata":"Q330158"},{"id":"country.10043599301797340","text":"Bangladesh","short_code":"bd","wikidata":"Q902"}],"relevance":1.0}
            placeLocationDataJson=new JSONObject(locationData);
        } catch (JSONException e) {
            Log.d(DIBAGING_TAG,"can't parse location data to json. exception: "+e);

            e.printStackTrace();
        }
        try {
            //contain latitude and longitude data
            JSONArray coordinates= (JSONArray) ((JSONObject)placeLocationDataJson.get("geometry")).get("coordinates");
            //contain place region country
            JSONArray contextArray=(JSONArray) placeLocationDataJson.get("context");
            Log.d(DIBAGING_TAG,"context data: "+contextArray);

            // sample data for countryJson{"id":"country.10043599301797340","text":"Bangladesh","short_code":"bd","wikidata":"Q902"}
            JSONObject countryJson=(new JSONObject(contextArray.getString(contextArray.length()-1)));
            country= countryJson.getString("text");
            Log.d(DIBAGING_TAG,"country name: "+country);

            // sample data for regionJson{"id":"region.9831996884466970","text":"Dhaka","short_code":"BD-C","wikidata":"Q330158"}
            JSONObject regionJson=(new JSONObject(contextArray.getString(contextArray.length()-2)));
            region= regionJson.getString("text");
            Log.d(DIBAGING_TAG,"region name: "+region);

            // sample data for countryJson{"id":"place.1406769746840860","text":"Dhaka","wikidata":"Q1354"}
            JSONObject placeJson=(new JSONObject(contextArray.getString(contextArray.length()-3)));
            place= placeJson.getString("text");
            Log.d(DIBAGING_TAG,"place name: "+place);







            longitude = coordinates.getString(0);
            latitude = coordinates.getString(1);
            Log.d(DIBAGING_TAG,"coordinates data: "+coordinates);
            Log.d(DIBAGING_TAG,"longitude: "+longitude+" latitude: "+latitude);
        } catch (JSONException e) {
            Log.d(DIBAGING_TAG,"can't get location data from json. exception: "+e);
            e.printStackTrace();
        }


        Intent locationInfo = new Intent(this, SavePlaceInformationActivity.class);
        locationInfo.putExtra("LONGITUDE",longitude);
        locationInfo.putExtra("LATITUDE",latitude);
        locationInfo.putExtra("COUNTRY",country);
        locationInfo.putExtra("REGION",region);
        locationInfo.putExtra("PLACE",place);
        //Log.d(DIBAGING_TAG,"location json data "+placeLocationData);
        startActivity(locationInfo);

    }

    void pickerButtonOnclickListener(){
        Button goToPickerActivityButton = findViewById(R.id.go_to_picker_button);
        goToPickerActivityButton.setVisibility(View.VISIBLE);
        goToPickerActivityButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(DIBAGING_TAG,"Activity palce_selection: picker button onClick");
                goToPickerActivity();
//                Intent intent=new Intent(getApplication(),SavePlaceInformationActivity.class);
//                startActivity(intent);
            }
        });
    }

}