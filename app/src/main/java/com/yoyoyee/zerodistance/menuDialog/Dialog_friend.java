package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.yoyoyee.zerodistance.app.NotificationID;
import com.yoyoyee.zerodistance.app.TextLenghLimiter;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.client.ClientResponseUser;
import com.yoyoyee.zerodistance.helper.FriendAdapter;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Friend;
import com.yoyoyee.zerodistance.helper.datatype.User;

import java.util.ArrayList;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class Dialog_friend extends Dialog {
    static RecyclerView isfriList, nofriList, noinvfriendList;
    LinearLayoutManager islayoutManager, notlayoutManager, noinvfriendManager;//CARD layout
    static Context context;
    int totalcount;
    FriendAdapter friendAdapter , nofriendAdapter, getNoInviteFriendsAdapter;
    RelativeLayout haveFriLine, noFriLine;
    Button delfri,otherpeo, cancel;
    TextView havefriend_count, nofriend_count;
    ImageView haveFriView, noFriView;
    SwipeRefreshLayout friswi;
    boolean iserror=false;
    int a=0;
    boolean isvisible = false , isdel = false , isplue=false;
    private ArrayList<Friend> isfriend, notfriend, getNoInviteFriends;
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
        friswi = (SwipeRefreshLayout)findViewById(R.id.friswi);
        upfriendDB();
        setAT();//亞堂的設定
        setdelfri(); //設定刪除好友按鈕
        setotherpeo();//找其他人
        setcancel();//離開
        setheight();//葉面大筱
        makecard();
        setfricount();//條條的人數設定
        setonclick();
        setfriscro();
    }
    private void setAT(){
        NotificationID list = new NotificationID();
        list.deleteFriendList();
    }
    private void setfriscro(){
        friswi.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upfriendDB();
                friswi.setRefreshing(false);
            }
        });
    }
    private void upfriendDB(){
        ClientFunctions.updateFriends(new ClientResponse() {
            @Override
            public void onResponse(String response) {

                makecard();
                setfricount();
            }

            @Override
            public void onErrorResponse(String response) {

            }
        });
    }
    public void makecard(){
        isfriList = (RecyclerView) findViewById(R.id.isfriendList);
        nofriList = (RecyclerView) findViewById(R.id.nofriendList);
        noinvfriendList = (RecyclerView)findViewById(R.id.noinvfriendList);
        islayoutManager = new LinearLayoutManager(context);
        notlayoutManager = new LinearLayoutManager(context);
        noinvfriendManager = new LinearLayoutManager(context);
        islayoutManager.setOrientation(VERTICAL);
        notlayoutManager.setOrientation(VERTICAL);
        noinvfriendManager.setOrientation(VERTICAL);
        isfriList.setLayoutManager(islayoutManager);
        nofriList.setLayoutManager(notlayoutManager);
        noinvfriendList.setLayoutManager(noinvfriendManager);

        isfriend = QueryFunctions.getIsAcceptedFriends();
        friendAdapter = new FriendAdapter(context, isvisible, isplue, isfriend);
        isfriList.setAdapter(friendAdapter);
        notfriend = QueryFunctions.getNoAcceptedFriends();
        getNoInviteFriends = QueryFunctions.getNoInviteFriends();
        nofriendAdapter = new FriendAdapter(context, isvisible, isplue, notfriend);
        getNoInviteFriendsAdapter = new FriendAdapter(context, isvisible, true, getNoInviteFriends);
        nofriList.setAdapter(nofriendAdapter);
        noinvfriendList.setAdapter(getNoInviteFriendsAdapter);
    }
    private void setdelfri(){
        delfri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isdel) {
                    a=0;
                    dodelfrithing();
                    friendAdapter.setdelCount();
                }else {
                    dodelfrithing();
                    for(int i = 0; i<friendAdapter.getdelCount().size();i++){
                       dodel(friendAdapter.getdelCount().get(i), i+1==friendAdapter.getdelCount().size());
                    }

                }
                isdel=!isdel;
            }
        });
    }
    private int dodel(String Uid, final boolean isend){
        final String[] S = {null};
        final int[] i = new int[1];
        ClientFunctions.deleteFriend(Uid, new ClientResponse() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    S[0] ="刪除好友成功";
                    i[0] =1;
                }
                System.out.println(isend+"  asadasdas");
                if(isend){
                    upfriendDB();
                    if(iserror){
                        Toast.makeText(context,"刪除失敗",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"刪除成功",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onErrorResponse(String response) {
                if(response.equals("-1")){
                    S[0]= "刪除好友錯誤";
                    i[0] =-1;
                } if(response.equals("-2")){
                    S[0]= "刪除失敗，沒有改動";
                    i[0] =-2;
                } if(response.equals("-3")){
                    S[0]= "伺服器未知錯誤";
                    i[0] =-3;
                } if(response.equals("-4")){
                    S[0]= "該好友不存在";
                    i[0] =-4;
                } if(response.equals("-5")){
                    S[0]= "Uid不能是自己";
                    i[0] =-5;
                } if(response.equals("-400")){
                    S[0]= "內部錯誤(ex未連上網路)";
                    i[0] =-400;
                } if(response.equals("-401")){
                    S[0]= "格式錯誤";
                    i[0] =-401;
                } if(response.equals("-402")){
                    S[0]= "驗證錯誤";
                    i[0] =-402;
                }
                iserror=true;
                if(isend){
                    upfriendDB();
                    if(iserror){
                        Toast.makeText(context,"刪除失敗",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"刪除成功",Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        return i[0];
    }
    private void dodelfrithing(){
        isvisible=!isvisible;
        friendAdapter = new FriendAdapter(context, isvisible, isplue, isfriend);
        isfriList.setAdapter(friendAdapter);
        nofriendAdapter = new FriendAdapter(context, isvisible, isplue, notfriend);
        nofriList.setAdapter(nofriendAdapter);
    }
    public static void updatainso4(final boolean isvisible){
        ClientFunctions.updateFriends(new ClientResponse() {
            @Override
            public void onResponse(String response) {
                isfriList.setAdapter(new FriendAdapter(context, isvisible, false, QueryFunctions.getIsAcceptedFriends()));
                nofriList.setAdapter( new FriendAdapter(context, isvisible, false, QueryFunctions.getNoAcceptedFriends()));
                noinvfriendList.setAdapter(new FriendAdapter(context, false, true, QueryFunctions.getNoInviteFriends()));
            }

            @Override
            public void onErrorResponse(String response) {
                isfriList.setAdapter(new FriendAdapter(context, isvisible, false, QueryFunctions.getIsAcceptedFriends()));
                nofriList.setAdapter( new FriendAdapter(context, isvisible, false, QueryFunctions.getNoAcceptedFriends()));
                noinvfriendList.setAdapter(new FriendAdapter(context, false, true, QueryFunctions.getNoInviteFriends()));

            }
        });
    }
    private void setotherpeo(){

            otherpeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isdel) {
                    final View item = LayoutInflater.from(context).inflate(R.layout.dialog_search, null);
                    final EditText editText = (EditText) item.findViewById(R.id.friname);
                         editText.addTextChangedListener(new TextLenghLimiter(15));
                         editText.setMinLines(1);
                         editText.setSingleLine(true);
                    new AlertDialog.Builder(context,R.style.DialogTheme)
                            .setView(item)
                            .setTitle("請輸入要查詢玩家名稱")
                            .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClientFunctions.searchFriend(editText.getText().toString(), new ClientResponseUser() {
                                        @Override
                                        public void onResponse(User[] users) {
                                            if(users.length>1||users.length<=0){
                                                Toast.makeText(context, "請輸入正確名稱", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Dialog_myself frimyself = new Dialog_myself(context, false, "user", users[0]);
                                                frimyself.show();
                                            }
                                        }

                                        @Override
                                        public void onErrorResponse(int errorCode) {
                                            Toast.makeText(context, "請輸入正確名稱", Toast.LENGTH_SHORT).show();
                                        }
                                    });
//                                    Toast.makeText(context, editText.getText().toString(), Toast.LENGTH_SHORT).show();
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
        totalcount=isfriend.size()+ notfriend.size();
        havefriend_count.setText("("+isfriend.size()+"/"+totalcount+")");
        nofriend_count.setText("("+notfriend.size()+"/"+totalcount+")");
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


