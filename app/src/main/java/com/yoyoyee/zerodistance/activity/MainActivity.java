package com.yoyoyee.zerodistance.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.app.TapService;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.fragment.fragment_mission;
import com.yoyoyee.zerodistance.helper.CustomToast;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.SessionManager;
import com.yoyoyee.zerodistance.helper.SlidingTabLayout;
import com.yoyoyee.zerodistance.helper.ViewPagerAdapter;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;
    public static Context context;
    ViewPagerAdapter adapter;
    Toolbar tool_bar;
    ViewPager pager;
    SlidingTabLayout tabs;
    private ProgressDialog pDialog;
    public static boolean PD=false;
    private SQLiteHandler db;
    String Titles[];
    int upDataCount=0;
    boolean beupData= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startService(new Intent(this, TapService.class));
        Titles = getResources().getStringArray(R.array.tabstyle);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage("載入中 ...");
        context = this;
        setFontSize();//字體
        updataphoneDB();//手機資料

//設置toolbar標題


//        LayoutInflater layout=this.getLayoutInflater();
//        View view=layout.inflate(R.layout.maintool_bar, null);
        tool_bar = (Toolbar)findViewById(R.id.tool_bar);
        TextView UserNames = (TextView)tool_bar.findViewById(R.id.UserName);
        TextView UserSchool = (TextView)tool_bar.findViewById(R.id.UserSchool);
        UserNames.setText(SessionFunctions.getUserNickName()+"");
        UserSchool.setText(SessionFunctions.getUserschoolName()+"");
        setSupportActionBar(tool_bar);
        ActionBar actionBar = getSupportActionBar();

        //actionBar.setTitle(SessionFunctions.getUserNickName()+"     我塞塞塞塞塞塞塞塞塞");
//設置toolbar標題

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.container);


        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width


        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.ColorPrimaryToolbar);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
    }


    public void onResume(){
        super.onResume();
        //更新手機資料庫
        //更新資料
//        showDialog();
//        if(!beupData){
//            updataMissionDB();
//
//        }
        beupData=false;

    }

    /**
     * A placeholder fragment containing a simple view.
     */


    //設置字體大小
    private void setFontSize(){
        TextView textViewTemp;
        tool_bar = (Toolbar)findViewById(R.id.tool_bar);

        textViewTemp = (TextView)tool_bar.findViewById(R.id.UserName);
        textViewTemp.setTextSize(12+(SessionFunctions.getUserTextSize())/3);
        textViewTemp = (TextView)tool_bar.findViewById(R.id.UserSchool);
        textViewTemp.setTextSize(12+(SessionFunctions.getUserTextSize())/3);

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
    private void delphoneDB(){
        QueryFunctions.deleteAllMissions();
        QueryFunctions.deleteAllGroups();
    }

    private void updataphoneDB(){//更新手機資料
        delphoneDB();//資料都先削掉
        showDialog();
        //fabtime();
        updataSchoolDB();//成功會更新Mission
        updataMissionDB();
        beupData=true;
    }
    private void updataSchoolDB(){
        ClientFunctions.updateSchools(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                upDataCount=0;
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=5){
                    CustomToast.showToast(context, "更新失敗學校(主)", 500);
                    hideDialog();
                }else{
                    upDataCount+=1;
                    updataSchoolDB();
                }
            }
        });
    }
    private void updataMissionDB(){  //成功會更新Group
        ClientFunctions.updateMissions(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                upDataCount=0;
                updataGroupDB();//更新揪團
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=5){
                CustomToast.showToast(context, "更新失敗任務(主)", 500);
                    hideDialog();
            }else{
                upDataCount+=1;
                    updataMissionDB();
            }
            }
        });
    }
    private void updataGroupDB(){
        ClientFunctions.updateGroups(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                MakeTabAndContext();
            }

            @Override
            public void onErrorResponse(String response) {
                //  Toast.makeText(context, "更新失敗", Toast.LENGTH_SHORT).show();
                if(upDataCount>=5){
                    CustomToast.showToast(context, "更新失敗揪團(主)", 500);
                    hideDialog();
                }else{
                    upDataCount+=1;
                    updataGroupDB();
                }
            }
        });
    }

    private void MakeTabAndContext(){
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Titles.length);
        pager.setOffscreenPageLimit(6);//儲存頁面數

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        pager.setCurrentItem(2);  //預設出現頁面
        hideDialog();
    }


}
