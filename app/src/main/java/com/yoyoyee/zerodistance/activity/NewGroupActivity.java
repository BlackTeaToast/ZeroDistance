package com.yoyoyee.zerodistance.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;

import java.util.Calendar;

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
    private Button dataPath;
    private TextView justViewNGA;

    private Calendar calendar;

    private int year;
    private int month;
    private int day;
    private DatePicker datePicker;
    private FrameLayout lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        contextNGAP = this.getApplicationContext();
        spinnerNGAPress = (Spinner) findViewById(R.id.spinnerNewGroupActPressing);
        spinnerNGAPay = (Spinner) findViewById(R.id.spinnerNewGroupActPay);
        arraysNGAPress = new ArrayAdapter<String>(NewGroupActivity.this, R.layout.support_simple_spinner_dropdown_item, stringNGAPress);
        arraysNGAPPay = new ArrayAdapter<String>(NewGroupActivity.this, R.layout.support_simple_spinner_dropdown_item, stringNGAPay);
        spinnerNGAPress.setAdapter(arraysNGAPress);
        spinnerNGAPay.setAdapter(arraysNGAPPay);
        justViewNGA=(TextView)findViewById(R.id.justView2);
        dataPath = (Button) findViewById(R.id.button);
        if(true){
            dataPath.setVisibility(View.VISIBLE);

        }
        else{
            dataPath.setVisibility(View.GONE);
        }



    }
        public void onClickDate(View v) {
            // 获取日历对象

            calendar = Calendar.getInstance();
// 获取当前对应的年、月、日的信息
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);



// 初始化DatePickerDialog
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setTitle(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
            }, year, month, day).show();
        }

}
