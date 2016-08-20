package com.yoyoyee.zerodistance.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.r0adkll.slidr.Slidr;
import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.helper.CustomToast;
import com.yoyoyee.zerodistance.helper.SessionFunctions;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class SettingActivity extends AppCompatActivity {
    int languageuse =0;//0中文 1英文
    private ArrayAdapter<String> adapterPress;
    private RadioButton textsizeLRB, textsizeMRB, textsizeSRB;//大中小字體
    private RadioButton showcontent, showpay;//顯示內容或獎勵
    private RadioButton poTimeFirst, endTimeFirst;
    private RadioButton layoutset1, layoutset2, layoutset3;
    private RadioGroup textsizeRG , showstyleRG, SortWay, Cardlayoutset;
    Spinner languageSpinner;
    Button Sign_outbut, saveandexit;
    float ttsize;  //字體大小
    static float sizeS=8,sizeM=10, sizeL=20;
    private boolean showstyle;
    private int sortway , cardlayoutway;
    TextView  userid;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);

        userid = (TextView) findViewById(R.id.userid);
        userid.setText(SessionFunctions.getUserUid());
        Sign_outbut = (Button) findViewById(R.id.Setting_cancel);
        saveandexit = (Button) findViewById(R.id.saveandexit);
        languageSpinner  = (Spinner) findViewById(R.id.languageSpinner);
        textsizeRG = (RadioGroup) findViewById(R.id.textsizeRG);
        showstyleRG = (RadioGroup) findViewById(R.id.showstyleRG);
        showcontent = (RadioButton) findViewById(R.id.showcontent);//顯示內容
        showpay = (RadioButton) findViewById(R.id.showpay);//顯示任務
        textsizeLRB = (RadioButton) findViewById(R.id.textsizeLRB);
        textsizeMRB = (RadioButton) findViewById(R.id.textsizeMRB);
        SortWay = (RadioGroup) findViewById(R.id.SortWay);
        textsizeSRB = (RadioButton) findViewById(R.id.textsizeSRB);
        poTimeFirst = (RadioButton) findViewById(R.id.poTimeFirst);
        endTimeFirst = (RadioButton) findViewById(R.id.endTimeFirst);
        Cardlayoutset = (RadioGroup)findViewById(R.id.Cardlayoutset);
        layoutset1 = (RadioButton)findViewById(R.id.layoutset1);
        layoutset2 = (RadioButton)findViewById(R.id.layoutset2);
        layoutset3 = (RadioButton)findViewById(R.id.layoutset3);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Slidr.attach(this);
        setRG();
        setSpinner();
        setbutton();
        checkbecontext();//選擇預設項目
       // setheight();//設定畫面大小
        setFontSize();//設定字體大小
        settoolbar();
        return ;}
    private void setRG(){
        textsizeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
            }
        });

        showstyleRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.showcontent:
                        showstyle = true;
                        break;
                    case R.id.showpay:
                        showstyle = false;
                        break;
                }
            }
        });

        SortWay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.poTimeFirst:
                        sortway = 1;
                        break;
                    case R.id.endTimeFirst:
                        sortway = 2;
                        break;
                }
            }
        });

        Cardlayoutset.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.layoutset1:
                        cardlayoutway = 1;
                        break;
                    case R.id.layoutset2:
                        cardlayoutway = 2;
                        break;
                    case R.id.layoutset3:
                        cardlayoutway = 3;
                }
            }
        });
    }
    private void setbutton(){
        final Intent in = new Intent(SettingActivity.this, MainActivity.class);
        Sign_outbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(in);
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
            }
        });
        saveandexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveinSF();
                CustomToast.showToast(v.getContext(), "設定資料已儲存", 500);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
            }
        });
    }
    private void setheight(){
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.85); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
        getWindow().setAttributes(p);
    }
    private void setSpinner(){
        adapterPress = new ArrayAdapter<String>(this, R.layout.spinner, this.getResources().getStringArray(R.array.languageSppingArrary));
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
        textViewTemp = (Button) findViewById(R.id.saveandexit);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (Button) findViewById(R.id.Setting_cancel);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) findViewById(R.id.SortWayTV);
        textViewTemp.setTextSize(SF.getUserTextSize()+10);
        textViewTemp = (TextView) findViewById(R.id.poTimeFirst);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) findViewById(R.id.endTimeFirst);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp =  (TextView)findViewById(R.id.cardlayout);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
    }
    private void saveinSF(){
        SessionFunctions.setUserTextSize(ttsize);
        SessionFunctions.setbecontext(showstyle);
        SessionFunctions.setSortWay(sortway);
        SessionFunctions.setCardlayoutWay(cardlayoutway);

    }
    private void settoolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.set);
    }
}