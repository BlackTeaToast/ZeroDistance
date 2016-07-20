package com.yoyoyee.zerodistance.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MainActivity;
import com.yoyoyee.zerodistance.activity.NewMissionActivity;

import java.util.Map;

/**
 * Created by futur on 2016/7/6.
 * FireBase的基本傳輸程式
 *使用data來傳輸資料，包括文字，辨認代碼，辨認字元為"data"
 * {
 *      使用type來決定要使用哪種通知，辨認字元為"type"
 *      {
 *      ----通知類別   辨認代碼----------
 *           普通通知   "normal"
 *           更新通知   "updata"
 *          QA通知       "QA"
 *              {
 *                  QA通知下，需要：
 *                      1.QA的編號，辨認代碼"QAID"
 *                      2.QA的任務或揪團的編號資料，辨認代碼"ID"
 *                      3.QA是任務還是揪團的資料，任務傳false，揪團傳true，辨認代碼"isGroup"
 *              }
 *          任務通知    "mission"
 *              {
 *                  任務通知，需要：
 *                      1.任務編號，辨認代碼"ID"
 *                      2.任務還是揪團的資料，任務傳false，揪團傳true，辨認代碼"isGroup"
 *              }
 *          揪團通知    "group"
 *              {
 *                  揪團通知，需要：
 *                      1.揪團編號，辨認代碼"ID"
 *                      2.任務還是揪團的資料，任務傳false，揪團傳true，辨認代碼"isGroup"
 *              }
 *          額外通知    "other"
 *          測試通知    "test"
 *              {
 *                  測試通知需要：
 *                      1.
 *              }
 *          }
 *      使用content來傳輸內容資料，辨認代碼為"content"
 *      使用title來傳輸標題資料，辨認代碼為"title"
 *}
 *----------------------------------------------------------------------------------------------------------------------------------------------------
 * 傳輸範例：
 * Content-Type    application/json
 * Authorization    key=AIzaSyB76rxvVGcmK7Z6PMwrxA6sKKvr-7AK--o
 * {
 *  "to" : "fxyyF5Yw9vs:APA91bHzRvqH_fCitKNHTXmcAcXLAqK9VToPkejzXnGUCuxKl6xbx6SfYFZrJ9eA6ZiqJerQ547W3-mepXawXHvUI3ay9B_Gv1Fc_dIzw7rNi4q2ux4AqHt0kReLwuNgsYGf4f30WOf5",
 *  "data" : {
 *      "type":"QA",
 *      "ID":"1252",
 *      "QAID":"25"
 *  }
 *}
 *----------------------------------------------------------------------------------------------------------------------------------------------------
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
        Intent i =new Intent(this,NewMissionActivity.class);
        i.putExtra("notificationModel",true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        if (data.get("type").equals("0")) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(data.get("title").toString()+"0")
                    .setContentText(data.get("data").toString())
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
        else if (data.get("type").equals("1")){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(data.get("title").toString()+"1")
                    .setContentText(data.get("data").toString())
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }

    }
}
