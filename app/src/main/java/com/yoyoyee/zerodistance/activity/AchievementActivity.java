package com.yoyoyee.zerodistance.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.SessionFunctions;

/**
 * Created by PatrickC on 2016/4/10.
 */
public class AchievementActivity extends AppCompatActivity {
    boolean hardToWork;
    boolean enmergency;
    boolean thunder;

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    TextView textView1;
    TextView textView2;
    TextView textView3;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        //findViewById
        imageView1 = (ImageView)findViewById(R.id.achievement1);
        imageView2 = (ImageView)findViewById(R.id.achievement2);
        imageView3 = (ImageView)findViewById(R.id.achievement3);
        textView1 = (TextView)findViewById(R.id.achievementText1);
        textView2 = (TextView)findViewById(R.id.achievementText2);
        textView3 = (TextView)findViewById(R.id.achievementText3);


    }

    protected void onResume(){
        super.onResume();

        //設置區域==========
        hardToWork = false;
        enmergency = false;
        thunder = false;

        //==================
        //設置圖片
        setImage();
        setFont();

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

    private void setFont(){
        float size = SessionFunctions.getUserTextSize();
        textView1.setText(R.string.achievement1);
        textView1.setTextSize(size);
        textView2.setText(R.string.achievement2);
        textView2.setTextSize(size);
        textView3.setText(R.string.achievement3);
        textView3.setTextSize(size);
    }

}
