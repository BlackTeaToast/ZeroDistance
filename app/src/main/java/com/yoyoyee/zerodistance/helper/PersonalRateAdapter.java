package com.yoyoyee.zerodistance.helper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.datatype.MissionAccept;

import java.util.ArrayList;

/**
 * Created by PatrickC on 2016/8/23.
 */
public class PersonalRateAdapter extends RecyclerView.Adapter <PersonalRateAdapter.ViewHolder>{
    private int acceptNumber;
    private ArrayList<MissionAccept> acceptUser;

    public PersonalRateAdapter(int acceptNumber){
        this.acceptNumber = acceptNumber;
        acceptUser = QueryFunctions.getMissionAceeptUser();

    }

    //確定每個ViewHolder中 有哪些元素 並且findViewById
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userId;
        public RatingBar ratingBar;
        public float star;


        public ViewHolder(View itemView){
            super(itemView);

            userId = (TextView) itemView.findViewById(R.id.idForRecycleView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarForRecycleView);

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    star = (int) rating;
                }
            });
        }


    }


    //把每一個進來的資料依XML版面產生一個ViewHolder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_rate_personal, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //將產生的ViewHolder TextView 的ID 修改
    public void onBindViewHolder(final ViewHolder holder, int position) {
       String temp =  acceptUser.get(position).userName;
       holder.userId.setText(temp);

    }

    public int getItemCount(){
        return acceptNumber;
    }

}
