package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by p1235 on 2016/3/30.
 */
public class MissionsTable {

    public static final String TABLE_NAME = "missions";

    public static final String KEY_ID = "id";
    public static final String KEY_USER_UID = "user_uid";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_SCHOOL_ID = "school_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IS_URGENT = "is_urgent";
    public static final String KEY_NEED_NUM = "need_num";
    public static final String KEY_CURRENT_NUM = "current_num";
    public static final String KEY_PLACE = "place";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_REWARD = "reward";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_EXP_AT = "exp_at";
    public static final String KEY_IS_RUNNING = "is_running";
    public static final String KEY_IS_FINISHED = "is_finished";
    public static final String KEY_FINISHED_AT = "finished_at";

    public static final int COLUMNS_NUM_ID = 0;
    public static final int COLUMNS_NUM_USER_UID = 1;
    public static final int COLUMNS_NUM_USER_NAME = 2;
    public static final int COLUMNS_NUM_SCHOOL_ID = 3;
    public static final int COLUMNS_NUM_TITLE = 4;
    public static final int COLUMNS_NUM_IS_URGENT = 5;
    public static final int COLUMNS_NUM_NEED_NUM = 6;
    public static final int COLUMNS_NUM_CURRENT_NUM = 7;
    public static final int COLUMNS_NUM_PLACE = 8;
    public static final int COLUMNS_NUM_CONTENT = 9;
    public static final int COLUMNS_NUM_REWARD = 10;
    public static final int COLUMNS_NUM_CREATED_AT = 11;
    public static final int COLUMNS_NUM_EXP_AT = 12;
    public static final int COLUMNS_NUM_IS_RUNNING = 13;
    public static final int COLUMNS_NUM_IS_FINISHED = 14;
    public static final int COLUMNS_NUM_FINISHED_AT = 15;

    public static final String CREATE_MISSIONS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + KEY_ID + " INTEGER NOT NULL PRIMARY KEY, "
            + KEY_USER_UID + " TEXT NOT NULL, "
            + KEY_USER_NAME + " TEXT NOT NULL, "
            + KEY_SCHOOL_ID + " INTEGER NOT NULL, "
            + KEY_TITLE + " TEXT NOT NULL, "
            + KEY_IS_URGENT + " tinyint(1) NOT NULL DEFAULT '0', "
            + KEY_NEED_NUM + " INTEGER NOT NULL, "
            + KEY_CURRENT_NUM + " INTEGER NOT NULL DEFAULT '0', "
            + KEY_PLACE + " TEXT, "
            + KEY_CONTENT + " TEXT , "
            + KEY_REWARD + " TEXT DEFAULT NULL, "
            + KEY_CREATED_AT + " datetime NOT NULL, "
            + KEY_EXP_AT + " datetime NOT NULL, "
            + KEY_IS_RUNNING + " tinyint(1) NOT NULL DEFAULT '0', "
            + KEY_IS_FINISHED + " tinyint(1) NOT NULL DEFAULT '0', "
            + KEY_FINISHED_AT + " datetime DEFAULT NULL)";
}
