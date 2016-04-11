package com.yoyoyee.zerodistance.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MainActivity;
import com.yoyoyee.zerodistance.activity.MissionActivity;
import com.yoyoyee.zerodistance.activity.NewMissionActivity;
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.RecyclerItemClickListener;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.CardViewAdapter;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_mission extends Fragment implements View.OnTouchListener {

    //

    int[] id;
    String[] title;
    Date[] expAt;
    int[] needNum;
    int[] currentNum;
    boolean[] missiondangerous;
    boolean becontext;
    ArrayList<Mission>  missionsUrgent;
    ArrayList<Mission>  missionsNotUrgent;
    ArrayList<Mission> missions;
    int missionnumber[];
    String[] detial;
    private ProgressDialog pDialog;
    //
    RecyclerView mList;
    FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeRefreshLayout;//RecyclerView外圍框
    CardViewAdapter CardViewAdapter;

    LinearLayoutManager layoutManager;//CARD layout
    GridLayout card_view;
    public fragment_mission(){
        updataphoneDB();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_mission, container, false);

        SessionFunctions SF = new SessionFunctions();

        pDialog = new ProgressDialog(v.getContext());
        pDialog.setCancelable(false);
        pDialog.setMessage("載入中 ...");
        showDialog();

        //makecard();

        try {
           // CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber,R.layout.fragment_fragment_mission);
            mList = (RecyclerView) v.findViewById(R.id.listView);
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(CardViewAdapter);  //設定內容
            mList.setOnTouchListener(this);//監聽動作
        } catch (Exception e) {
            e.printStackTrace();
        }

        //漂浮
         fab=  (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                        .setAction("Action", null).show();
                Intent in = new Intent(getActivity(), NewMissionActivity.class);
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
                CardViewAdapter.setItemCount(0);
                mList.scrollToPosition(0);
                CardViewAdapter.notifyDataSetChanged();
                updataphoneDB();
            }
        });
        // 頂端向下滑更新

        //偵測滑動
//        mList.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {//注意第二參數，元事件有三 action_down,action_move,action_up
//                int actionType = event.getAction();//取得action
//                if(actionType == MotionEvent.ACTION_DOWN){
////                    Toast.makeText(getContext(), "點", Toast.LENGTH_SHORT).show();
//                }else if(actionType == MotionEvent.ACTION_MOVE){
////                    Toast.makeText(getContext(), "拖曳移動", Toast.LENGTH_SHORT).show();
//                    fabtime();
//                }else if(actionType == MotionEvent.ACTION_UP){
////                    Toast.makeText(getContext(), "抬起", Toast.LENGTH_SHORT).show();
//                }
//
//                return true;
//            }//public boolean onTouch(View v,MotionEvent event){
//        });
        //偵測滑動

        return v;
    }
    //設置字體大小
    private void setFontSize(){
        TextView textViewTemp;
        SessionFunctions SF= new SessionFunctions();

    }
    private void updataphoneDB(){//更新手機資料

        ClientFunctions.updateMissions(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                SQLiteHandler db = AppController.getDB();
                String TAG = AppController.class.getSimpleName();
                ArrayList<Mission> missions = db.getMissions();
                if (missions.size() > 0) {
                    Log.d(TAG, "onResponse: " + missions.get(0).getTitle() + " " + missions.get(0).createdAt + " " + missions.get(0).finishedAt);
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
                mList.setAdapter(CardViewAdapter);
                hideDialog();
            }

            @Override
            public void onErrorResponse(String response) {

                Toast.makeText(getContext(), "更新失敗", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {

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
    public void totalvuledel(){
        id = null;
        title = null;
        detial = null;
        expAt = null;
        needNum = null;
        currentNum = null;
        missiondangerous = null;
    }
    public void makecard(){
        missionsUrgent = QueryFunctions.getUnfinishedMissions();
        missionsNotUrgent = QueryFunctions.getUnfinishedMissions();
        missions  = QueryFunctions.getMissions();
     id = new int[missions.size()];//任務id
     title = new String[missions.size()];//任務標題
     detial = new String[missions.size()];//任務內容or獎勵
     expAt = new Date[missions.size()];//任務結束時間
     needNum = new int[missions.size()]; //需要人數
     currentNum = new int[missions.size()];//已有人數
     missiondangerous = new boolean[missions.size()];//任務是否緊急
     becontext = SessionFunctions.getbecontext();//true 為內容 false為獎勵
     for(int i = 0;i <missions.size();i++){

             id[i]= missions.get(missions.size()-i-1).id;
             title[i]= missions.get(missions.size()-i-1).title;
             if(becontext){
                 detial[i] = missions.get(missions.size()-i-1).content;
             }else{
                 detial[i] = missions.get(missions.size()-i-1).reward;
             }
             expAt[i] = missions.get(missions.size()-i-1).expAt;
             needNum[i] = missions.get(missions.size()-i-1).needNum;
             currentNum[i] = missions.get(missions.size()-i-1).currentNum;
             missiondangerous[i] = missions.get(missions.size()-i-1).isUrgent;
             title[i]=limitString(title[i], 0);//0為title , 1為detial
             detial[i]=limitString(detial[i], 1);
     }
        isUrgentCount();
     missionnumber=new int[missions.size()];
     for(int i=0;i<missions.size();i++){
         missionnumber[i]=i;
     }
        try {
//            Toast.makeText(getContext(), "現在有"+missions.size()+"個", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources res =this.getResources();
        CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber,R.layout.fragment_fragment_mission/*,res*/);
 }
    public int isUrgentCount(){
        int Count=0;
        for(int i = 0;i <missions.size();i++){
            if(missions.get(i).isUrgent==true){
                Count+=1;
            }
        }
        Toast.makeText(getContext(), "全部有"+missions.size()+"個任務"+"現在有"+Count+"個緊急任務", Toast.LENGTH_SHORT).show();
        return Count;
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
