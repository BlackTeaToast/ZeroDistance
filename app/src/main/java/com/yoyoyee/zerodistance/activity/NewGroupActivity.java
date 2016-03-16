package com.yoyoyee.zerodistance.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yoyoyee.zerodistance.R;

public class NewGroupActivity extends AppCompatActivity {

    /*------------------------------------------Tree命名規則---------------------------------------------
    原名為newGroupActivity，縮寫newGA，並以此為開頭編寫變數
    newGAPress為為連結spinnerNewGroupActPressing(緊急程度)





     -------------------------------------------------------------------------------------------------------------*/

    private Spinner spinnerNGAPress,spinnerNGAPay;
    private ArrayAdapter<String> arraysNGAPress,arraysNGAPPay;
    private Context contextNGAP;
    private String[] stringNGAPress ={"普通","非常緊急"};
    private String[] stringNGAPay={"飲料","其他"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        contextNGAP =this.getApplicationContext();
        spinnerNGAPress = (Spinner)findViewById(R.id.spinnerNewGroupActPressing);
        spinnerNGAPay = (Spinner)findViewById(R.id.spinnerNewGroupActPay);
        arraysNGAPress =new ArrayAdapter<String>(NewGroupActivity.this,R.layout.support_simple_spinner_dropdown_item,stringNGAPress);
        arraysNGAPPay =new ArrayAdapter<String>(NewGroupActivity.this,R.layout.support_simple_spinner_dropdown_item,stringNGAPay);
        spinnerNGAPress.setAdapter(arraysNGAPress);
        spinnerNGAPay.setAdapter(arraysNGAPPay);

    }
}
