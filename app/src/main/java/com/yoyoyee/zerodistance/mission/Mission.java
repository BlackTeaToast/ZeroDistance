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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getExpAt() {
        return expAt;
    }

    public void setExpAt(Date expAt) {
        this.expAt = expAt;
    }

    public String getQaId() {
        return qaId;
    }

    public void setQaId(String qaId) {
        this.qaId = qaId;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isFinshed() {
        return isFinshed;
    }

    public void setIsFinshed(boolean isFinshed) {
        this.isFinshed = isFinshed;
    }




}

