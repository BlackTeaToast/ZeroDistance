package com.yoyoyee.zerodistance.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MainActivity;
import com.yoyoyee.zerodistance.activity.NewMissionActivity;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.datatype.QA;

import java.util.ArrayList;
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
    interface Callback {
        void run();
    }
    private ArrayList<QA> DataQas;
    public String content,title;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNoticication(remoteMessage.getData());
        Log.d("FCM","成功讀取資料");
    }
    private void showNoticication(final Map data){

        if (data.get("type").toString().equals("normal")) {
            Intent i =new Intent(getApplicationContext(),NewMissionActivity.class);
            i.putExtra("notificationModel",true);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setContentTitle(R.string.notification_QA_Title_HadAnswer + content +"mor")
                    .setContentText(data.get("data").toString()+data.get("ID").toString())
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(Integer.parseInt(data.get("ID").toString()), builder.build());
        }

        else if (data.get("type").toString().equals("updata")) {
            Intent i =new Intent(getApplicationContext(),NewMissionActivity.class);
            i.putExtra("notificationModel",true);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setContentTitle(R.string.notification_QA_Title_HadAnswer + content+"updata")
                    .setContentText(data.get("data").toString()+data.get("ID").toString())
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(Integer.parseInt(data.get("ID").toString()), builder.build());
        }

        else if (data.get("type").toString().equals("QA")) {
            //讀取QA資料
            loadQandAData(Boolean.valueOf(data.get("isGroup").toString()),Integer.parseInt(data.get("ID").toString()),Integer.parseInt(data.get("QAID").toString()), new Callback() {
                @Override
                public void run() {
                    Intent i =new Intent(getApplicationContext(),NewMissionActivity.class);
                    i.putExtra("notificationModel",true);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                            .setAutoCancel(true)
                            .setContentTitle(getResources().getString(R.string.notification_QA_Title_HadAnswer))
                            .setContentText(getResources().getString(R.string.notification_QA_Contest_Answer) + content)
                            .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                            .setContentIntent(pendingIntent);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(Integer.parseInt(data.get("ID").toString()), builder.build());
                }

            });

        }

        else if (data.get("type").toString().equals("mission")) {
            Intent i =new Intent(getApplicationContext(),NewMissionActivity.class);
            i.putExtra("notificationModel",true);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setContentTitle(R.string.notification_QA_Title_HadAnswer + content+"miss")
                    .setContentText(data.get("data").toString()+data.get("ID").toString())
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(Integer.parseInt(data.get("ID").toString()), builder.build());
        }

        else if (data.get("type").toString().equals("group")) {
            Intent i =new Intent(getApplicationContext(),NewMissionActivity.class);
            i.putExtra("notificationModel",true);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setContentTitle(R.string.notification_QA_Title_HadAnswer + content+ "group")
                    .setContentText(data.get("data").toString()+data.get("ID").toString())
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(Integer.parseInt(data.get("ID").toString()), builder.build());
        }
        else if (data.get("type").toString().equals("other")) {
            Intent i =new Intent(getApplicationContext(),NewMissionActivity.class);
            i.putExtra("notificationModel",true);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setContentTitle(R.string.notification_QA_Title_HadAnswer + content+"other")
                    .setContentText(data.get("data").toString()+data.get("ID").toString())
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(Integer.parseInt(data.get("ID").toString()), builder.build());
        }
        else if (data.get("type").toString().equals("test")) {
            Intent i =new Intent(getApplicationContext(),NewMissionActivity.class);
            i.putExtra("notificationModel",true);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setContentTitle(R.string.notification_QA_Title_HadAnswer + content+"test")
                    .setContentText(data.get("data").toString()+data.get("ID").toString())
                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(Integer.parseInt(data.get("ID").toString()), builder.build());
        }
        else{
            Log.d("FCM","訊號錯誤");
        }



    }
    /*
    * 以下為擷取QA程式碼
    *
    * */
    private void loadQandAData(boolean isGroup, final int ID, final int QAID, final Callback callback){
        if (isGroup) {
            ClientFunctions.updateGroupQA(ID, new ClientResponse() {
                @Override
                public void onResponse(String response) {
                    DataQas = QueryFunctions.getQAs();
                    int list =DataQas.size();
                    QA[] q =new QA[list];
                    for (int i=0;i<list;i++){
                        q[i]=DataQas.get(i);
                        if (q[i].id==QAID){
                            content=q[i].answer;
                            title=response;
                            break;
                        }
                    }
                    callback.run();
                }
                @Override
                public void onErrorResponse(String response) {
                    Log.d("FCM",response);
                }
            });
        } else if(!isGroup)  {
            ClientFunctions.updateMissionQA(ID, new ClientResponse() {
                @Override
                public void onResponse(String response) {
                    DataQas = QueryFunctions.getQAs();
                    int list =DataQas.size();
                    QA[] q =new QA[list];
                    Log.d("FCMTest", String.valueOf(list));
                    for (int i=0;i<list;i++){
                        q[i]=DataQas.get(i);
                       if (q[i].id==QAID){
                            content = q[i].answer;
                           break;
                        }
                    }
                    callback.run();
                }

                @Override
                public void onErrorResponse(String response) {
                    Log.d("FCM",response);
                }
            });
        }

    }

}