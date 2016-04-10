package com.yoyoyee.zerodistance.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.yoyoyee.zerodistance.R;

/**
 * Created by PatrickC on 2016/4/10.
 */
public class AchievementActivity extends AppCompatActivity {
    Toolbar toolbar;
    boolean hardToWork;
    boolean enmergency;
    boolean thunder;

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        //findViewById
        toolbar = (Toolbar)findViewById(R.id.mission_tool_bar);


//設置toolbar標題

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("成就");

        //設置區域==========
        hardToWork = true;
        enmergency = true;
        thunder = false;

        //==================

        if(hardToWork){

        }


    }
}
