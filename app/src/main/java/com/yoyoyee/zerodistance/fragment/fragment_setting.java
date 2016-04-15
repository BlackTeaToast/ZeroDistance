package com.yoyoyee.zerodistance.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.LoginActivity;
import com.yoyoyee.zerodistance.app.UsedData;
import com.yoyoyee.zerodistance.helper.SQLiteHandler;
import com.yoyoyee.zerodistance.helper.SessionFunctions;
import com.yoyoyee.zerodistance.helper.SessionManager;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_setting extends Fragment {
    int languageuse =0;//0中文 1英文
    private ArrayAdapter<String> adapterPress;
    private RadioButton textsizeLRB, textsizeMRB, textsizeSRB;//大中小字體
    private RadioButton showcontent, showpay;//顯示內容或獎勵
    private RadioGroup textsizeRG , showstyleRG;
    private SQLiteHandler db;
    private SessionManager session;
    float ttsize;  //字體大小
    static float sizeS=10,sizeM=15, sizeL=20;
    TextView textsize , userid;
    SessionFunctions SF= new SessionFunctions();//手機內設定
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_setting, container, false);
        // SQLite database handler
        db = new SQLiteHandler(v.getContext());
        // session manager
        session = new SessionManager(v.getContext());
        textsize= (TextView) v.findViewById(R.id.textsize);
        ttsize =textsize.getTextSize();
        textsize.setText(""+((ttsize/5)+10));
        userid = (TextView) v.findViewById(R.id.userid);
        userid.setText(SF.getUserUid());
        //登出
        Button Sign_outbut = (Button) v.findViewById(R.id.Sign_outbut);
        Sign_outbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
                getActivity().finish();
            }
        });

        //登出
        //spinner
            Spinner languageSpinner = (Spinner) v.findViewById(R.id.languageSpinner);
            adapterPress = new ArrayAdapter<String>(getActivity(), R.layout.spinner,getResources().getStringArray(R.array.languageSppingArrary));

            adapterPress.setDropDownViewResource(R.layout.spinner);
            languageSpinner.setAdapter(adapterPress);
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
        textsizeRG = (RadioGroup) v.findViewById(R.id.textsizeRG);
        textsizeRG.setOnCheckedChangeListener(textsizeListener);
        showstyleRG = (RadioGroup) v.findViewById(R.id.showstyleRG);
        showstyleRG.setOnCheckedChangeListener(showstyleListener);
        showcontent = (RadioButton) v.findViewById(R.id.showcontent);//顯示內容
        showpay = (RadioButton) v.findViewById(R.id.showpay);//顯示任務
        textsizeLRB = (RadioButton) v.findViewById(R.id.textsizeLRB);
        textsizeMRB = (RadioButton) v.findViewById(R.id.textsizeMRB);
        textsizeSRB = (RadioButton) v.findViewById(R.id.textsizeSRB);
        checkbecontext();//選擇預設項目

        setFontSize(v);//設定字體大小
        //radio button

        return v;}
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
            SF.setUserTextSize(ttsize);
//            textsize.setText("" + ttsize);
//            textsize.setTextSize(SF.getUserTextSize());
        }

    };

    //textsize radio button 監聽

    //showstyle radio button 監聽

    RadioGroup.OnCheckedChangeListener showstyleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){
                case R.id.showcontent:
                    SF.setbecontext(true);
                    break;
                case R.id.showpay:
                    SF.setbecontext(false);
                    break;
            }

        }

    };

    //showstyle radio button 監聽

    private void logoutUser() {

        session.setLogin(false);

        db.deleteUsers();
        Intent in = new Intent(getActivity(), LoginActivity.class);
        startActivity(in);
    }
    //設定預設大小或顯示資料
    private void checkbecontext(){
        if(SF.getbecontext()){
            showcontent.setChecked(true);
        }else{
            showpay.setChecked(true);
        }
        if(SF.getUserTextSize()==sizeM){
            textsizeMRB.setChecked(true);
        }else if (SF.getUserTextSize()==sizeS){
            textsizeSRB.setChecked(true);
        }else if (SF.getUserTextSize()==sizeL){
            textsizeLRB.setChecked(true);
        }
    }
    private void setFontSize(View v){
        TextView textViewTemp;
        SessionFunctions SF= new SessionFunctions();
        textViewTemp = (TextView) v.findViewById(R.id.languageread);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById (R.id.showstyleread);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (TextView) v.findViewById(R.id.textsize);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
//        textViewTemp = (TextView) v.findViewById(R.id.userid);
//        textViewTemp.setTextSize(SF.getUserTextSize()-5);
        textViewTemp =(RadioButton) v.findViewById(R.id.showcontent);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (RadioButton) v.findViewById(R.id.showpay);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp =(RadioButton) v.findViewById(R.id.textsizeLRB);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (RadioButton) v.findViewById(R.id.textsizeMRB);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp =  (RadioButton) v.findViewById(R.id.textsizeSRB);
        textViewTemp.setTextSize(SF.getUserTextSize()+5);
        textViewTemp = (Button) v.findViewById(R.id.Sign_outbut);
        textViewTemp.setTextSize(SF.getUserTextSize());
    }
}