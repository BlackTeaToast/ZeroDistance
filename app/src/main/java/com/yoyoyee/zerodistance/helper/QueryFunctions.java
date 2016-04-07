package com.yoyoyee.zerodistance.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.School;
import com.yoyoyee.zerodistance.helper.table.LoginTable;
import com.yoyoyee.zerodistance.helper.table.MissionsTable;
import com.yoyoyee.zerodistance.helper.table.SchoolsTable;

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
                        String access_key) {

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

        Cursor cursor = db.query(LoginTable.TABLE_NAME, new String[]{LoginTable.KEY_UID},
                "1 = 1", null, null, null, null);

        cursor.moveToFirst();
        uid = cursor.getString(0);
        Log.d(TAG, "getUserUid: " + cursor.getString(0));
        cursor.close();
        db.close();
        return uid;
    }

    public static String getUserAccessKey() {
        String accessKey;
        SQLiteDatabase db = DB.getReadableDatabase();

        Cursor cursor = db.query(LoginTable.TABLE_NAME, new String[]{LoginTable.KEY_ACCESS_KEY},
                "1 = 1", null, null, null, null);

        cursor.moveToFirst();
        accessKey = cursor.getString(0);
        Log.d(TAG, "getUserUid: " + cursor.getString(0));
        cursor.close();
        db.close();
        return accessKey;
    }

    public void updateMissions(ArrayList<Mission> missionsList) {
        SQLiteDatabase db = DB.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(MissionsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<missionsList.size(); i++) {
                values.put(MissionsTable.KEY_ID, missionsList.get(i).id); // Name
                values.put(MissionsTable.KEY_USER_UID, missionsList.get(i).userUid); // Email
                values.put(MissionsTable.KEY_SCHOOL_ID, missionsList.get(i).schoolID); // Email
                values.put(MissionsTable.KEY_TITLE, missionsList.get(i).title); // Created At
                values.put(MissionsTable.KEY_IS_URGENT, missionsList.get(i).content); // Created At
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

    public ArrayList<Mission> getMissions() {

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

}
