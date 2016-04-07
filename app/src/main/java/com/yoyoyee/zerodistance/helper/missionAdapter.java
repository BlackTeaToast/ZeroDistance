package com.yoyoyee.zerodistance.helper;

/**
 * Created by 楊霖村 on 2016/3/30.
 */

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;

public class missionAdapter extends RecyclerView.Adapter<missionAdapter.ViewHolder> {
    private String[] mData ,missionName;
    private Drawable[] missiondangerous;
    private int[] missionnumber;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView missionDetial, missionName, missionnumber;
        public ImageView missiondangerous;
        public ViewHolder(View v) {
            super(v);
            missionDetial = (TextView) v.findViewById(R.id.missionDetial);
            missionName = (TextView) v.findViewById(R.id.missionName);
            missiondangerous = (ImageView) v.findViewById(R.id.missiondangerous);
            missionnumber = (TextView) v.findViewById(R.id.missionnumber);
        }
    }

    public missionAdapter(String[] data ,String[] missionName , Drawable[] missiondangerous, int[] missionnumber) {
        mData = data;
        this.missionName = missionName;
        this.missiondangerous = missiondangerous;
        this.missionnumber = missionnumber;
    }

    @Override
    public missionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fragment_mission, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.missionDetial.setText(mData[position]);
            holder.missionName.setText(missionName[position]);
            holder.missiondangerous.setImageDrawable(missiondangerous[position]);
            holder.missionnumber.setText(""+missionnumber[position]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }


}
