package com.yoyoyee.zerodistance.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.app.GetDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewGroupActivity extends AppCompatActivity {

    /*------------------------------------------Tree命名規則---------------------------------------------
    原名為newGroupActivity，縮寫newGA，並以此為開頭編寫變數
    newGAPress為為連結spinnerNewGroupActPressing(緊急程度)





     -------------------------------------------------------------------------------------------------------------*/
    private Boolean oneTimesDate =true,oneTimesTime=true;
    private Spinner spinnerNGAPress, spinnerNGAPay;
    private String[] stringPress ;
    private String[] stringPay ;
    private ArrayAdapter<String> arraysNGAPress, arraysNGAPPay;
    private Context contextNGAP;
    private ContentResolver cv;
    private String outPutString;
    private String timeAMPM;
    private Button dataPath;
    private TextView justViewNGA, Display, textViewTime, textViewDate;
    private Toolbar toolbar;
    private Calendar calendar;
    private GetDate getDate;
    final int theme = 5; //TimePickerDialog的主題，有0~6;
    private boolean time12or24=true; //設定true為24小時制，false12小時制
    private boolean timeAMPMAuto=true;//設定為true時為自動偵測系統時間，fales時為手動設定12或是24小時制
    private SimpleDateFormat formatter, formatYear, formatMonth, formatDay, formatHour, formatMin, formatSec;

    private int yearNow, monthNow, dayNow, hourNow, minuteNow,pmamNow;
    private int year, month, day, hour, minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        //FindByID區--------------------------------------------------------------------------------
        //toolbar 定位區
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //textView 定位區
        textViewTime = (TextView) findViewById(R.id.textViewTimePick);
        textViewDate = (TextView) findViewById(R.id.textViewDatePick);
        Display = (TextView) findViewById(R.id.Display);
        justViewNGA = (TextView) findViewById(R.id.justView2);
        //buttom 定位區
        //spinner 定位區
        spinnerNGAPress = (Spinner) findViewById(R.id.spinnerNewGroupActPressing);
        spinnerNGAPay = (Spinner) findViewById(R.id.spinnerNewGroupActPay);
        //ActionBar 設定區，主要為為了Spinner使用---------------------------------------------------
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.actionbar_new_group);
        //Calendar定義區----------------------------------------------------------------------------
        calendar =Calendar.getInstance();
        //定義區
        stringPress =getResources().getStringArray(R.array.press_new_group);
        stringPay =getResources().getStringArray(R.array.pay_new_group);
        //取的時間設定12或24小時制



       spinnerNGAPress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Display.setText(stringPress[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerNGAPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Display.setText(stringPay[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // TimePickerDialog設置彈出視窗，
    public void onClickDate(View v) {
        if (oneTimesDate == true) {
            yearNow = calendar.get(Calendar.YEAR);
            monthNow =  calendar.get(Calendar.MONTH);
            dayNow =  calendar.get(Calendar.DAY_OF_MONTH);
            year =yearNow;
            month =monthNow;
            day =dayNow;
            Toast.makeText(this,"開始設定", Toast.LENGTH_SHORT).show();
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, theme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int lisYear, int lisMonth, int lisDay) {
                    year = lisYear;
                    month = lisMonth;
                    day = lisDay;
                    textViewDate.setText(year + "年" + month + "月" + day + "日");
            }
        }, year, month, day);
        datePickerDialog.show();

        Display.setText(String.valueOf(year));
        oneTimesDate =false;
    }

    public void onClickTime(View v) {
        if (oneTimesTime == true) {
            hourNow =calendar.get(Calendar.HOUR_OF_DAY);
            minuteNow =calendar.get(Calendar.MINUTE);
            hour =hourNow;
            minute =minuteNow;
            Toast.makeText(this,"開始設定", Toast.LENGTH_SHORT).show();
        }
        if (timeAMPMAuto==true) {//自動偵測系統設定的時間格式為12小時制或24小時制
            cv = this.getContentResolver();
            timeAMPM = android.provider.Settings.System.getString(cv, Settings.System.TIME_12_24);
            if (timeAMPM.equals("12")) {
                time12or24 = false;
            }
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, theme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int lisHour, int lisminute) {
                hour = lisHour;
                minute = lisminute;
                textViewTime.setText( hour + "點" + minute + "分");
            }
        }, hour, minute, time12or24);
        timePickerDialog.show();
        oneTimesTime =false;
    }
}
    //
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
/*預備軍
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


     /*SimpleDateFormat date_sim = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date_date = new Date(System.currentTimeMillis());
        outPutString = date_sim.format(date_date);
        useToCompare = Float.parseFloat(outPutString);*/

  /* 初始化spinner用的
         contextNGAP = this.getApplicationContext();
        arraysNGAPress = new ArrayAdapter<String>(NewGroupActivity.this, R.layout.support_simple_spinner_dropdown_item, stringPress);
        spinnerNGAPress.setAdapter(arraysNGAPress);
        arraysNGAPPay = new ArrayAdapter<String>(NewGroupActivity.this, R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.pay_new_group));
        spinnerNGAPay.setAdapter(arraysNGAPPay);
*/
