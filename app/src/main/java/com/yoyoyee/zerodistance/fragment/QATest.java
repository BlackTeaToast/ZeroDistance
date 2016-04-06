package com.yoyoyee.zerodistance.fragment;
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

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MissionActivity;
import com.yoyoyee.zerodistance.activity.NewMissionActivity;
import com.yoyoyee.zerodistance.helper.QAAdapter;
import com.yoyoyee.zerodistance.helper.missionAdapter;

/**
 * Created by Falan on 2016/4/6.
 */
public class QATest {
    public void onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String[] q_Q_Titletext = {"滾", "你有病嘛", "閃邊", "87", "P0", "好啦好啦"},a_A_Titletext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] q_Qtimetext = {"1/11 1:11", "12/11 11:11", "xx", "48/43", "154/45", "12/12"}, q_Qnametext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] q_Qcontenttext = {"你說把愛漸漸放下會走更遠,或許命運的謙讓我遇見", "你好阿", "xx", "你好阿", "xx", "你好阿"}, a_Atimetext = {"我難過", "打屁屁", "878787", "打屁屁", "878787", "打屁屁"};
        String[] a_Acontenttext = {"滾", "你有病嘛", "閃邊", "87", "P0", "好啦好啦"}, a_Anametext = {"978", "978", "978", "978", "978", "978"};

//        QAAdapter QAAdapter = new QAAdapter(q_Q_Titletext, q_Qtimetext, q_Qnametext, q_Qcontenttext, a_A_Titletext, a_Atimetext, a_Acontenttext, a_Anametext);
//        RecyclerView mList = (RecyclerView) findViewById(R.id.listView);
//
//        LinearLayoutManager layoutManager;
//        layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mList.setLayoutManager(layoutManager);
//        mList.setAdapter(QAAdapter);

    }
}
