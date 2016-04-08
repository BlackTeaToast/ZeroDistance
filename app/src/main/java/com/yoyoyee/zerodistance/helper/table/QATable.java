package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by p1235 on 2016/4/9.
 */
public class QATable {

    public static final String TABLE_NAME = "questions_and_answers";

    public static final String KEY_ID = "id";
    public static final String KEY_USER_UID = "user_uid";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_IS_ANSWERED = "is_answered";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_ANSWERED_AT = "answered_at";

    public static final int COLUMNS_NUM_ID = 0;
    public static final int COLUMNS_NUM_USER_UID = 1;
    public static final int COLUMNS_NUM_USER_NAME = 2;
    public static final int COLUMNS_NUM_QUESTION = 3;
    public static final int COLUMNS_NUM_ANSWER = 4;
    public static final int COLUMNS_NUM_IS_ANSWERED = 5;
    public static final int COLUMNS_NUM_CREATED_AT = 6;
    public static final int COLUMNS_NUM_ANSWERED_AT = 7;


    public static final String CREATE_QA_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + KEY_ID + " INTEGER NOT NULL PRIMARY KEY, "
            + KEY_USER_UID + " TEXT NOT NULL, "
            + KEY_USER_NAME + " TEXT NOT NULL, "
            + KEY_QUESTION + " TEXT NOT NULL, "
            + KEY_ANSWER + " TEXT DEFAULT NULL, "
            + KEY_IS_ANSWERED + " INTEGER NOT NULL DEFAULT'0', "
            + KEY_CREATED_AT + " DATETIME NOT NULL, "
            + KEY_ANSWERED_AT + " DATETIME NOT NULL)";

}
