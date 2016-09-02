package com.yoyoyee.zerodistance.client;

/**
 * Created by red on 2016/9/1.
 */
public interface ClientResponseInteger {

    void onResponse(int statusCode);

    void onErrorResponse(int errorCode);

}