package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by p1235 on 2016/3/30.
 */
public class LoginTable {

    // Login table name
    public static final String TABLE_NAME = "user";

    // Login Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_UID = "uid";
    public static final String KEY_CREATED_AT = "created_at";

    public static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
            + KEY_CREATED_AT + " TEXT" + ")";


}
