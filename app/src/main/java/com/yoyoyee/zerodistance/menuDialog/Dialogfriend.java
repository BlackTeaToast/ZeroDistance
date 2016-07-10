package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.FriendAdapter;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class Dialogfriend  extends Dialog {
    RecyclerView mList;
    LinearLayoutManager layoutManager;//CARD layout
    Context context;
    FriendAdapter friendAdapter;
    TextView name, profession, level, exp, Achievement, Aboutmyself;
    public Dialogfriend(Context context) {
        super(context);
        this.context=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_friend_list);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = (RecyclerView) findViewById(R.id.friendList);
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
//        makecard();

    }
    public void makecard(){

//        friendAdapter = new FriendAdapter(mission,R.layout.fragment_fragment_mission/*,res*/);

            mList.setAdapter(friendAdapter);
    }
}

