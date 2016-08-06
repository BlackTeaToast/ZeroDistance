package com.yoyoyee.zerodistance.activity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
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
import com.yoyoyee.zerodistance.helper.ImageViewAdapter;
import com.yoyoyee.zerodistance.helper.QAAdapter;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.datatype.MissionAccept;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
missonActivity架構：
onPrepareOptionsMenu為右上角選單會使用到，每當invalidateOptionsMenu()被呼叫時，都會去執行

剛進來這個Activity時，會去抓取傳進來的id
設定每個按鈕的作用，不管他們有沒有顯示在畫面上

onResume每次都會去呼叫readValue()，每次執行readValue之前都會開啟loading讀取視窗，
並且在錯誤時(抓不到資料庫時)，或是成功抓到資料後執行getMissionImageCount()之後，關掉loading讀取視窗。
getMissionImageCount()成功後會去setValue()
setValue裡面有：
        設定字型大小
        setFontSize();
        //設定語言
        setFont();
        還有
        //設置老師與學生的差別，以及是否參加
        setTeacherOrStudent();



 */

public class MissionActivity extends AppCompatActivity {

    //變數區====================================
    //更新成功或失敗讀取用
    private boolean updateError;
    private int updateCount = 0;


    //創建選單按鈕
   public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);

       //把前一個選單資料先清除
        menu.clear();
         getMenuInflater().inflate(R.menu.menu_mission, menu);


        //現在改成 是不是發佈者一開始看到的選單都一樣 2016/7/7
        if(isTeacher){

            //把編輯跟刪除打開
            menu.setGroupVisible(R.id.mission_toolbar_group_more, true);

            //把參加鈕關掉
            menu.setGroupVisible(R.id.mission_toolbar_group_join, false);
            menu.setGroupVisible(R.id.mission_toolbar_group_not_join, false);

        }
        else{

                //不顯示參加、不參加鈕。這兩個功能改到more裡面，所以現在選單上的兩個鈕都關掉了
            menu.setGroupVisible(R.id.mission_toolbar_group_join, false);
            menu.setGroupVisible(R.id.mission_toolbar_group_not_join, false);


            //把有參加、不參加與檢舉的more選項打開
            menu.setGroupVisible(R.id.mission_toolbar_group_more, true);

        }
       return true;

    }

    //設定選單按下之動作
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getGroupId()) {
            //點選Q&A
            case R.id.mission_toolbar_group_QA:
                boolean isGroup = false;//是任務
                String publisher  = mission.getUserID();//發布者ID
                Intent it = new Intent();
                it.setClass(MissionActivity.this, QAActivity.class);
                it.putExtra("isGroup", isGroup);
                it.putExtra("publisher", publisher );
                it.putExtra("id",id);

                startActivity(it);

                break;

            //點選更多選單
            case R.id.mission_toolbar_group_more:
                //顯示more 裡面的項目，現在除了Edit 跟 Delete 之外，如果不是發佈者，改為顯示參加鈕與檢舉
                showEditAndDelete();
                break;
        }
        return true;
    }

    //取得任務id
    private int id ;//任務的編號 ; 錯誤則傳回0
    private Mission mission;//拿來抓misson
    private ArrayList<MissionAccept> missonAccept;

    private float size;//定義所有文字的大小
    private Toolbar toolbar;
    private TextView need;
    private TextView wait;
    private TextView whoSent;
    private TextView timeSent;
    private TextView content;
    private TextView timeToDo;
    private TextView place;
    private TextView price;
    private TextView users;
    private RatingBar ratingBar;
    private ArrayList<String> user;//拿來存有那些人
    private CheckBox checkBox;
    private GridLayout gridLayout;//評分區
    private Button buttonVisible;
    private boolean isTeacher;
    private int needNumber; //需求人數
    private int acceptNumber;//已餐與人數
    private boolean isFinished;//一開始是否勾選已完成
    private ImageView imageView;

    private Button rateButton;
    private Button joinButton;
    private Button qAndAButton;
    private Button editButton;
    private Button deleteButton;
    private Button finishButton;

    private String title;
    private String who;//發布者
    private String timeS;//timeSent
    private String timeT;//timeToDo
    private String where;//place
    private String whatPrice;
    private String doWhat;//content
    private String whoSeeID;//看到的人的ID,拿來判斷是否參與
    private boolean joined;//是否有餐與
    private ArrayList<String> imageURL;
    private int stars = 0;//設定的星星數量
    private int trueStars = 0;//已評分過的星星數量，比較用，是實際上的資料庫資料。

    //拿來停止，直到某件事做完才繼續執行用
    private ProgressDialog pDialog;
    public static boolean PD=false;

    //在finish( ) 之前 update手機資料庫且不執行readValue
    //此任務刪除時，會先把notRead設定為ture，以避免再次readValue讀到空的任務，造成錯誤
    private boolean notRead = false;


    //拿來Format Date之用
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");


    //============================================



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        Intent it  = this.getIntent();
        id= it.getIntExtra("id", 0);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");


        //findViewById--------------------------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.mission_tool_bar);
        checkBox = (CheckBox)findViewById(R.id.checkFinishM);
        gridLayout = (GridLayout)findViewById(R.id.checkedM);
        need = (TextView)findViewById(R.id.needM);
        wait = (TextView)findViewById(R.id.waitM);
        whoSent = (TextView)findViewById(R.id.whoSentM);
        timeSent = (TextView)findViewById(R.id.timeSentM);
        content = (TextView)findViewById(R.id.contentM);
        timeToDo = (TextView)findViewById(R.id.timeToDoM);
        place = (TextView)findViewById(R.id.whereM);
        price = (TextView)findViewById(R.id.priceM);
        users = (TextView)findViewById(R.id.usersM);
        ratingBar = (RatingBar)findViewById(R.id.ratingBarM);
        imageView = (ImageView)findViewById(R.id.imageViewM);
        rateButton = (Button)findViewById(R.id.ratebuttonM);
        finishButton = (Button)findViewById(R.id.finishButton) ;

        //-------------------------------------------------------------------------------


        //如果點選了才開啟評分區
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //有勾選的話
                if (isChecked) {

                    //打開評分區
                    gridLayout.setVisibility(View.VISIBLE);

                    //因應人數改變評分文字與按鈕
                    if (acceptNumber > 1) {
                        TextView rateTotal = (TextView) findViewById(R.id.rateTotalM);
                        rateTotal.setText(R.string.rate_total);
                        //將個別評分開啟
                        buttonVisible = (Button) findViewById(R.id.ratebuttonM);
                        buttonVisible.setVisibility(View.GONE);
                    } else {
                        TextView rateTotal = (TextView) findViewById(R.id.rateTotalM);
                        rateTotal.setText(R.string.rate_total_one);
                        //將個別評分關閉
                        buttonVisible = (Button) findViewById(R.id.ratebuttonM);
                        buttonVisible.setVisibility(View.GONE);
                    }
                } else {
                    //關閉評分區
                    gridLayout.setVisibility(View.GONE);
                }
            }
        });



        //評分之後
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                //調整評分
                stars = (int) rating;
                //有調整評分時，打開完成紐(重新評分用)。由於評分完成後，完成紐會關閉。
                //如果任務已經被完成過，且星星有調整過的話才顯示完成鈕
                if(isFinished && stars!=trueStars)
                    finishButton.setVisibility(View.VISIBLE);
                //如果任務已經被完成過，但是調整的星星數量又調整回原本的數量，則不顯示完成鈕。
                else if(isFinished && stars==trueStars)
                    finishButton.setVisibility(View.GONE);

            }
        });

        //點選評分完成鈕
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areYouSureToGiveStar();
            }
        });

        //點選個別評分
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "點選了個別評分" ,Toast.LENGTH_SHORT).show();
            }
        });
    }//onCreate


    //寫在這個裡面同時有重新整理的功效(每次回到此頁面都會重整)
    protected void onResume(){
        super.onResume();

        updateError = true;
        updateCount = 5;

       //設定
        showDialog();
        readValue();

    }

    //讀取並設置值
    private void readValue(){

        //有圖片的話設置URL
        //imagePath = "http://ec2-52-26-84-202.us-west-2.compute.amazonaws.com:3000/zerodistance/update/getMissionImage";

        //更新接受此任務的人

        //更新手機資料庫的參與者
        ClientFunctions.updateMissionAcceptUser(id, new ClientResponse() {
            @Override
            public void onResponse(String response) {
                //將手機資料庫的參與者取得
                missonAccept = QueryFunctions.getMissionAceeptUser();
                //設置餐與者
                user = new ArrayList<>();
                if(missonAccept.size()!=0)
                    for(int i=0 ; i<missonAccept.size() ; i++){
                        user.add(missonAccept.get(i).userName);
                    }
                else{
                    user.add(getResources().getString(R.string.no_one_join_now));
                }

                //設定自己是否是參與者
                joined = isJoined();

                //更新成功
                updateError = true;
                updateCount = 5;
                //更新圖片數量並且讀入url
                getMissionImageCount();

            }

            @Override
            public void onErrorResponse(String response) {

                if(updateCount>0){
                    updateCount--;
                    readValue();
                }
                if(updateError == true){
                    hideDialog();
                    Toast.makeText(getApplicationContext(), R.string.reading_error ,Toast.LENGTH_SHORT).show();
                    updateError = false;
                }
            }
        });

        mission = QueryFunctions.getMission(id);

        // 需先讀進以下變數才能正常顯示============================
        title = mission.getTitle();
        needNumber = mission.getNeedNum();
        acceptNumber = mission.getCurrentNum();
        //須在改過，改成發布者名
        who = mission.getUserName();
        //設置時間
        Date dateTemp = mission.getCreateAt();
        timeS = dateFormat.format(dateTemp);
        //須在改過，改成執行時間
        dateTemp = mission.expAt;
        timeT = dateFormat.format(dateTemp);
        where = mission.getPlace();
        whatPrice = mission.getReward();
        isFinished = mission.isFinished;

        //字體大小
        size = SessionFunctions.getUserTextSize();


        //抓取內容
        doWhat =mission.getContent();

        //誰看到這個版面與是否參與
        whoSeeID = SessionFunctions.getUserUid();
        isTeacher = mission.getUserID().equals(SessionFunctions.getUserUid()) && SessionFunctions.isTeacher() ;//是否是老師且是發佈者


    }

    //更改View中的值並顯示
    private void setValue(){

        //設置toolbar標題
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        //imageViewAdapter 顯示圖片 ; 使用fragment_imageview.xml 當作每個 holder 來使用==========
        ImageViewAdapter imageViewAdapter = new ImageViewAdapter(imageURL);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //使用默認動畫
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(imageViewAdapter);
        //================================================================================

        //設定字型大小
        setFontSize();

        //設定語言
        setFont();

        //把參與者丟上去
        int howMany = user.size();
        String userTemp = "";
        //將ArrayList裡的資料讀出
        for(int i=0 ; i<howMany && i<needNumber ; i++){
            userTemp += user.get(i);
            if(i!=needNumber-1)
                userTemp += "\n";
        }
        users.setText(userTemp);
        //如果有候補的話也讀出
        if(acceptNumber > needNumber){
            userTemp = "";
            for(int i=needNumber ; i<howMany ; i++){
                userTemp += user.get(i);
                if(i!=howMany-1)
                    userTemp += "\n";
            }
            wait.setText(userTemp);
        }

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
        //獎勵種類
        price.setText(whatPrice);

        //設置老師與學生的差別，以及是否參加
        setTeacherOrStudent();

    }

    //設置老師與學生的差別，包含參加鈕的文字
    private void setTeacherOrStudent(){
        //設置已完成是否被勾選
        checkBox.setChecked(isFinished);
        //設定評分區，如果是已完成，則勾選已完成並顯示評分區
        if(isFinished && isTeacher) {
            //將評分完成鈕關閉
            finishButton.setVisibility(View.GONE);
            //顯示評分區
            gridLayout.setVisibility(View.VISIBLE);
            //不能取消勾選已完成
            checkBox.setClickable(false);

        }
        else
            gridLayout.setVisibility(View.GONE);

        //重新整理 (刷新)OptionsMenu
        invalidateOptionsMenu();

        if(isTeacher){

            //把已完成鈕打開
            checkBox.setVisibility(View.VISIBLE);

        }
        else{

            //把已完成鈕關掉
            checkBox.setVisibility(View.GONE);


        }
    }

    //回傳觀看者是否參加
    private boolean isJoined(){
        boolean temp = false;
        for(int i=0 ; i<missonAccept.size() ; i++){
            if(whoSeeID.equals(missonAccept.get(i).userUid)){
                temp = true;
            }
        }
        return temp;
    }

    //設置字體大小
    private void setFontSize(){
        TextView textViewTemp;
        //委託人
        textViewTemp = (TextView)findViewById(R.id.whoSentTitleM);
        textViewTemp.setTextSize(size+9);
        textViewTemp = (TextView)findViewById(R.id.whoSentM);
        textViewTemp.setTextSize(size+5);
        //發文時間
        textViewTemp = (TextView)findViewById(R.id.timeSentTitleM);
        textViewTemp.setTextSize(size+5);
        textViewTemp = (TextView)findViewById(R.id.timeSentM);
        textViewTemp.setTextSize(size);
        //內容
        textViewTemp = (TextView)findViewById(R.id.contentM);
        textViewTemp.setTextSize(size+5);
        //執行時間
        textViewTemp = (TextView)findViewById(R.id.timeToDoTitleM);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.timeToDoM);
        textViewTemp.setTextSize(size+3);
        //執行地點
        textViewTemp = (TextView)findViewById(R.id.whereTitleM);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.whereM);
        textViewTemp.setTextSize(size+3);
        //獎勵
        textViewTemp = (TextView)findViewById(R.id.priceTitleM);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.priceM);
        textViewTemp.setTextSize(size+3);
        //人數
        textViewTemp = (TextView)findViewById(R.id.needTitleM);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.needM);
        textViewTemp.setTextSize(size+3);
        //參與者
        textViewTemp = (TextView)findViewById(R.id.usersTitleM);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.usersM);
        textViewTemp.setTextSize(size+3);
        //候補者
        textViewTemp = (TextView)findViewById(R.id.waitTitleM);
        textViewTemp.setTextSize(size+7);
        textViewTemp = (TextView)findViewById(R.id.waitM);
        textViewTemp.setTextSize(size+3);
        //已完成
        checkBox.setTextSize(size+7);
        //評分(統一、個別)
        textViewTemp = (TextView)findViewById(R.id.rateTotalM);
        textViewTemp.setTextSize(size+7);
        Button ButtonTemp;
        ButtonTemp = (Button)findViewById(R.id.ratebuttonM);
        ButtonTemp.setTextSize(size+5);

    }

    //設置文字，更改語言時使用
    private void setFont(){
        TextView textViewTemp;
        //委託人
        textViewTemp = (TextView)findViewById(R.id.whoSentTitleM);
        textViewTemp.setText(R.string.who_is_publisher);
        //發文時間
        textViewTemp = (TextView)findViewById(R.id.timeSentTitleM);
        textViewTemp.setText(R.string.when_update);
        //執行時間
        textViewTemp = (TextView)findViewById(R.id.timeToDoTitleM);
        textViewTemp.setText(R.string.when_to_do);
        //執行地點
        textViewTemp = (TextView)findViewById(R.id.whereTitleM);
        textViewTemp.setText(R.string.where_to_do);
        //獎勵
        textViewTemp = (TextView)findViewById(R.id.priceTitleM);
        textViewTemp.setText(R.string.what_price);
        //人數
        textViewTemp = (TextView)findViewById(R.id.needTitleM);
        textViewTemp.setText(R.string.how_many_people);
        //參與者
        textViewTemp = (TextView)findViewById(R.id.usersTitleM);
        textViewTemp.setText(R.string.who_is_joined);
        //已完成
        checkBox.setText(R.string.is_finished);
        //評分(統一、個別)
        textViewTemp = (TextView)findViewById(R.id.rateTotalM);
        if(acceptNumber>1)
        textViewTemp.setText(R.string.rate_total);
        else
        textViewTemp.setText(R.string.rate_total_one);

        Button ButtonTemp;
        ButtonTemp = (Button)findViewById(R.id.ratebuttonM);
        ButtonTemp.setText(R.string.rate_personal);


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
                //確定有成功參加
                updateError = true;
                updateCount = 5;
                //更新手機資料庫
                Toast.makeText(getApplicationContext(), R.string.is_already_joined ,Toast.LENGTH_SHORT).show();
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
        ClientFunctions.removeMissionAccept(id, new ClientResponse() {
            @Override
            public void onResponse(String response) {
                //確定有取消參加
                updateError = true;
                updateCount = 5;
                //更新手機資料庫
                Toast.makeText(getApplicationContext(), R.string.not_aleady_joined ,Toast.LENGTH_SHORT).show();
                updateMissions();
            }

            @Override
            public void onErrorResponse(String response) {
                if(updateCount>0){
                    updateCount--;
                    dontWantJoin();
                }

                if(updateError){
                    updateError = false;
                    Toast.makeText(getApplicationContext(), R.string.not_join_error ,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //更新手機任務資料庫
    private void updateMissions(){
        ClientFunctions.updateMissions(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                //確定更新完之後，重新讀取
                //確定有成功參加，且成功更新手機資料庫

                //沒有被刪掉才重新整理，如果此任務被刪除，執行updateMission時則直接關掉
                if(!notRead) {
                    showDialog();
                    updateError = true;
                    updateCount = 5;
                    readValue();
                }
                //如果不讀，代表要finish()掉的前置動作
                else{
                    //關掉
                    finish();
                }


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

    //刪除任務
    private void deleteMisson(){
        ClientFunctions.deleteMission(id, new ClientResponse() {
            @Override
            //確定刪除完成後
            public void onResponse(String response) {
                //確定有成功刪除
                updateError = true;
                updateCount = 5;
                //提示該揪團或任務已刪除
                Toast.makeText(getApplicationContext(), R.string.delete_success ,Toast.LENGTH_SHORT).show();

                //更新資料庫完，finish()
                notRead = true;
                updateMissions();

            }

            @Override
            public void onErrorResponse(String response) {
                if(updateCount>0){
                    updateCount--;
                    deleteMisson();
                }

                if(updateError){
                    updateError = false;
                    Toast.makeText(getApplicationContext(), R.string.delete_error ,Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    //確認是否要刪除Dialog
    private void areYouSureToDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MissionActivity.this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.are_you_sure_to_delete);
        builder.setIcon(R.drawable.ic_delete_black_24dp);


        //取消鈕
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //確認鈕
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //刪除
                updateCount = 5;
                updateError = true;
                deleteMisson();
            }
        });

        builder.create().show();

    }

    //確認參加Dialog
    private void areYouSureToJoin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MissionActivity.this);
        builder.setTitle(R.string.is_joined);
        builder.setMessage(R.string.are_you_sure_to_join);
        builder.setIcon(R.drawable.ic_person_add_black_48dp);


        //取消鈕
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //確認鈕
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //參加
                updateCount = 5;
                updateError = true;
                wantJoin();
            }
        });

        builder.create().show();

    }

    //確認取消參加Dialog
    private void areYouSureToNotJoin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MissionActivity.this);
        builder.setTitle(R.string.not_joined);
        builder.setMessage(R.string.are_you_sure_to_not_join);
        builder.setIcon(R.drawable.ic_person_black_48dp);


        //取消鈕
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //確認鈕
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(acceptNumber<=needNumber) {
                    Toast.makeText(getApplicationContext(), "沒有候補者，取消失敗", Toast.LENGTH_SHORT).show();

                }

                //取消參加
                else{
                    updateCount = 5;
                    updateError = true;
                    dontWantJoin();
                }

            }
        });

        builder.create().show();

    }

    //確認是否要給予幾顆星Dialog
    private void areYouSureToGiveStar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MissionActivity.this);
        //設置標題
        //如果已經完成的話，則由給予評分改為，重新給予評分
        if(isFinished)
            builder.setTitle(R.string.re_give_score);
        else
            builder.setTitle(R.string.give_score);

        String message = getString(R.string.give) + " "+stars+" " + getString(R.string.star);
        builder.setMessage(message);

        if(stars == 0)
            builder.setIcon(R.drawable.ic_thumb_down_black_24dp);
        else
            builder.setIcon(R.drawable.ic_thumb_up_black_24dp);

        //取消鈕
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //確認鈕
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //重新設定讀取資料庫次數為5，預設為不能正常讀取資料庫
                updateCount = 5;
                updateError = true;
                setMissionFinished();

            }
        });

        builder.create().show();

    }

    private void areYouSureToReport(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MissionActivity.this);
        builder.setTitle(R.string.report_pop);
        builder.setMessage(R.string.are_you_sure_to_report);
        builder.setIcon(R.drawable.ic_gavel_black_48dp);


        //取消鈕
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //確認鈕
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //檢舉

            }
        });

        builder.create().show();

    }

    //顯示編輯與刪除選單； 如果非發布者，改為顯示是否參加鈕與檢舉鈕
    private void showEditAndDelete(){
        View viewTemp = findViewById(R.id.misson_toolbar_more);
        final PopupMenu popupmenu = new PopupMenu(MissionActivity.this, viewTemp);
         popupmenu.inflate(R.menu.menu_popup_mission_and_group); // API 14以上才支援此方法.

        if(isTeacher){
            //如果是發佈者，只顯示發佈者的group
            popupmenu.getMenu().setGroupVisible(R.id.not_publisher_not_joined_group, false);
            popupmenu.getMenu().setGroupVisible(R.id.not_publisher_joined_group, false);

        }
        else if(joined){
            //如果非發布者，有參加則顯示取消參加的選項；沒有則相反，把編輯刪除選項關掉
            popupmenu.getMenu().setGroupVisible(R.id.is_publisher_group, false);
            popupmenu.getMenu().setGroupVisible(R.id.not_publisher_not_joined_group, false);
            popupmenu.getMenu().setGroupVisible(R.id.not_publisher_joined_group, true);

        }
        else{
            popupmenu.getMenu().setGroupVisible(R.id.is_publisher_group, false);
            popupmenu.getMenu().setGroupVisible(R.id.not_publisher_not_joined_group, true);
            popupmenu.getMenu().setGroupVisible(R.id.not_publisher_joined_group, false);
        }

        popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // 設定popupmenu項目點擊傾聽者.

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //點選編輯
                    case (R.id.popedit_publishergroup):
                        Intent it = new Intent(MissionActivity.this, NewMissionActivity.class);
                        it.putExtra("id", id);
                        it.putExtra("isEdit", true);
                        startActivity(it);

                        break;

                    //點選刪除
                    case (R.id.popdel_publishergroup):
                        areYouSureToDelete();
                        break;

                    //點選參加
                    case (R.id.join_not_publishergroup):
                        areYouSureToJoin();
                        break;

                    //點選取消參加
                    case (R.id.notjoin_not_publishergroup):
                        areYouSureToNotJoin();
                        break;

                    //點選檢舉(尚未參加)
                    case (R.id.report_not_joinedgroup):
                        areYouSureToReport();
                        break;

                    //點選檢舉(已參加)
                    case (R.id.report_joinedgroup):
                        areYouSureToReport();
                        break;
                }
                return true;
            }

        });
        //設置setForceShowIcon為true，強制顯示圖案
        try {
            Field mFieldPopup=popupmenu.getClass().getDeclaredField("mPopup");
            mFieldPopup.setAccessible(true);
            MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupmenu);
            mPopup.setForceShowIcon(true);
        } catch (Exception e) {

        }
        popupmenu.show();

    }

    //讀取有幾張圖片
    private void getMissionImageCount(){

        ClientFunctions.getMissionImageCount(id, new ClientResponse() {
            @Override
            public void onResponse(String response) {
                //將count設置為response 傳回之值
                int count;
                count = Integer.valueOf(response);
                //成功獲取照片數量
                //依數量設置url
                setImageUrl(count);

                //全部皆已成功
                updateCount = 0;
                updateError = false;
                //顯示
                setValue();
                hideDialog();
            }

            @Override
            public void onErrorResponse(String response) {
                if(updateCount>0){
                    updateCount--;
                    getMissionImageCount();
                }

                if(updateError){
                    updateError = false;
                    Toast.makeText(getApplicationContext(), R.string.pic_reading_error_ ,Toast.LENGTH_SHORT).show();
                }
                hideDialog();

            }
        });
    }

    //依數量設置圖片url
    public void setImageUrl(int howMany){
        imageURL = new ArrayList<>();


        for(int i=0 ; i<howMany ; i++) {
           // imageURL.add(ClientFunctions.getMissionImageUrl(id, i));
            imageURL.add(ClientFunctions.getMissionImageUrl(id, i));
        }

    }

    //將任務設置為已完成並且評分
    public void setMissionFinished(){
        ClientFunctions.setMissionFinished(id, new ClientResponse() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MissionActivity.this, R.string.rating_finished,Toast.LENGTH_SHORT).show();
                //全部皆已成功
                //更新手機資料庫，並且finish()
                updateCount = 0;
                updateError = false;
                notRead = true;
                updateMissions();

            }

            @Override
            public void onErrorResponse(String response) {
                if(updateCount>0){
                    updateCount--;
                    //失敗則繼續try完updateCount的次數
                    setMissionFinished();
                }

                //到最後還是沒有成功則顯示失敗
                if(updateError){
                    updateError = false;
                    Toast.makeText(getApplicationContext(), R.string.rating_fail ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





}
