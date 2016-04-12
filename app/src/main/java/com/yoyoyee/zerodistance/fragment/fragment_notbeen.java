package com.yoyoyee.zerodistance.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.CardViewAdapter;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Group;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_notbeen extends Fragment {
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


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notbeen, container, false);

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
        CardViewAdapter = new CardViewAdapter(id, title , detial ,expAt, needNum, currentNum, missiondangerous , missionnumber,R.layout.fragment_fragment_team/*,res*/);
    }
}
