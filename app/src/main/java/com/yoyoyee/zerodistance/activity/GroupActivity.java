package com.yoyoyee.zerodistance.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.GroupAccept;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupActivity extends AppCompatActivity {

    //變數區====================================
    //更新成功或失敗讀取用
    private int updateCount = 0;
    private boolean updateError;

    //取得任務id
    int id ;//揪團的編號 ; 錯誤則傳回0
    private Group group;//拿來抓group
    private ArrayList<GroupAccept> groupAccept;

    private float size;//定義所有文字的大小
    private Toolbar toolbar;
    private TextView need;
    private TextView whoSent;
    private TextView timeSent;
    private TextView content;
    private TextView timeToDo;
    private TextView place;
    private TextView users;
    private ArrayList<String> user;//拿來存有那些人
    private Button buttonVisible;
    private boolean isPublisher;//是否為發佈者
    private int needNumber;
    private int acceptNumber;
    private ImageView imageView;

    private Button joinButton;
    private Button qAndAButton;
    private Button editButton;
    private Button deleteButton;

    private String title;
    private String who;
    private String timeS;//timeSent
    private String timeT;//timeToDo
    private String where;//place
    private String doWhat;//content
    private String whoSeeID;//看到的人的ID,拿來判斷是否參與
    private boolean joined;//是否有餐與
    private String imagePath;

    //拿來停止，直到某件事做完才繼續執行用
    private ProgressDialog pDialog;
    public static boolean PD=false;

    //拿來Format Date之用
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    //=============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent it  = this.getIntent();
        id= it.getIntExtra("id", 0);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");

        //findViewById--------------------------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.mission_tool_bar);
        need = (TextView)findViewById(R.id.needG);
        whoSent = (TextView)findViewById(R.id.whoSentG);
        timeSent = (TextView)findViewById(R.id.timeSentG);
        content = (TextView)findViewById(R.id.contentG);
        timeToDo = (TextView)findViewById(R.id.timeToDoG);
        place = (TextView)findViewById(R.id.whereG);
        users = (TextView)findViewById(R.id.usersG);
        imageView = (ImageView)findViewById(R.id.imageViewG);
        joinButton = (Button)findViewById(R.id.joinOrNotG);
        qAndAButton = (Button)findViewById(R.id.qAndAButtonG);
        editButton = (Button)findViewById(R.id.editButtonG);
        deleteButton = (Button)findViewById(R.id.deleteButtonG);

        //-------------------------------------------------------------------------------

        //參加或取消
        joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (joined) {

                    dontWantJoin();

                } else {
                    //參加
                    updateCount = 5;
                    updateError = true;
                    wantJoin();

                }


            }
        });

        //點選Q&A
        qAndAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isGroup = true;//是揪團
                String publisher  = group.getUserID();//發布者ID
                Intent it = new Intent();
                it.setClass(GroupActivity.this, QAActivity.class);
                it.putExtra("isGroup", isGroup);
                it.putExtra("publisher", publisher );
                it.putExtra("id",id);

                startActivity(it);
            }
        });

        //點選編輯
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "點選了編輯" ,Toast.LENGTH_SHORT).show();
            }
        });

        //點選刪除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "點選了刪除" ,Toast.LENGTH_SHORT).show();
            }
        });


    }//onCreate

    //寫在這裡面有重新整理的功效
    protected void onResume(){
        super.onResume();

        updateCount = 5;
        updateError = true;
        //讀取值
        readValue();


        //如果有圖片則顯示圖片
        if(imagePath!=null) {
            imageView.setVisibility(View.VISIBLE);
            //取自http://dean-android.blogspot.tw/2013/06/androidimageviewconverting-image-url-to.html
            //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    String url = params[0];
                    return getBitmapFromURL(url);
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    imageView.setImageBitmap(result);
                    super.onPostExecute(result);
                }
            }.execute(imagePath);
        }
        else{
            imageView.setVisibility(View.GONE);
        }


    }

    //讀取值
    private void readValue(){

        //有圖片的話設置URL
        imagePath = "https://9559e92bf486a841acd42998e93115b5aa646a77.googledrive.com/host/0B79Cex31nQeXMTFWdmxTTUMwdFE/images/Macaw01.jpg";

        //更新接受此任務的人
        showDialog();
        //更新手機資料庫的參與者
        ClientFunctions.updateGroupAcceptUser(id, new ClientResponse() {
            @Override
            public void onResponse(String response) {
                //將手機資料庫的參與者取得
                groupAccept = QueryFunctions.getGroupAceeptUser();
                //設置餐與者
                user = new ArrayList<>();
                if(groupAccept.size()!=0)
                    for(int i=0 ; i<groupAccept.size() ; i++){
                        user.add(groupAccept.get(i).userName);
                    }
                else{
                    user.add(getResources().getString(R.string.no_one_join_now));
                }

                //設定自己是否是參與者
                joined = isJoined();
                //顯示
                //成功讀取
                updateError = false;
                updateCount = 0;
                setValue();
                hideDialog();
            }

            @Override
            public void onErrorResponse(String response) {

                hideDialog();
                if(updateCount>0){
                    updateCount--;
                    readValue();
                }
                if(updateError==true){
                    updateError=false;
                    Toast.makeText(getApplicationContext(), R.string.reading_error ,Toast.LENGTH_SHORT).show();
                }
            }
        });

        //取得group
        group = QueryFunctions.getGroup(id);

        // 需先讀進以下變數才能正常顯示============================
        title = group.getTitle();
        needNumber = group.getNeedNum();
        acceptNumber = group.getCurrentNum();
        //須在改過，改成發布者名
        who = group.getUserID();
        //設置時間
        Date dateTemp = group.getCreateAt();
        timeS = dateFormat.format(dateTemp);
        //須在改過，改成執行時間
        dateTemp = group.expAt;
        timeT = dateFormat.format(dateTemp);
        where = group.getPlace();

        //字體大小
        size = SessionFunctions.getUserTextSize();

        //抓取內容
        doWhat =group.getContent();

        //誰看到這個版面與是否參與
        whoSeeID = SessionFunctions.getUserUid();
        isPublisher = group.getUserID().equals(SessionFunctions.getUserUid());//是否是發佈者

    }

    //顯示值
    private void setValue(){
        //設置toolbar標題
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        //設定字型大小
        setFontSize();

        //設定語言
        setFont();

        //參與者
        int howMany = user.size();
        String userTemp = "";
        //將ArrayList裡的資料讀出
        for(int i=0 ; i<howMany ; i++){
            userTemp += user.get(i);
            if(i!=howMany-1)
                userTemp += "\n";
        }
        users.setText(userTemp);

        //設置人數
        need.setText(String.valueOf(acceptNumber) + "/" + String.valueOf(needNumber));
        //發布者
        whoSent.setText(who);
        //發布時間
        timeSent.setText(timeS);
        //內容
        content.setText(doWhat);
        //執行時間
        timeToDo.setText(timeT);
        //執行地點
        place.setText(where);

        //設置發佈者與非發佈者的差別
        setPublisherOrNot();

    }

    //設置發佈者與非發佈者的差別
    private void setPublisherOrNot(){
        if(isPublisher){
            //把編輯跟刪除打開
            buttonVisible = (Button)findViewById(R.id.editButtonG);
            buttonVisible.setVisibility(View.VISIBLE);
            buttonVisible = (Button)findViewById(R.id.deleteButtonG);
            buttonVisible.setVisibility(View.VISIBLE);
            //把參加鈕關掉
            buttonVisible = (Button)findViewById(R.id.joinOrNotG);
            buttonVisible.setVisibility(View.GONE);
            //QandA比重調成全版
            buttonVisible = (Button)findViewById(R.id.qAndAButtonG);
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) buttonVisible.getLayoutParams();
            layoutParams.weight = 1;
            buttonVisible.getParent().requestLayout();//ViewParent的requestLayout方法可以重新安排子視圖
        }
        else{
            //參加鈕要顯示
            buttonVisible = (Button)findViewById(R.id.joinOrNotG);
            buttonVisible.setVisibility(View.VISIBLE);
            //設定參與按鈕顯示
            if(joined)
                buttonVisible.setText(R.string.not_joined);
            else
                buttonVisible.setText(R.string.is_joined);
            //把編輯跟刪除關掉
            buttonVisible = (Button)findViewById(R.id.editButtonG);
            buttonVisible.setVisibility(View.GONE);
            buttonVisible = (Button)findViewById(R.id.deleteButtonG);
            buttonVisible.setVisibility(View.GONE);
            //QandA比重調成一半
            buttonVisible = (Button)findViewById(R.id.qAndAButtonG);
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) buttonVisible.getLayoutParams();
            layoutParams.weight = 0.5f;
            buttonVisible.getParent().requestLayout();//ViewParent的requestLayout方法可以重新安排子視圖

        }
    }

    //回傳觀看者是否參加
    private boolean isJoined(){
        boolean temp = false;
        for(int i=0 ; i<groupAccept.size() ; i++){
            if(whoSeeID.equals(groupAccept.get(i).userUid)){
                temp = true;
            }
        }
        return temp;
    }

    //設置字體大小
    private void setFontSize(){
        TextView textViewTemp;
        //委託人
        textViewTemp = (TextView)findViewById(R.id.whoSentTitleG);
        textViewTemp.setTextSize(size+9);
        textViewTemp = (TextView)findViewById(R.id.whoSentG);
        textViewTemp.setTextSize(size+5);
        //發文時間
        textViewTemp = (TextView)findViewById(R.id.timeSentTitleG);
        textViewTemp.setTextSize(size+5);
        textViewTemp = (TextView)findViewById(R.id.timeSentG);
        textViewTemp.setTextSize(size);
        //內容
        textViewTemp = (TextView)findViewById(R.id.contentG);
        textViewTemp.setTextSize(size+5);
        //執行時間
        textViewTemp = (TextView)findViewById(R.id.timeToDoTitleG);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.timeToDoG);
        textViewTemp.setTextSize(size+3);
        //執行地點
        textViewTemp = (TextView)findViewById(R.id.whereTitleG);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.whereG);
        textViewTemp.setTextSize(size+3);
        //人數
        textViewTemp = (TextView)findViewById(R.id.needTitleG);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.needG);
        textViewTemp.setTextSize(size+3);
        //參與者
        textViewTemp = (TextView)findViewById(R.id.usersTitleG);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.usersG);
        textViewTemp.setTextSize(size+3);
        //按鈕群(參加鈕、Q&A、編輯、刪除)
        Button ButtonTemp;
        ButtonTemp = (Button)findViewById(R.id.joinOrNotG);
        ButtonTemp.setTextSize(size+5);
        ButtonTemp = (Button)findViewById(R.id.qAndAButtonG);
        ButtonTemp.setTextSize(size+5);
        ButtonTemp = (Button)findViewById(R.id.editButtonG);
        ButtonTemp.setTextSize(size+5);
        ButtonTemp = (Button)findViewById(R.id.deleteButtonG);
        ButtonTemp.setTextSize(size+5);
    }

    //設置文字，更改語言時使用
    private void setFont(){
        TextView textViewTemp;
        //委託人
        textViewTemp = (TextView)findViewById(R.id.whoSentTitleG);
        textViewTemp.setText(R.string.who_is_publisher);
        //發文時間
        textViewTemp = (TextView)findViewById(R.id.timeSentTitleG);
        textViewTemp.setText(R.string.when_update);
        //執行時間
        textViewTemp = (TextView)findViewById(R.id.timeToDoTitleG);
        textViewTemp.setText(R.string.when_to_do);
        //執行地點
        textViewTemp = (TextView)findViewById(R.id.whereTitleG);
        textViewTemp.setText(R.string.where_to_do);
        //人數
        textViewTemp = (TextView)findViewById(R.id.needTitleG);
        textViewTemp.setText(R.string.how_many_people);
        //參與者
        textViewTemp = (TextView)findViewById(R.id.usersTitleG);
        textViewTemp.setText(R.string.who_is_joined);

        //按鈕群(參加鈕、Q&A、編輯、刪除)
        Button ButtonTemp;
        ButtonTemp = (Button)findViewById(R.id.qAndAButtonG);
        ButtonTemp.setText(R.string.q_and_a);
        ButtonTemp = (Button)findViewById(R.id.editButtonG);
        ButtonTemp.setText(R.string.edit_button);
        ButtonTemp = (Button)findViewById(R.id.deleteButtonG);
        ButtonTemp.setText(R.string.delete_button);

    }

    //讀取網路圖片，型態為Bitmap
    //取自http://dean-android.blogspot.tw/2013/06/androidimageviewconverting-image-url-to.html
    private Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing()){
            pDialog.show();
            PD=true;
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            PD=false;
        }
    }

    //參加
    private void wantJoin(){
        ClientFunctions.publishMissionAccept(id, new ClientResponse() {
            @Override
            //確定參加完成後
            public void onResponse(String response) {
                updateError = true;
                updateCount = 5;
                //更新手機資料庫
                updateMissions();

            }

            @Override
            public void onErrorResponse(String response) {
                if(updateCount>0){
                    updateCount--;
                    wantJoin();
                }

                if(updateError){
                    updateError = false;
                    Toast.makeText(getApplicationContext(), R.string.join_error ,Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    //不參加
    private void dontWantJoin(){
        Toast.makeText(getApplicationContext(), "無法取消參加", Toast.LENGTH_SHORT).show();

        //重新整理
        readValue();
    }

    private void updateMissions(){
        ClientFunctions.updateMissions(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                //確定更新完之後，重新讀取
                //確定有成功參加，且成功更新手機資料庫
                updateError = true;
                updateCount = 5;
                readValue();

            }

            @Override
            public void onErrorResponse(String response) {
                if(updateCount>0){
                    updateCount--;
                    updateMissions();
                }

                if(updateError){
                    updateError = false;
                    Toast.makeText(getApplicationContext(), R.string.reading_error ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
