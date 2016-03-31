package com.yoyoyee.zerodistance.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.yoyoyee.zerodistance.R;


import java.util.Calendar;


public class NewGroupActivity extends AppCompatActivity {

    /*------------------------------------------Tree命名規則---------------------------------------------
    原名為newGroupActivity，縮寫newGA，並以此為開頭編寫變數
    newGAPress為為連結spinnerNewGroupActPressing(緊急程度)





     -------------------------------------------------------------------------------------------------------------*/
    private Boolean oneTimesDate =true,oneTimesTime=true;
    private Boolean time12or24=false; //設定true為24小時制，false12小時制
    private Boolean timeAMPMAuto=true;//設定為true時為自動偵測系統時間，fales時為手動設定12或是24小時制
    private Button buttonDate,buttonTime;

    private Calendar calendar;

    private EditText editTextcontent;

    private Spinner spinnerPress, spinnerPay;
    private String[] stringPress ,stringPay;
    private String getThatString;

    private TextView textViewName,textViewPress,textViewPay,Display,textViewMissionDate, textViewcontent;
    private TextView textViewTime,textViewDate;//Timepickerdialog使用
    private Toolbar toolbar;

    final int theme = 5; //TimePickerDialog的主題，有0~6;

    private int yearNow, monthNow, dayNow, hourNow, minuteNow,pmamNow;
    private int year, month, day, hour, minute;
    private int hourAMPM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        //FindByID區--------------------------------------------------------------------------------
        //toolbar 定位區
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //textView 定位區
        Display = (TextView) findViewById(R.id.printf);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewPress = (TextView) findViewById(R.id.textViewPress);
        textViewPay = (TextView) findViewById(R.id.textViewPay);
        textViewMissionDate = (TextView) findViewById(R.id.textViewMissionDate);
        textViewcontent = (TextView) findViewById(R.id.textViewContent);

        //buttom 定位區
        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonTime = (Button) findViewById(R.id.buttonTime);
        //spinner 定位區
        spinnerPress = (Spinner) findViewById(R.id.spinnerPress);
        spinnerPay = (Spinner) findViewById(R.id.spinnerPay);
        //ActionBar 設定區，主要為為了toolbar使用---------------------------------------------------
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.actionbar_new_mission);
        //定義區------------------------------------------------------------------------------------
        //時間

        calendar =Calendar.getInstance();

        stringPress =getResources().getStringArray(R.array.press_new_mission);
        stringPay =getResources().getStringArray(R.array.pay_new_mission);
        //取的時間設定12或24小時制
        //設定文字

        textViewName.setText(R.string.missionname_new_mission);
        textViewPress.setText(R.string.press_new_mission);
        textViewPay.setText(R.string.pay_new_mission);

        textViewcontent.setText(R.string.content_new_mission);
        textViewMissionDate.setText(R.string.date_new_mission);
        buttonTime.setText(R.string.buttomtime_new_mission);
        buttonDate.setText(R.string.buttomdate_new_mission);





       //spinner的監聽
       spinnerPress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Display.setText(stringPress[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    buttonDate.setText(year + getResources().getString(R.string.year_new_mission) + month + getResources().getString(R.string.month_new_mission) + day + getResources().getString(R.string.day_new_mission));
            }
        }, year, month, day);
        datePickerDialog.show();

       // Display.setText(String.valueOf(year));
        oneTimesDate =false;
    }

    public void onClickTime(View v) {
        if (oneTimesTime == true) {
            hourNow =calendar.get(Calendar.HOUR_OF_DAY);
            minuteNow =calendar.get(Calendar.MINUTE);
            hour =hourNow;
            minute =minuteNow;
           // Toast.makeText(this,"開始設定", Toast.LENGTH_SHORT).show();
        }
        
        if (timeAMPMAuto==true) {//自動偵測系統設定的時間格式為12小時制或24小時制
            ContentResolver cv = this.getContentResolver();
            String timeAMPM = Settings.System.getString(cv, Settings.System.TIME_12_24);
            if (timeAMPM.equals("12")) {
                time12or24 = false;
            }
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, theme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int lisHour, int lisminute) {
                hour = lisHour;
                minute = lisminute;
                buttonTime.setText(hour + getResources().getString(R.string.hour_new_mission) + minute + getResources().getString(R.string.minute_new_mission));
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
        spinnerPress.setAdapter(arraysNGAPress);
        arraysNGAPPay = new ArrayAdapter<String>(NewGroupActivity.this, R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.pay_new_group));
        spinnerPay.setAdapter(arraysNGAPPay);
*/
