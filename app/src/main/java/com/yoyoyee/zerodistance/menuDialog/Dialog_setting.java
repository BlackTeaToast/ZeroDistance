package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.LoginActivity;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.SessionManager;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class Dialog_setting extends Dialog {
    int languageuse =0;//0中文 1英文
    private ArrayAdapter<String> adapterPress;
    private RadioButton textsizeLRB, textsizeMRB, textsizeSRB;//大中小字體
    private RadioButton showcontent, showpay;//顯示內容或獎勵
    private RadioButton poTimeFirst, endTimeFirst;
    private RadioButton layoutset1, layoutset2, layoutset3;
    private RadioGroup textsizeRG , showstyleRG, SortWay, Cardlayoutset;
    private SQLiteHandler db;
    private SessionManager session;
    float ttsize;  //字體大小
    static float sizeS=8,sizeM=10, sizeL=20;
    TextView  userid;
    Context context;
    public Dialog_setting(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_setting);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // SQLite database handler
        db = new SQLiteHandler(getContext());
        // session manager
        session = new SessionManager(getContext());
        try {
            userid = (TextView) findViewById(R.id.userid);

        userid.setText(SessionFunctions.getUserUid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //登出
        Button Sign_outbut = (Button) findViewById(R.id.Sign_outbut);
        Sign_outbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
//                getOwnerActivity().finish(); //之後設定為儲存
            }
        });

        //登出
        //spinner
            Spinner languageSpinner = (Spinner) findViewById(R.id.languageSpinner);
            adapterPress = new ArrayAdapter<String>(getContext(), R.layout.spinner, context.getResources().getStringArray(R.array.languageSppingArrary));

            adapterPress.setDropDownViewResource(R.layout.spinner);
            languageSpinner.setAdapter(adapterPress);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  //spinner監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageuse=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner
        //radio button
        textsizeRG = (RadioGroup) findViewById(R.id.textsizeRG);
        textsizeRG.setOnCheckedChangeListener(textsizeListener);
        showstyleRG = (RadioGroup) findViewById(R.id.showstyleRG);
        showstyleRG.setOnCheckedChangeListener(showstyleListener);
        showcontent = (RadioButton) findViewById(R.id.showcontent);//顯示內容
        showpay = (RadioButton) findViewById(R.id.showpay);//顯示任務
        textsizeLRB = (RadioButton) findViewById(R.id.textsizeLRB);
        textsizeMRB = (RadioButton) findViewById(R.id.textsizeMRB);
        SortWay = (RadioGroup) findViewById(R.id.SortWay);
        SortWay.setOnCheckedChangeListener(SortWayListener);
        textsizeSRB = (RadioButton) findViewById(R.id.textsizeSRB);
        poTimeFirst = (RadioButton) findViewById(R.id.poTimeFirst);
        endTimeFirst = (RadioButton) findViewById(R.id.endTimeFirst);
        Cardlayoutset = (RadioGroup)findViewById(R.id.Cardlayoutset);
        Cardlayoutset.setOnCheckedChangeListener(Cardlayoutway);
        layoutset1 = (RadioButton)findViewById(R.id.layoutset1);
        layoutset2 = (RadioButton)findViewById(R.id.layoutset2);
        layoutset3 = (RadioButton)findViewById(R.id.layoutset3);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        checkbecontext();//選擇預設項目
        setheight();
        setFontSize();//設定字體大小
        //radio button

        return ;}

