package com.yoyoyee.zerodistance.app;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.datatype.QA;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by futur on 2016/4/11.
 * 背景運行
 */
public class TapService extends Service {
    private Handler handler = new Handler();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            while (action.equals(Intent.ACTION_TIME_TICK)) {
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
        }
    };

}
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
