package com.yoyoyee.zerodistance.activity;

        import android.content.Intent;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;


        import com.yoyoyee.zerodistance.R;
        import com.yoyoyee.zerodistance.app.QAndA;
        import com.yoyoyee.zerodistance.helper.QAAdapter;

public class QAActivity extends AppCompatActivity {
    private float size;//定義所有文字大小
    private Button GO;
   // QAndA Q =new QAndA();
    private Boolean isQ=true;
    private Toolbar toolbar;
    private int userID;
    private Button A;



    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        //toolbar
        toolbar= (Toolbar) findViewById(R.id.qAndA_Toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.qAndA_Title));
        //字體大小
        size = 15;
        //設定字型大小
    //    setFontSize();
        //按發問紐換頁功能
        GO = (Button)findViewById(R.id.for_Q_Button);
        if(equals(userID))
        {
         GO.setVisibility(Button.GONE);//發文者沒有發問功能
        }
        else {
            GO.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QAActivity.this, AskActivity.class);
                    startActivity(intent);
                }
            });
        }
        //回答按鍵
        A = (Button)findViewById(R.id.for_A_Buttom);

        //listview start
        String[] q_Q_Titletext = {"Q", "Q", "Q", "Q","Q", "Q"},a_A_Titletext = {"A", "A", "A", "A", "A", "A"};
        String[] q_Qtimetext = {"1/11 1:11", "12/11 11:11", "xx", "48/43", "154/45", "12/12"}, q_Qnametext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] q_Qcontenttext = {"你說把愛漸漸放下會走更遠,或許命運的謙讓我遇見", "你好阿", "xx", "你好阿", "xx", "你好阿"}, a_Atimetext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] a_Acontenttext = {"滾", "你有病嘛", "閃邊", "87", "P0", "好啦好啦"};

            QAAdapter QAAdapter = new QAAdapter(q_Q_Titletext, q_Qtimetext, q_Qnametext, q_Qcontenttext, a_A_Titletext, a_Atimetext, a_Acontenttext);
            RecyclerView mList = (RecyclerView) findViewById(R.id.QAlistView);

            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(QAAdapter);


        //listview end
    }
/*    private void setFontSize() {
        TextView textViewTemp;
        //Q
        textViewTemp = (TextView)findViewById(R.id.q_Q_Title);
        textViewTemp.setTextSize(size+9);
        //Q者
        textViewTemp = (TextView)findViewById(R.id.q_Qname);
        textViewTemp.setTextSize(size);
        //Q時
        textViewTemp = (TextView)findViewById(R.id.q_Qtime);
        textViewTemp.setTextSize(size);
        //Q內容
        textViewTemp = (TextView)findViewById(R.id.q_Qcontent);
        textViewTemp.setTextSize(size+5);
        //A
        textViewTemp = (TextView)findViewById(R.id.a_A_Title);
        textViewTemp.setTextSize(size+9);
        //A時
        textViewTemp = (TextView)findViewById(R.id.a_Atime);
        textViewTemp.setTextSize(size);
        //A內容
        textViewTemp = (TextView)findViewById(R.id.a_Acontent);
        textViewTemp.setTextSize(size+5);
    }*/
}
