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
import com.yoyoyee.zerodistance.helper.datatype.Group;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
    Mission[] missions;

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
                date[i].setTime(missions[i].getExpAt());
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

//                title[i] = limitString(title[i], 0);//0為title , 1為detial
//                detial[i] = limitString(detial[i], 1);
            }
            setFontSize(v);//設定字體大小
        }

    }
    public CardViewAdapter(Mission[] missions, int fragment) {
        this.missions = missions;
        this.fragment = fragment;
    }
//    public CardViewAdapter(Group[] Group, int fragment) {
//        this.missions = Group;
//        this.fragment = fragment;
//    }
    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(fragment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        dosort();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.missionName.setText(missions[position].getTitle());
            holder.missionDetial.setText(missions[position].getContent());
            holder.datetime.setText(month[position]+"/"+day[position]);
            holder.expAt.setText(hour[position]+":"+mm[position]);
            holder.peoplenumber.setText(missions[position].getCurrentNum()+"/"+missions[position].getNeedNum()+"人");

        if(missions[position].isMission()&&fragment==R.layout.fragment_fragment_mission){//其他頁面沒有緊急任務
                if(missions[position].getUrgent()){
                    holder.missiondangerous.setVisibility(View.VISIBLE);
//                    holder.CardView.setCardBackgroundColor(Color.parseColor("#fdf1a5"));
                holder.CardView.setBackgroundResource(R.drawable.dangreous_background2);
                }
                else{
                    holder.missiondangerous.setVisibility(View.INVISIBLE);
//                    holder.CardView.setCardBackgroundColor(Color.parseColor("#fafafa"));
                holder.CardView.setBackgroundResource(R.drawable.undangreous_background1);
                }
        }
        if(missions[position].isMission()){
            holder.missionName.setTextColor(Color.parseColor("#FF428BCA"));
        }else {
            holder.missionName.setTextColor(Color.parseColor("#ff99cc00"));
        }

        holder.missionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //    Toast.makeText(v.getContext(), ""+missions[position].isMission(), Toast.LENGTH_SHORT).show();
                doIntent(v.getContext(), missions[position].isMission());
                in.putExtra("id", missions[position].getId());
//                in.putExtra("id", 2);
                v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        Count = missions.length;//長度
        return Count;
    }
    public int setItemCount(int Count){
        this.Count = Count;
        return Count;
    }
    public Intent doIntent(Context c,boolean isMission){
        in=null;
        switch (fragment){
            case R.layout.fragment_fragment_mission:{
                in = new Intent(c, MissionActivity.class);

            }
            break;
            case R.layout.fragment_fragment_team:{
                in = new Intent(c, GroupActivity.class);
            }break;
            case R.layout.fragment_fragment_havebeen:{
                if(isMission){
                    in = new Intent(c, MissionActivity.class);
                }else {
                    in = new Intent(c, GroupActivity.class);
                }

            }break;
            case R.layout.fragment_fragment_notbeen:{
                if(isMission){
                    Toast.makeText(c, isMission+"", Toast.LENGTH_LONG).show();
                    in = new Intent(c, MissionActivity.class);
                }else {
                    Toast.makeText(c, isMission+"", Toast.LENGTH_LONG).show();
                    in = new Intent(c, GroupActivity.class);
                }

            }break;
        }
        return in;
    }
    //排序
    private void dosort(){
//        if(fragment!=R.layout.fragment_fragment_mission){
//            Arrays.sort(missions);
//        }
    }
    //字數限制
    private String limitString(String context, int type){//0為title , 1為detial
        switch ((int)SessionFunctions.getUserTextSize()) {
            case 20:{
                if(type==0){
                    if(context.length()>30){//限制字數
                        context = (String)context.subSequence(0, 30)+"...";
                        //  Toast.makeText(getContext(), ""+context, Toast.LENGTH_SHORT).show();
                    }
                }
                if(type==1){
                    if(context.length()>30){//限制字數
                        context = (String)context.subSequence(0, 30)+"...";
                    }
                }
                break;}
            case 15:{
                if(type==0){
                    if(context.length()>30){//限制字數
                        context = (String)context.subSequence(0, 30)+"...";
                    }
                }
                if(type==1){
                    if(context.length()>30){//限制字數
                        context = (String)context.subSequence(0, 30)+"...";
                    }
                }

                break;}
            case 10:{
                if(type==0){
                    if(context.length()>15){//限制字數
                        context = (String)context.subSequence(0, 14)+"...";
                    }
                }
                if(type==1){
                    if(context.length()>48){//限制字數
                        context = (String)context.subSequence(0, 47)+"...";
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
        textViewTemp.setMaxLines(1);//顯示幾行
        textViewTemp = (TextView) v.findViewById(R.id.dateread);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById(R.id.timeread);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
//        textViewTemp = (TextView) v.findViewById(R.id.peopleNumberread);
//        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById(R.id.missionDetial);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        if(SessionFunctions.getUserTextSize()==10){
            textViewTemp.setMaxLines(4);
        }if(SessionFunctions.getUserTextSize()==15){
            textViewTemp.setMaxLines(3);
        }if(SessionFunctions.getUserTextSize()==20){
            textViewTemp.setMaxLines(2);
        }
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