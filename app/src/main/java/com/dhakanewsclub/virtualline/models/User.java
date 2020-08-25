package com.dhakanewsclub.virtualline.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";
    private String userId,name,phoneNumber,password;



    private String[] permission;
    private ArrayList<User> userType;

    public User(String name) {
        this.name = name;
    }

    public User(String userId, String name, String phoneNumber, ArrayList<User> userType) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.userId=userId;
    }

    public User(JSONObject user){
        try {
            String name=user.getString("name");
            String userId=user.getString("userId");
            String phoneNumber=user.getString("phoneNumber");
            JSONArray permission = user.getJSONArray("permission");
            for(int index=0;index<permission.length();index++){
                String typeId = permission.getJSONObject(index).getJSONObject("permission").getString("typeId");
                String typeName = permission.getJSONObject(index).getJSONObject("permission").getString("typeName");
                Log.d(DIBAGING_TAG,"user permission name "+typeName);
            }


        }
        catch (JSONException e){
            e.printStackTrace();
            Log.d(DIBAGING_TAG,"error in parsing response data");
        }


    }

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<User> getUserType() {
        return userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String[] getPermission() {
        return permission;
    }

    public void setPermission(String[] permission) {
        this.permission = permission;
    }
}
