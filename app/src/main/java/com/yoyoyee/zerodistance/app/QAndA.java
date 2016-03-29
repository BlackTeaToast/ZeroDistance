package com.yoyoyee.zerodistance.app;

/**
 Created by PatrickC on 2016/3/29.
 */
public class QAndA {
    public String questionUser;//	存放問問題的人的暱稱.
    public QAndA	next;//	指到下一個詢問者的問題串
    public Questions	firstQuestion;//	存放當前詢問者的第一個問題
    public  QAndA(){
        this.firstQuestion = null;
        this.next = null;
        this.questionUser = null;
    }
    public  QAndA(String QuestionUser, String content){
        this.firstQuestion = new Questions(true, content);
        this.next = null;
        this.questionUser = QuestionUser;
    }

    public void nextQAndA(String QuestionUser, String content){
        QAndA newQAndA = new QAndA();
        newQAndA.firstQuestion =  new Questions(true, content);
        newQAndA.questionUser = questionUser;
        newQAndA.next = null;
        this.next = newQAndA;

    }


    //回傳最後一節 QAndA
    public QAndA findFinal(){
        QAndA index;
        index = this;
        while(index.next!=null){
            index = index.next;
        }
        return index;
    }
    //到最後一節QAndA 新增一個新的 QandA 並加上第一個Question
    public void newQAndA(String QuestionUser, String content){
        QAndA index;
        index = this.findFinal();
        index.nextQAndA(QuestionUser, content);
    }

}
