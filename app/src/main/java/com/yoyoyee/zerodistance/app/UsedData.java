package com.yoyoyee.zerodistance.app;

import java.util.Calendar;

/**
  Created by PatrickC on 2016/3/29.
 */
public class UsedData {
    private int	userID;//用來儲存每個人的編碼，每個人的編碼都是獨一無二的流水號，0開始
    private boolean	isTeacher;//	用來判斷是否為老師，true為是，false為否
    private int	emergency;	//用來決定緊急程度，為應應未來增加趨勢，以數字方式決定等級，0為無緊急，往上則越高(預定1為緊急)
    private String price; //	自定義獎勵的文字
    private String userName;//	使用者名稱(可能為老師姓名或是暱稱)
    private String title;//	任務/揪團的標題
    private String content;//	任務/揪團的內容
    private Calendar endTime;//任務/揪團的截止時間
    private Calendar startTime;//任務/揪團的開始時間
    private int needNumber;//	任務/揪團的需求人數
    private int acceptNumber;//	任務/揪團的參與人數
    private QAndA qAndA; //存放Q&A
    private int	isFinish;//	任務揪團的完成度 預設0(未完成) 1執行中 2已完成
    private int	satisfaction;//	任務揪團的滿意度 預設0(代表未完成) 1~3星(已完成)

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
        qAndA = new QAndA();
        isFinish = 0;
        satisfaction = 0;
    }
    //setting
    public void	setUserID(int userID){this.userID = userID;}//用來儲存每個人的編碼，每個人的編碼都是獨一無二的流水號，0開始
    public void	setTeacher(boolean isTeacher){this.isTeacher = isTeacher;}//	用來判斷是否為老師，true為是，false為否
    public void	setEmergency(int emergency){this.emergency = emergency;}	//用來決定緊急程度，為應應未來增加趨勢，以數字方式決定等級，0為無緊急，往上則越高(預定1為緊急)
    public void setPrice(String price){this.price = price;} //	自定義獎勵的文字
    public void setUserName(String userName){this.userName = userName;}//	使用者名稱(可能為老師姓名或是暱稱)
    public void setTitle(String title){this.title = title;}//	任務/揪團的標題
    public void setContent(String content){this.content = content;}//	任務/揪團的內容
    public void setEndTime(Calendar endTime){this.endTime = endTime;}//任務/揪團的截止時間
    public void setStartTime(Calendar startTime){this.startTime = startTime;}//任務/揪團的開始時間
    public void setNeedNumber(int needNumber){this.needNumber = needNumber;}//	任務/揪團的需求人數
    public void setAcceptNumber(int acceptNumber){this.acceptNumber = acceptNumber;}//	任務/揪團的參與人數
    public void newQAndA(String QuestionUser, String content){this.qAndA.newQAndA(QuestionUser, content);} //設立一個新的QAndA
    public void setIsFinish(int isFinish){this.isFinish = isFinish;}//	任務揪團的完成度 預設0(未完成) 1執行中 2已完成
    public void	setSatisfaction(int satisfaction){this.satisfaction = satisfaction;}//	任務揪團的滿意度 預設0(代表未完成) 1~3星(已完成)

    //getting
    public int getUserID(){return this.userID;}//用來儲存每個人的編碼，每個人的編碼都是獨一無二的流水號，0開始
    public boolean	getTeacher(){return this.isTeacher;}//	用來判斷是否為老師，true為是，false為否
    public int	getEmergency(){return this.emergency;}	//用來決定緊急程度，為應應未來增加趨勢，以數字方式決定等級，0為無緊急，往上則越高(預定1為緊急)
    public String getPrice(){return this.price;} //	自定義獎勵的文字
    public String getUserName(){return this.userName;}//	使用者名稱(可能為老師姓名或是暱稱)
    public String getTitle(){return this.title;}//	任務/揪團的標題
    public String getContent(){return this.content;}//	任務/揪團的內容
    public Calendar getEndTime(){return this.endTime;}//任務/揪團的截止時間
    public Calendar getStartTime(){return this.startTime;}//任務/揪團的開始時間
    public int getNeedNumber(){return this.needNumber;}//	任務/揪團的需求人數
    public int getAcceptNumber(){return this.acceptNumber;}//	任務/揪團的參與人數
    public int getIsFinish(){return this.isFinish;}//	任務揪團的完成度 預設0(未完成) 1執行中 2已完成
    public int	getSatisfaction(){return this.satisfaction;}//	任務揪團的滿意度 預設0(代表未完成) 1~3星(已完成)
    public QAndA getQAndA(){return this.qAndA;} //回傳當前QAndA
    public QAndA getQAndAByUser(String questionUser){//回傳有指定questionUser 的 QAndA;若找不到會回傳null; 請另外做判斷
        QAndA index;
        QAndA newQAndA = null;
        index = this.qAndA;
        while(index.next!=null){
            if(!index.questionUser.equals(questionUser)){
                index = index.next;
            }
            else{
                newQAndA = index;
            }
        }
        return newQAndA;
    }

}
