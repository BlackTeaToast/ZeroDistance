package com.yoyoyee.zerodistance.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionManager;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private static SQLiteHandler db;
    private static SessionManager session;
	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}
	public static synchronized SQLiteHandler getDB() {
		return db;
	}
    public static synchronized  SessionManager getSession() {
        return session;
    }

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}