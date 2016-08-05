package com.yoyoyee.zerodistance.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.GroupAccept;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.MissionAccept;
import com.yoyoyee.zerodistance.helper.datatype.QA;
import com.yoyoyee.zerodistance.helper.datatype.School;
import com.yoyoyee.zerodistance.helper.datatype.UserAcceptGroups;
import com.yoyoyee.zerodistance.helper.datatype.UserAcceptMissions;
import com.yoyoyee.zerodistance.helper.table.GroupAcceptUserTable;
import com.yoyoyee.zerodistance.helper.table.GroupsTable;
import com.yoyoyee.zerodistance.helper.table.LoginTable;
import com.yoyoyee.zerodistance.helper.table.MissionAcceptUserTable;
import com.yoyoyee.zerodistance.helper.table.MissionsTable;
import com.yoyoyee.zerodistance.helper.table.QATable;
import com.yoyoyee.zerodistance.helper.table.SchoolsTable;
import com.yoyoyee.zerodistance.helper.table.UserAcceptGroupsTable;
import com.yoyoyee.zerodistance.helper.table.UserAcceptMissionsTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by p1235 on 2016/4/7.
 */
public class QueryFunctions {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final SQLiteHandler DB = AppController.getDB();

    /**
     * Storing user details in database
     * */
    public static void addUser(String isTeacher ,String name, String nickName, String schoolID,
                        String studentID, String email, String uid, String created_at,
                        String access_key, String isConfirmed) {

        SQLiteDatabase db = DB.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(LoginTable.KEY_IS_TEACHER, isTeacher);
            values.put(LoginTable.KEY_NAME, name); // Name
            values.put(LoginTable.KEY_NICKNAME, nickName);
            values.put(LoginTable.KEY_SCHOOL_ID, schoolID);
            values.put(LoginTable.KEY_STUDENT_ID, studentID);
            values.put(LoginTable.KEY_EMAIL, email); // Email
            values.put(LoginTable.KEY_UID, uid); // Email
            values.put(LoginTable.KEY_CREATED_AT, created_at); // Created At
            values.put(LoginTable.KEY_ACCESS_KEY, access_key); // Created At
            values.put(LoginTable.KEY_IS_CONFIRMED_KEY, isConfirmed); // Created At

            // Inserting Row
            long id = db.insert(LoginTable.TABLE_NAME, null, values);

            Log.d(TAG, "New user inserted into SQLite: " + id);
        } catch (Exception e) {
            throw e;
        } finally {
            db.close(); // Closing database connection
        }

    }

    /**
     * Getting user data from database
     * */
    public static HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + LoginTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        //Log.d(TAG, "getUserDetails: "+db.rawQuery("SELECT * FROM "+TABLE_USER ,null));
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getCount(); i++){
            Log.d(TAG, "getUserDetails: "+cursor.getString(0)+", "+cursor.getString(1)+", "+cursor.getString(2)+", "+cursor.getString(3)+", "+cursor.getString(4));
            cursor.moveToNext();
        }
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(LoginTable.COLUMNS_NUM_NAME));
            user.put("email", cursor.getString(LoginTable.COLUMNS_NUM_EMAIL));
            user.put("uid", cursor.getString(LoginTable.COLUMNS_NUM_UID));
            user.put("created_at", cursor.getString(LoginTable.COLUMNS_NUM_CREATED_AT));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from SQLite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public static void deleteUsers() {
        SQLiteDatabase db = DB.getWritableDatabase();
        // Delete All Rows
        db.delete(LoginTable.TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from SQLite");
    }

    public static void updateSchools(ArrayList<School> schoolList) {
        SQLiteDatabase db = DB.getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(SchoolsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<schoolList.size(); i++) {
                values.put(SchoolsTable.KEY_ID, schoolList.get(i).id); // Name
                values.put(SchoolsTable.KEY_AREA, schoolList.get(i).area); // Email
                values.put(SchoolsTable.KEY_COUNTY, schoolList.get(i).county); // Email
                values.put(SchoolsTable.KEY_NAME, schoolList.get(i).name); // Created At
                // Inserting Row
                db.insert(SchoolsTable.TABLE_NAME, null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateSchools: " + e.toString());
        } finally {
            db.endTransaction();
        }


        Log.d(TAG, "Updated all schools info from SQLite");
        db.close();

    }

    public static ArrayList<School> getSchools() {

        ArrayList<School> schools = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + SchoolsTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            schools.add(new School(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3)));
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return schools;
    }

    public static ArrayList<String> getSchoolsArea() {

        ArrayList<String> area = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT area FROM " + SchoolsTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            //Log.d(TAG, "getSchoolsArea: " + cursor.getString(0));
            area.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return area;
    }

    public static ArrayList<String> getSchoolsCounty(String area) {

        ArrayList<String> county = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT county FROM " + SchoolsTable.TABLE_NAME +
                " WHERE area = ?";

        SQLiteDatabase db = DB.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[] {area});
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){

            county.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return county;
    }

    public static ArrayList<String> getSchoolsName(String county) {

        ArrayList<String> name = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT name FROM " + SchoolsTable.TABLE_NAME +
                " WHERE county = ?";

        SQLiteDatabase db = DB.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{county});
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            name.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return name;
    }

    public static String getSchoolName(int id) {

        String name;
        String selectQuery = "SELECT name FROM " + SchoolsTable.TABLE_NAME +
                " WHERE id = ?";

        SQLiteDatabase db = DB.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        name = cursor.getString(0);

        cursor.close();
        db.close();
        return name;
    }

    public static int getSchoolID(String area, String county, String school) {
        int schoolID;
        SQLiteDatabase db = DB.getReadableDatabase();
        Cursor cursor = db.query(SchoolsTable.TABLE_NAME, new String[]{"id"},
                "area = ? AND county = ? AND name = ?", new String[]{area, county, school},
                null, null, null);
        cursor.moveToFirst();
        schoolID = cursor.getInt(0);

        cursor.close();
        db.close();
        return schoolID;
    }

    public static boolean isTeacher() {
        boolean isTeacher;
        SQLiteDatabase db = DB.getReadableDatabase();

        Cursor cursor = db.query(LoginTable.TABLE_NAME, new String[]{"is_teacher"},
                "1 = 1", null, null, null, null);

        cursor.moveToFirst();
        isTeacher = cursor.getInt(0) != 0;
        Log.d(TAG, "isTeacher: " + cursor.getString(0));
        cursor.close();
        db.close();
        return isTeacher;
    }

    public static String getUserUid() {
        String uid;
        SQLiteDatabase db = DB.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.query(LoginTable.TABLE_NAME, new String[]{LoginTable.KEY_UID},
                "1 = 1", null, null, null, null);

        cursor.moveToFirst();
        uid = cursor.getString(0);
        Log.d(TAG, "getUserUid: " + cursor.getString(0));
        cursor.close();
        db.endTransaction();
        db.close();
        return uid;
    }

    public static String getUserAccessKey() {
        String accessKey;
        SQLiteDatabase db = DB.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.query(LoginTable.TABLE_NAME, new String[]{LoginTable.KEY_ACCESS_KEY},
                "1 = 1", null, null, null, null);

        cursor.moveToFirst();
        accessKey = cursor.getString(0);
        Log.d(TAG, "getUserUid: " + cursor.getString(0));
        cursor.close();
        db.endTransaction();
        db.close();
        return accessKey;
    }

    public static void updateMissions(ArrayList<Mission> missionsList) {
        SQLiteDatabase db = DB.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(MissionsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<missionsList.size(); i++) {
                values.put(MissionsTable.KEY_ID, missionsList.get(i).id); // Name
                values.put(MissionsTable.KEY_USER_UID, missionsList.get(i).userUid); // Email
                values.put(MissionsTable.KEY_USER_NAME, missionsList.get(i).userName); // Email
                values.put(MissionsTable.KEY_SCHOOL_ID, missionsList.get(i).schoolID); // Email
                values.put(MissionsTable.KEY_TITLE, missionsList.get(i).title); // Created At
                values.put(MissionsTable.KEY_IS_URGENT, missionsList.get(i).isUrgent); // Created At
                values.put(MissionsTable.KEY_NEED_NUM, missionsList.get(i).needNum); // Created At
                values.put(MissionsTable.KEY_CURRENT_NUM, missionsList.get(i).currentNum); // Created At
                values.put(MissionsTable.KEY_PLACE, missionsList.get(i).place);
                values.put(MissionsTable.KEY_CONTENT, missionsList.get(i).content); // Created At
                values.put(MissionsTable.KEY_REWARD, missionsList.get(i).reward); // Created At
                values.put(MissionsTable.KEY_CREATED_AT, dateFormat.format(missionsList.get(i).createdAt)); // Created At
                values.put(MissionsTable.KEY_EXP_AT, dateFormat.format(missionsList.get(i).expAt)); // Created At
                values.put(MissionsTable.KEY_IS_RUNNING, missionsList.get(i).isRunning); // Created At
                values.put(MissionsTable.KEY_IS_FINISHED, missionsList.get(i).isFinished); // Created At
                values.put(MissionsTable.KEY_FINISHED_AT, dateFormat.format(missionsList.get(i).finishedAt)); // Created At
                // Inserting Row
                db.insert(MissionsTable.TABLE_NAME, null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateMissions: " + e.toString());
        } finally {
            db.endTransaction();
        }

        Log.d(TAG, "Updated all missions info from SQLite");
        db.close();

    }

    public static void updateGroups(ArrayList<Group> groupsList) {
        SQLiteDatabase db = DB.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(GroupsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<groupsList.size(); i++) {
                values.put(GroupsTable.KEY_ID, groupsList.get(i).id); // Name
                values.put(GroupsTable.KEY_USER_UID, groupsList.get(i).userUid); // Email
                values.put(GroupsTable.KEY_USER_NAME, groupsList.get(i).userName); // Email
                values.put(GroupsTable.KEY_SCHOOL_ID, groupsList.get(i).schoolID); // Email
                values.put(GroupsTable.KEY_TITLE, groupsList.get(i).title); // Created At
                values.put(GroupsTable.KEY_NEED_NUM, groupsList.get(i).needNum); // Created At
                values.put(GroupsTable.KEY_CURRENT_NUM, groupsList.get(i).currentNum); // Created At
                values.put(GroupsTable.KEY_PLACE, groupsList.get(i).place);
                values.put(GroupsTable.KEY_CONTENT, groupsList.get(i).content); // Created At
                values.put(GroupsTable.KEY_CREATED_AT, dateFormat.format(groupsList.get(i).createdAt)); // Created At
                values.put(GroupsTable.KEY_EXP_AT, dateFormat.format(groupsList.get(i).expAt)); // Created At
                values.put(GroupsTable.KEY_IS_RUNNING, groupsList.get(i).isRunning); // Created At
                values.put(GroupsTable.KEY_IS_FINISHED, groupsList.get(i).isFinished); // Created At
                values.put(GroupsTable.KEY_FINISHED_AT, dateFormat.format(groupsList.get(i).finishedAt)); // Created At
                // Inserting Row
                db.insert(GroupsTable.TABLE_NAME, null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateGroups: " + e.toString());
        } finally {
            db.endTransaction();
        }

        Log.d(TAG, "Updated all groups info from SQLite");
        db.close();

    }

    public static Mission getMission(int id) {

        Mission mission = new Mission();
        String selectQuery = "SELECT  * FROM " + MissionsTable.TABLE_NAME + " WHERE "
                + MissionsTable.KEY_ID + " = ?";

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        cursor.moveToFirst();

        try {

            mission.id = cursor.getInt(MissionsTable.COLUMNS_NUM_ID);
            mission.userUid = cursor.getString(MissionsTable.COLUMNS_NUM_USER_UID);
            mission.userName = cursor.getString(MissionsTable.COLUMNS_NUM_USER_NAME);
            mission.schoolID = cursor.getInt(MissionsTable.COLUMNS_NUM_SCHOOL_ID);
            mission.title = cursor.getString(MissionsTable.COLUMNS_NUM_TITLE);
            mission.isUrgent = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_URGENT) != 0;
            mission.needNum = cursor.getInt(MissionsTable.COLUMNS_NUM_NEED_NUM);
            mission.currentNum = cursor.getInt(MissionsTable.COLUMNS_NUM_CURRENT_NUM);
            mission.place = cursor.getString(MissionsTable.COLUMNS_NUM_PLACE);
            mission.content = cursor.getString(MissionsTable.COLUMNS_NUM_CONTENT);
            mission.reward = cursor.getString(MissionsTable.COLUMNS_NUM_REWARD);
            mission.createdAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_CREATED_AT));
            mission.expAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_EXP_AT));
            mission.isRunning = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_RUNNING) != 0;
            mission.isFinished = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0;
            mission.finishedAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_FINISHED_AT));

        } catch (Exception e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return mission;
    }

    public static Group getGroup(int id) {

        Group group = new Group();
        String selectQuery = "SELECT  * FROM " + GroupsTable.TABLE_NAME + " WHERE "
                + GroupsTable.KEY_ID + " = ?";

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        cursor.moveToFirst();

        try {

            group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
            group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
            group.userName = cursor.getString(GroupsTable.COLUMNS_NUM_USER_NAME);
            group.schoolID = cursor.getInt(GroupsTable.COLUMNS_NUM_SCHOOL_ID);
            group.title = cursor.getString(GroupsTable.COLUMNS_NUM_TITLE);
            group.needNum = cursor.getInt(GroupsTable.COLUMNS_NUM_NEED_NUM);
            group.currentNum = cursor.getInt(GroupsTable.COLUMNS_NUM_CURRENT_NUM);
            group.place = cursor.getString(GroupsTable.COLUMNS_NUM_PLACE);
            group.content = cursor.getString(GroupsTable.COLUMNS_NUM_CONTENT);
            group.createdAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_CREATED_AT));
            group.expAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_EXP_AT));
            group.isRunning = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_RUNNING) != 0;
            group.isFinished = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0;
            group.finishedAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_FINISHED_AT));

        } catch (Exception e) {
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return group;
    }

    public static ArrayList<Mission> getMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MissionsTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                Mission mission = new Mission();
                mission.id = cursor.getInt(MissionsTable.COLUMNS_NUM_ID);
                mission.userUid = cursor.getString(MissionsTable.COLUMNS_NUM_USER_UID);
                mission.userName = cursor.getString(MissionsTable.COLUMNS_NUM_USER_NAME);
                mission.schoolID = cursor.getInt(MissionsTable.COLUMNS_NUM_SCHOOL_ID);
                mission.title = cursor.getString(MissionsTable.COLUMNS_NUM_TITLE);
                mission.isUrgent = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_URGENT) != 0;
                mission.needNum = cursor.getInt(MissionsTable.COLUMNS_NUM_NEED_NUM);
                mission.currentNum = cursor.getInt(MissionsTable.COLUMNS_NUM_CURRENT_NUM);
                mission.place = cursor.getString(MissionsTable.COLUMNS_NUM_PLACE);
                mission.content = cursor.getString(MissionsTable.COLUMNS_NUM_CONTENT);
                mission.reward = cursor.getString(MissionsTable.COLUMNS_NUM_REWARD);
                mission.createdAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_CREATED_AT));
                mission.expAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_EXP_AT));
                mission.isRunning = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                mission.isFinished = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                mission.finishedAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_FINISHED_AT));
                missions.add(mission);
                Log.d(TAG, "getMissions: " );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return missions;
    }

    public static ArrayList<Mission> getFinishedMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MissionsTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                if(cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0) {
                    Mission mission = new Mission();
                    mission.id = cursor.getInt(MissionsTable.COLUMNS_NUM_ID);
                    mission.userUid = cursor.getString(MissionsTable.COLUMNS_NUM_USER_UID);
                    mission.userName = cursor.getString(MissionsTable.COLUMNS_NUM_USER_NAME);
                    mission.schoolID = cursor.getInt(MissionsTable.COLUMNS_NUM_SCHOOL_ID);
                    mission.title = cursor.getString(MissionsTable.COLUMNS_NUM_TITLE);
                    mission.isUrgent = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_URGENT) != 0;
                    mission.needNum = cursor.getInt(MissionsTable.COLUMNS_NUM_NEED_NUM);
                    mission.currentNum = cursor.getInt(MissionsTable.COLUMNS_NUM_CURRENT_NUM);
                    mission.place = cursor.getString(MissionsTable.COLUMNS_NUM_PLACE);
                    mission.content = cursor.getString(MissionsTable.COLUMNS_NUM_CONTENT);
                    mission.reward = cursor.getString(MissionsTable.COLUMNS_NUM_REWARD);
                    mission.createdAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_CREATED_AT));
                    mission.expAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_EXP_AT));
                    mission.isRunning = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                    mission.isFinished = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                    mission.finishedAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_FINISHED_AT));
                    missions.add(mission);
                    Log.d(TAG, "getMissions: " );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return missions;
    }

    public static ArrayList<Mission> getUnfinishedMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MissionsTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                if(!(cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0)) {
                    Mission mission = new Mission();
                    mission.id = cursor.getInt(MissionsTable.COLUMNS_NUM_ID);
                    mission.userUid = cursor.getString(MissionsTable.COLUMNS_NUM_USER_UID);
                    mission.userName = cursor.getString(MissionsTable.COLUMNS_NUM_USER_NAME);
                    mission.schoolID = cursor.getInt(MissionsTable.COLUMNS_NUM_SCHOOL_ID);
                    mission.title = cursor.getString(MissionsTable.COLUMNS_NUM_TITLE);
                    mission.isUrgent = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_URGENT) != 0;
                    mission.needNum = cursor.getInt(MissionsTable.COLUMNS_NUM_NEED_NUM);
                    mission.currentNum = cursor.getInt(MissionsTable.COLUMNS_NUM_CURRENT_NUM);
                    mission.place = cursor.getString(MissionsTable.COLUMNS_NUM_PLACE);
                    mission.content = cursor.getString(MissionsTable.COLUMNS_NUM_CONTENT);
                    mission.reward = cursor.getString(MissionsTable.COLUMNS_NUM_REWARD);
                    mission.createdAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_CREATED_AT));
                    mission.expAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_EXP_AT));
                    mission.isRunning = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                    mission.isFinished = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                    mission.finishedAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_FINISHED_AT));
                    missions.add(mission);
                    Log.d(TAG, "getMissions: " );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return missions;
    }

    public static ArrayList<Group> getGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GroupsTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                Group group = new Group();
                group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
                group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
                group.userName = cursor.getString(GroupsTable.COLUMNS_NUM_USER_NAME);
                group.schoolID = cursor.getInt(GroupsTable.COLUMNS_NUM_SCHOOL_ID);
                group.title = cursor.getString(GroupsTable.COLUMNS_NUM_TITLE);
                group.needNum = cursor.getInt(GroupsTable.COLUMNS_NUM_NEED_NUM);
                group.currentNum = cursor.getInt(GroupsTable.COLUMNS_NUM_CURRENT_NUM);
                group.place = cursor.getString(GroupsTable.COLUMNS_NUM_PLACE);
                group.content = cursor.getString(GroupsTable.COLUMNS_NUM_CONTENT);
                group.createdAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_CREATED_AT));
                group.expAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_EXP_AT));
                group.isRunning = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                group.isFinished = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                group.finishedAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_FINISHED_AT));
                groups.add(group);
                Log.d(TAG, "getGroups: " );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return groups;
    }

    public static ArrayList<Group> getUnfinishedGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GroupsTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                if(!(cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0)) {
                    Group group = new Group();
                    group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
                    group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
                    group.userName = cursor.getString(GroupsTable.COLUMNS_NUM_USER_NAME);
                    group.schoolID = cursor.getInt(GroupsTable.COLUMNS_NUM_SCHOOL_ID);
                    group.title = cursor.getString(GroupsTable.COLUMNS_NUM_TITLE);
                    group.needNum = cursor.getInt(GroupsTable.COLUMNS_NUM_NEED_NUM);
                    group.currentNum = cursor.getInt(GroupsTable.COLUMNS_NUM_CURRENT_NUM);
                    group.place = cursor.getString(GroupsTable.COLUMNS_NUM_PLACE);
                    group.content = cursor.getString(GroupsTable.COLUMNS_NUM_CONTENT);
                    group.createdAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_CREATED_AT));
                    group.expAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_EXP_AT));
                    group.isRunning = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                    group.isFinished = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                    group.finishedAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_FINISHED_AT));
                    groups.add(group);
                    Log.d(TAG, "getGroups: " );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return groups;
    }

    public static ArrayList<Group> getFinishedGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GroupsTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                if(cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0) {
                    Group group = new Group();
                    group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
                    group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
                    group.userName = cursor.getString(GroupsTable.COLUMNS_NUM_USER_NAME);
                    group.schoolID = cursor.getInt(GroupsTable.COLUMNS_NUM_SCHOOL_ID);
                    group.title = cursor.getString(GroupsTable.COLUMNS_NUM_TITLE);
                    group.needNum = cursor.getInt(GroupsTable.COLUMNS_NUM_NEED_NUM);
                    group.currentNum = cursor.getInt(GroupsTable.COLUMNS_NUM_CURRENT_NUM);
                    group.place = cursor.getString(GroupsTable.COLUMNS_NUM_PLACE);
                    group.content = cursor.getString(GroupsTable.COLUMNS_NUM_CONTENT);
                    group.createdAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_CREATED_AT));
                    group.expAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_EXP_AT));
                    group.isRunning = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                    group.isFinished = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                    group.finishedAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_FINISHED_AT));
                    groups.add(group);
                    Log.d(TAG, "getGroups: " );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return groups;
    }

    public static void updateQAs(ArrayList<QA> QAsList) {
        SQLiteDatabase db = DB.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(MissionsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<QAsList.size(); i++) {
                values.put(QATable.KEY_ID, QAsList.get(i).id); // Name
                values.put(QATable.KEY_USER_UID, QAsList.get(i).userUid); // Email
                values.put(QATable.KEY_USER_NAME, QAsList.get(i).userName); // Email
                values.put(QATable.KEY_QUESTION, QAsList.get(i).question); // Email
                values.put(QATable.KEY_ANSWER, QAsList.get(i).answer); // Email
                values.put(QATable.KEY_IS_ANSWERED, QAsList.get(i).isAnswered); // Email
                values.put(QATable.KEY_CREATED_AT, dateFormat.format(QAsList.get(i).createdAt)); // Created At
                values.put(QATable.KEY_ANSWERED_AT, dateFormat.format(QAsList.get(i).answeredAt)); // Created At

                // Inserting Row
                db.insert(QATable.TABLE_NAME, null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateQAs: " + e.toString());
        } finally {
            db.endTransaction();
        }

        Log.d(TAG, "Updated all QAs info from SQLite");
        db.close();

    }

    public static ArrayList<QA> getQAs() {

        ArrayList<QA> QAs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + QATable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                QA qa = new QA();
                qa.id = cursor.getInt(QATable.COLUMNS_NUM_ID);
                qa.userUid = cursor.getString(QATable.COLUMNS_NUM_USER_UID);
                qa.userName = cursor.getString(QATable.COLUMNS_NUM_USER_NAME);
                qa.question = cursor.getString(QATable.COLUMNS_NUM_QUESTION);
                qa.answer = cursor.getString(QATable.COLUMNS_NUM_ANSWER);
                qa.isAnswered = cursor.getInt(QATable.COLUMNS_NUM_IS_ANSWERED)!=0;
                qa.createdAt = dateFormat.parse(cursor.getString(QATable.COLUMNS_NUM_CREATED_AT));
                qa.answeredAt = dateFormat.parse(cursor.getString(QATable.COLUMNS_NUM_ANSWERED_AT));

                QAs.add(qa);
                Log.d(TAG, "getQAs: " + qa.id + ", " + qa.userUid + ", " + qa.userName + ", "
                        + qa.question + ", " + qa.answer + ", " + qa.isAnswered + ", "
                        + qa.createdAt + ", " + qa.answeredAt );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return QAs;
    }

    public static void deleteAllMissions() {
        SQLiteDatabase db = DB.getWritableDatabase();
        db.delete(MissionsTable.TABLE_NAME, null, null);
        db.close();
    }

    public static void deleteAllGroups() {
        SQLiteDatabase db = DB.getWritableDatabase();
        db.delete(GroupsTable.TABLE_NAME, null, null);
        db.close();
    }

    public static void updateMissionAcceptUser(ArrayList<MissionAccept> maList) {
        SQLiteDatabase db = DB.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(MissionAcceptUserTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<maList.size(); i++) {
                values.put(MissionAcceptUserTable.KEY_MISSION_ID, maList.get(i).missionID); // Name
                values.put(MissionAcceptUserTable.KEY_USER_UID, maList.get(i).userUid); // Email
                values.put(MissionAcceptUserTable.KEY_USER_NAME, maList.get(i).userName); // Email
                values.put(MissionAcceptUserTable.KEY_ACCEPTED_AT, dateFormat.format(maList.get(i).acceptedAt)); // Created At

                // Inserting Row
                db.insert(MissionAcceptUserTable.TABLE_NAME, null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateMissionAcceptUser: " + e.toString());
        } finally {
            db.endTransaction();
        }

        Log.d(TAG, "Updated all MissionAcceptUser info from SQLite");
        db.close();

    }

    public static void updateGroupAcceptUser(ArrayList<GroupAccept> gaList) {
        SQLiteDatabase db = DB.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(GroupAcceptUserTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<gaList.size(); i++) {
                values.put(GroupAcceptUserTable.KEY_GROUP_ID, gaList.get(i).groupID); // Name
                values.put(GroupAcceptUserTable.KEY_USER_UID, gaList.get(i).userUid); // Email
                values.put(GroupAcceptUserTable.KEY_USER_NAME, gaList.get(i).userName); // Email
                values.put(GroupAcceptUserTable.KEY_ACCEPTED_AT, dateFormat.format(gaList.get(i).acceptedAt)); // Created At

                // Inserting Row
                db.insert(GroupAcceptUserTable.TABLE_NAME, null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateGroupAcceptUser: " + e.toString());
        } finally {
            db.endTransaction();
        }

        Log.d(TAG, "Updated all GroupAcceptUser info from SQLite");
        db.close();

    }

    public static ArrayList<MissionAccept> getMissionAceeptUser() {

        ArrayList<MissionAccept> maList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + MissionAcceptUserTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                MissionAccept ma = new MissionAccept();
                ma.missionID = cursor.getInt(MissionAcceptUserTable.COLUMNS_NUM_MISSION_ID);
                ma.userUid = cursor.getString(MissionAcceptUserTable.COLUMNS_NUM_USER_UID);
                ma.userName = cursor.getString(MissionAcceptUserTable.COLUMNS_NUM_USER_NAME);
                ma.acceptedAt = dateFormat.parse(cursor.getString(MissionAcceptUserTable.COLUMNS_NUM_ACCEPTED_AT));

                maList.add(ma);
                Log.d(TAG, "getMissionAcceptUser: " + ma.missionID + ", " + ma.userUid + ", " + ma.userName + ", "
                        + ma.acceptedAt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return maList;
    }

    public static ArrayList<GroupAccept> getGroupAceeptUser() {

        ArrayList<GroupAccept> gaList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GroupAcceptUserTable.TABLE_NAME;

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                GroupAccept ga = new GroupAccept();
                ga.groupID = cursor.getInt(GroupAcceptUserTable.COLUMNS_NUM_GROUP_ID);
                ga.userUid = cursor.getString(GroupAcceptUserTable.COLUMNS_NUM_USER_UID);
                ga.userName = cursor.getString(GroupAcceptUserTable.COLUMNS_NUM_USER_NAME);
                ga.acceptedAt = dateFormat.parse(cursor.getString(GroupAcceptUserTable.COLUMNS_NUM_ACCEPTED_AT));

                gaList.add(ga);
                Log.d(TAG, "getGroupAcceptUser: " + ga.groupID + ", " + ga.userUid + ", " + ga.userName + ", "
                        + ga.acceptedAt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return gaList;
    }

    public static void updateUserAcceptMissions(ArrayList<UserAcceptMissions> list) {
        SQLiteDatabase db = DB.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(UserAcceptMissionsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<list.size(); i++) {
                values.put(UserAcceptMissionsTable.KEY_MISSION_ID, list.get(i).missionID); // Name
                values.put(UserAcceptMissionsTable.KEY_ACCEPTED_AT, dateFormat.format(list.get(i).acceptedAt)); // Created At

                // Inserting Row
                db.insert(UserAcceptMissionsTable.TABLE_NAME, null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateUserAcceptMissionsTable: " + e.toString());
        } finally {
            db.endTransaction();
        }

        Log.d(TAG, "Updated all UserAcceptMissionsTable info from SQLite");
        db.close();

    }

    public static ArrayList<Mission> getUserAcceptMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + MissionsTable.TABLE_NAME + " WHERE id IN (SELECT mission_id FROM " + UserAcceptMissionsTable.TABLE_NAME + ")";

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        //Log.d(TAG, "getUserAcceptMissions: "+cursor.getCount());
        for(int i=0; i<cursor.getCount(); i++){
            try {
                Mission mission = new Mission();
                mission.id = cursor.getInt(MissionsTable.COLUMNS_NUM_ID);
                mission.userUid = cursor.getString(MissionsTable.COLUMNS_NUM_USER_UID);
                mission.userName = cursor.getString(MissionsTable.COLUMNS_NUM_USER_NAME);
                mission.schoolID = cursor.getInt(MissionsTable.COLUMNS_NUM_SCHOOL_ID);
                mission.title = cursor.getString(MissionsTable.COLUMNS_NUM_TITLE);
                mission.isUrgent = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_URGENT) != 0;
                mission.needNum = cursor.getInt(MissionsTable.COLUMNS_NUM_NEED_NUM);
                mission.currentNum = cursor.getInt(MissionsTable.COLUMNS_NUM_CURRENT_NUM);
                mission.place = cursor.getString(MissionsTable.COLUMNS_NUM_PLACE);
                mission.content = cursor.getString(MissionsTable.COLUMNS_NUM_CONTENT);
                mission.reward = cursor.getString(MissionsTable.COLUMNS_NUM_REWARD);
                mission.createdAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_CREATED_AT));
                mission.expAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_EXP_AT));
                mission.isRunning = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                mission.isFinished = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                mission.finishedAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_FINISHED_AT));
                missions.add(mission);
                //Log.d(TAG, "getUserAcceptMissions: " );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return missions;
    }

    public static ArrayList<Mission> getUserAcceptUnfinishedMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + MissionsTable.TABLE_NAME + " as t1, "
                + UserAcceptMissionsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.mission_id AND t1.is_finished = 0";

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                Mission mission = new Mission();
                mission.id = cursor.getInt(MissionsTable.COLUMNS_NUM_ID);
                mission.userUid = cursor.getString(MissionsTable.COLUMNS_NUM_USER_UID);
                mission.userName = cursor.getString(MissionsTable.COLUMNS_NUM_USER_NAME);
                mission.schoolID = cursor.getInt(MissionsTable.COLUMNS_NUM_SCHOOL_ID);
                mission.title = cursor.getString(MissionsTable.COLUMNS_NUM_TITLE);
                mission.isUrgent = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_URGENT) != 0;
                mission.needNum = cursor.getInt(MissionsTable.COLUMNS_NUM_NEED_NUM);
                mission.currentNum = cursor.getInt(MissionsTable.COLUMNS_NUM_CURRENT_NUM);
                mission.place = cursor.getString(MissionsTable.COLUMNS_NUM_PLACE);
                mission.content = cursor.getString(MissionsTable.COLUMNS_NUM_CONTENT);
                mission.reward = cursor.getString(MissionsTable.COLUMNS_NUM_REWARD);
                mission.createdAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_CREATED_AT));
                mission.expAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_EXP_AT));
                mission.isRunning = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                mission.isFinished = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                mission.finishedAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_FINISHED_AT));
                missions.add(mission);
                Log.d(TAG, "getUserAcceptMissions: " );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return missions;
    }

    public static ArrayList<Mission> getUserAcceptFinishedMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + MissionsTable.TABLE_NAME + " as t1, "
                + UserAcceptMissionsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.mission_id AND t1.is_finished = 1";

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                Mission mission = new Mission();
                mission.id = cursor.getInt(MissionsTable.COLUMNS_NUM_ID);
                mission.userUid = cursor.getString(MissionsTable.COLUMNS_NUM_USER_UID);
                mission.userName = cursor.getString(MissionsTable.COLUMNS_NUM_USER_NAME);
                mission.schoolID = cursor.getInt(MissionsTable.COLUMNS_NUM_SCHOOL_ID);
                mission.title = cursor.getString(MissionsTable.COLUMNS_NUM_TITLE);
                mission.isUrgent = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_URGENT) != 0;
                mission.needNum = cursor.getInt(MissionsTable.COLUMNS_NUM_NEED_NUM);
                mission.currentNum = cursor.getInt(MissionsTable.COLUMNS_NUM_CURRENT_NUM);
                mission.place = cursor.getString(MissionsTable.COLUMNS_NUM_PLACE);
                mission.content = cursor.getString(MissionsTable.COLUMNS_NUM_CONTENT);
                mission.reward = cursor.getString(MissionsTable.COLUMNS_NUM_REWARD);
                mission.createdAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_CREATED_AT));
                mission.expAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_EXP_AT));
                mission.isRunning = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                mission.isFinished = cursor.getInt(MissionsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                mission.finishedAt = dateFormat.parse(cursor.getString(MissionsTable.COLUMNS_NUM_FINISHED_AT));
                missions.add(mission);
                Log.d(TAG, "getUserAcceptMissions: " );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return missions;
    }

    public static void updateUserAcceptGroups(ArrayList<UserAcceptGroups> list) {
        SQLiteDatabase db = DB.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(UserAcceptMissionsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<list.size(); i++) {
                values.put(UserAcceptGroupsTable.KEY_GROUP_ID, list.get(i).groupID); // Name
                values.put(UserAcceptGroupsTable.KEY_ACCEPTED_AT, dateFormat.format(list.get(i).acceptedAt)); // Created At

                // Inserting Row
                db.insert(UserAcceptGroupsTable.TABLE_NAME, null, values);
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateUserAcceptGroupsTable: " + e.toString());
        } finally {
            db.endTransaction();
        }

        Log.d(TAG, "Updated all UserAcceptGroupsTable info from SQLite");
        db.close();

    }

    public static ArrayList<Group> getUserAcceptGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + GroupsTable.TABLE_NAME + " as t1 WHERE t1.id IN (SELECT group_id FROM " + UserAcceptGroupsTable.TABLE_NAME + ")";

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                Group group = new Group();
                group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
                group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
                group.userName = cursor.getString(GroupsTable.COLUMNS_NUM_USER_NAME);
                group.schoolID = cursor.getInt(GroupsTable.COLUMNS_NUM_SCHOOL_ID);
                group.title = cursor.getString(GroupsTable.COLUMNS_NUM_TITLE);
                group.needNum = cursor.getInt(GroupsTable.COLUMNS_NUM_NEED_NUM);
                group.currentNum = cursor.getInt(GroupsTable.COLUMNS_NUM_CURRENT_NUM);
                group.place = cursor.getString(GroupsTable.COLUMNS_NUM_PLACE);
                group.content = cursor.getString(GroupsTable.COLUMNS_NUM_CONTENT);
                group.createdAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_CREATED_AT));
                group.expAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_EXP_AT));
                group.isRunning = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                group.isFinished = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                group.finishedAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_FINISHED_AT));
                groups.add(group);
                Log.d(TAG, "getUserAcceptGroups: " );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return groups;
    }

    public static ArrayList<Group> getUserAcceptUnfinishedGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + GroupsTable.TABLE_NAME + " as t1, "
                + UserAcceptGroupsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.group_id AND t1.is_finished = 0";

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                Group group = new Group();
                group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
                group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
                group.userName = cursor.getString(GroupsTable.COLUMNS_NUM_USER_NAME);
                group.schoolID = cursor.getInt(GroupsTable.COLUMNS_NUM_SCHOOL_ID);
                group.title = cursor.getString(GroupsTable.COLUMNS_NUM_TITLE);
                group.needNum = cursor.getInt(GroupsTable.COLUMNS_NUM_NEED_NUM);
                group.currentNum = cursor.getInt(GroupsTable.COLUMNS_NUM_CURRENT_NUM);
                group.place = cursor.getString(GroupsTable.COLUMNS_NUM_PLACE);
                group.content = cursor.getString(GroupsTable.COLUMNS_NUM_CONTENT);
                group.createdAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_CREATED_AT));
                group.expAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_EXP_AT));
                group.isRunning = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                group.isFinished = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                group.finishedAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_FINISHED_AT));
                groups.add(group);
                Log.d(TAG, "getUserAcceptUnfinishedGroups: " );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return groups;
    }

    public static ArrayList<Group> getUserAcceptFinishedGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + GroupsTable.TABLE_NAME + " as t1, "
                + UserAcceptGroupsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.group_id AND t1.is_finished = 1";

        SQLiteDatabase db = DB.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                Group group = new Group();
                group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
                group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
                group.userName = cursor.getString(GroupsTable.COLUMNS_NUM_USER_NAME);
                group.schoolID = cursor.getInt(GroupsTable.COLUMNS_NUM_SCHOOL_ID);
                group.title = cursor.getString(GroupsTable.COLUMNS_NUM_TITLE);
                group.needNum = cursor.getInt(GroupsTable.COLUMNS_NUM_NEED_NUM);
                group.currentNum = cursor.getInt(GroupsTable.COLUMNS_NUM_CURRENT_NUM);
                group.place = cursor.getString(GroupsTable.COLUMNS_NUM_PLACE);
                group.content = cursor.getString(GroupsTable.COLUMNS_NUM_CONTENT);
                group.createdAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_CREATED_AT));
                group.expAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_EXP_AT));
                group.isRunning = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_RUNNING) != 0;
                group.isFinished = cursor.getInt(GroupsTable.COLUMNS_NUM_IS_FINISHED) != 0;
                group.finishedAt = dateFormat.parse(cursor.getString(GroupsTable.COLUMNS_NUM_FINISHED_AT));
                groups.add(group);
                Log.d(TAG, "getUserAcceptFinishedGroups: " );
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        // Move to first row
        cursor.close();
        db.close();
        return groups;
    }

}
