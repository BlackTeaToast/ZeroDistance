package com.yoyoyee.zerodistance.activity;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.yoyoyee.zerodistance.R;
        import com.yoyoyee.zerodistance.app.QAndA;
        import com.yoyoyee.zerodistance.client.ClientFunctions;
        import com.yoyoyee.zerodistance.client.ClientResponse;
        import com.yoyoyee.zerodistance.helper.QAAdapter;
        import com.yoyoyee.zerodistance.helper.QueryFunctions;
        import com.yoyoyee.zerodistance.helper.SessionFunctions;
        import com.yoyoyee.zerodistance.helper.datatype.QA;
        import com.yoyoyee.zerodistance.helper.table.QATable;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;

public class QAActivity extends AppCompatActivity {
    private float size;//定義所有文字大小
    private Button GO;
   // QAndA Q =new QAndA();
    private Boolean isQ=true;
    private Toolbar toolbar;
    private int userID ;
    private Button A;
    private int group_or_mission_ID;
    private SQLiteDatabase db ;
    private ArrayList<QA> DataQas;


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
        GO = (Button)findViewById(R.id.for_Q_Button);
        loadQandAData(true, 1);

        //按發問紐換頁功能

        if(equals(userID))
        {
         GO.setVisibility(Button.GONE);//發文者沒有發問功能
        }
        else {
            GO.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QAActivity.this, AskActivity.class);
                    intent.putExtra("isGroup",true);
                    intent.putExtra("groupID",1);
                    startActivity(intent);
                }
            });
        }
        //回答按鍵
        A = (Button)findViewById(R.id.for_A_Buttom);

        //listview start
     //   String[] q_Q_Titletext = {"Q", "Q", "Q", "Q","Q", "Q"},a_A_Titletext = {"A", "A", "A", "A", "A", "A"};



        //listview end
    }

    private void loadQandAData(boolean isGroup,int ID){
            if (isGroup) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.show();
                ClientFunctions.updateGroupQA(ID, new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        TextView print = (TextView) findViewById(R.id.textView3);
                        print.setText(response);
                        DataQas = QueryFunctions.getQAs();
                        printListView();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        TextView print = (TextView) findViewById(R.id.textView3);
                        print.setText(response);
                    }
                });
            } else {
                ClientFunctions.updateMissionQA(ID, new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        DataQas = QueryFunctions.getQAs();
                        printListView();
                    }

                    @Override
                    public void onErrorResponse(String response) {

                    }
                });
            }
        /**
         * 抓資料進入手機的QAtable裡面，並且使用動態新增出來↓↓↓↓↓↓↓↓
         */

    }
    protected void printListView () {
        int list =DataQas.size();
        GO.setText(String.valueOf(list));
        QA[] q =new QA[list];
        String[] q_Qtimetext =new String[list]; //= {"1/11 1:11", "12/11 11:11", "xx", "48/43", "154/45", "12/12"}, q_Qnametext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] q_Qnametext =new String[list];
        String[] q_Qcontenttext =new String[list];//= {"你說把愛漸漸放下會走更遠,或許命運的謙讓我遇見", "你好阿", "xx", "你好阿", "xx", "你好阿"}, a_Atimetext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] a_Atimetext=new String[list];
        String[] a_Acontenttext=new String[list]; //= {"滾", "你有病嘛", "閃邊", "87", "P0", "好啦好啦"}
        SimpleDateFormat format =new SimpleDateFormat("MM/dd HH:mm");
        for(int z=0;z<list;z++) {
            q[z]=DataQas.get(z);
            q_Qtimetext[z] =format.format(q[z].createdAt);
            q_Qnametext[z] =q[z].userName;
            q_Qcontenttext[z] = q[z].question;
            a_Acontenttext [z]=q[z].answer;
        }
        QAAdapter QAAdapter = new QAAdapter(/*q_Q_Titletext,*/ q_Qtimetext, q_Qnametext, q_Qcontenttext, /*a_A_Titletext,*/ a_Atimetext, a_Acontenttext);
        RecyclerView mList = (RecyclerView) findViewById(R.id.QAlistView);

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(QAAdapter);
    }



}
