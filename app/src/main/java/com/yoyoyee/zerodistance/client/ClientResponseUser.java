package com.yoyoyee.zerodistance.client;

import com.yoyoyee.zerodistance.helper.datatype.User;

public interface ClientResponseUser {

    void onResponse(User[] users);

    void onErrorResponse(int errorCode);

}