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
import java.util.Arrays;
import java.util.Date;


/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_team extends Fragment implements View.OnTouchListener{
   //
    ArrayList<Group> Group;
    private ProgressDialog pDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;//RecyclerView外圍框
    CardViewAdapter CardViewAdapter;
    LinearLayoutManager layoutManager;
    //
    int upDataCount=0;
    boolean isfirst=true;

    RecyclerView mList;
    FloatingActionButton fab;


    public fragment_team(){
//        updataphoneDB();
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team, container, false);
        makecard();
        try {

            //CardViewAdapter CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber, R.layout.fragment_fragment_team);
            mList = (RecyclerView) v.findViewById(R.id.listView);
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
//            mList.setAdapter(CardViewAdapter);
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
                    CardViewAdapter.setItemCount(0);
                    mList.scrollToPosition(0);
                    CardViewAdapter.notifyDataSetChanged();
                    updataphoneDB();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        // 頂端向下滑更新

        return v;
    }
    public void  onResume(){
        super.onResume();
        //  Toast.makeText(getContext(), "onResume{mission}", Toast.LENGTH_SHORT).show();
        CardViewAdapter.setItemCount(0);
        mList.scrollToPosition(0);
        makecard();
        mList.setAdapter(CardViewAdapter);
    }

    public void makecard() {
        Group  = QueryFunctions.getGroups();
        Group[] group = new Group[Group.size()];
        for(int i = 0;i <Group.size();i++){
                group[i] = new Group(Group.get(Group.size()-i-1).id, Group.get(Group.size()-i-1).title
                        , Group.get(Group.size()-i-1).content, Group.get(Group.size()-i-1).expAt
                        , Group.get(Group.size()-i-1).needNum, Group.get(Group.size()-i-1).currentNum
                        , Group.get(Group.size()-i-1).getUserName());

        }
        Arrays.sort(group);//時間排序
        CardViewAdapter = new CardViewAdapter(group,R.layout.fragment_fragment_team/*,res*/);
        if(!isfirst) {
            mList.setAdapter(CardViewAdapter);
        }
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
                    fab.setVisibility(View.VISIBLE);
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

    //更新
    private void updataphoneDB(){//更新手機資料
        updataMissionDB();
    }
    private void updataMissionDB(){  //成功會更新Group
        ClientFunctions.updateMissions(new ClientResponse() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(getContext(), "更新成功(任務)", Toast.LENGTH_SHORT).show();
                upDataCount=0;
                updataGroupDB();//更新揪團
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=5){
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
                //Toast.makeText(getContext(), "更新成功(揪團)", Toast.LENGTH_SHORT).show();
                makecard();
                mList.setAdapter(CardViewAdapter);
                upDataCount=0;
            }

            @Override
            public void onErrorResponse(String response) {
                if(upDataCount>=5){
                //    Toast.makeText(getContext(), "更新失敗(揪團)", Toast.LENGTH_SHORT).show();
                }else{
                    upDataCount+=1;
                    updataGroupDB();
                }
            }
        });
    }
    //更新


}
