package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by p1235 on 2016/4/11.
 */
public class MissionAcceptUserTable {

    public static final String TABLE_NAME = "mission_accept_user";

    public static final String KEY_MISSION_ID = "mission_id";
    public static final String KEY_USER_UID = "user_uid";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_ACCEPTED_AT = "accepted_at";

    public static final int COLUMNS_NUM_MISSION_ID = 0;
    public static final int COLUMNS_NUM_USER_UID = 1;
    public static final int COLUMNS_NUM_USER_NAME = 2;
    public static final int COLUMNS_NUM_ACCEPTED_AT = 3;

    public static final String CREATE_MISSION_ACCEPT_USER_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + KEY_MISSION_ID + " INTEGER NOT NULL, "
            + KEY_USER_UID + " TEXT NOT NULL, "
            + KEY_USER_NAME + " TEXT NOT NULL, "
            + KEY_ACCEPTED_AT + " DATETIME NOT NULL) ";

}
