/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.yoyoyee.zerodistance.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.School;
import com.yoyoyee.zerodistance.helper.table.GroupsTable;
import com.yoyoyee.zerodistance.helper.table.LoginTable;
import com.yoyoyee.zerodistance.helper.table.MissionsTable;
import com.yoyoyee.zerodistance.helper.table.SchoolsTable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 10;

	// Database Name
	private static final String DATABASE_NAME = "zero_distance_api";
    private static final SessionManager session = AppController.getSession();

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

        db.execSQL(LoginTable.CREATE_LOGIN_TABLE);
        db.execSQL(SchoolsTable.CREATE_SCHOOLS_TABLE);
        db.execSQL(MissionsTable.CREATE_MISSIONS_TABLE);
        db.execSQL(GroupsTable.CREATE_GROUPS_TABLE);
        if(session.isLoggedIn()) {
            ClientFunctions.checkLogin(session.getEmail(), session.getPassword(), new ClientResponse() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "onResponse: ReLogin success!");
                }

                @Override
                public void onErrorResponse(String response) {
                    Log.d(TAG, "onResponse: ReLogin error!");
                }
            });
        }
		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + LoginTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + SchoolsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MissionsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GroupsTable.TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String isTeacher ,String name, String nickName, String schoolID,
                        String studentID, String email, String uid, String created_at,
                        String access_key) {

        SQLiteDatabase db = this.getWritableDatabase();
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
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + LoginTable.TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
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
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(LoginTable.TABLE_NAME, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from SQLite");
	}

	public void updateSchools(ArrayList<School> schoolList) {
		SQLiteDatabase db = this.getWritableDatabase();

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

	public ArrayList<School> getSchools() {

		ArrayList<School> schools = new ArrayList<>();
		String selectQuery = "SELECT  * FROM " + SchoolsTable.TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();

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

    public ArrayList<String> getSchoolsArea() {

        ArrayList<String> area = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT area FROM " + SchoolsTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

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

    public ArrayList<String> getSchoolsCounty(String area) {

        ArrayList<String> county = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT county FROM " + SchoolsTable.TABLE_NAME +
                " WHERE area = ?";

        SQLiteDatabase db = this.getReadableDatabase();

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

    public ArrayList<String> getSchoolsName(String county) {

        ArrayList<String> name = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT name FROM " + SchoolsTable.TABLE_NAME +
                " WHERE county = ?";

        SQLiteDatabase db = this.getReadableDatabase();

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

	public int getSchoolID(String area, String county, String school) {
		int schoolID;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(SchoolsTable.TABLE_NAME, new String[]{"id"},
                "area = ? AND county = ? AND name = ?", new String[]{area, county, school},
                null, null, null);
		cursor.moveToFirst();
		schoolID = cursor.getInt(0);

		cursor.close();
		db.close();
		return schoolID;
	}

    public boolean isTeacher() {
        boolean isTeacher;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(LoginTable.TABLE_NAME, new String[]{"is_teacher"},
                "1 = 1", null, null, null, null);

        cursor.moveToFirst();
        isTeacher = cursor.getInt(0) != 0;
        Log.d(TAG, "isTeacher: " + cursor.getString(0));
        cursor.close();
        db.close();
        return isTeacher;
    }

    public String getUserUid() {
        String uid;
        SQLiteDatabase db = this.getReadableDatabase();
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

    public String getUserAccessKey() {
        String accessKey;
        SQLiteDatabase db = this.getReadableDatabase();
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

    public void updateMissions(ArrayList<Mission> missionsList) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public void updateGroups(ArrayList<Group> groupsList) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(GroupsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<groupsList.size(); i++) {
                values.put(GroupsTable.KEY_ID, groupsList.get(i).id); // Name
                values.put(GroupsTable.KEY_USER_UID, groupsList.get(i).userUid); // Email
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

    public Mission getMission(int id) {

        Mission mission = new Mission();
        String selectQuery = "SELECT  * FROM " + MissionsTable.TABLE_NAME + " WHERE "
                + MissionsTable.KEY_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
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

    public Group getGroup(int id) {

        Group group = new Group();
        String selectQuery = "SELECT  * FROM " + GroupsTable.TABLE_NAME + " WHERE "
                + GroupsTable.KEY_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        cursor.moveToFirst();

        try {

            group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
            group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
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

    public ArrayList<Mission> getMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MissionsTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
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

    public ArrayList<Group> getGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GroupsTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Taipei"));

        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i=0; i<cursor.getCount(); i++){
            try {
                Group group = new Group();
                group.id = cursor.getInt(GroupsTable.COLUMNS_NUM_ID);
                group.userUid = cursor.getString(GroupsTable.COLUMNS_NUM_USER_UID);
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

}
