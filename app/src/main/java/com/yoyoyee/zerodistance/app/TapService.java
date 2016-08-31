package com.yoyoyee.zerodistance.app;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telecom.Call;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MissionActivity;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.QA;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by futur on 2016/4/11.
 * 背景運行
 */
public class TapService extends Service {
    interface Callback {void run();}
    final TimeBroadcastReceiver receiver =new TimeBroadcastReceiver();


    public static int i = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);





        Log.d("tapService","start");


/*        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            compareTime();
        }catch (Exception e){
         Log.e("TapService: ",e.toString());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    public void reloadAcceptList(final Callback call){
       ClientFunctions.updateUserAcceptMissions(new ClientResponse() {
           @Override
           public void onResponse(String response) {
               Log.d("sort Mission",response);
               ClientFunctions.updateUserAcceptGroups(new ClientResponse() {
                   @Override
                   public void onResponse(String response) {
                       Log.d("sort Group",response);
                       call.run();
                   }
                   @Override
                   public void onErrorResponse(String response) {
                   }
               });
           }
           @Override
           public void onErrorResponse(String response) {
           }
       });
    }


    public void compareTime(){
        reloadAcceptList(new Callback() {
            @Override
            public void run() {
                int listG,listM;
                SimpleDateFormat compareDate = new SimpleDateFormat("yyyyMMddHHmm");
                ArrayList<Group> groups ;
                ArrayList<Mission> missions ;
             //   try {
                    missions = QueryFunctions.getUserAcceptMissions();
                    groups = QueryFunctions.getUserAcceptGroups();
                    listG = groups.size();
                    listM = missions.size();
                    String[][] groupIDList = new String[2][listG];
                    String[][] missionIDList = new String[2][listM];
                    for (int z = 0; z < listG; z++) {
                        Group group = groups.get(z);
                        groupIDList[0][z] = String.valueOf(group.getId());
                        groupIDList[1][z] = compareDate.format(group.getExpAt()).toString();
                        Log.d("sort list", groupIDList[1][z]);
                    }
                    for (int z = 0; z < listM; z++) {
                        Mission mission = missions.get(z);
                        missionIDList[0][z] = String.valueOf( mission.getId());
                        missionIDList[1][z] = compareDate.format(mission.getExpAt()).toString();
                        Log.d("sort list", missionIDList[1][z]);
                    }
                    //String[] s ={"9833","278","786","857","877","999","104","786","2","785"};
                    int[] groupSort = sortAndOutNumber(Arrays.copyOf(groupIDList[1],listG));
                    int[] missionSort = sortAndOutNumber(Arrays.copyOf(missionIDList[1],listM));
                    String[][] groupIDListSort = new String[2][listG];
                    String[][] missionIDListSort = new String[2][listM];
                    for (int z=0;z<listG;z++)
                    {
                        Log.d("GSort",String.valueOf(groupSort[z]));
                        groupIDListSort[0][z] = groupIDList[0][groupSort[z]];
                        groupIDListSort[1][z] = groupIDList[1][groupSort[z]];
                    }
                    for (int z=0;z<listM;z++)
                    {
                        missionIDListSort[0][z] =missionIDList[0][missionSort[z]];
                        missionIDListSort[1][z] =missionIDList[1][missionSort[z]];
                        Log.d("MSort",String.valueOf(missionIDList[1][z]));
                    }
                        receiver.updataOrderData(missionIDListSort,groupIDListSort,listM,listG);
                        Log.d("sort","Success");


              //  }catch (Exception e){Log.d("tapService compareTime","error "+e);}
            }
        });
    }
    private int[] sortAndOutNumber(String sortNumber[]){
        int length =sortNumber.length;
        int[] list =new int[length];
        for (int x=0;x<length;x++) {
            list[x] = x;
            for (int z = 0; z < length; z++) {
                if (sortNumber[list[x]].compareTo(sortNumber[z]) <=0) {
                    list[x] = z;
                }
            }
            sortNumber[list[x]] ="0";
            for (int xx=0;xx<length;xx++)
            {
                Log.d("sort And"+"YEE"+String.valueOf(x), String .valueOf(sortNumber[xx]));
            }
        }
        return list;
    }

}

/* 20160711碼調
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            while (action.equals(Intent.ACTION_TIME_TICK)) {
                final int notifyID = 1; // 通知的識別號碼
                final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                try {
                    Calendar calender =Calendar.getInstance();

                    final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp).setContentTitle("每分報時系統").setContentText("現在時間" + calender.get(Calendar.HOUR_OF_DAY) + "點" + calender.get(Calendar.MINUTE) + "分").build(); // 建立通知
                    notificationManager.notify(notifyID, notification);
                }catch (Exception ex){
                    System.out.println("系統版本不支援");
                }

            }
        }
    };
*/


/*
if (action.equals(Intent.ACTION_TIME_TICK)) {
                final int notifyID = 1; // 通知的識別號碼
                final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                ArrayList<QA> allData = QueryFunctions.getQAs();
                try {
                    Calendar calender =Calendar.getInstance();

                    final Notification notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp).setContentTitle("每分報時系統").setContentText("現在時間" + calender.get(Calendar.HOUR_OF_DAY) + "點" + calender.get(Calendar.MINUTE) + "分").build(); // 建立通知
                    notificationManager.notify(notifyID, notification);
                }catch (Exception ex){
                    System.out.println("系統版本不支援");
                }

            }

*
 */
