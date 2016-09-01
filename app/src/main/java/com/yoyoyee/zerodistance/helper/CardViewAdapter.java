package com.yoyoyee.zerodistance.helper;

/**
 * Created by 楊霖村 on 2016/3/30.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.GroupActivity;
import com.yoyoyee.zerodistance.activity.MissionActivity;
import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.text.SimpleDateFormat;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private int fragment;
    private int Count;
    Mission[] missions;
    private int Countplus;
    private SimpleDateFormat dateFormat;
    Intent in;
    private Context context;
    Activity mActivity;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView missionDetial, missionName , datetime, peoplenumber, PoName;
        public ImageView missiondangerous;
        public CardView CardView;
        public FrameLayout fr_fr_mi;
        private Space space;
        public ViewHolder(View v) {
            super(v);
            missionName = (TextView) v.findViewById(R.id.missionName);
            missionDetial = (TextView) v.findViewById(R.id.missionDetial);
            datetime = (TextView)v.findViewById(R.id.datetime);
            PoName = (TextView)v.findViewById(R.id.PoName);
            missiondangerous = (ImageView) v.findViewById(R.id.missiondangerous);
            peoplenumber = (TextView)v.findViewById(R.id.peoplenumber);
            fr_fr_mi = (FrameLayout) v.findViewById(R.id.fr_fr_mi);
            CardView = (CardView) v.findViewById(R.id.CardView);
            space = (Space)v.findViewById(R.id.space);

            dateFormat  = new SimpleDateFormat("MM/dd HH:mm");

            setFontSize(v);//設定字體大小
        }
    }

    public CardViewAdapter(Mission[] missions, int fragment, Context context, Activity mActivity) {
        this.missions = missions;
        this.fragment = fragment;
        this.context = context;
        this.mActivity = mActivity;
    }

    @Override
    public CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=null;
//            v = setcardlayout(v,parent);

            v = LayoutInflater.from(parent.getContext())
                    .inflate(fragment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.missionName.setText(missions[position].getTitle());
            holder.missionDetial.setText(missions[position].getContent());
            holder.datetime.setText(dateFormat.format(missions[position].getExpAt()));
            holder.peoplenumber.setText(missions[position].getCurrentNum() + "/" + missions[position].getNeedNum());
            holder.PoName.setText(missions[position].getUserName());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {  //版本過低會葛闢
            holder.CardView.setBackgroundResource(R.drawable.paper1);
            //  getResources().getString(R.string.peopleCount)+
            if (missions[position].isMission() && fragment == R.layout.fragment_fragment_mission1) {//其他頁面沒有緊急任務
                if (missions[position].getUrgent()) {
//                    holder.CardView.setBackgroundResource(R.drawable.dangreous_background2);
                    holder.CardView.setBackgroundResource(R.drawable.woodcard8);
                } else {
                    holder.CardView.setBackgroundResource(R.drawable.woodcard8);
                }
            }
        }
            if (missions[position].isMission()) {
                holder.missionName.setTextColor(Color.parseColor("#FF428BCA"));
            } else {
                holder.missionName.setTextColor(Color.parseColor("#ff99cc00"));
            }

            holder.fr_fr_mi.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.ECLAIR)
                @Override
                public void onClick(View v) {
                    doIntent(v.getContext(), missions[position].isMission());
                    in.putExtra("id", missions[position].getId());
                    v.getContext().startActivity(in);
                    mActivity.overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
                }
            });
//        if(position==missions.length){
//            holder.space.setVisibility(View.VISIBLE);
//        }else{
//            holder.space.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        Count = missions.length;//長度
        return Count;
    }

    public Intent doIntent(Context c,boolean isMission){
        in=null;
        switch (fragment){
            case R.layout.fragment_fragment_mission: {
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


    //設置字體大小
    private void setFontSize(View v){
        if(SessionFunctions.getCardlayoutWay()==1) {
            TextView textViewTemp;
            SessionFunctions SF = new SessionFunctions();
            textViewTemp = (TextView) v.findViewById(R.id.datetime);
            textViewTemp.setTextSize(SF.getUserTextSize() + 4);
            textViewTemp = (TextView) v.findViewById(R.id.peoplenumber);
            textViewTemp.setTextSize(SF.getUserTextSize() + 7);
            textViewTemp = (TextView) v.findViewById(R.id.missionName);
            textViewTemp.setTextSize(SF.getUserTextSize() + 7);
            textViewTemp.setMaxLines(1);//顯示幾行
//        textViewTemp = (TextView) v.findViewById(R.id.peopleNumberread);
//        textViewTemp.setTextSize(SF.getUserTextSize()+5);
            textViewTemp = (TextView) v.findViewById(R.id.missionDetial);
            textViewTemp.setTextSize(SF.getUserTextSize() + 4);
            if (SessionFunctions.getUserTextSize() == 10) {
                textViewTemp.setMaxLines(4);
            }
            if (SessionFunctions.getUserTextSize() == 15) {
                textViewTemp.setMaxLines(3);
            }
            if (SessionFunctions.getUserTextSize() == 20) {
                textViewTemp.setMaxLines(2);
            }
            textViewTemp = (TextView) v.findViewById(R.id.PoName);
            textViewTemp.setTextSize(SF.getUserTextSize() + 7);
        }
        if(SessionFunctions.getCardlayoutWay()==2) {
            TextView textViewTemp;
            SessionFunctions SF = new SessionFunctions();
            textViewTemp = (TextView) v.findViewById(R.id.datetime);
            textViewTemp.setTextSize(SF.getUserTextSize() + 4);
            textViewTemp = (TextView) v.findViewById(R.id.peoplenumber);
            textViewTemp.setTextSize(SF.getUserTextSize() + 4);
            textViewTemp = (TextView) v.findViewById(R.id.missionName);
            textViewTemp.setTextSize(SF.getUserTextSize() + 10);
            textViewTemp.setMaxLines(1);//顯示幾行
            textViewTemp = (TextView) v.findViewById(R.id.PoName);
            textViewTemp.setTextSize(SF.getUserTextSize() + 4);
            textViewTemp = (TextView) v.findViewById(R.id.peoplecount);
            textViewTemp.setTextSize(SF.getUserTextSize()+4);
            textViewTemp = (TextView) v.findViewById(R.id.missionDetial);
            textViewTemp.setTextSize(SF.getUserTextSize() + 7);
            if (SessionFunctions.getUserTextSize() == 10) {
                textViewTemp.setMaxLines(1);
            }
            if (SessionFunctions.getUserTextSize() == 15) {
                textViewTemp.setMaxLines(1);
            }
            if (SessionFunctions.getUserTextSize() == 20) {
                textViewTemp.setMaxLines(1);
            }

        }
        if(SessionFunctions.getCardlayoutWay()==3) {
            TextView textViewTemp;
            SessionFunctions SF = new SessionFunctions();
            textViewTemp = (TextView) v.findViewById(R.id.datetime);
            textViewTemp.setTextSize(SF.getUserTextSize() + 4);
            textViewTemp = (TextView) v.findViewById(R.id.peoplenumber);
            textViewTemp.setTextSize(SF.getUserTextSize() + 4);
            textViewTemp = (TextView) v.findViewById(R.id.missionName);
            textViewTemp.setTextSize(SF.getUserTextSize() + 10);
            textViewTemp.setMaxLines(1);//顯示幾行
            textViewTemp = (TextView) v.findViewById(R.id.PoName);
            textViewTemp.setTextSize(SF.getUserTextSize() + 4);
//        textViewTemp = (TextView) v.findViewById(R.id.peopleNumberread);
//        textViewTemp.setTextSize(SF.getUserTextSize()+5);
            textViewTemp = (TextView) v.findViewById(R.id.missionDetial);
            textViewTemp.setTextSize(SF.getUserTextSize() + 7);
            if (SessionFunctions.getUserTextSize() == 10) {
                textViewTemp.setMaxLines(1);
            }
            if (SessionFunctions.getUserTextSize() == 15) {
                textViewTemp.setMaxLines(1);
            }
            if (SessionFunctions.getUserTextSize() == 20) {
                textViewTemp.setMaxLines(1);
            }

        }
    }
    private View setcardlayout(View v,ViewGroup parent){
        switch (SessionFunctions.getCardlayoutWay()) {
            case 1: {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(fragment, parent, false);//原本
                break;
            }
            case 2: {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_fragment_mission1, parent, false);//原本
                break;
            }
            case 3: {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_fragment_mission2, parent, false);//原本
                break;
            }
            case 4:{
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.test_cardview, parent, false);//黑板專用
                break;
            }
        }
        return v;
    }
}