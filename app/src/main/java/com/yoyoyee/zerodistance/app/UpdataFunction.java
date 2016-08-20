package com.yoyoyee.zerodistance.app;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.QueryFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by futur on 2016/8/20.
 */
public class UpdataFunction {
    private DownloadManager manager;
    private long downloadId;

    public interface VolleyCallback {
        void onSuccess(int version,String url); //被showUpdataDialog()，getlastVersion()使用
    }
    /*
    * 使用方式
    *UpdataFunction updataFunction =new UpdataFunction();
    *updataFunction.showUpdataDialog(MainActivity.this);
    * */
    public void showUpdataDialog(final Context context){
        getlastVersion(context, new UpdataFunction.VolleyCallback() {
            @Override
            public void onSuccess(int version,String url) {
                AlertDialog alertDialog = getAlertDialog("這是一個對話框",String.valueOf(version),context,url);
                alertDialog.show();

            }
        });
    }

    private void getlastVersion(Context context, final VolleyCallback callback ){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(AppConfig.URL_GET_LAST_VERSION, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {
                    int vers =0;
                    String url;
                    vers = response.getInt("version");
                    url =response.getString("url");
                    Log.d("01-1"," lastVersionProcess");
                    callback.onSuccess(vers,url);
                } catch (JSONException e) {
                    Log.d("TAG", e.toString());
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        mQueue.add(jsonObjectRequest);
    }
/*Dialog
* 自訂更新Dialog
* 會使用到startDownload()
* */
    private AlertDialog getAlertDialog(String title,String message,final Context context,final String url){
        //產生一個Builder物件
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //設定Dialog的標題
        builder.setTitle(title);
        //設定Dialog的內容
        builder.setMessage(message);
        //設定Positive按鈕資料
        builder.setPositiveButton("現在下載", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //按下按鈕時顯示快顯
                Toast.makeText(context, "開始下載，下載進度請查看通知列", Toast.LENGTH_SHORT).show();
                startDownload(url,context);

            }
        });
        //設定Negative按鈕資料
        builder.setNegativeButton("下次再說", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //按下按鈕時顯示快顯
                Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
            }
        });
        //利用Builder物件建立AlertDialog
        return builder.create();
    }

    /*
    * 透過DownloadManager下載
    * 會被getAlertDialog()使用
    * */
    public void startDownload(String url,final Context context){
        manager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = manager.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                            Intent i = new Intent();
                            i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                            Toast.makeText(context, "下載完成，點擊通知列下載完成訊息安裝", Toast.LENGTH_SHORT).show();
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                    .setAutoCancel(true)
                                    .setContentTitle("更新檔"+filename+"已下載完成")
                                    .setContentText("點擊我轉跳下載檔案列表")
                                    .setSmallIcon(R.drawable.ic_chat_bubble_outline_white_48dp)
                                    .setContentIntent(pendingIntent);
                            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                            manager.notify(NotificationID.UPDATA_ID, builder.build());
                        }
                    }
                }
            }
        };

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downloadId = manager.enqueue(request);

    }

}
