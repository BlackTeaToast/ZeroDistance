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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.FriendAdapter;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class Dialog_friend extends Dialog {
    RecyclerView isfriList, nofriList;
    LinearLayoutManager islayoutManager, notlayoutManager;//CARD layout
    Context context;
    FriendAdapter friendAdapter , nofriendAdapter;
    RelativeLayout haveFriLine, noFriLine;
    Button delfri,otherpeo, cancel;
    TextView havefriend_count, nofriend_count;
    ImageView haveFriView, noFriView;
    boolean isvisible = false , isdel = false;
    public Dialog_friend(Context context) {
        super(context);
        this.context=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_friend);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new BitmapDrawable());
        otherpeo = (Button)findViewById(R.id.otherpeo);
        delfri = (Button)findViewById(R.id.delfri);
        cancel = (Button)findViewById(R.id.cancel);
        havefriend_count = (TextView)findViewById(R.id.havefriend_count);
        nofriend_count = (TextView)findViewById(R.id.nofriend_count);
        haveFriLine = (RelativeLayout)findViewById(R.id.haveFriLine);
        noFriLine = (RelativeLayout)findViewById(R.id.noFriLine);
        haveFriView = (ImageView)findViewById(R.id.haveFriView);
        noFriView = (ImageView)findViewById(R.id.noFriView);
        setdelfri(); //設定刪除好友按鈕
        setotherpeo();//找其他人
        setcancel();//離開
        setheight();//葉面大筱
        makecard();
        setfricount();//條條的人數設定
        setonclick();
    }
    public void makecard(){
        isfriList = (RecyclerView) findViewById(R.id.isfriendList);
        nofriList = (RecyclerView) findViewById(R.id.nofriendList);
        islayoutManager = new LinearLayoutManager(context);
        notlayoutManager = new LinearLayoutManager(context);
        islayoutManager.setOrientation(VERTICAL);
        notlayoutManager.setOrientation(VERTICAL);
        isfriList.setLayoutManager(islayoutManager);
        nofriList.setLayoutManager(notlayoutManager);
        friendAdapter = new FriendAdapter(isvisible, true);
        isfriList.setAdapter(friendAdapter);
        nofriendAdapter = new FriendAdapter(isvisible, false);
        nofriList.setAdapter(nofriendAdapter);
    }
    private void setdelfri(){
        delfri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dodelfrithing();
            }
        });
    }
    private void dodelfrithing(){
        if(!isdel){
            otherpeo.setText(R.string.ask_for_ok);
        }else {
            otherpeo.setText(R.string.search);
        }
        isdel=!isdel;
        isvisible=!isvisible;
        friendAdapter = new FriendAdapter(isvisible, true);
        isfriList.setAdapter(friendAdapter);
        nofriendAdapter = new FriendAdapter(isvisible, false);
        nofriList.setAdapter(nofriendAdapter);
    }
    private void setotherpeo(){

            otherpeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isdel) {
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
                            .setPositiveButton("取消", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   // cancel();
                                }
                            })
                            .show();
                    }else {
                        Toast.makeText(context, "已送出", Toast.LENGTH_SHORT).show();
                        dodelfrithing();
                    }
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
    private void setfricount(){
        havefriend_count.setText("("+"5"+"/"+"10"+")");
        nofriend_count.setText("("+"5"+"/"+"10"+")");
    }
    private void setonclick(){
        haveFriLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isfriList.getVisibility()==View.VISIBLE){
                    isfriList.setVisibility(View.GONE);
                    haveFriView.setImageResource(R.drawable.ic_add_white_24dp);
                }else{
                    isfriList.setVisibility(View.VISIBLE);
                    haveFriView.setImageResource(R.drawable.ic_remove_white_24dp);
                }
            }
        });
        noFriLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nofriList.getVisibility() == View.VISIBLE) {
                    nofriList.setVisibility(View.GONE);
                    noFriView.setImageResource(R.drawable.ic_add_white_24dp);
                }else {
                    nofriList.setVisibility(View.VISIBLE);
                    noFriView.setImageResource(R.drawable.ic_remove_white_24dp);
                }
            }
        });
    }
}


