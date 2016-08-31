package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.app.TextLenghLimiter;
import com.yoyoyee.zerodistance.client.ClientFunctions;
import com.yoyoyee.zerodistance.client.ClientResponse;
import com.yoyoyee.zerodistance.helper.QueryFunctions;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.datatype.Friend;
import com.yoyoyee.zerodistance.helper.datatype.Profession;
import com.yoyoyee.zerodistance.helper.datatype.User;

/**
 * Created by 楊霖村 on 2016/7/9.
 */
public class Dialog_myself extends Dialog {
    Button btn_confirm;
    Button btn_exit, makefri;
    TextView name, profession, level, Achievement, Aboutmyself , Fource, Agile, Intelligence, money, expTxt;
    LinearLayout editLL, otherLL;
    Context context;
    String Uid;
    User user;
    RoundCornerProgressBar expp;
    boolean ismyself;
    public Dialog_myself(Context context , boolean ismyself , String Uid , User user) {
       super(context);
        this.context = context;
        this.ismyself = ismyself;
        this.Uid = Uid;
        this.user = user;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_personal_page);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn_confirm = (Button)findViewById(R.id.edit);
        btn_exit = (Button)findViewById(R.id.exit);
        name = (TextView)findViewById(R.id.name);
        profession  = (TextView)findViewById(R.id.profession);
        level = (TextView)findViewById(R.id.level);
        Achievement = (TextView)findViewById(R.id.Achievement);
        Aboutmyself = (TextView)findViewById(R.id.Aboutmyself);
        money = (TextView)findViewById(R.id.money);
        Fource = (TextView)findViewById(R.id.Force);
        Agile = (TextView)findViewById(R.id.Agile);
        Intelligence = (TextView)findViewById(R.id.Intelligence);
        editLL = (LinearLayout)findViewById(R.id.editLL);
        otherLL = (LinearLayout)findViewById(R.id.otherLL);
        makefri = (Button)findViewById(R.id.makefri);
        expp = (RoundCornerProgressBar)findViewById(R.id.expp);
        expTxt = (TextView)findViewById(R.id.expTxt);
        ImageView iv = (ImageView)findViewById(R.id.mypicture);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ori();//自我介紹內容
        setheight();//畫面大小
        setbtn_confirm();//自我介紹按鈕
        setbtn_exit();//離開按鈕
        setismyself();//是不適自己
        setmakefri();
        setexp();
    }
    private void setexp(){
        expTxt.setText((int)(SessionFunctions.getUserExp()+expp.getProgress())+"/100");
        expp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expp.setProgress(expp.getProgress() +10);
                expTxt.setText((int)(SessionFunctions.getUserExp()+expp.getProgress())+"/100");
            }
        });
    }
    private void ori(){
        if(SessionFunctions.getUserUid().equals(Uid)) {
            name.setText(SessionFunctions.getUserName() + "");
            profession.setText(Profession.getProfessionName(SessionFunctions.getUserProfession()));
            level.setText(SessionFunctions.getUserLevel() + "");
            expTxt.setText((int)(SessionFunctions.getUserExp())+"/100");
            money.setText(SessionFunctions.getUserMoney() + "");
            Fource.setText(SessionFunctions.getUserStrength() + "");
            Agile.setText(SessionFunctions.getUserAgile() + "");
            Intelligence.setText(SessionFunctions.getUserIntelligence() + "");
            Achievement.setText("萬粽之王我");
            Aboutmyself.setText(SessionFunctions.getUserIntroduction() + "");
            Aboutmyself.setMovementMethod(ScrollingMovementMethod.getInstance());
        }else if(!Uid.equals("user")){
            Friend friend = QueryFunctions.getFriendsData(Uid);
            Toast.makeText(context, "Uid:"+Uid, Toast.LENGTH_SHORT).show();
            name.setText(friend.name + "");
            profession.setText(Profession.getProfessionName(friend.profession));
            level.setText(friend.level + "");
            expTxt.setText((int)(friend.exp)+"/100");
            money.setText(friend.money + "");
            Fource.setText(friend.strength + "");
            Agile.setText(friend.agile + "");
            Intelligence.setText(friend.intelligence + "");
            Achievement.setText("萬粽之王");
            Aboutmyself.setText(friend.introduction+ "");
            Aboutmyself.setMovementMethod(ScrollingMovementMethod.getInstance());
        }else if(Uid.equals("user")){
            Toast.makeText(context, "user:"+Uid, Toast.LENGTH_SHORT).show();
            name.setText(user.name + "");
            profession.setText(Profession.getProfessionName(user.profession));
            level.setText(user.level + "");
            expTxt.setText((int)(user.exp)+"/100");
            money.setText(user.money + "");
            Fource.setText(user.strength + "");
            Agile.setText(user.agile + "");
            Intelligence.setText(user.intelligence + "");
            Achievement.setText("萬粽之王");
            Aboutmyself.setText(user.introduction+ "");
            Aboutmyself.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
//        "你好，我是楊霖村，我對金融業有絕大的信心和熱忱，因為我擁有專業的金融知識，利用一年時間積極考去取十張證照，在財務個案分析方面也有研究，另外也具有豐富的實習經驗(拿出財富管理競賽作品、實習在職證明給面試官看)\n" +
//                "\n" +
//                "我的個性是樂於與人相處的，服務客人時遇到不同情況也應對進退得宜，凡事不斤斤計較，做事不拖泥帶水，在學校參與過許多活動，所以與人相處融洽是我能做到的!\n" +
//                "\n" +
//                "我所推崇的大人物為巴菲特，雖然他不是銀行家，但卻是我很推崇的專業投資人(備用人物:彭淮南、威爾許、張忠謀、李昌鈺、陳嫦芬)\n" +
//                "\n" +
//                "我會來爭取此次○○機會的原因為，我把自己當作商品一樣來經營，必須要隨時(不斷地)更新、隨時進步，把每一次挑戰都當作是對自己的磨練，這就是我的工作哲學\n" +
//                "\n" +
//                "有句話說:「當別人逃避，而你留下來挑戰，就能得到更上一層樓的機會。」這句話我覺得很符合○○○○這工作的正面態度，希望進入貴公司可以秉持我的工作哲學以及座右銘面對挑戰，貢獻所學並勝任愉快! "
    }
    private void setheight(){
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.85); // 高度设置为屏幕的0.6
        getWindow().setAttributes(p);
    }
    private void setbtn_confirm(){
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View item = LayoutInflater.from(context).inflate(R.layout.dialog_search, null);
                final EditText editText = (EditText) item.findViewById(R.id.friname);
                editText.addTextChangedListener(new TextLenghLimiter(30));
                editText.setMaxLines(2);
                editText.setMaxWidth(editText.getWidth());
                editText.setText(Aboutmyself.getText().toString());
                new AlertDialog.Builder(context,R.style.DialogTheme)
                        .setView(item)
                        .setTitle("請輸入自我介紹")
                        .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClientFunctions.setUserIntroduction(editText.getText().toString(), new ClientResponse() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("1")){
                                            Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context,"修改成功,但未知錯誤",Toast.LENGTH_SHORT).show();
                                        }
                                        SessionFunctions.setUserIntroduction(editText.getText().toString());
                                        Aboutmyself.setText(SessionFunctions.getUserIntroduction());
                                    }

                                    @Override
                                    public void onErrorResponse(String response) {
                                        if(response.equals("-1")){
                                            Toast.makeText(context,"修改失敗",Toast.LENGTH_SHORT).show();
                                        }else if(response.equals("-400")){
                                            Toast.makeText(context,"內部錯誤",Toast.LENGTH_SHORT).show();

                                        }else if(response.equals("-401")){
                                            Toast.makeText(context,"格式錯誤",Toast.LENGTH_SHORT).show();
                                        }else if(response.equals("-402")){
                                            Toast.makeText(context,"驗證錯誤",Toast.LENGTH_SHORT).show();
                                    }else {
                                            Toast.makeText(context,"修改失敗未知錯誤",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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
    private void setbtn_exit(){
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    private void setismyself(){
        if(ismyself){
            editLL.setVisibility(View.VISIBLE);
            otherLL.setVisibility(View.GONE);
        }else {
            editLL.setVisibility(View.GONE);
            otherLL.setVisibility(View.VISIBLE);
        }
    }
    private void setmakefri(){
        makefri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Uid.equals("user")){
                    Uid=user.uid;
                }
                ClientFunctions.addFriend(Uid, new ClientResponse() {
                    @Override
                    public void onResponse(String response) {
                        int i = Integer.parseInt(response);
                        String speak="增加成功";
                        switch(i) {
                            case 1:
                                speak="增加好友成功，等待對方確認";
                                break;
                            case 2:
                                speak="增加好友成功，等待對方確認";
                                break;
                            default:
                                speak="增加成功但有未知錯誤";
                                break;
                        }
                        Toast.makeText(getContext(),speak+"",Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(getContext(),speak+"",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

