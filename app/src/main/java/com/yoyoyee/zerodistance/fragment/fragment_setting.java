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
import com.yoyoyee.zerodistance.helper.SessionFunctions;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_setting extends Fragment {
    int languageuse =0;//0中文 1英文
    private ArrayAdapter<String> adapterPress;
    private RadioButton textsizeLRB, textsizeMRB, textsizeSRB;//大中小字體
    private RadioButton showcontent, showpay;//顯示內容或獎勵
    private RadioGroup textsizeRG , showstyleRG;
    float ttsize;  //字體大小
    TextView textsize;
    SessionFunctions SF= new SessionFunctions();//手機內設定
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_setting, container, false);
        textsize= (TextView) v.findViewById(R.id.textsize);
        ttsize =textsize.getTextSize();
        textsize.setText(""+((ttsize/5)+10));

        //登出
        Button Sign_outbut = (Button) v.findViewById(R.id.Sign_outbut);
        Sign_outbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                        .setAction("Action", null).show();
                Intent in = new Intent(getActivity(), LoginActivity.class);
                startActivity(in);
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
        if(SF.getbecontext()){
            showcontent.setChecked(true);
        }else{
            showpay.setChecked(true);
        }
        if(SF.getUserTextSize()==25){
            textsizeMRB.setChecked(true);
        }else if (SF.getUserTextSize()==20){
            textsizeSRB.setChecked(true);
        }else if (SF.getUserTextSize()==30){
            textsizeLRB.setChecked(true);
        }


        //radio button

        return v;}
//textsize radio button 監聽
    RadioGroup.OnCheckedChangeListener textsizeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch(checkedId){
                case R.id.textsizeLRB:
                    ttsize=30;
                    break;
                case R.id.textsizeMRB:
                    ttsize=25;
                    break;
                case R.id.textsizeSRB:
                    ttsize=20;
                    break;
            }
            SF.setUserTextSize(ttsize);
            textsize.setText("" + ttsize);
            textsize.setTextSize(SF.getUserTextSize());
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


}