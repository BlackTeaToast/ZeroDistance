package com.yoyoyee.zerodistance.helper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.datatype.Friend;
import com.yoyoyee.zerodistance.helper.datatype.User;
import com.yoyoyee.zerodistance.menuDialog.Dialog_friend;
import com.yoyoyee.zerodistance.menuDialog.Dialog_myself;

import java.util.ArrayList;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class FriendAdapter  extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private int fragment;
    private int Count;
    private boolean boxisvisible = false , checkfribevisible=false;
    private ArrayList<Friend> friend;
    private static ArrayList<String> delcount;
    private Context context;
    static int i=0;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;
        public TextView friend_name , saveuid;
        public LinearLayout CheckfriLL;
        public ImageView check,nocheck;
        public android.support.v7.widget.CardView friendlistCV;
        public ViewHolder(View v) {
            super(v);
            checkbox = (CheckBox)v.findViewById(R.id.checkBox);
            friendlistCV= (android.support.v7.widget.CardView)v.findViewById(R.id.friendlistCV);
            friend_name = (TextView)v.findViewById(R.id.friend_name);
            saveuid = (TextView)v.findViewById(R.id.saveuid);
            CheckfriLL = (LinearLayout)v.findViewById(R.id.CheckfriLL);
            check = (ImageView)v.findViewById(R.id.check);
            nocheck = (ImageView)v.findViewById(R.id.nocheck);

        }
    }
    public FriendAdapter (Context context, boolean boxisvisible, boolean checkfribevisible, ArrayList<Friend> friend){
        this.context = context;
        this.boxisvisible = boxisvisible;
        this.checkfribevisible = checkfribevisible;
        this.friend = friend;
    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friendlist, parent, false);
        ViewHolder vh = new ViewHolder(v);

        delcount = new ArrayList<>();
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.friend_name.setText(friend.get(position).name);
        holder.saveuid.setText(friend.get(position).uid);
        if(friend.get(position).isAccepted){
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
                        delcount.remove(holder.saveuid.getText().toString());
                    }else {
                        holder.checkbox.setChecked(true);
                        delcount.add(holder.saveuid.getText().toString());
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
        if(checkfribevisible){
            holder.CheckfriLL.setVisibility(View.VISIBLE);
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Toast.makeText(context, "ssss，等待回覆", Toast.LENGTH_SHORT).show();
                    ClientFunctions.addFriend(friend.get(position).uid, new ClientResponse() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("1")){
                                Toast.makeText(context, "增加好友成功，已成為好友", Toast.LENGTH_SHORT).show();
                            }else if(response.equals("2")){
                                Toast.makeText(context, "增加好友成功，等待回覆", Toast.LENGTH_SHORT).show();
                            }
                            updatalist(false);
                        }

                        @Override
                        public void onErrorResponse(String response) {
                            String speak="增加失敗";
                            int i = Integer.parseInt(response);
                            switch(i) {
                                case -1:
                                    speak="增加好友錯誤";
                                    break;
                                case -2:
                                    speak="增加失敗，已加入好友";
                                    break;
                                case -3:
                                    speak="伺服器未知錯誤";
                                    break;
                                case -4:
                                    speak="該好友不存在";
                                    break;
                                case -5:
                                    speak="不能加自己好友";
                                    break;
                                case -400:
                                    speak="內部錯誤";
                                    break;
                                case -401:
                                    speak="格式錯誤";
                                    break;
                                case -402:
                                    speak="驗證錯誤";
                                    break;
                                default:
                                    speak="增加失敗and未知錯誤";
                                    break;
                            }
                            Toast.makeText(context, speak, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            holder.nocheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    ClientFunctions.deleteFriend(friend.get(position).uid, new ClientResponse() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("1")){
                                Toast.makeText(context, "刪除好友成功", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onErrorResponse(String response) {
                            String speak="增加失敗";
                            int i = Integer.parseInt(response);
                            switch(i) {
                                case -1:
                                    speak="刪除好友錯誤";
                                    break;
                                case -2:
                                    speak="刪除失敗，沒有改動";
                                    break;
                                case -3:
                                    speak="伺服器未知錯誤";
                                    break;
                                case -4:
                                    speak="該好友不存在";
                                    break;
                                case -5:
                                    speak="不能自己刪自己好友";
                                    break;
                                case -400:
                                    speak="內部錯誤";
                                    break;
                                case -401:
                                    speak="格式錯誤";
                                    break;
                                case -402:
                                    speak="驗證錯誤";
                                    break;
                                default:
                                    speak="增加失敗and未知錯誤";
                                    break;
                            }
                            Toast.makeText(context, speak, Toast.LENGTH_SHORT).show();
                        }
                    });
                    updatalist(false);
                }
            });
        }else{
            holder.CheckfriLL.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        Count=friend.size();
        return Count;
    }
    private void setonclick(final View v, final ViewHolder hoder){
        final PopupMenu popupmenu = new PopupMenu(v.getContext(), hoder.friendlistCV);
        popupmenu.getMenuInflater().inflate(R.menu.menu_friend, popupmenu.getMenu());
        popupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.pop_frienddetail):
                        Dialog_myself dialog = new Dialog_myself(v.getContext(), false ,hoder.saveuid.getText().toString(), new User());
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
    public void setdelCount(){
        if(delcount!=null)
        delcount.clear();
    }
    public ArrayList<String> getdelCount(){
//        Toast.makeText(context, delcount+"", Toast.LENGTH_SHORT).show();
        return delcount;
    }
    public void updatalist(boolean isvisible){
        Dialog_friend.updatainso4(isvisible);
    }
}