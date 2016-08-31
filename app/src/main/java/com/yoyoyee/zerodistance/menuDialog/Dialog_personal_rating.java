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
import android.widget.RatingBar;
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
    private float[] stars;//存放星星評價用
    private ArrayList <MissionAccept> acceptUser;
    OnRatingResult mRratingResult;

    //回傳activity用的接口
    public interface OnRatingResult{
        void finish(float[] manyStars);
    }

    public void setRatingResult(OnRatingResult ratingResult){
        mRratingResult = ratingResult;
    }

    public Dialog_personal_rating(Context context,int acceptNumber) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_rating);
        this.acceptNumber = acceptNumber;
        this.context = context;
        stars = new float[acceptNumber];

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acceptUser = QueryFunctions.getMissionAceeptUser();

      // 使用fragment_rate_personal.xml 當作每個 holder 來使用==========

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.personalRecyclerView);

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final PersonalRateAdapter personalRateAdapter = new PersonalRateAdapter(acceptNumber);
        recyclerView.setAdapter(personalRateAdapter);

        //================================================================================
        finishButton = (Button)findViewById(R.id.finishButtonForPersonalRating);

       finishButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Toast.makeText(context, "請按下完成鈕完成最後步驟" ,Toast.LENGTH_SHORT).show();
               stars = personalRateAdapter.getStars();
               //回傳接口
               if(mRratingResult != null){
                   mRratingResult.finish(stars);
               }

               Dialog_personal_rating.this.dismiss();
           }
       });

    }


}
