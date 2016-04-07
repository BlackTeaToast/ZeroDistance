package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by p1235 on 2016/3/30.
 */
public class GroupsTable {

    public static final String TABLE_NAME = "groups";

    public static final String KEY_ID = "id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_SCHOOL_ID = "school_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NEED_NUM = "need_num";
    public static final String KEY_CURRENT_NUM = "current_num";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_EXP_AT = "exp_at";
    public static final String KEY_IS_RUNNING = "is_running";
    public static final String KEY_IS_FINISHED = "is_finished";
    public static final String KEY_FINISHED_AT = "finished_at";

    public static final String CREATE_GROUPS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + KEY_ID + " INTEGER NOT NULL PRIMARY KEY, "
            + KEY_USER_ID + " varchar(30) NOT NULL, "
            + KEY_SCHOOL_ID + " int(3) NOT NULL, "
            + KEY_TITLE + " varchar(60) NOT NULL, "
            + KEY_NEED_NUM + " int(10) NOT NULL, "
            + KEY_CURRENT_NUM + " int(10) NOT NULL DEFAULT '0', "
            + KEY_CONTENT + " TEXT , "
            + KEY_CREATED_AT + " datetime NOT NULL, "
            + KEY_EXP_AT + " datetime NOT NULL, "
            + KEY_IS_RUNNING + " tinyint(1) NOT NULL DEFAULT '0', "
            + KEY_IS_FINISHED + " tinyint(1) NOT NULL DEFAULT '0', "
            + KEY_FINISHED_AT + " datetime DEFAULT NULL)";

}
