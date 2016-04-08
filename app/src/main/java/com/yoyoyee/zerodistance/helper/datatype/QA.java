package com.yoyoyee.zerodistance.helper.datatype;

import java.util.Date;

/**
 * Created by p1235 on 2016/4/9.
 */
public class QA {

    public int id;
    public String userUid;
    public String userName;
    public String question;
    public String answer;
    public boolean isAnswered;
    public Date createdAt;
    public Date answeredAt;

    public QA() {

    }

    public QA(int id, String userUid, String userName, String question, String answer,
              boolean isAnswered, Date createdAt, Date answeredAt) {

        this.id = id;
        this.userUid = userUid;
        this.userName = userName;
        this.question = question;
        this.answer = answer;
        this.isAnswered = isAnswered;
        this.createdAt = createdAt;
        this.answeredAt = answeredAt;

    }

}
