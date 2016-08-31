package com.yoyoyee.zerodistance.helper.datatype;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;

/**
 * Created by futur on 2016/8/31.
 */
public class Permission {
    //PermissionUse-100~-110
    public static final int WRITE_EXTERNAL_STORAGE_KEY =-100;
    public static final String WRITE_PERMISSION =Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public void writeStorage(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(context, WRITE_PERMISSION);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE權限，
                if (activity.shouldShowRequestPermissionRationale(WRITE_PERMISSION) == false) {
                    activity.requestPermissions(new String[]{WRITE_PERMISSION}, WRITE_EXTERNAL_STORAGE_KEY);
                } else {
                    activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permission.WRITE_EXTERNAL_STORAGE_KEY);
                }
            }
        }
    }
    public boolean onRequestPermissionsResult_writeStorage(Context context, String[] permissions, int[] grantResults){
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context,"您已取消權限",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }



    }


}
