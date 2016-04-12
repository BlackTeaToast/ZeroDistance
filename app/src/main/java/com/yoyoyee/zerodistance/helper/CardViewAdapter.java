package com.yoyoyee.zerodistance.helper;

/**
 * Created by 楊霖村 on 2016/3/30.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.GroupActivity;
import com.yoyoyee.zerodistance.activity.MissionActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private String[] detial ,title;
    private boolean[] missiondangerous;
    private int[] id, needNum, currentNum, missionnumber, month, day, hourInt, mmInt;
    private String[] hour, mm;
    private Date[] expAtD;
    private Calendar[] date ;
    private int fragment;
    private int Count;

    Intent in;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView missionDetial, missionName, missionnumber, datetime, expAt, peoplenumber;
        public Button missionbutton;
        public ImageView missiondangerous;
        public RelativeLayout card;
        public CardView CardView;
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
            card = (RelativeLayout) v.findViewById(R.id.fr_fr_mi);
            CardView = (CardView) v.findViewById(R.id.CardView);
            //處利日期
            date = new Calendar[getItemCount()];
            month = new int[getItemCount()];
            day = new int[getItemCount()];
            hourInt = new int[getItemCount()];
            hour = new String[getItemCount()];
            mmInt = new int[getItemCount()];
            mm = new String[getItemCount()];
            for(int i = 0;i<getItemCount();i++){
                date[i] = Calendar.getInstance();
                date[i].setTime(expAtD[i]);
                month[i] = date[i].get(Calendar.MONTH) + 1;  //取出月，月份的編號是由0~11 故+1
                day[i] = date[i].get(Calendar.DAY_OF_MONTH);  //取出日
                hourInt[i] = date[i].get(Calendar.HOUR_OF_DAY);  //取出hour
                if(hourInt[i]<10){
                    hour[i]="0"+hourInt[i];
                }else{
                    hour[i]=""+hourInt[i];
                }
                mmInt[i] = date[i].get(Calendar.MINUTE);  //取出分
                if(mmInt[i]<10){
                    mm[i]="0"+mmInt[i];
                }else {
                    mm[i]=""+mmInt[i];
                }
                //處理日期

                title[i] = limitString(title[i], 0);//0為title , 1為detial
                detial[i] = limitString(detial[i], 1);
            }
            setFontSize(v);//設定字體大小
        }

    }

    public CardViewAdapter(int[] id ,String[] title , String[] detial, Date[] expAt, int[] needNum, int[] currentNum, boolean[] missiondangerous, int[] missionnumber, int fragment) {
       this.id = id;
        this.title = title;
        this.detial = detial;
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

            holder.missionName.setText(title[position]);
            holder.missionDetial.setText(detial[position]);
            holder.datetime.setText(month[position]+"/"+day[position]);
            holder.expAt.setText(hour[position]+":"+mm[position]);
            holder.peoplenumber.setText(currentNum[position]+"/"+needNum[position]+"人");
            holder.missionnumber.setText("" + missionnumber[position]);

        if(missiondangerous[position]){
            holder.missiondangerous.setVisibility(View.VISIBLE);
            holder.CardView.setCardBackgroundColor(Color.parseColor("#fdf1a5"));
        }
        else{
            try {
                holder.missiondangerous.setVisibility(View.INVISIBLE);
                holder.CardView.setCardBackgroundColor(Color.parseColor("#fafafa"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.missionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(v.getContext(), "id為"+id[position]+"  序列為"+"第"+position+"個", Toast.LENGTH_SHORT).show();
                doIntent(v.getContext());
                in.putExtra("id", id[position]);
//                in.putExtra("id", 2);
                v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        Count = detial.length;//長度
        return Count;
    }
    public int setItemCount(int Count){
        this.Count = Count;
        return Count;
    }
    public Intent doIntent(Context c){
        in=null;
        switch (fragment){
            case R.layout.fragment_fragment_mission:{
                in = new Intent(c, MissionActivity.class);
            }
            break;
            case R.layout.fragment_fragment_team:{
                in = new Intent(c, GroupActivity.class);
            }
            case R.layout.fragment_fragment_havebeen:{
                in = new Intent(c, GroupActivity.class);
            }case R.layout.fragment_fragment_notbeen:{
                in = new Intent(c, GroupActivity.class);
            }
        }
        return in;
    }

    //字數限制
    private String limitString(String context, int type){//0為title , 1為detial
        switch ((int)SessionFunctions.getUserTextSize()) {
            case 20:{
                if(type==0){
                    if(context.length()>4){//限制字數
                        context = (String)context.subSequence(0, 4)+"...";
                        //  Toast.makeText(getContext(), ""+context, Toast.LENGTH_SHORT).show();
                    }
                }
                if(type==1){
                    if(context.length()>14){//限制字數
                        context = (String)context.subSequence(0, 14)+"...";
                    }
                }
                break;}
            case 15:{
                if(type==0){
                    if(context.length()>6){//限制字數
                        context = (String)context.subSequence(0, 6)+"...";
                    }
                }
                if(type==1){
                    if(context.length()>20){//限制字數
                        context = (String)context.subSequence(0, 20)+"...";
                    }
                }

                break;}
            case 10:{
                if(type==0){
                    if(context.length()>11){//限制字數
                        context = (String)context.subSequence(0, 11)+"...";
                    }
                }
                if(type==1){
                    if(context.length()>22){//限制字數
                        context = (String)context.subSequence(0, 22)+"...";
                    }
                }
                break;}
        }
        return context;
    }

    //設置字體大小
    private void setFontSize(View v){
        TextView textViewTemp;
        SessionFunctions SF= new SessionFunctions();
        textViewTemp = (TextView) v.findViewById(R.id.missionName);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById(R.id.dateread);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById(R.id.timeread);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
//        textViewTemp = (TextView) v.findViewById(R.id.peopleNumberread);
//        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById(R.id.missionDetial);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView)v.findViewById(R.id.datetime);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView)v.findViewById(R.id.expAt);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById(R.id.missionnumber);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView)v.findViewById(R.id.peoplenumber);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (Button)v.findViewById(R.id.missionbutton);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
    }
}