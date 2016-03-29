package com.yoyoyee.zerodistance.client;

/**
 * Created by p1235 on 2016/3/28.
 */
public interface ClientResponse {

    void onResponse(String response);

    void onErrorResponse(String response);

}

