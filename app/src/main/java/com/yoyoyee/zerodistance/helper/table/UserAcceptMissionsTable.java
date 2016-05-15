package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by p1235 on 2016/5/15.
 */
public class UserAcceptMissionsTable {

    public static final String TABLE_NAME = "user_accept_missions";

    public static final String KEY_MISSION_ID = "mission_id";
    public static final String KEY_ACCEPTED_AT = "accepted_at";

    public static final int COLUMNS_NUM_MISSION_ID = 0;
    public static final int COLUMNS_NUM_ACCEPTED_AT = 1;

    public static final String CREATE_USER_ACCEPT_MISSIONS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + KEY_MISSION_ID + " INTEGER NOT NULL, "
            + KEY_ACCEPTED_AT + " datetime NOT NULL)";

}
