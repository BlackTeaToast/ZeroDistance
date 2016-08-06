package com.yoyoyee.zerodistance.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MainActivity;
import com.yoyoyee.zerodistance.activity.MissionActivity;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by futur on 2016/7/30.
 * 未來進度，進行時間BUG維護，若調整時間將重新計算
 *
 *
 */
public class TimeBroadcastReceiver extends BroadcastReceiver{
    public static int[] mission_ID;
    public static int[] group_ID;
    public static String[] mission_AT;
    public static String[] group_AT;
    public static int[] howNotiNotification ={5,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30} ;//更改這裡的值，值多少就是多久前通知(單位分鐘)
    public static Calendar check;
    private Boolean initial=false;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (initial==false){
            initial();
        }
        if (action.equals(Intent.ACTION_TIME_TICK)) {
            Calendar nowCal = Calendar.getInstance();
            if (check.compareTo(nowCal) <= 0) {
                int[] now = {nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH) + 1, nowCal.get(Calendar.DATE), nowCal.get(Calendar.HOUR_OF_DAY), nowCal.get(Calendar.MINUTE)};
                String nowString = "";
                for (int z = 0; z < now.length; z++) {

                    nowString = nowString + (now[z]<10 ? "0" + String.valueOf(now[z]):String.valueOf(now[z]));
                }
                for (int z = 0; z < mission_ID.length; z++) {
                    if ((Long.parseLong(mission_AT[z]) - Long.parseLong(nowString)) < 100) {
                        int math = (Integer.parseInt(mission_AT[z].substring(8, 10)) * 60 + Integer.parseInt(mission_AT[z].substring(10, 12))) - (now[3] * 60 + now[4]);
                        for (int x=0;x<howNotiNotification.length;x++) {
                            if (math ==howNotiNotification[x]){
                                showNotificationMission(context,mission_ID[z],howNotiNotification[x]);
                            }
                        }
                    }
                }
                for (int z = 0; z < group_ID.length; z++) {
                    if ((Long.parseLong(group_AT[z]) - Long.parseLong(nowString)) < 100) {
                        long math = (Long.parseLong(group_AT[z].substring(8, 10)) * 60 +Long.parseLong(group_AT[z].substring(10, 12))) - (now[3] * 60 + now[4]);
                        for (int x=0;x<howNotiNotification.length;x++) {
                            if (math ==howNotiNotification[x]){
                                showNotificationGroup(context,group_ID[z],howNotiNotification[x]);
                            }
                        }
                    }
                }
            }
            else if (check.compareTo(nowCal)> 0){
                Log.d("TimeBroadcast:","error :Time Error");
            }
        }

    }

    private void showNotificationMission(Context context,int missionID,int time){
        Mission mission=QueryFunctions.getMission(missionID);
        String title ="任務時間快到通知";
        String text ="任務："+mission.getTitle()+"還剩"+String.valueOf(time)+"分鐘就要開始囉~";
        Intent i =new Intent(context,MainActivity.class);
        i.putExtra("whichpage",2);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.login_pic8)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(missionID, builder.build());
    }
    private void showNotificationGroup(Context context,int groupID,int time ){
        Mission mission=QueryFunctions.getGroup(groupID);
        String title ="揪團時間快到通知";
        String text ="揪團："+mission.getTitle()+"還剩"+String.valueOf(time)+"分鐘就要開始囉~";
        Intent i =new Intent(context,MainActivity.class);
        i.putExtra("whichpage",2);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.login_pic8)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(groupID, builder.build());

    }
    public void updataOrderData(String[][] missions,String[][] groups,int listM,int listG){
        int[] missionTempID =new int[listM];
        int[] groupTempID =new int[listG];

        for (int z=0;z<listM;z++)
        {
            missionTempID[z] =Integer.parseInt(missions[0][z]);
            Log.d("TimeBoardcast M",missions[1][z]);
        }
        for (int z=0;z<listG;z++)
        {
            groupTempID[z] =Integer.parseInt(groups[0][z]);
        }
        mission_ID =missionTempID;
        group_ID =groupTempID;
        mission_AT =missions[1];
        group_AT =groups[1];
        Log.d("TimeBoardcast","load success ,"+String.valueOf(listM)+" mission ,"+String.valueOf(listG) +"group.");
    }
    public void initial(){
        check =Calendar.getInstance();
    }

    public void changeTime(int[] how,boolean initial){
        /*更換通知時間使用
        *  */
        if (initial ==false) { //不要重製
            howNotiNotification = how;
        }
        else if (initial ==true){ //還原原本設定
            howNotiNotification = new int[]{5,30};
        }
    }
}
//missions[1][z].codePointCount(0,4)+missions[1][z].codePointCount(4,6) + missions[1][z].codePointCount(6,8)*60*24 + missions[1][z].codePointCount(8,10)*60 + missions[1][z].codePointCount(10,12);