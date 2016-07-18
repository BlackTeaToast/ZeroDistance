package com.yoyoyee.zerodistance.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MainActivity;

import java.util.Map;

/**
 * Created by futur on 2016/7/6.
 * FireBase的基本傳輸程式
 */
public class MyFireBaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNoticication(remoteMessage.getData());
        String mess ="data";
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();
        Log.d("FCM",(data.get(mess)).toString());
    }
    private void showNoticication(Map data){
        Intent i =new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent =PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(data.get("title").toString())
                .setContentText(data.get("data").toString())
                .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

    }
}
