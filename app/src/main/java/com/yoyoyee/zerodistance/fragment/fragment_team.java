package com.yoyoyee.zerodistance.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.GroupActivity;
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
public class fragment_team extends Fragment {
    RecyclerView mList;
    FloatingActionButton fab;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team, container, false);
        ArrayList<Group> Group = QueryFunctions.getGroups();
        int[] id = new int[Group.size()];//任務id
        String[] title = new String[Group.size()];//任務標題
        String[] detial = new String[Group.size()];//任務內容
        Date[] expAt = new Date[Group.size()];//任務結束時間
        int[] needNum = new int[Group.size()]; //需要人數
        int[] currentNum = new int[Group.size()];//已有人數
        boolean[] missiondangerous = new boolean[Group.size()];//任務是否緊急
        for(int i = 0;i <Group.size();i++){
            id[i]= Group.get(i).id;
            title[i]= Group.get(i).title;
            detial[i] = Group.get(i).content;
            expAt[i] = Group.get(i).expAt;
            needNum[i] = Group.get(i).needNum;
            currentNum[i] = Group.get(i).currentNum;
            missiondangerous[i] = false;
        }

        int missionnumber[]=new int[title.length];
        for(int i=0;i<title.length;i++){
            missionnumber[i]=i;
        }
        try {

            CardViewAdapter CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber, R.layout.fragment_fragment_team);
            mList = (RecyclerView) v.findViewById(R.id.listView);
            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(CardViewAdapter);
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
            Intent in = new Intent(getActivity(), GroupActivity.class);
            in.putExtra("id", SessionFunctions.getUserUid());
            startActivity(in);
        }
    });

        //漂浮
        return v;
    }
}
