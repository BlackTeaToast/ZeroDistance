package com.yoyoyee.zerodistance.app;

/**
 Created by PatrickC on 2016/3/29.
 */
public class QAndA {
    public String questionUser;//	存放問問題的人的暱稱.
    public String content;//存放問題的問題
    public String answer;//存放問題的答案
    public QAndA	 next;//	指到下一個詢問者的問題串
    public QAndA previous;//前一個問題串，若為第一個，則當前Q&A為第一個，則為null

    //版本修正有可能會用到
    //public Questions firstQuestion;//	存放當前詢問者的第一個問題


    //初始化QAndA
    public  QAndA(){
        this.content = null;
        this.next = null;
        this.questionUser = null;
        this.answer = null;
        this.previous = null;
    }
    //第一個QAndA
    public  QAndA(String questionUser, String content){
        this.content = content;
        this.next = null;
        this.questionUser = questionUser;
        this.answer=null;
        this.previous = null;
    }

    //新增一筆QAndA
    public void nextQAndA(String questionUser, String content){
        QAndA newQAndA = new QAndA();
        newQAndA.content = content;
        newQAndA.questionUser = questionUser;
        newQAndA.next = null;

        //找到最尾端加入, 原本的最尾端相當於new前一個，原本的最尾端下一個變成new
        newQAndA.previous = this.findFinal();
        this.findFinal().next = newQAndA;

    }
    //回覆當前的QandA
    public void reply(String answer){
        this.answer = answer;
    }
    //刪除當前QandA的回覆
    public void deleteReply(){
        this.answer = null;
    }



    //回傳最後一節 QAndA,新增時會呼叫到
    public QAndA findFinal(){
        QAndA index;
        index = this;
        while(index.next!=null){
            index = index.next;
        }
        return index;
    }

    public void deleteQAndA(){
        QAndA index;
        index = this;
        //第一個Q&A，請直接砍掉，別用此方法刪除

        //Q&A在最尾 (前面有人，後面沒人)
        if(this.previous!=null && this.next==null){
            //前一個的next拿掉，然後當前改為null(不知道有沒有必要)
            index.previous.next = null;

        }

        //Q&A在中間 (前面有人，後面沒人已經判斷過了，因此else只剩下 後面有人)
        else if(this.previous!=null ){
            //上一個的next當前的下一個Q&A
            index.previous.next = index.next;
            //下一個的previous改為目前的前一個
            index.next.previous = index.previous;

        }


    }

}
