package com.yoyoyee.zerodistance.activity;

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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MissionActivity extends AppCompatActivity {

    //變數區====================================
    private float size;//定義所有文字的大小
    private Toolbar toolbar;
    private TextView need;
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
    private int needNumber;
    private int acceptNumber;
    private boolean isFinished;//一開始是否勾選已完成
    private ImageView imageView;

    private Button rateButton;
    private Button joinButton;
    private Button qAndAButton;
    private Button editButton;
    private Button deleteButton;

    private String title;
    private String who;
    private String timeS;//timeSent
    private String timeT;//timeToDo
    private String where;//place
    private String whatPrice;
    private String doWhat;//content
    private String whoSeeID;//看到的人的ID,拿來判斷是否參與
    private boolean joined;//是否有餐與
    private String imagePath;

    //============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        // 需先讀進以下變數才能正常顯示============================
        title = "鸚鵡";
        needNumber = 8;
        acceptNumber = 5;
        who = "鸚鵡養殖專家";
        timeS = "2016/12/12";
        timeT = "20170101";
        where = "你家";
        whatPrice = "飲料";
        isFinished = false;

        //字體大小
        size = 15;

        //設置餐與者
        user = new ArrayList<>();
        user.add("PatrickC");
        user.add("Treetops");
        user.add("Deep Moon");
        user.add("小毽子在飛呀");
        user.add("血色的狂氣-不滅的葛路米");

        //抓取內容
        doWhat = "    一個人去買鸚鵡，看到一隻鸚鵡前標：此鸚鵡會兩門語言，售價二百元。另一隻鸚鵡前則標道：" +
                "此鸚鵡會四門語言，售價四百元。該買哪只呢？兩隻都毛色光鮮，非常靈活可愛。這人轉啊轉，拿不定主意。" +
                "結果突然發現一隻老掉了牙的鸚鵡，毛色暗淡散亂，標價八百元。                     " +
                "\n   這人趕緊將老闆叫來：這隻鸚鵡是不是會說八門語言？店主說：不。這人奇怪了：那為什麼又老又丑，" +
                "又沒有能力，會值這個數呢？店主回答：“因為另外兩隻鸚鵡叫這隻鸚鵡老闆。”" +
                "  這故事告訴我們，真正的領導人，不一定自己能力有多強，只要懂信任，懂授權，懂珍惜，就能團結比自己更強的力量，" +
                "進而提升自己的身價。\n        相反許多能力非常強的人卻因為過於完美主義，事必躬親，什麼人都不如自己，" +
                "最後只能做最好的公關人員、銷售代表，成不了優秀的領導人。";

        //誰看到這個版面與是否參與
        whoSeeID = "鸚鵡養殖專家";
        isTeacher = true;
        joined = false;

        //有圖片的話設置URL
        imagePath = "https://9559e92bf486a841acd42998e93115b5aa646a77.googledrive.com/host/0B79Cex31nQeXMTFWdmxTTUMwdFE/images/Macaw01.jpg";

        // =========================================================

        //findViewById--------------------------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.mission_tool_bar);
        checkBox = (CheckBox)findViewById(R.id.checkFinishM);
        gridLayout = (GridLayout)findViewById(R.id.checkedM);
        need = (TextView)findViewById(R.id.needM);
        whoSent = (TextView)findViewById(R.id.whoSentM);
        timeSent = (TextView)findViewById(R.id.timeSentM);
        content = (TextView)findViewById(R.id.contentM);
        timeToDo = (TextView)findViewById(R.id.timeToDoM);
        place = (TextView)findViewById(R.id.whereM);
        price = (TextView)findViewById(R.id.priceM);
        users = (TextView)findViewById(R.id.usersM);
        ratingBar = (RatingBar)findViewById(R.id.ratingBarM);
        imageView = (ImageView)findViewById(R.id.imageViewM);
        joinButton = (Button)findViewById(R.id.joinOrNotM);
        qAndAButton = (Button)findViewById(R.id.qAndAButtonM);
        editButton = (Button)findViewById(R.id.editButtonM);
        deleteButton = (Button)findViewById(R.id.deleteButtonM);
        rateButton = (Button)findViewById(R.id.ratebuttonM);

        //-------------------------------------------------------------------------------

        //設置toolbar標題
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        //設定字型大小
        setFontSize();

        //顯示標題文字，更改語言時使用


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

        //設置老師與學生的差別
        setTeacherOrStudent();

        //關掉評分區，等待被選取
        gridLayout.setVisibility(View.GONE);

        //如果點選了才開啟評分區
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //有勾選的話
                if (isChecked) {
                    isFinished = true;
                    gridLayout.setVisibility(View.VISIBLE);

                    //因應人數改變評分文字與按鈕
                    if (needNumber > 1) {
                        TextView rateTotal = (TextView) findViewById(R.id.rateTotalM);
                        rateTotal.setText("統一評分");
                        //將個別評分開啟
                        buttonVisible = (Button) findViewById(R.id.ratebuttonM);
                        buttonVisible.setVisibility(View.VISIBLE);
                    } else {
                        TextView rateTotal = (TextView) findViewById(R.id.rateTotalM);
                        rateTotal.setText("評分");
                        //將個別評分關閉
                        buttonVisible = (Button) findViewById(R.id.ratebuttonM);
                        buttonVisible.setVisibility(View.GONE);
                    }
                } else {
                    isFinished = false;
                    gridLayout.setVisibility(View.GONE);
                }
            }
        });

        //參加或取消
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joined) {
                    joined = false;
                    joinButton.setText("參加");
                } else {
                    joined = true;
                    joinButton.setText("不參加");
                }
            }
        });

        //點選Q&A
        qAndAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTeacher) {
                    isTeacher = false;
                    gridLayout.setVisibility(View.GONE);
                    setTeacherOrStudent();
                }
                else {
                    isTeacher = true;
                    setTeacherOrStudent();
                }
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

        //點選個別評分
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "點選了個別評分" ,Toast.LENGTH_SHORT).show();
            }
        });

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

    }//onCreate

    //設置老師與學生的差別
    private void setTeacherOrStudent(){
        //設置已完成是否被勾選
        checkBox.setChecked(isFinished);
        if(isFinished && isTeacher)
            gridLayout.setVisibility(View.VISIBLE);
        else
            gridLayout.setVisibility(View.GONE);

        if(isTeacher){
            //把已完成鈕打開
            checkBox.setVisibility(View.VISIBLE);
            //把編輯跟刪除打開
            buttonVisible = (Button)findViewById(R.id.editButtonM);
            buttonVisible.setVisibility(View.VISIBLE);
            buttonVisible = (Button)findViewById(R.id.deleteButtonM);
            buttonVisible.setVisibility(View.VISIBLE);
            //把參加鈕關掉
            buttonVisible = (Button)findViewById(R.id.joinOrNotM);
            buttonVisible.setVisibility(View.GONE);
        }
        else{
            //參加鈕要顯示
            buttonVisible = (Button)findViewById(R.id.joinOrNotM);
            buttonVisible.setVisibility(View.VISIBLE);
            //設定參與按鈕顯示
            if(joined)
                buttonVisible.setText("不參加");
            else
                buttonVisible.setText("參加");
            //把已完成鈕關掉
            checkBox.setVisibility(View.GONE);
            //把編輯跟刪除關掉
            buttonVisible = (Button)findViewById(R.id.editButtonM);
            buttonVisible.setVisibility(View.GONE);
            buttonVisible = (Button)findViewById(R.id.deleteButtonM);
            buttonVisible.setVisibility(View.GONE);

        }
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
        //已完成
        checkBox.setTextSize(size+7);
        //評分(統一、個別)
        textViewTemp = (TextView)findViewById(R.id.rateTotalM);
        textViewTemp.setTextSize(size+7);
        Button ButtonTemp;
        ButtonTemp = (Button)findViewById(R.id.ratebuttonM);
        ButtonTemp.setTextSize(size+5);
        //按鈕群(參加鈕、Q&A、編輯、刪除)
        ButtonTemp = (Button)findViewById(R.id.joinOrNotM);
        ButtonTemp.setTextSize(size+5);
        ButtonTemp = (Button)findViewById(R.id.qAndAButtonM);
        ButtonTemp.setTextSize(size+5);
        ButtonTemp = (Button)findViewById(R.id.editButtonM);
        ButtonTemp.setTextSize(size+5);
        ButtonTemp = (Button)findViewById(R.id.deleteButtonM);
        ButtonTemp.setTextSize(size+5);
    }

    //設置文字，更改語言時使用
    private void setFont(){

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

}