//textsize radio button 監聽
    RadioGroup.OnCheckedChangeListener textsizeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch(checkedId){
                case R.id.textsizeLRB:
                    ttsize=sizeL;
                    break;
                case R.id.textsizeMRB:
                    ttsize=sizeM;
                    break;
                case R.id.textsizeSRB:
                    ttsize=sizeS;
                    break;
            }
            SessionFunctions.setUserTextSize(ttsize);
        }

    };

    //showstyle radio button 監聽
    RadioGroup.OnCheckedChangeListener showstyleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){
                case R.id.showcontent:
                    SessionFunctions.setbecontext(true);
                    break;
                case R.id.showpay:
                    SessionFunctions.setbecontext(false);
                    break;
            }

        }

    };

    //showstyle radio button 監聽
    RadioGroup.OnCheckedChangeListener SortWayListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){
                case R.id.poTimeFirst:
                    SessionFunctions.setSortWay(1);
                    break;
                case R.id.endTimeFirst:
                    SessionFunctions.setSortWay(2);
                    break;
            }

        }

    };

    //showstyle radio button 監聽
    RadioGroup.OnCheckedChangeListener Cardlayoutway = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){
                case R.id.layoutset1:
                    SessionFunctions.setCardlayoutWay(1);
                    break;
                case R.id.layoutset2:
                    SessionFunctions.setCardlayoutWay(2);
                    break;
                case R.id.layoutset3:
                    SessionFunctions.setCardlayoutWay(3);
            }
        }
    };

    private void setheight(){
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.85); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
        getWindow().setAttributes(p);

    }
    private void logoutUser() {

        session.setLogin(false);

        db.deleteUsers();
        Intent in = new Intent(context, LoginActivity.class);
        context.startActivity(in);
    }
    //設定預設大小或顯示資料
    private void checkbecontext(){
        if(SessionFunctions.getbecontext()){
            showcontent.setChecked(true);
        }else{
            showpay.setChecked(true);
        }

        if(SessionFunctions.getUserTextSize()==sizeM){
            textsizeMRB.setChecked(true);
        }else if (SessionFunctions.getUserTextSize()==sizeS){
            textsizeSRB.setChecked(true);
        }else if (SessionFunctions.getUserTextSize()==sizeL){
            textsizeLRB.setChecked(true);
        }

        if(SessionFunctions.getSortWay()==1){
            poTimeFirst.setChecked(true);
        }else if(SessionFunctions.getSortWay()==2){
            endTimeFirst.setChecked(true);
        }


        if(SessionFunctions.getCardlayoutWay()==1){
            layoutset1.setChecked(true);
        }else if(SessionFunctions.getCardlayoutWay()==2){
            layoutset2.setChecked(true);
        }else if(SessionFunctions.getCardlayoutWay()==3){
            layoutset3.setChecked(true);}
    }
    private void setFontSize(){
        TextView textViewTemp;
        SessionFunctions SF= new SessionFunctions();
        textViewTemp = (TextView) findViewById(R.id.textsizeread);
        textViewTemp.setTextSize(SF.getUserTextSize()+10);
        textViewTemp = (TextView) findViewById(R.id.languageread);
        textViewTemp.setTextSize(SF.getUserTextSize()+10);
//        textViewTemp = (TextView) findViewById(R.id.userid);
//        textViewTemp.setTextSize(SF.getUserTextSize()-5);

        textViewTemp =(TextView) findViewById(R.id.showstyleread);
        textViewTemp.setTextSize(SF.getUserTextSize()+10);
        textViewTemp =(RadioButton) findViewById(R.id.showcontent);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (RadioButton) findViewById(R.id.showpay);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp =(RadioButton) findViewById(R.id.textsizeLRB);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (RadioButton) findViewById(R.id.textsizeMRB);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp =  (RadioButton) findViewById(R.id.textsizeSRB);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (Button) findViewById(R.id.Sign_outbut);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) findViewById(R.id.SortWayTV);
        textViewTemp.setTextSize(SF.getUserTextSize()+10);
        textViewTemp = (TextView) findViewById(R.id.poTimeFirst);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) findViewById(R.id.endTimeFirst);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp =  (TextView)findViewById(R.id.cardlayout);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView)findViewById(R.id.talktous);
        SpannableString content = new SpannableString(context.getResources().getString(R.string.talktous));//畫底線
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textViewTemp.setText(content);//畫底線
    }
}