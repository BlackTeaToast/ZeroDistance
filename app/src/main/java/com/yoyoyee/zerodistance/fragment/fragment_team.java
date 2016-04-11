package com.yoyoyee.zerodistance.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.GroupActivity;
import com.yoyoyee.zerodistance.activity.NewGroupActivity;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.CardViewAdapter;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_team extends Fragment{
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
    //


    RecyclerView mList;
    FloatingActionButton fab;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team, container, false);

        makecard();

        ArrayList<Group> Group = QueryFunctions.getGroups();

        try {

            CardViewAdapter CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber, R.layout.fragment_fragment_team);
            mList = (RecyclerView) v.findViewById(R.id.listView);
            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(CardViewAdapter);
           // mList.setOnTouchListener(this);//監聽動作
        } catch (Exception e) {
            e.printStackTrace();
        }



        //漂浮
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
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
}
