package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.FriendAdapter;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class Dialog_friend extends Dialog {
    RecyclerView mList;
    LinearLayoutManager layoutManager;//CARD layout
    Context context;
    FriendAdapter friendAdapter;
    Button newfir,otherpeo, cancel;
    public Dialog_friend(Context context) {
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
        newfir = (Button)findViewById(R.id.newfir);
        otherpeo = (Button)findViewById(R.id.otherpeo);
        cancel = (Button)findViewById(R.id.cancel);
        newfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFriend dialog = new SearchFriend(context, 1);
                dialog.setContentView(R.layout.dialog_searchfriend);
                dialog.show();
            }
        });
        otherpeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFriend dialog = new SearchFriend(context, 2);
                dialog.setContentView(R.layout.dialog_searchfriend);
                dialog.show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cancel();
            }
        });
    }
    public void makecard(){

//        friendAdapter = new FriendAdapter(mission,R.layout.fragment_fragment_mission/*,res*/);

            mList.setAdapter(friendAdapter);
    }
}

