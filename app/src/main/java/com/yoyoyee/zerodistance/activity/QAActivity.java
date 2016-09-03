package com.yoyoyee.zerodistance.activity;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Typeface;
        import android.support.v4.widget.SwipeRefreshLayout;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.yoyoyee.zerodistance.R;
        import com.yoyoyee.zerodistance.app.FontManager;
        import com.yoyoyee.zerodistance.app.QAndA;
        import com.yoyoyee.zerodistance.client.ClientFunctions;
        import com.yoyoyee.zerodistance.client.ClientResponse;
        import com.yoyoyee.zerodistance.helper.CardViewAdapter;
        import com.yoyoyee.zerodistance.helper.QAAdapter;
        import com.yoyoyee.zerodistance.helper.QueryFunctions;
        import com.yoyoyee.zerodistance.helper.SessionFunctions;
        import com.yoyoyee.zerodistance.helper.datatype.QA;
        import com.yoyoyee.zerodistance.helper.table.QATable;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;

        import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class QAActivity extends AppCompatActivity {
    private float size;//定義所有文字大小
    private Boolean isGroup;
    private Toolbar toolbar;
    private String userID =QueryFunctions.getUserUid() ;
    private int group_or_mission_ID;
    private String publisher;
    private Intent intentData;
    private ArrayList<QA> DataQas;
    private Toolbar.OnMenuItemClickListener onMenuItemClick;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    protected Boolean isreload=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        //toolbar
        intentData =getIntent();
        isGroup=intentData.getBooleanExtra("isGroup", true);
        group_or_mission_ID=intentData.getIntExtra("id", 1);
        publisher =intentData.getStringExtra("publisher");
        toolbar= (Toolbar) findViewById(R.id.qAndA_Toolbar);

        onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_QAtoolbar:
                        Intent intent = new Intent(QAActivity.this, AskActivity.class);
                        intent.putExtra("isGroup", isGroup);
                        intent.putExtra("group_missionID", group_or_mission_ID);
                        intent.putExtra("isAsk", true);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        };
        //下拉更新
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isreload=true;
                loadQandAData(isGroup, group_or_mission_ID);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.qAndA_Title));

        //字體大小
        size = SessionFunctions.getUserTextSize();
        allTextSizeSet(size);
        //設定字型大小

        //按發問紐換頁功能


    }
    private void loadQandAData(boolean isGroup,int ID){
            if (isGroup) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                if (!isreload){
                    progressDialog.show();
                    isreload=false;
                }
                ClientFunctions.updateGroupQA(ID, new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        DataQas = QueryFunctions.getQAs();
                        printListView();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(QAActivity.this, "讀取失敗，請確認網路連線", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if(!isGroup)  {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                if (!isreload){
                    progressDialog.show();
                    isreload=false;
                }
                ClientFunctions.updateMissionQA(ID, new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        DataQas = QueryFunctions.getQAs();
                        printListView();
                    }

                    @Override
                    public void onErrorResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(QAActivity.this, "讀取失敗，請確認網路連線", Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }
        /**
         * 抓資料進入手機的QAtable裡面，並且使用動態新增出來↓↓↓↓↓↓↓↓
         */
    protected void printListView () {
        //
      /* TextView qT,aT,qC,aC;
        qT= (TextView) findViewById(R.id.a_A_Title);
        aT= (TextView) findViewById(R.id.q_Q_Title);
        aC = (TextView) findViewById(R.id.a_Acontent);
        Typeface face =Typeface.createFromAsset(this.getAssets(),"setofont.ttf");
        qT.setTypeface(face);
        aC.setTypeface(face);
        aT.setTypeface(face);*/

        int list =DataQas.size();
        Toast.makeText(this, "讀取完成，總共"+String.valueOf(list)+"則", Toast.LENGTH_SHORT).show();
        QA[] q =new QA[list];
        int[] q_a_ID =new int[list];
        String[] q_Qtimetext =new String[list]; //= {"1/11 1:11", "12/11 11:11", "xx", "48/43", "154/45", "12/12"}, q_Qnametext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] q_Qnametext =new String[list];
        String[] q_Qcontenttext =new String[list];//= {"你說把愛漸漸放下會走更遠,或許命運的謙讓我遇見", "你好阿", "xx", "你好阿", "xx", "你好阿"}, a_Atimetext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] a_Atimetext=new String[list];
        String[] a_Acontenttext=new String[list]; //= {"滾", "你有病嘛", "閃邊", "87", "P0", "好啦好啦"}
        String[] userUID=new String[list];
        SimpleDateFormat format =new SimpleDateFormat("MM/dd HH:mm");
        for(int z=0;z<list;z++) {
            q[z]=DataQas.get(z);
            q_a_ID[z] =q[z].id;
            q_Qtimetext[z] =format.format(q[z].createdAt);
            q_Qnametext[z] =q[z].userName;
            q_Qcontenttext[z] = q[z].question;
            a_Acontenttext [z]=q[z].answer;
            userUID[z] =q[z].userUid;
        }
        QAAdapter QAAdapter = new QAAdapter(q_a_ID,size, userID.equals(publisher),isGroup,q_Qtimetext, q_Qnametext, q_Qcontenttext, a_Atimetext, a_Acontenttext,userUID);
        RecyclerView mList = (RecyclerView) findViewById(R.id.QAlistView);


        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(QAAdapter);
    }
    protected void allTextSizeSet(float size){
    }
    //圖案發文
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (userID.equals(publisher)){
            return false;
        }
        else{
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadQandAData(isGroup, group_or_mission_ID);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
