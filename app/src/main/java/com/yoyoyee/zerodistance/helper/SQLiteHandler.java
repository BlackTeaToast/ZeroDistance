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
import com.yoyoyee.zerodistance.app.NotificationID;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.datatype.Friend;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.GroupAccept;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.MissionAccept;
import com.yoyoyee.zerodistance.helper.datatype.QA;
import com.yoyoyee.zerodistance.helper.datatype.School;
import com.yoyoyee.zerodistance.helper.datatype.UserAcceptGroups;
import com.yoyoyee.zerodistance.helper.datatype.UserAcceptMissions;
import com.yoyoyee.zerodistance.helper.table.FriendsTable;
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
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 30;

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
        db.execSQL(QATable.CREATE_QA_TABLE);
        db.execSQL(MissionAcceptUserTable.CREATE_MISSION_ACCEPT_USER_TABLE);
        db.execSQL(GroupAcceptUserTable.CREATE_GROUP_ACCEPT_USER_TABLE);
        db.execSQL(UserAcceptMissionsTable.CREATE_USER_ACCEPT_MISSIONS_TABLE);
        db.execSQL(UserAcceptGroupsTable.CREATE_USER_ACCEPT_GROUPS_TABLE);
        db.execSQL(FriendsTable.CREATE_FRIENDS_TABLE);
        db.execSQL(NotificationID.CREATE_TABLE);
        if(session.isLoggedIn()) {
            ClientFunctions.checkLogin(session.getUserEmail(), session.getUserPassword(), new ClientResponse() {
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
        db.execSQL("DROP TABLE IF EXISTS " + QATable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MissionAcceptUserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GroupAcceptUserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserAcceptMissionsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserAcceptGroupsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FriendsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NotificationID.TABLE_NAME);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String isTeacher ,String name, String nickName, String schoolID,
                        String studentID, String email, String uid, String created_at,
                        String access_key, String isConfirmed) {

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

    public String getSchoolName(int id) {

        String name = new String();
        String selectQuery = "SELECT name FROM " + SchoolsTable.TABLE_NAME +
                " WHERE id = ?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        name = cursor.getString(0);

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

    public ArrayList<Mission> getFinishedMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MissionsTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
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

    public ArrayList<Mission> getUnfinishedMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + MissionsTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
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

    public ArrayList<Group> getUnfinishedGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GroupsTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
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

    public ArrayList<Group> getFinishedGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + GroupsTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
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

    public void updateQAs(ArrayList<QA> QAsList) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(QATable.TABLE_NAME, null, null);
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

    public ArrayList<QA> getQAs() {

        ArrayList<QA> QAs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + QATable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
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

    public void deleteAllMissions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MissionsTable.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllGroups() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GroupsTable.TABLE_NAME, null, null);
        db.close();
    }

    public void updateMissionAcceptUser(ArrayList<MissionAccept> maList) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public void updateGroupAcceptUser(ArrayList<GroupAccept> gaList) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public ArrayList<MissionAccept> getMissionAceeptUser() {

        ArrayList<MissionAccept> maList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + MissionAcceptUserTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
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

    public ArrayList<GroupAccept> getGroupAceeptUser() {

        ArrayList<GroupAccept> gaList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + GroupAcceptUserTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
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

    public void updateUserAcceptMissions(ArrayList<UserAcceptMissions> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(UserAcceptMissionsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<list.size(); i++) {
                //Log.d(TAG, "updateUserAcceptMissions: "+list.get(i).missionID);

                values.put(UserAcceptMissionsTable.KEY_MISSION_ID, list.get(i).missionID);
                values.put(UserAcceptMissionsTable.KEY_ACCEPTED_AT, dateFormat.format(list.get(i).acceptedAt));
                //Log.d(TAG, "updateUserAcceptMissions: "+ values.toString());
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

    public ArrayList<Mission> getUserAcceptMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + MissionsTable.TABLE_NAME + " as t1, "
                + UserAcceptMissionsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.mission_id";

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

    public ArrayList<Mission> getUserAcceptUnfinishedMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + MissionsTable.TABLE_NAME + " as t1, "
                + UserAcceptMissionsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.mission_id AND t1.is_finished = 0";

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

    public ArrayList<Mission> getUserAcceptFinishedMissions() {

        ArrayList<Mission> missions = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + MissionsTable.TABLE_NAME + " as t1, "
                + UserAcceptMissionsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.mission_id AND t1.is_finished = 1";

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

    public void updateUserAcceptGroups(ArrayList<UserAcceptGroups> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        db.beginTransaction();
        try {
            db.delete(UserAcceptGroupsTable.TABLE_NAME, null, null);
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

    public ArrayList<Group> getUserAcceptGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + GroupsTable.TABLE_NAME + " as t1, "
                + UserAcceptGroupsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.group_id";

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

    public ArrayList<Group> getUserAcceptUnfinishedGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + GroupsTable.TABLE_NAME + " as t1, "
                + UserAcceptGroupsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.group_id AND t1.is_finished = 0";

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

    public ArrayList<Group> getUserAcceptFinishedGroups() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + GroupsTable.TABLE_NAME + " as t1, "
                + UserAcceptGroupsTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.group_id AND t1.is_finished = 1";

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

    public void updateFriends(ArrayList<Friend> friends) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(FriendsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for(int i=0; i<friends.size(); i++) {

                values.put(FriendsTable.KEY_UID, friends.get(i).uid);
                values.put(FriendsTable.KEY_IS_TEACHER, friends.get(i).isTeacher);
                values.put(FriendsTable.KEY_NAME, friends.get(i).name);
                values.put(FriendsTable.KEY_NICKNAME, friends.get(i).nickName);
                values.put(FriendsTable.KEY_SCHOOL_ID, friends.get(i).schoolID);
                values.put(FriendsTable.KEY_STUDENT_ID, friends.get(i).studentID);
                values.put(FriendsTable.KEY_EMAIL, friends.get(i).email);
                values.put(FriendsTable.KEY_PROFESSION, friends.get(i).profession);
                values.put(FriendsTable.KEY_LEVEL, friends.get(i).level);
                values.put(FriendsTable.KEY_EXP, friends.get(i).exp);
                values.put(FriendsTable.KEY_MONEY, friends.get(i).money);
                values.put(FriendsTable.KEY_STRENGTH, friends.get(i).strength);
                values.put(FriendsTable.KEY_INTELLIGENCE, friends.get(i).intelligence);
                values.put(FriendsTable.KEY_AGILE, friends.get(i).agile);
                values.put(FriendsTable.KEY_INTRODUCTION, friends.get(i).introduction);
                values.put(FriendsTable.KEY_IS_ACCEPTED, friends.get(i).isAccepted);
                values.put(FriendsTable.KEY_IS_INVITE, friends.get(i).isInvite);

                db.insert(FriendsTable.TABLE_NAME, null, values);
                Log.d(TAG, "updateFriends: " + values.toString());
                values.clear();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "updateFriends: " + e.toString());
        } finally {
            db.endTransaction();
        }

        Log.d(TAG, "Updated all FriendsTable info from SQLite");
        db.close();

    }

    public void putFriendList(String put){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(NotificationID.NICKNAME, put);
            // Inserting Row
            db.insert(NotificationID.TABLE_NAME, null, values);

            Log.d(TAG, "New Friend put " );
        } catch (Exception e) {
            throw e;
        } finally {
            db.close(); // Closing database connection
        }
    }

    public ArrayList<String> getFriendList() {
        ArrayList<String> st = new ArrayList<>();
        String selectQuery = "SELECT "+NotificationID.NICKNAME+" FROM "+NotificationID.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

            for(int i=0; i<cursor.getCount(); i++){
                try {
                        String s=new String();
                        s = cursor.getString(0);
                        st.add(s);

                } catch (Exception e) {
                        e.printStackTrace();
                }
                cursor.moveToNext();
            }
        // Move to first row
        cursor.close();
        db.close();
        return st;
    }

    public void friendListDelete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
                db.delete(NotificationID.TABLE_NAME, null, null);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, "delete Friend List " + e.toString());
            } finally {
                db.endTransaction();
            }

                Log.d(TAG, "delete Friend List Complete");
        db.close();
    }
/*
    public void addGroupOrderList(int groupOrder[],String date[]) {
        int listG =groupOrder.length;
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        db.beginTransaction();
        try {
            db.delete(NotificationGroupOrderTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for (int i = 0; i < listG; i++) {
                values.put(NotificationMissionOrderTable.KEY_MISSION_ORDER, groupOrder[i]); // Name
                values.put(NotificationMissionOrderTable.KEY_MISSION_TIME ,date[i]);
                //Log.d(TAG, "updateUserAcceptMissions: "+ values.toString());
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
    public void addMissionOrderList(int missionOrder[], Date mission) {
        int listG =missionOrder.length;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(UserAcceptMissionsTable.TABLE_NAME, null, null);
            ContentValues values = new ContentValues();
            for (int i = 0; i < listG; i++) {
                values.put(UserAcceptGroupsTable.KEY_GROUP_ID, missionOrder[i]); // Name
                //Log.d(TAG, "updateUserAcceptMissions: "+ values.toString());
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
    public ArrayList<Group> getGroupsOrder() {

        ArrayList<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT t1.* FROM " + GroupsTable.TABLE_NAME + " as t1, "
                + NotificationMissionOrderTable.TABLE_NAME + " as t2 WHERE t1.id IN t2.group_id AND t1.is_finished = 1";

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
*/
}