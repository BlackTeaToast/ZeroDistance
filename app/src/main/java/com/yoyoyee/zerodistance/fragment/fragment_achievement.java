package com.yoyoyee.zerodistance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.SessionFunctions;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_achievement extends Fragment {
    boolean hardToWork;
    boolean enmergency;
    boolean thunder;

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    TextView textView1;
    TextView textView2;
    TextView textView3;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_achievement, container, false);
        //findViewById
        imageView1 = (ImageView)v.findViewById(R.id.achievement1);
        imageView2 = (ImageView)v.findViewById(R.id.achievement2);
        imageView3 = (ImageView)v.findViewById(R.id.achievement3);
        textView1 = (TextView)v.findViewById(R.id.achievementText1);
        textView2 = (TextView)v.findViewById(R.id.achievementText2);
        textView3 = (TextView)v.findViewById(R.id.achievementText3);


        //設置區域==========
        hardToWork = false;
        enmergency = false;
        thunder = false;

        //==================
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "尚餘9次達成" ,Toast.LENGTH_SHORT).show();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "尚餘10次達成" ,Toast.LENGTH_SHORT).show();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "尚餘10次達成" ,Toast.LENGTH_SHORT).show();
            }
        });

        return v;

    }



    public void  onResume(){

        //  Toast.makeText(getContext(), "onResume{mission}", Toast.LENGTH_SHORT).show();
        super.onResume();
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
        textView1.setTextSize(size+10);

        textView2.setText(R.string.achievement2);
        textView2.setTextSize(size+10);

        textView3.setText(R.string.achievement3);
        textView3.setTextSize(size+10);

    }



}