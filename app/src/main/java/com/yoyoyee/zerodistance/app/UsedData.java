package com.yoyoyee.zerodistance.app;

import java.util.Calendar;

/**
  Created by PatrickC on 2016/3/29.
 */
public class UsedData {
    public String 	id;	         //揪團/任務編號
    public String 	schoolID;   //	學校編號
    public int	userID;         //      用來儲存發布者的編碼，每個人的編碼都是獨一無二的流水號，0開始
    public boolean	isMission;  //	辨別當前資料為任務 = true; 還是揪團=false
    public boolean	isTeacher;  //	用來判斷是否為老師，true為是，false為否
    public int	emergency;	    //      用來決定緊急程度，為應應未來增加趨勢，以數字方式決定等級，0為無緊急，往上則越高(預定1為緊急)
    public String price;        //	自定義獎勵的文字
    public String userName;    //	使用者名稱(可能為老師姓名或是暱稱)
    public String title;         //	任務/揪團的標題
    public String content;      //	任務/揪團的內容
    public Calendar endTime;   //      任務/揪團的截止時間
    public Calendar startTime;  //      任務/揪團的開始時間
    public int needNumber;    //	任務/揪團的需求人數
    public int acceptNumber;   //	任務/揪團的參與人數
    public int	isFinish;       //	任務揪團的完成度 預設0(未完成) 1執行中 2已完成
    public int	satisfaction;    //	任務揪團的滿意度 預設0(代表未完成) 1~3星(已完成)
    public String 	imagePath; //	圖片的url							QAndA 鏈結串列說明圖
    public String	voicePath;  //	語音的url
    public String	videoPath;  //	影片的url
    public QAndA qAndA;       //       存放Q&A
    public WhoJoin whoJoin;    //       存放有哪些參與者(鏈結串列)
    public int textsize;        //          文字大小
    public int showstyle;       //顯示內容或獎勵 0 內容 ,1 獎勵

    //初始化
    public UsedData(){
        userID = 0;
        isTeacher = false;
        emergency = 0;
        price = null;
        userName = null;
        title = null;
        content  = null;
        startTime = null;
        endTime = null;
        needNumber = 0;
        acceptNumber = 0;
        qAndA = null;
        isFinish = 0;
        satisfaction = 0;
        id = null;
        schoolID = null;
        isMission = true;
        imagePath = null;
        voicePath = null;
        videoPath = null;
        whoJoin  = new WhoJoin();
        textsize = 20;
        showstyle =0;
    }

    //第一個問與答(尚未有人問問題時，第一個問與答建立時使用)
    public void firstQAndA(String questionUser, String content){
      this.qAndA = new QAndA(questionUser, content);
    }

    

//可能改版會用到
//    //回傳當前位置以後(當前位置算在內)，第一個指定questionUser 的 QAndA;若找不到會回傳null; 請另外做判斷
//    public QAndA getQAndAByUser(String questionUser){
//        QAndA index;
//        QAndA newQAndA = null;
//        index = this.qAndA;
//        while(index.next!=null){
//            if(!index.questionUser.equals(questionUser)){
//                index = index.next;
//            }
//            else{
//                newQAndA = index;
//            }
//        }
//        return newQAndA;
//    }

}
