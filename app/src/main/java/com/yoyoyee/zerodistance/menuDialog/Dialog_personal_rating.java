package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.ImageViewAdapter;
import com.yoyoyee.zerodistance.helper.PersonalRateAdapter;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.datatype.MissionAccept;

import java.util.ArrayList;

/**
 * Created by PatrickC on 2016/8/23.
 */

//本Dialog用於直接抓取 activity_personal_rating.xml 來顯示，並且XML中的recycleView 是用 fragment_rate_personal.xml 來當 Holder
public class Dialog_personal_rating extends Dialog{
    private int acceptNumber;//有多少人參加
    private Button finishButton;
    private Context context;
    private ArrayList <MissionAccept> acceptUser;
    private float[] star = new float[acceptNumber];

    public Dialog_personal_rating(Context context,int acceptNumber) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_rating);
        this.acceptNumber = acceptNumber;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acceptUser = QueryFunctions.getMissionAceeptUser();

      // 使用fragment_rate_personal.xml 當作每個 holder 來使用==========

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.personalRecyclerView);

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        PersonalRateAdapter personalRateAdapter = new PersonalRateAdapter(acceptNumber);
        recyclerView.setAdapter(personalRateAdapter);

        //================================================================================
        finishButton = (Button)findViewById(R.id.finishButtonForPersonalRating);

       finishButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(context, "完成評分，已可關閉此任務" ,Toast.LENGTH_SHORT).show();
               cancel();

           }
       });

    }


}
