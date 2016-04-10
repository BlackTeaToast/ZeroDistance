package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
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
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MissionActivity extends AppCompatActivity {

    //變數區====================================
    //取得傳入的intent

    //取得任務id
    int id ;//任務的編號 ; 錯誤則傳回0
    private Mission mission;//拿來抓misson

    private Toast toast;//改善參加鈕的toast 重複出現問題
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


    //拿來Format Date之用
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    //============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        Intent it  = this.getIntent();
        id= it.getIntExtra("id", 0);

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
                    if (acceptNumber > 1) {
                        TextView rateTotal = (TextView) findViewById(R.id.rateTotalM);
                        rateTotal.setText(R.string.rate_total);
                        //將個別評分開啟
                        buttonVisible = (Button) findViewById(R.id.ratebuttonM);
                        buttonVisible.setVisibility(View.VISIBLE);
                    } else {
                        TextView rateTotal = (TextView) findViewById(R.id.rateTotalM);
                        rateTotal.setText(R.string.rate_total_one);
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
                    joinButton.setText(R.string.is_joined);
                    if(toast == null)
                         toast.makeText(v.getContext(), R.string.not_aleady_joined ,Toast.LENGTH_SHORT).show();
                    else {
                        toast.cancel();
                        toast.makeText(v.getContext(), R.string.not_aleady_joined ,Toast.LENGTH_SHORT).show();

                    }

                } else {
                    joined = true;
                    joinButton.setText(R.string.not_joined);
                    if(toast == null)
                        toast.makeText(v.getContext(), R.string.is_already_joined ,Toast.LENGTH_SHORT).show();
                    else {
                        toast.cancel();
                        toast.makeText(v.getContext(), R.string.is_already_joined ,Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        //點選Q&A
        qAndAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isGroup = false;//是任務
                String publisher  = mission.getUserID();//發布者ID
                Intent it = new Intent();
                it.setClass(MissionActivity.this, QAActivity.class);
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
                Intent it = new Intent(MissionActivity.this, NewMissionActivity.class);
                it.putExtra("id", id);
                it.putExtra("isEdit", true);
                startActivity(it);

            }
        });

        //點選刪除
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "點選了刪除" ,Toast.LENGTH_SHORT).show();
              Intent  it = new Intent(MissionActivity.this, AchievementActivity.class);
                startActivity(it);
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

    //寫在這個裡面同時有重新整理的功效
    protected void onResume(){
        super.onResume();
        //設定
        readValue(id);
        //顯示
        setValue();

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

    //讀取並設置值
    private void readValue(int id){

        /*
        //取得misson
        mission = QueryFunctions.getMission(id);
        // 需先讀進以下變數才能正常顯示============================
        title = "鸚鵡";
        needNumber = 8;
        acceptNumber = 5;
        who = "鸚鵡養殖專家";
        timeS = "2016/12/12 15:11";
        timeT = "2017/01/01 12:00";
        where = "你家";
        whatPrice = "飲料";
        isFinished = false;

        //字體大小
        size = SessionFunctions.getUserTextSize();

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
*/

        //取得misson
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

        //設置餐與者
        user = new ArrayList<>();
        user.add("PatrickC");
        user.add("Treetops");
        user.add("Deep Moon");
        user.add("小毽子在飛呀");
        user.add("血色的狂氣-不滅的葛路米");

        //抓取內容
        doWhat =mission.getContent();

        //誰看到這個版面與是否參與
        whoSeeID = SessionFunctions.getUserUid();
        isTeacher = mission.getUserID().equals(SessionFunctions.getUserUid());//是否是發佈者
        //須在改過
        joined = false;

        //有圖片的話設置URL
        imagePath = "https://9559e92bf486a841acd42998e93115b5aa646a77.googledrive.com/host/0B79Cex31nQeXMTFWdmxTTUMwdFE/images/Macaw01.jpg";

    }

    //更改View中的值並顯示
    private void setValue(){
        //設置toolbar標題
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        //設定字型大小
        setFontSize();

        //設定語言
        setFont();

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

    }

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
            //QandA比重調成全版
            buttonVisible = (Button)findViewById(R.id.qAndAButtonM);
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) buttonVisible.getLayoutParams();
            layoutParams.weight = 1;
            buttonVisible.getParent().requestLayout();//ViewParent的requestLayout方法可以重新安排子視圖
        }
        else{
            //參加鈕要顯示
            buttonVisible = (Button)findViewById(R.id.joinOrNotM);
            buttonVisible.setVisibility(View.VISIBLE);
            //設定參與按鈕顯示
            if(joined)
                buttonVisible.setText(R.string.not_joined);
            else
                buttonVisible.setText(R.string.is_joined);
            //把已完成鈕關掉
            checkBox.setVisibility(View.GONE);
            //把編輯跟刪除關掉
            buttonVisible = (Button)findViewById(R.id.editButtonM);
            buttonVisible.setVisibility(View.GONE);
            buttonVisible = (Button)findViewById(R.id.deleteButtonM);
            buttonVisible.setVisibility(View.GONE);
            //QandA比重調成一半
            buttonVisible = (Button)findViewById(R.id.qAndAButtonM);
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) buttonVisible.getLayoutParams();
            layoutParams.weight = 0.5f;
            buttonVisible.getParent().requestLayout();//ViewParent的requestLayout方法可以重新安排子視圖

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
        //按鈕群(參加鈕、Q&A、編輯、刪除)
        ButtonTemp = (Button)findViewById(R.id.qAndAButtonM);
        ButtonTemp.setText(R.string.q_and_a);
        ButtonTemp = (Button)findViewById(R.id.editButtonM);
        ButtonTemp.setText(R.string.edit_button);
        ButtonTemp = (Button)findViewById(R.id.deleteButtonM);
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

}
