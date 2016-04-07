package com.yoyoyee.zerodistance.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "ZeroDistanceLogin";
	
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_USER_EMAIL = "email";
	private static final String KEY_USER_PASSWORD = "password";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}

	public void setEmail(String email) {

		editor.putString(KEY_USER_EMAIL, email);
		editor.commit();

	}

	public String getEmail() {
		return pref.getString(KEY_USER_EMAIL, "");
	}

	public void setPassword(String password) {

		editor.putString(KEY_USER_PASSWORD, password);
		editor.commit();

	}

	public String getPassword() {
		return pref.getString(KEY_USER_PASSWORD, "");
	}
	
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
}
