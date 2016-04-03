package com.yoyoyee.zerodistance.app;

/**
  Created by PatrickC on 2016/3/29.
 */
public class Questions {
   public boolean	isQuestion;//	question = true; answer = false.
   public String	content;//	內容
   public   Questions	next;//	指到下一個question or answer
    public  Questions(){
        this.isQuestion = true;
        this.content =null;
        this.next = null;
    }
    public Questions(boolean isQuestion, String content){
        this.isQuestion = isQuestion;
        this.content =content;
        this.next = null;
    }

    public void nextQuestions(boolean isQuestion, String content){
       Questions newQuestion = new Questions();
       newQuestion.isQuestion = isQuestion;
       newQuestion.content = content;
       this.next = newQuestion;
     }

    //回傳此 QandA 的最後一節 Questions
    public Questions findFinal(){
        Questions index;
        index = this;
        while(index.next!=null){
            index = index.next;
        }
        return index;
    }

    //到最後一節Quetions 新增一個新的 Question
    public void newQuestions(boolean isQuestion, String content){
        Questions index;
        index = this.findFinal();
        index.nextQuestions(isQuestion , content);
    }
}
