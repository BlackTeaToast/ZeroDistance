package com.yoyoyee.zerodistance.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoyoyee.zerodistance.helper.datatype.Mission;

import java.text.SimpleDateFormat;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class FriendAdapter  extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private int fragment;
    private int Count;

    private SimpleDateFormat dateFormat;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
            dateFormat  = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        }
    }

    public FriendAdapter(Mission[] missions, int fragment) {
//        this.missions = missions;
        this.fragment = fragment;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(fragment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
//        Count = missions.length;//長度
        Count=0;
        return Count;
    }
    public int setItemCount(int Count){
        this.Count = Count;
        return Count;
    }


}