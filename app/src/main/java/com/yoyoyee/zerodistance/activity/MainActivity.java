package com.yoyoyee.zerodistance.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.CustomToast;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.SessionManager;
import com.yoyoyee.zerodistance.helper.SlidingTabLayout;
import com.yoyoyee.zerodistance.helper.ViewPagerAdapter;
import com.yoyoyee.zerodistance.menuDialog.Dialog_aboutus;
import com.yoyoyee.zerodistance.menuDialog.Dialog_achievement;
import com.yoyoyee.zerodistance.menuDialog.Dialog_friend;
import com.yoyoyee.zerodistance.menuDialog.Dialog_myself;
import com.yoyoyee.zerodistance.menuDialog.Dialog_setting;

public class MainActivity extends AppCompatActivity{

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
    private SQLiteHandler db;
    String Titles[];
    int upDataCount=0;
    FloatingActionButton fab;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startService(new Intent(this, TapService.class));
        Titles = getResources().getStringArray(R.array.tabstyle); //設定tab

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
        UserSchool.setText(SessionFunctions.getUserschoolName()+"　　");
        setSupportActionBar(tool_bar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setDrawerLayout();
        setnav();//navigation的menu點選

        //actionBar.setTitle(SessionFunctions.getUserNickName()+"     我塞塞塞塞塞塞塞塞塞");
//設置toolbar標題

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.container);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch( event.getAction() ) {

                    case MotionEvent.ACTION_DOWN:  // 按下
                    {

                        break;
                    }

                    case MotionEvent.ACTION_MOVE:  // 拖曳移動
                    {
                        break;}

                    case MotionEvent.ACTION_UP:  // 放開
                    {
                        fabtime();
                        break;}
                }
                return false;
            }
        });

        setfab();//fab設定

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

    }

    /**
     * A placeholder fragment containing a simple view.
     */


    //設置字體大小
    private void setFontSize(){
        TextView textViewTemp;
        tool_bar = (Toolbar)findViewById(R.id.tool_bar);

        textViewTemp = (TextView)tool_bar.findViewById(R.id.UserName);
        textViewTemp.setTextSize(14+(SessionFunctions.getUserTextSize())/3);
        textViewTemp = (TextView)tool_bar.findViewById(R.id.UserSchool);
        textViewTemp.setTextSize(14+(SessionFunctions.getUserTextSize())/3);

    }
    private void showDialog() {
        if (!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
    private void delphoneDB(){
        QueryFunctions.deleteAllMissions();
        QueryFunctions.deleteAllGroups();
    }

    private void updataphoneDB(){//更新手機資料
        showDialog();
        delphoneDB();//資料都先削掉
        updataSchoolDB();
        updataMissionDB();
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
                    MakeTabAndContext();
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
                    MakeTabAndContext();
                }else{
                    upDataCount+=1;
                    updataGroupDB();
                }
            }
        });
    }
    public void fabtime(){
        new CountDownTimer(100,100){

            @Override
            public void onFinish() {
                switch (pager.getCurrentItem()){
                    case 0:
                        if (SessionFunctions.isTeacher()) {
                            fab.show();
                        }else if(!SessionFunctions.isTeacher()){
                            fab.hide();
                        }
                        break;
                    case 1:
                        fab.show();
                        break;
                    default:
                        fab.hide();
                        break;
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }

        }.start();
    }
    private void setfab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (SessionFunctions.isTeacher()&&pager.getCurrentItem()==0||pager.getCurrentItem()==1) {
            fab.show();
        }else {
            fab.hide();
        }
         fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (pager.getCurrentItem()) {
                        case 0: {
                                Intent in = new Intent(context, NewMissionActivity.class);
                                in.putExtra("id", SessionFunctions.getUserUid());
                                startActivity(in);
                                break;
                                 }
                        case 1:{
                            Intent in = new Intent(context, NewGroupActivity.class);
                            in.putExtra("id", SessionFunctions.getUserUid());
                            startActivity(in);
                        }
                                                    }
                                        }
                                        });

        }

    private void MakeTabAndContext(){
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Titles.length);
        pager.setOffscreenPageLimit(4);//儲存頁面數

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
     //   pager.setCurrentItem(2);  //預設出現頁面
        hideDialog();
    }
    private void setDrawerLayout(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, tool_bar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setScrimColor(Color.parseColor("#3c448AFF"));
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
 private void setnav(){
     NavigationView navigation = (NavigationView) findViewById(R.id.navigation_view);
    // navigation.inflateHeaderView(R.layout.navigation_header);
     navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(MenuItem menuItem) {
             int id = menuItem.getItemId();
             switch (id) {
                 case R.id.myselfpage:
                     makeMyfselfDialog();
                     break;
                 case R.id.friendList:
                     makeFriendDialog();
                     break;
                 case R.id.myAchievement:
                     makeAchievementDialog();
                     break;
                 case R.id.setting:
                     makeSettingDialog();
                     break;
                 case R.id.about_us:
                     makeAboutusDialog();
                     break;
                 case R.id.sign_out:
                     logoutUser();
                     break;
                 case R.id.exit:
                     finish();
                     break;

             }
             return false;
         }
     });
 }
    private void makeMyfselfDialog(){
        Dialog_myself dialog = new Dialog_myself(context);
            dialog.show();
    }
    private void makeFriendDialog(){
        Dialog_friend dialog = new Dialog_friend(context);
//        dialog.setContentView(R.layout.dialog_friend_list);
        dialog.show();
    }
    private void makeSettingDialog(){
        Dialog_setting dialog = new Dialog_setting(context);
        dialog.show();
    }
    private void makeAchievementDialog(){
        Dialog_achievement dialog = new Dialog_achievement(context);
        dialog.show();
    }
    private void logoutUser() {
        SessionManager session = new SessionManager(this);
        session.setLogin(false);
        db = new SQLiteHandler(this);
        db.deleteUsers();
        Intent in = new Intent(this, LoginActivity.class);
        startActivity(in);
        finish();
    }

    private void makeAboutusDialog(){
        Dialog_aboutus dialog = new Dialog_aboutus(context);
        dialog.setContentView(R.layout.dialog_aboutus);
        dialog.show();
    }
}
