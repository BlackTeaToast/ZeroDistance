package com.yoyoyee.zerodistance.helper.datatype;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;

/**
 * Created by futur on 2016/8/31.
 */
public class Permission {
    //PermissionUse-100~-110
    private static final String TAG="Permission";
    public static final int WRITE_EXTERNAL_STORAGE_KEY =1;
    public static final String WRITE_PERMISSION =Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static void writeStorage(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "writeStorage:  確定6.0UP");
            int permission = ActivityCompat.checkSelfPermission(context, WRITE_PERMISSION);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "writeStorage: 不具有"+WRITE_PERMISSION+"權限");
                //申请WRITE_EXTERNAL_STORAGE權限，
                if (activity.shouldShowRequestPermissionRationale(WRITE_PERMISSION) == false) {
                    Log.d(TAG, "writeStorage: false");
                    activity.requestPermissions(new String[]{WRITE_PERMISSION}, WRITE_EXTERNAL_STORAGE_KEY);
                } else {
                    Log.d(TAG, "writeStorage: true");
                    activity.requestPermissions(new String[]{WRITE_PERMISSION}, Permission.WRITE_EXTERNAL_STORAGE_KEY);
                }
            }
        }
    }


}
