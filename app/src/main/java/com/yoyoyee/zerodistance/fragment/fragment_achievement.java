package com.yoyoyee.zerodistance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yoyoyee.zerodistance.R;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_achievement extends Fragment {
    Toolbar toolbar;
    boolean hardToWork;
    boolean enmergency;
    boolean thunder;

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_achievement, container, false);
        //findViewById
        toolbar = (Toolbar)v.findViewById(R.id.mission_tool_bar);
        imageView1 = (ImageView)v.findViewById(R.id.achievement1);
        imageView2 = (ImageView)v.findViewById(R.id.achievement2);
        imageView3 = (ImageView)v.findViewById(R.id.achievement3);


        //設置區域==========
        hardToWork = true;
        enmergency = true;
        thunder = false;

        //==================

        //設置圖片
        setImage();
        return v;}

    private void setImage(){
        if(hardToWork)
            imageView1.setImageResource(R.drawable.price_hard_to_work);
        else
            imageView1.setImageResource(R.drawable.price_hard_to_work_null);

        if(thunder)
            imageView2.setImageResource(R.drawable.price_thunder);
        else
            imageView2.setImageResource(R.drawable.price_thunder_null);

        if(enmergency)
            imageView3.setImageResource(R.drawable.price_emergency);
        else
            imageView3.setImageResource(R.drawable.price_emergency_null);
    }
}