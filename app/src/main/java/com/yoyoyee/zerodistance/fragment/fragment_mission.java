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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.MissionActivity;
import com.yoyoyee.zerodistance.activity.NewMissionActivity;
import com.yoyoyee.zerodistance.helper.missionAdapter;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_mission extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_mission,container,false);
        String[] myDataset={"你說把愛漸漸放下會走更遠,或許命運的謙讓我遇見","你好阿","xx","你好阿","xx","你好阿"} , missionName ={"我難過","打屁屁SASAjjjijjiji","878787","打屁屁","878787","打屁屁"};
        Drawable[] missiondangerous={Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"),Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"), Drawable.createFromPath("@android:drawable/star_big_on"),Drawable.createFromPath("@android:drawable/star_big_on")} ;
        int missionnumber[]=new int[myDataset.length];
        for(int i=0;i<myDataset.length;i++){
            missionnumber[i]=i;
        }
        try {

            missionAdapter missionAdapter = new missionAdapter(myDataset , missionName , missiondangerous , missionnumber);
            RecyclerView mList = (RecyclerView) v.findViewById(R.id.listView);

            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mList.setLayoutManager(layoutManager);
            mList.setAdapter(missionAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //漂浮
        FloatingActionButton fab=  (FloatingActionButton) v.findViewById(R.id.fab);
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
    }

}
