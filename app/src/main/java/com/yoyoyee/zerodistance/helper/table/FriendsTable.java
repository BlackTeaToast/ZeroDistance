package com.yoyoyee.zerodistance.helper.table;

/**
 * Created by red on 2016/8/17.
 */
public class FriendsTable {

    public static final String TABLE_NAME = "friends";

    // Login Table Columns names
    public static final String KEY_UID = "uid";
    public static final String KEY_IS_TEACHER = "is_teacher";
    public static final String KEY_NAME = "name";
    public static final String KEY_NICKNAME = "nick_name";
    public static final String KEY_SCHOOL_ID = "school_id";
    public static final String KEY_STUDENT_ID = "student_id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFESSION = "profession";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_EXP = "exp";
    public static final String KEY_MONEY = "money";
    public static final String KEY_STRENGTH = "strength";
    public static final String KEY_INTELLIGENCE = "intelligence";
    public static final String KEY_AGILE = "agile";
    public static final String KEY_INTRODUCTION = "introduction";
    public static final String KEY_IS_ACCEPTED = "is_accepted";
    public static final String KEY_IS_INVITE = "is_invite";

    public static final int COLUMNS_NUM_UID = 0;
    public static final int COLUMNS_NUM_IS_TEACHER = 1;
    public static final int COLUMNS_NUM_NAME = 2;
    public static final int COLUMNS_NUM_NICKNAME = 3;
    public static final int COLUMNS_NUM_SCHOOL_ID = 4;
    public static final int COLUMNS_NUM_STUDENT_ID = 5;
    public static final int COLUMNS_NUM_EMAIL = 6;
    public static final int COLUMNS_NUM_PROFESSION = 7;
    public static final int COLUMNS_NUM_LEVEL = 8;
    public static final int COLUMNS_NUM_EXP = 9;
    public static final int COLUMNS_NUM_MONEY = 10;
    public static final int COLUMNS_NUM_STRENGTH = 11;
    public static final int COLUMNS_NUM_INTELLIGENCE = 12;
    public static final int COLUMNS_NUM_AGILE = 13;
    public static final int COLUMNS_NUM_INTRODUCTION = 14;
    public static final int COLUMNS_NUM_IS_ACCEPTED = 15;
    public static final int COLUMNS_NUM_IS_INVITE = 16;

    public static final String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_UID + " TEXT,"
            + KEY_IS_TEACHER + " BOOLEAN,"
            + KEY_NAME + " TEXT,"
            + KEY_NICKNAME + " TEXT,"
            + KEY_SCHOOL_ID + " INTEGER,"
            + KEY_STUDENT_ID + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PROFESSION + " INTEGER,"
            + KEY_LEVEL + " INTEGER,"
            + KEY_EXP + " INTEGER,"
            + KEY_MONEY + " INTEGER,"
            + KEY_STRENGTH + " INTEGER,"
            + KEY_INTELLIGENCE + " INTEGER,"
            + KEY_AGILE + " INTEGER,"
            + KEY_INTRODUCTION + " TEXT,"
            + KEY_IS_ACCEPTED + " BOOLEAN,"
            + KEY_IS_INVITE + " BOOLEAN" + ")";

}
