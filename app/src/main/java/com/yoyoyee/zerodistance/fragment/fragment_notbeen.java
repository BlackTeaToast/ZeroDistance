package com.yoyoyee.zerodistance.fragment;

import android.content.res.Resources;
import android.os.Bundle;
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
import com.yoyoyee.zerodistance.app.AppController;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.CardViewAdapter;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Group;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_notbeen extends Fragment{
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
//
    CardViewAdapter CardViewAdapter;
    int upDataCount=0;
    private SwipeRefreshLayout mSwipeRefreshLayout;//RecyclerView外圍框
    RecyclerView mList;
    LinearLayoutManager layoutManager;
    public fragment_notbeen(){
        updataphoneDB();
    }// 讓一開始有資料
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notbeen, container, false);

        try {

            //CardViewAdapter CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber, R.layout.fragment_fragment_team);
            mList = (RecyclerView) v.findViewById(R.id.listView);
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(CardViewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        return v;}


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
        }

        missionnumber = new int[missions.size()];
        for (int i = 0; i < missions.size(); i++) {
            missionnumber[i] = i;
        }
        Resources res =this.getResources();
        CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber,R.layout.fragment_fragment_notbeen/*,res*/);
    }
    private void updataphoneDB(){//更新手機資料
        totalvuledel();
        updataMissionDB();
    }
    private void updataMissionDB(){  //成功會更新Group
        ClientFunctions.updateGroups(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                SQLiteHandler db = AppController.getDB();
                String TAG = AppController.class.getSimpleName();
                ArrayList<Group> Group = db.getGroups();
                if (Group.size() > 0) {
                    Log.d(TAG, "onResponse: " + Group.get(0).getTitle() + " " + Group.get(0).createdAt + " " + Group.get(0).finishedAt);
                }
               // Toast.makeText(getContext(), "更新成功(任務)", Toast.LENGTH_SHORT).show();
                upDataCount=0;
                updataGroupDB();//更新揪團
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=10){
                    Toast.makeText(getContext(), "更新失敗(任務)", Toast.LENGTH_SHORT).show();
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
                SQLiteHandler db = AppController.getDB();
                String TAG = AppController.class.getSimpleName();
                ArrayList<Group> Group = db.getGroups();
                if (Group.size() > 0) {
                    Log.d(TAG, "onResponse: " + Group.get(0).getTitle() + " " + Group.get(0).createdAt + " " + Group.get(0).finishedAt);
                }
                makecard();
               // Toast.makeText(getContext(), "更新成功(揪團)", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
                mList.setAdapter(CardViewAdapter);
                upDataCount=0;
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=10){
                    Toast.makeText(getContext(), "更新失敗(揪團)", Toast.LENGTH_SHORT).show();
                }else{
                    upDataCount+=1;
                    updataGroupDB();
                }
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
