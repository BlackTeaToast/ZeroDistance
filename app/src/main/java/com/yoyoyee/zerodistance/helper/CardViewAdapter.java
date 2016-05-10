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
    private int[] id, needNum, currentNum, hourInt, mmInt;
    private Date time;
    private String[] hour, mm;
    private Calendar[] date ;
    private int fragment;
    private int Count;
    Mission[] missions;

    private SimpleDateFormat dateFormat;
    Intent in;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView missionDetial, missionName , datetime, peoplenumber, PoName;
        public ImageView missiondangerous;
        public CardView CardView;
        public RelativeLayout fr_fr_mi;
        public ViewHolder(View v) {
            super(v);
            missionName = (TextView) v.findViewById(R.id.missionName);
            missionDetial = (TextView) v.findViewById(R.id.missionDetial);
            datetime = (TextView)v.findViewById(R.id.datetime);
            PoName = (TextView)v.findViewById(R.id.PoName);
            missiondangerous = (ImageView) v.findViewById(R.id.missiondangerous);
            peoplenumber = (TextView)v.findViewById(R.id.peoplenumber);
            fr_fr_mi = (RelativeLayout) v.findViewById(R.id.fr_fr_mi);
            CardView = (CardView) v.findViewById(R.id.CardView);

            dateFormat  = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            setFontSize(v);//設定字體大小
        }
    }

    public CardViewAdapter(Mission[] missions, int fragment) {
        this.missions = missions;
        this.fragment = fragment;
    }

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
            holder.datetime.setText(dateFormat.format(missions[position].getExpAt()));
        holder.peoplenumber.setText(missions[position].getCurrentNum()+"/"+missions[position].getNeedNum());
        holder.PoName.setText(missions[position].getUserName());
      //  getResources().getString(R.string.peopleCount)+
        if(missions[position].isMission()&&fragment==R.layout.fragment_fragment_mission){//其他頁面沒有緊急任務
                if(missions[position].getUrgent()){
           //         holder.missiondangerous.setVisibility(View.VISIBLE);
//                    holder.CardView.setCardBackgroundColor(Color.parseColor("#fdf1a5"));
                holder.CardView.setBackgroundResource(R.drawable.dangreous_background2);
                }
                else{
//                    holder.missiondangerous.setVisibility(View.INVISIBLE);
//                    holder.CardView.setCardBackgroundColor(Color.parseColor("#fafafa"));
                holder.CardView.setBackgroundResource(R.drawable.undangreous_background1);
                }
        }
        if(missions[position].isMission()){
            holder.missionName.setTextColor(Color.parseColor("#FF428BCA"));
        }else {
            holder.missionName.setTextColor(Color.parseColor("#ff99cc00"));
        }

            holder.fr_fr_mi.setOnClickListener(new View.OnClickListener() {
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
            default:{
                if(isMission){
                    in = new Intent(c, MissionActivity.class);
                }else {
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
        textViewTemp = (TextView)v.findViewById(R.id.datetime);
        textViewTemp.setTextSize(SF.getUserTextSize());
        textViewTemp = (TextView)v.findViewById(R.id.peoplenumber);
        textViewTemp.setTextSize(SF.getUserTextSize()+3);
        textViewTemp = (TextView) v.findViewById(R.id.missionName);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp.setMaxLines(1);//顯示幾行
//        textViewTemp = (TextView) v.findViewById(R.id.peopleNumberread);
//        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById(R.id.missionDetial);
        textViewTemp.setTextSize(SF.getUserTextSize());
        if(SessionFunctions.getUserTextSize()==10){
            textViewTemp.setMaxLines(4);
        }if(SessionFunctions.getUserTextSize()==15){
            textViewTemp.setMaxLines(3);
        }if(SessionFunctions.getUserTextSize()==20){
            textViewTemp.setMaxLines(2);
        }
        textViewTemp = (TextView)v.findViewById(R.id.PoName);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
    }
}