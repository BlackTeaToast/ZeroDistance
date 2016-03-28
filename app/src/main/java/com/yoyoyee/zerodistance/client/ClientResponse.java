package com.yoyoyee.zerodistance.client;

/**
 * Created by p1235 on 2016/3/28.
 */
public interface ClientResponse {

    public void onResponse(String response);

    public void onErrorResponse(String response);

}

