package com.yoyoyee.zerodistance.mission;

import java.util.Date;

/**
 * Created by p1235 on 2016/3/29.
 */
public class Mission {

    public String id;             //任務id
    public String userID;         //擁有者id
    public String schoolID;       //學校id
    public String title;          //任務名稱
    public Boolean isUrgent;      //是否緊急
    public int needNum;           //需求人數
    public int currentNum;        //目前人數
    public String content;        //任務內容
    public String imagePath;      //圖片url
    public String voicePath;      //語音url
    public String videoPath;      //影片url
    public String reward;         //獎勵
    public Date createAt;         //創建時間
    public Date expAt;            //到期時間
    public String qaId;           //Q&A ID
    public boolean isRunning;     //是否執行中
    public boolean isFinshed;     //是否完成

}

