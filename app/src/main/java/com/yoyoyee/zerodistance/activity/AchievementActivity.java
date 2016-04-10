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
        imageView1 = (ImageView)findViewById(R.id.achievement1);
        imageView2 = (ImageView)findViewById(R.id.achievement2);
        imageView3 = (ImageView)findViewById(R.id.achievement3);


//設置toolbar標題

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("成就");

        //設置區域==========
        hardToWork = true;
        enmergency = true;
        thunder = false;

        //==================

        //設置圖片
        setImage();

    }

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
