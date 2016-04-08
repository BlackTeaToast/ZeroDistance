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

public class GroupActivity extends AppCompatActivity {

    //變數區====================================
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

    //=============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // 需先讀進以下變數才能正常顯示============================
        title = "鸚鵡";
        needNumber = 8;
        acceptNumber = 5;
        who = "鸚鵡養殖專家";
        timeS = "2016/12/12";
        timeT = "20170101";
        where = "你家";

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
        isPublisher = true;
        joined = false;

        //有圖片的話設置URL
        imagePath = "https://9559e92bf486a841acd42998e93115b5aa646a77.googledrive.com/host/0B79Cex31nQeXMTFWdmxTTUMwdFE/images/Macaw01.jpg";

        // =========================================================

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

        //設置發佈者與非發佈者的差別
        setPublisherOrNot();

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
                if (isPublisher) {
                    isPublisher = false;
                    setPublisherOrNot();
                }
                else {
                    isPublisher = true;
                    setPublisherOrNot();
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
        }
        else{
            //參加鈕要顯示
            buttonVisible = (Button)findViewById(R.id.joinOrNotG);
            buttonVisible.setVisibility(View.VISIBLE);
            //設定參與按鈕顯示
            if(joined)
                buttonVisible.setText("不參加");
            else
                buttonVisible.setText("參加");
            //把編輯跟刪除關掉
            buttonVisible = (Button)findViewById(R.id.editButtonG);
            buttonVisible.setVisibility(View.GONE);
            buttonVisible = (Button)findViewById(R.id.deleteButtonG);
            buttonVisible.setVisibility(View.GONE);

        }
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
