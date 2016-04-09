package com.yoyoyee.zerodistance.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MissionActivity;
import com.yoyoyee.zerodistance.activity.NewMissionActivity;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.RecyclerItemClickListener;
import com.yoyoyee.zerodistance.helper.datatype.Mission;
import com.yoyoyee.zerodistance.helper.missionAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_mission extends Fragment {
    RecyclerView mList;
    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_mission, container, false);
       // Drawable[] missiondangerous={Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"),Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"),Drawable.createFromPath("@android:drawable/star_big_on")} ;
        ArrayList<Mission> missions = QueryFunctions.getMissions();
        int[] id = new int[missions.size()];//任務id
        String[] title = new String[missions.size()];//任務標題
        String[] detial = new String[missions.size()];//任務內容
        Date[] expAt = new Date[missions.size()];//任務結束時間
        int[] needNum = new int[missions.size()]; //需要人數
        int[] currentNum = new int[missions.size()];//已有人數
        boolean[] missiondangerous = new boolean[missions.size()];//任務是否緊急
        for(int i = 0;i <missions.size();i++){
            id[i]= missions.get(i).id;
            title[i]= missions.get(i).title;
            detial[i] = missions.get(i).content;
            expAt[i] = missions.get(i).expAt;
            needNum[i] = missions.get(i).needNum;
            currentNum[i] = missions.get(i).currentNum;
            missiondangerous[i] = missions.get(i).isUrgent;
        }

        int missionnumber[]=new int[title.length];
        for(int i=0;i<title.length;i++){
            missionnumber[i]=i;
        }
        try {

            missionAdapter missionAdapter = new missionAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber);
            mList = (RecyclerView) v.findViewById(R.id.listView);
            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(missionAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //漂浮
         fab=  (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                        .setAction("Action", null).show();
                Intent in = new Intent(getActivity(), NewMissionActivity.class);

                startActivity(in);



            }
        });

        //漂浮
        return v;

        //listView 典籍

        //listView 典籍
    }

}
