package com.yoyoyee.zerodistance.helper.datatype;

import java.util.Date;

/**
 * Created by p1235 on 2016/3/29.
 */
public class Group {

    public int id;             //任務id
    public String userUid;         //擁有者id
    public int schoolID;       //學校id
    public String title;          //任務名稱
    public int needNum;           //需求人數
    public int currentNum;        //目前人數
    public String place;
    public String content;        //任務內容
    public Date createdAt;         //創建時間
    public Date expAt;            //到期時間
    public boolean isRunning;     //是否執行中
    public boolean isFinished;     //是否完成
    public Date finishedAt;

    public Group() {

    }

    public Group(int id, String userUid, int schoolID, String title, int needNum, int currentNum,
                 String place, String content, Date createdAt, Date expAt, boolean isRunning,
                 boolean isFinished, Date finishedAt) {
        this.id = id;
        this.userUid = userUid;
        this.schoolID = schoolID;
        this.title = title;
        this.needNum = needNum;
        this.currentNum = currentNum;
        this.place = place;
        this.content = content;
        this.createdAt = createdAt;
        this.expAt = expAt;
        this.isRunning = isRunning;
        this.isFinished = isFinished;
        this.finishedAt = finishedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userUid;
    }

    public void setUserID(String userID) {
        this.userUid = userID;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNeedNum() {
        return needNum;
    }

    public void setNeedNum(int needNum) {
        this.needNum = needNum;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createdAt;
    }

    public void setCreateAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpAt() {
        return expAt;
    }

    public void setExpAt(Date expAt) {
        this.expAt = expAt;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isFinshed() {
        return isFinished;
    }

    public void setIsFinshed(boolean isFinshed) {
        this.isFinished = isFinshed;
    }




}
