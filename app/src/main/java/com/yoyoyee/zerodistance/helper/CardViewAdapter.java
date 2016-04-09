package com.yoyoyee.zerodistance.helper;

/**
 * Created by 楊霖村 on 2016/3/30.
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.GroupActivity;
import com.yoyoyee.zerodistance.activity.MissionActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private String[] mData ,missionName;
    private boolean[] missiondangerous;
    private int[] id, needNum, currentNum, missionnumber, month, day, hour, mm;
    private Date[] expAtD;
    private Calendar[] date ;
    private int fragment;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView missionDetial, missionName, missionnumber, datetime, expAt, peoplenumber;
        public Button missionbutton;
        public ImageView missiondangerous;
        public ViewHolder(View v) {
            super(v);

            missionName = (TextView) v.findViewById(R.id.missionName);
            missionDetial = (TextView) v.findViewById(R.id.missionDetial);
            datetime = (TextView)v.findViewById(R.id.datetime);
            expAt = (TextView)v.findViewById(R.id.expAt);
            missiondangerous = (ImageView) v.findViewById(R.id.missiondangerous);
            missionnumber = (TextView) v.findViewById(R.id.missionnumber);
            peoplenumber = (TextView)v.findViewById(R.id.peoplenumber);
                missionbutton = (Button)v.findViewById(R.id.missionbutton);
            //處利日期
            date = new Calendar[getItemCount()];
            month = new int[getItemCount()];
            day = new int[getItemCount()];
            hour = new int[getItemCount()];
            mm = new int[getItemCount()];
            for(int i = 0;i<getItemCount();i++){
                date[i] = Calendar.getInstance();
                date[i].setTime(expAtD[i]);
                month[i] = date[i].get(Calendar.MONTH) + 1;  //取出月，月份的編號是由0~11 故+1
                day[i] = date[i].get(Calendar.DAY_OF_MONTH);  //取出日
                hour[i] = date[i].get(Calendar.HOUR_OF_DAY);  //取出hour
                mm[i] = date[i].get(Calendar.MINUTE);  //取出分
                //處理日期
            }
            setFontSize(v);//設定字體大小
        }

    }

    public CardViewAdapter(int[] id ,String[] missionName , String[] detial, Date[] expAt, int[] needNum, int[] currentNum, boolean[] missiondangerous, int[] missionnumber, int fragment) {
       this.id = id;
        this.missionName = missionName;
        mData = detial;
        this.expAtD = expAt;
        this.needNum = needNum;
        this.currentNum = currentNum;
        this.missiondangerous = missiondangerous;
        this.missionnumber = missionnumber;
        this.fragment = fragment;
    }
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(fragment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.missionName.setText(missionName[position]);
            holder.missionDetial.setText(mData[position]);
            holder.datetime.setText(month[position]+"/"+day[position]);
            holder.expAt.setText(hour[position]+":"+mm[position]);
            holder.peoplenumber.setText(currentNum[position]+"/"+needNum[position]);
            holder.missionnumber.setText("" + missionnumber[position]);

        if(missiondangerous[position]){
            holder.missiondangerous.setVisibility(View.VISIBLE);}
            holder.missionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "id為"+id+"  序列為"+"第"+position+"個", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(v.getContext(), MissionActivity.class);
                in.putExtra("id", id[position]);
                v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }
    //設置字體大小
    private void setFontSize(View v){
        TextView textViewTemp;
        SessionFunctions SF= new SessionFunctions();
        textViewTemp = (TextView) v.findViewById(R.id.missionName);
        textViewTemp.setTextSize(SF.getUserTextSize()-5);
        textViewTemp = (TextView) v.findViewById(R.id.missionDetial);
        textViewTemp.setTextSize(SF.getUserTextSize()-5);
        textViewTemp = (TextView)v.findViewById(R.id.datetime);
        textViewTemp.setTextSize(SF.getUserTextSize()-5);
        textViewTemp = (TextView)v.findViewById(R.id.expAt);
        textViewTemp.setTextSize(SF.getUserTextSize()-5);
        textViewTemp = (TextView) v.findViewById(R.id.missionnumber);
        textViewTemp.setTextSize(SF.getUserTextSize()-5);
        textViewTemp = (TextView)v.findViewById(R.id.peoplenumber);
        textViewTemp.setTextSize(SF.getUserTextSize()-5);
        textViewTemp = (Button)v.findViewById(R.id.missionbutton);
        textViewTemp.setTextSize(SF.getUserTextSize()-5);
    }
}