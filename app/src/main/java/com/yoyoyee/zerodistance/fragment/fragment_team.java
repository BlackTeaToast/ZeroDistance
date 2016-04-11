package com.yoyoyee.zerodistance.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.GroupActivity;
import com.yoyoyee.zerodistance.activity.NewGroupActivity;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.CardViewAdapter;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_team extends Fragment implements View.OnTouchListener{
   //
   int[] id;
    String[] title;
    Date[] expAt;
    int[] needNum;
    int[] currentNum;
    boolean[] missiondangerous;
    boolean becontext;
    String[] detial;
    ArrayList<Group> missions;
    int missionnumber[];
    private ProgressDialog pDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;//RecyclerView外圍框
    CardViewAdapter CardViewAdapter;
    LinearLayoutManager layoutManager;
    //


    RecyclerView mList;
    FloatingActionButton fab;


    public fragment_team(){
        updataphoneDB();
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team, container, false);
//        makecard();


        try {

            //CardViewAdapter CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber, R.layout.fragment_fragment_team);
            mList = (RecyclerView) v.findViewById(R.id.listView);
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(CardViewAdapter);
            mList.setOnTouchListener(this);//監聽動作
        } catch (Exception e) {
            e.printStackTrace();
        }



        //漂浮
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //                        .setAction("Action", null).show();
            Intent in = new Intent(getActivity(), NewGroupActivity.class);
            in.putExtra("id", SessionFunctions.getUserUid());
            Toast.makeText(getContext(), "UserUid: "+SessionFunctions.getUserUid(), Toast.LENGTH_SHORT).show();
            startActivity(in);
        }
    });

        //漂浮

        // 判斷目前card位置
        mList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4個item自動載入，各位自由選擇
                // dy>0 表示向下滑動
                if (lastVisibleItem >= totalItemCount && dy < 0) {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
        // 判斷目前card位置

        // 頂端向下滑更新
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    CardViewAdapter.setItemCount(0);
                    mList.scrollToPosition(0);
                    CardViewAdapter.notifyDataSetChanged();
                    updataphoneDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 頂端向下滑更新

        return v;
    }

    public void makecard() {
        missions = QueryFunctions.getGroups();
        id = new int[missions.size()];//任務id
        title = new String[missions.size()];//任務標題
        detial = new String[missions.size()];//任務內容or獎勵
        expAt = new Date[missions.size()];//任務結束時間
        needNum = new int[missions.size()]; //需要人數
        currentNum = new int[missions.size()];//已有人數
        missiondangerous = new boolean[missions.size()];//任務是否緊急
        becontext = SessionFunctions.getbecontext();//true 為內容 false為獎勵
        for (int i = 0; i < missions.size(); i++) {

            id[i] = missions.get(missions.size() - i - 1).id;
            title[i] = missions.get(missions.size() - i - 1).title;
            detial[i] = missions.get(missions.size() - i - 1).content;
            expAt[i] = missions.get(missions.size() - i - 1).expAt;
            needNum[i] = missions.get(missions.size() - i - 1).needNum;
            currentNum[i] = missions.get(missions.size() - i - 1).currentNum;
            missiondangerous[i] = false;
            title[i] = limitString(title[i], 0);//0為title , 1為detial
            detial[i] = limitString(detial[i], 1);
        }

        missionnumber = new int[missions.size()];
        for (int i = 0; i < missions.size(); i++) {
            missionnumber[i] = i;
        }
        Resources res =this.getResources();
        CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber,R.layout.fragment_fragment_team/*,res*/);
    }
    private String limitString(String context, int type){//0為title , 1為detial
        switch ((int)SessionFunctions.getUserTextSize()) {
            case 20:{
                if(type==0){
                    if(context.length()>4){//限制字數
                        context = (String)context.subSequence(0, 4)+"...";
                        //  Toast.makeText(getContext(), ""+context, Toast.LENGTH_SHORT).show();
                    }
                }
                if(type==1){
                    if(context.length()>14){//限制字數
                        context = (String)context.subSequence(0, 14)+"...";
                    }
                }
                break;}
            case 15:{
                if(type==0){
                    if(context.length()>6){//限制字數
                        context = (String)context.subSequence(0, 6)+"...";
                    }
                }
                if(type==1){
                    if(context.length()>20){//限制字數
                        context = (String)context.subSequence(0, 20)+"...";
                    }
                }

                break;}
            case 10:{
                if(type==0){
                    if(context.length()>11){//限制字數
                        context = (String)context.subSequence(0, 11)+"...";
                    }
                }
                if(type==1){
                    if(context.length()>22){//限制字數
                        context = (String)context.subSequence(0, 22)+"...";
                    }
                }
                break;}
        }
        return context;
    }
    public boolean onTouch(View v, MotionEvent event) {

        try {
            switch( event.getAction() ) {

                case MotionEvent.ACTION_DOWN:  // 按下
                {
                    // 設定 TextView 內容, 大小, 位置
                    break;}

                case MotionEvent.ACTION_MOVE:  // 拖曳移動
                {
    //                Toast.makeText(getContext(), "拖曳移動", Toast.LENGTH_SHORT).show();
                    fab.setVisibility(View.INVISIBLE);
                    fabtime();
                    // 設定 TextView 內容, 大小, 位置
                    break;}

                case MotionEvent.ACTION_UP:  // 放開
                {
                    // 設定 TextView 內容
                    break;}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void fabtime(){
        new CountDownTimer(1000,500){

            @Override
            public void onFinish() {
                fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }

        }.start();
    }
    private void updataphoneDB(){//更新手機資料

        ClientFunctions.updateGroups(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                SQLiteHandler db = AppController.getDB();
                String TAG = AppController.class.getSimpleName();
                ArrayList<Group> Group = db.getGroups();
                if (Group.size() > 0) {
                    Log.d(TAG, "onResponse: " + Group.get(0).getTitle() + " " + Group.get(0).createdAt + " " + Group.get(0).finishedAt);
                }
                totalvuledel();
                mSwipeRefreshLayout.setRefreshing(false);
                makecard();
                mList.setAdapter(CardViewAdapter);
            }

            @Override
            public void onErrorResponse(String response) {

                Toast.makeText(getContext(), "更新失敗", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void totalvuledel(){
        id = null;
        title = null;
        detial = null;
        expAt = null;
        needNum = null;
        currentNum = null;
        missiondangerous = null;
    }

}
