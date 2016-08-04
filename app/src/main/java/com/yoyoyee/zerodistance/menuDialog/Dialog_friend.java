package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.FriendAdapter;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

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
        layoutManager.setOrientation(VERTICAL);
        mList.setLayoutManager(layoutManager);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        newfir = (Button)findViewById(R.id.newfir);
        otherpeo = (Button)findViewById(R.id.otherpeo);
        cancel = (Button)findViewById(R.id.cancel);
        setnewfir();
        setotherpeo();
        setcancel();
        setheight();
    }
    public void makecard(){

//        friendAdapter = new FriendAdapter(mission,R.layout.fragment_fragment_mission/*,res*/);

            mList.setAdapter(friendAdapter);
    }
    private void setnewfir(){
        newfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View item = LayoutInflater.from(context).inflate(R.layout.dialog_search, null);
                new AlertDialog.Builder(context)
                        .setView(item)
                        .setTitle("請輸入玩家名稱")
                        .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) item.findViewById(R.id.friname);
                                Toast.makeText(context, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("取消" , new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancel();
                            }
                        })
                        .show();
            }
        });
    }
    private void setotherpeo(){
        otherpeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View item = LayoutInflater.from(context).inflate(R.layout.dialog_search, null);
                new AlertDialog.Builder(context)
                        .setView(item)
                        .setTitle("請輸入要查詢玩家名稱")
                        .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) item.findViewById(R.id.friname);
                                Toast.makeText(context, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("取消" , new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancel();
                            }
                        })
                        .show();
            }
        });
    }
    private void setcancel(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    private void setheight(){
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.85); // 高度设置为屏幕的0.6
        getWindow().setAttributes(p);
    }
}

