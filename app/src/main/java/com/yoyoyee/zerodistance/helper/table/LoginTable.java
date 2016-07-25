package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by p1235 on 2016/3/30.
 */
public class LoginTable {

    // Login table name
    public static final String TABLE_NAME = "user";

    // Login Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_IS_TEACHER = "is_teacher";
    public static final String KEY_NAME = "name";
    public static final String KEY_NICKNAME = "nick_name";
    public static final String KEY_SCHOOL_ID = "school_id";
    public static final String KEY_STUDENT_ID = "student_id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_UID = "uid";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_ACCESS_KEY = "access_key";
    public static final String KEY_IS_CONFIRMED_KEY = "is_confirmed";

    public static final int COLUMNS_NUM_ID = 0;
    public static final int COLUMNS_NUM_IS_TEACHER = 1;
    public static final int COLUMNS_NUM_NAME = 2;
    public static final int COLUMNS_NUM_NICKNAME = 3;
    public static final int COLUMNS_NUM_SCHOOL_ID = 4;
    public static final int COLUMNS_NUM_STUDENT_ID = 5;
    public static final int COLUMNS_NUM_EMAIL = 6;
    public static final int COLUMNS_NUM_UID = 7;
    public static final int COLUMNS_NUM_CREATED_AT = 8;
    public static final int COLUMNS_NUM_ACCESS_KEY = 9;
    public static final int COLUMNS_NUM_IS_CONFIRM_KEY = 10;

    public static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IS_TEACHER + " BOOLEAN," + KEY_NAME
            + " TEXT," + KEY_NICKNAME + " TEXT," + KEY_SCHOOL_ID + " INTEGER,"
            + KEY_STUDENT_ID + " TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
            + KEY_CREATED_AT + " DATETIME," + KEY_ACCESS_KEY + " TEXT,"
            + KEY_IS_CONFIRMED_KEY + " INTEGER" + ")";

}
