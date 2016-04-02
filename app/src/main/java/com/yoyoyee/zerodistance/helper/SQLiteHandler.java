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

import com.yoyoyee.zerodistance.helper.datatype.School;
import com.yoyoyee.zerodistance.helper.table.LoginTable;
import com.yoyoyee.zerodistance.helper.table.SchoolsTable;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "zero_distance_api";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(LoginTable.CREATE_LOGIN_TABLE);
		db.execSQL(SchoolsTable.CREATE_SCHOOLS_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + LoginTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + SchoolsTable.TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String uid, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(LoginTable.KEY_NAME, name); // Name
		values.put(LoginTable.KEY_EMAIL, email); // Email
		values.put(LoginTable.KEY_UID, uid); // Email
		values.put(LoginTable.KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		long id = db.insert(LoginTable.TABLE_NAME, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
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
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

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

}
