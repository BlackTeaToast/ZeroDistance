package com.yoyoyee.zerodistance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.CardViewAdapter;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_mission extends Fragment  {

    //

    ArrayList<Mission> missions;
    Mission[] mission;
    //
    RecyclerView mList;
//    FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeRefreshLayout;//RecyclerView外圍框
    CardViewAdapter CardViewAdapter;

    int upDataCount;
    LinearLayoutManager layoutManager;//CARD layout
    boolean isfirst=true;
    public fragment_mission(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_mission, container, false);


        try {
            mList = (RecyclerView) v.findViewById(R.id.listView);
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //漂浮

//         fab=  (FloatingActionButton) v.findViewById(R.id.fab);
//        if(SessionFunctions.isTeacher()) {
//            fab.setVisibility(View.VISIBLE);
//        }else{
//            fab.setVisibility(View.GONE);
//        }
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                //                        .setAction("Action", null).show();
//                Intent in = new Intent(getActivity(), NewMissionActivity.class);
//                in.putExtra("id", SessionFunctions.getUserUid());
//             //   Toast.makeText(getContext(), "UserUid: "+SessionFunctions.getUserUid(), Toast.LENGTH_SHORT).show();
//                startActivity(in);
//            }
//        });

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
//                         fab.setVisibility(View.VISIBLE);
                }
            }
        });
        // 判斷目前card位置


        // 頂端向下滑更新
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    updataphoneDB();
                    makecard();
                mSwipeRefreshLayout.setRefreshing(false);
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

    public void  onResume(){
        super.onResume();
      //  Toast.makeText(getContext(), "onResume{mission}", Toast.LENGTH_SHORT).show();
        makecard();

        mList.scrollToPosition(0);//顯示位置
        mList.setLayoutManager(layoutManager);
//        mList.setOnTouchListener(this);//監聽動作
        mList.setAdapter(CardViewAdapter);
//        if(SessionFunctions.isTeacher()) {
//            fab.setVisibility(View.VISIBLE);
//        }else{
//            fab.setVisibility(View.GONE);
//        }
    }
    //設置字體大小
    private void setFontSize(){
        TextView textViewTemp;
        SessionFunctions SF= new SessionFunctions();

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
                updataGroupDB();//更新揪團
            }

            @Override
            public void onErrorResponse(String response) {
                    Toast.makeText(getContext(), "更新失敗(任務)", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updataGroupDB(){
        ClientFunctions.updateGroups(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                CardViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorResponse(String response) {
                    updataGroupDB();
            }
        });
    }


    //任務排序 (緊急任務先)
    public void bedangerfirst(){
        Mission[] dangerlist=new Mission[getUrgentCount()];
        int dagnerCount=0;
        Mission[] noddangerlist=new Mission[missions.size()-getUrgentCount()];
        int nodangerCount=0;
        for(int i = 0;i <missions.size();i++){
            if(mission[i].isUrgent){
                dangerlist[dagnerCount] = mission[i];
                dagnerCount+=1;
            }else{
                noddangerlist[nodangerCount] = mission[i];
                nodangerCount+=1;
            }
        }
        for(int i = 0;i <getUrgentCount();i++){
            mission[i]=dangerlist[i];
        }for(int i = getUrgentCount();i <missions.size();i++){
            mission[i]=noddangerlist[i-getUrgentCount()];
        }
    }
//任務排序 (緊急任務先)

    public void makecard(){
        missions  = QueryFunctions.getMissions();
        mission = new Mission[missions.size()];
        for(int i = 0;i <missions.size();i++){
                if((SessionFunctions.getbecontext())){
                    mission[i] = new Mission(missions.get(missions.size()-i-1).id, missions.get(missions.size()-i-1).title
                            , missions.get(missions.size()-i-1).content, missions.get(missions.size()-i-1).expAt
                            , missions.get(missions.size()-i-1).needNum, missions.get(missions.size()-i-1).currentNum
                            , missions.get(missions.size()-i-1).isUrgent, true, missions.get(missions.size()-i-1).getUserName());
                }else {//獎勵
                    mission[i] = new Mission(missions.get(missions.size()-i-1).id, missions.get(missions.size()-i-1).title
                            , missions.get(missions.size()-i-1).reward, missions.get(missions.size()-i-1).expAt
                            , missions.get(missions.size()-i-1).needNum, missions.get(missions.size()-i-1).currentNum
                            , missions.get(missions.size()-i-1).isUrgent, true, missions.get(missions.size()-i-1).getUserName());
                }
        }
        dosort();
//        bedangerfirst();    //任務排序 (緊急任務先)

            CardViewAdapter = new CardViewAdapter(mission,R.layout.fragment_fragment_mission1/*,res*/);
        if(!isfirst) {
            mList.setAdapter(CardViewAdapter);
        }else {
            isfirst=false;
        }
    }
    public int getUrgentCount(){
        int Count=0;
            for(int i = 0;i <missions.size();i++){
                if(missions.get(i).isUrgent){
                    Count+=1;
                }
            }
        return Count;
    }
    //排序
    private void dosort(){
        if(SessionFunctions.getSortWay()==1){
            Arrays.sort(mission);
        }else if(SessionFunctions.getSortWay()==2){
        }
    }
}
