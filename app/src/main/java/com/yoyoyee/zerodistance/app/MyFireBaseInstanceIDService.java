package com.yoyoyee.zerodistance.app;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by futur on 2016/7/6.
 */
public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();

        registerToken(token);
    }

    private void registerToken(String token) {
        OkHttpClient clinet = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/testfcm")
                .post(body)
                .build();
        try{
            clinet.newCall(request).execute();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
