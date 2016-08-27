package com.yoyoyee.zerodistance.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.table.SchoolsTable;

import java.util.ArrayList;

/**
 * Created by futur on 2016/8/16.
 * In this class,it save the notigication's ID that we don't to member by ourself.
 */
public class NotificationID {
    //updata's notification ID
    public static final int UPDATA_ID = -1;
    //normal's notification ID
    public static final int NORMAL_ID = -2;
    //Friend notification ID
    public static final int FRIEND_ID = -3;
    //stack friend
    public static final String TABLE_NAME = "FriendList";
    public static final String NICKNAME = "nickName";
    public static final String NUMBER = "number";
    private static final SQLiteHandler DB = AppController.getDB();

    public static final String CREATE_TABLE = "CREATE TABLE " +TABLE_NAME +
            "("+NICKNAME+" TEXT)";

    public void putNameList(String put) {
       DB.putFriendList(put);
        getNameList();

    }
    public String[] getNameList(){
        ArrayList<String> s;
        s=DB.getFriendList();
        String list[]=new String[s.size()];
        for (int i=0;i<s.size();i++)
        {
            list[i]=s.get(i);
        }
        Log.d("Notification :",String.valueOf(s.size())+"reload");
        return list;
    }
    public void deleteFriendList(){
        DB.friendListDelete();
    }
}
