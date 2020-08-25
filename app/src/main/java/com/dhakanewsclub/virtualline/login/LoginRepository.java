package com.dhakanewsclub.virtualline.login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.dhakanewsclub.virtualline.home.MainActivity;
import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.models.retrofit.Permission;
import com.dhakanewsclub.virtualline.models.retrofit.UserInfo;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepository  {
    private static final String DIBAGING_TAG = "DIBAGING_TAG";

    private static LoginRepository instance;


    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();

        }
        return instance;
    }

    public ProgressDialog showDialog(Context applicationContext){
        final ProgressDialog progressDialog = new ProgressDialog(applicationContext,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        return progressDialog;
    }

    public void login(Context applicationContext, UserInfo user){

        final ProgressDialog progressDialog=showDialog(applicationContext);

        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:8000/system_user/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        LoginRetrofit loginRetrofit=retrofit.create(LoginRetrofit.class);
        Call<UserInfo> call = loginRetrofit.createAccount(user);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                progressDialog.dismiss();
                String r=new Gson().toJson(response.body());
                Log.d(DIBAGING_TAG,"response :"+r);
                if(r.equals("null")){
                    Toast failToast=Toast.makeText(applicationContext,"login failed",Toast.LENGTH_LONG);

                    failToast.show();
                    return;
                }

                Log.d(DIBAGING_TAG,"login success"+response.code());
                Log.d(DIBAGING_TAG,"login success"+new Gson().toJson(response.body()));

                if(user.getPhoneNumber().equals(response.body().getPhoneNumber())){
                    Toast.makeText(applicationContext,"login successful",Toast.LENGTH_LONG).show();
                    saveLoginData(applicationContext,response);
                    startMainActivity(applicationContext);
                }

//                Log.d(DIBAGING_TAG,"login success"+response.body().getUserId());
//                Log.d(DIBAGING_TAG,"login success"+response.body().getUserId());

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Log.d(DIBAGING_TAG,"login failed"+t.getMessage());
                Toast.makeText(applicationContext,"login failed",Toast.LENGTH_LONG).show();
                t.getMessage();
                progressDialog.dismiss();

            }
        });

    }

    public void saveLoginData(Context applicationContext, Response response){
        UserInfo loginUserInfo=(UserInfo) response.body();
        Log.d(DIBAGING_TAG,"save login data user id: "+loginUserInfo.getName());
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("login_data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("loginUserId",loginUserInfo.getUserId().toString());
        editor.putString("loginUserName",loginUserInfo.getName());
        editor.putString("loginUserPhoneNumber",loginUserInfo.getPhoneNumber());
        int index=1;
        for(Permission permission: loginUserInfo.getPermission()){
            String p="loginUserPermission"+index++;
            editor.putString(p,permission.getUserType().getTypeName());
        }
        editor.apply();



    }

    public void startMainActivity(Context context){
        Intent main = new Intent(context, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(main);
    }


//    private void loginResponseData(JSONObject response){
//        User loginUser= new User(response);
//    }

//    public void login(Context applicationContext, User user)  {
//        JSONObject jsonObject = new JSONObject();
//        String urls="http://10.0.2.2:8000/system_user/login/";
//        try {
//            jsonObject.put("phoneNumber", user.getPhoneNumber());
//            jsonObject.put("password", user.getPassword());
//        }
//        catch (JSONException e){
//            e.printStackTrace();
//        }
//        ByteArrayEntity entity = null;
//        try{
//            entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
//            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//        }
//        catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
//
//
//
//
//        AsyncHttpClient client= new AsyncHttpClient();
//        client.post(applicationContext,urls,entity,"application/json",new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.d(DIBAGING_TAG,"login success"+response);
//                Intent main = new Intent(applicationContext, MainActivity.class);
//                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                applicationContext.startActivity(main);
//                //((Activity)applicationContext).finish();
//                loginResponseData(response);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d(DIBAGING_TAG,"error login "+errorResponse);
//            }
//        });
//    }
}

