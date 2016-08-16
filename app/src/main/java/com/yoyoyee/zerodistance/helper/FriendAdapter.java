package com.yoyoyee.zerodistance.helper;

import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.menuDialog.Dialog_myself;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class FriendAdapter  extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private int fragment;
    private int Count;
    private boolean boxisvisible = false , ischeck;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;
        public android.support.v7.widget.CardView friendlistCV;
        public ViewHolder(View v) {
            super(v);
            checkbox = (CheckBox)v.findViewById(R.id.checkBox);
            friendlistCV= (android.support.v7.widget.CardView)v.findViewById(R.id.friendlistCV);
        }
    }

    public FriendAdapter (boolean boxisvisible , boolean ischeck){
        this.boxisvisible = boxisvisible;
        this.ischeck = ischeck;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friendlist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(ischeck){
            holder.friendlistCV.setCardBackgroundColor(Color.parseColor("#D9FFFFFF"));
        }else {
            holder.friendlistCV.setCardBackgroundColor(Color.parseColor("#8FFFFFFF"));
        }

        if(boxisvisible){
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.friendlistCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkbox.isChecked()){
                        holder.checkbox.setChecked(false);
                    }else {
                        holder.checkbox.setChecked(true);
                    }
                }
            });
        }else {
            holder.checkbox.setVisibility(View.INVISIBLE);
            holder.friendlistCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setonclick(v, holder);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
//        Count = missions.length;//長度
        Count=5;
        return Count;
    }
    private void setonclick(final View v, ViewHolder hoder){
        final PopupMenu popupmenu = new PopupMenu(v.getContext(), hoder.friendlistCV);
        popupmenu.getMenuInflater().inflate(R.menu.menu_friend, popupmenu.getMenu());
        popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.pop_frienddetail):
                        Dialog_myself dialog = new Dialog_myself(v.getContext(), false);
                        dialog.show();
                        break;
//                    case (R.id.pop_frienddel):
//                        CustomToast.showToast(v.getContext(),"pop_frienddel", 500);
//                        break;
                }
                return false;
            }
        });
        popupmenu.show();

    }


}