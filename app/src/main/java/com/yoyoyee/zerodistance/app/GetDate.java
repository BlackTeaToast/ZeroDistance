package com.yoyoyee.zerodistance.app;

import android.os.SystemClock;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by futur on 2016/3/30.
 */
public class GetDate {
    private SimpleDateFormat year,day,month,hour,minute,second,date_sim,time;
    private String outPutString,outPutTimeString,getOutPutDateString,getOutPutAllString;
    private Date date_date;
    public GetDate(){

    }
    /*
    public  String getYearString(){ //輸出年
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =year.format(date_date);
        return outPutString;
    }
    public  String getMonthString(){ //輸出月
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =month.format(date_date);
        return outPutString;
    }
    public  String getDayString(){  //輸出日
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =day.format(date_date);
        return outPutString;
    }
    public  String getHourString(){  //輸出小時
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =hour.format(date_date);
        return outPutString;
    }
    public  String getMinuteString(){  //輸出分
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =minute.format(date_date);
        return outPutString;
    }
    public  String getSecondString(){  //輸出秒
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =second.format(date_date);
        return outPutString;
    }
    public String getDateString(String beforYear,String y_m,String m_d,String afterDay){  //輸入年前面要什麼，年和月之間要什麼，月和日之間要什麼，日之後要什麼
        date_sim =new SimpleDateFormat(beforYear+"yyyy"+y_m+"MM"+m_d+"dd"+afterDay);
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =second.format(date_date);
        return outPutString;
    }
    public String getDateString(){  //若空，則全部串起來
        date_sim =new SimpleDateFormat("yyyyMMdd");
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =second.format(date_date);
        return outPutString;
    }
    public String getTimeString(String beforHour,String h_m,String m_s,String afterSecond){  //輸入小時 前面要什麼，時和分之間要什麼，分和秒之間要什麼，秒之後要什麼
        date_sim =new SimpleDateFormat(beforHour+"HH"+h_m+"mm"+m_s+"ss"+afterSecond);
        date_date =new Date(System.currentTimeMillis()) ;
        outPutString =second.format(date_date);
        return outPutString;
    }
    public String getTimeString() {  //若空，則全部串起來
        date_sim = new SimpleDateFormat("HHmmss");
        date_date = new Date(System.currentTimeMillis());
        outPutString = second.format(date_date);
        return outPutString;
    }*/
    public String getAllString(){
        date_sim =new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        date_date = new Date(System.currentTimeMillis());
        outPutString = date_sim.format(date_date);
        return outPutString;
    }
}
