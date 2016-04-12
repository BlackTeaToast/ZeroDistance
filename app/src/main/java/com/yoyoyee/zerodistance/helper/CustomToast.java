package com.yoyoyee.zerodistance.helper;

/**
 * Created by 楊霖村 on 2016/4/12.
 */
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class CustomToast {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };
    public static void showToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mHandler.postDelayed(r, duration+1000);
        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

}