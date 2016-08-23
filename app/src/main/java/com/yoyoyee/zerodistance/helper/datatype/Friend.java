package com.yoyoyee.zerodistance.helper.datatype;

/**
 * Created by red on 2016/8/16.
 */
public class Friend {

    public String uid;
    public boolean isTeacher;
    public String name;
    public String nickName;
    public int schoolID;
    public String studentID;
    public String email;
    public int profession;
    public int level;
    public int exp;
    public int money;
    public int strength;
    public int intelligence;
    public int agile;
    public String introduction;
    public boolean isAccepted;
    public boolean isInvite;

    public Friend() {

    }

    public Friend(String uid, boolean isTeacher, String name, String nickName, int schoolID,
                  String studentID, String email, int profession, int level, int exp, int money,
                  int strength, int intelligence, int agile,String introduction,
                  boolean isAccepted, boolean isInvite) {

        this.uid = uid;
        this.isTeacher = isTeacher;
        this.name = name;
        this.nickName = nickName;
        this.schoolID = schoolID;
        this.studentID = studentID;
        this.email = email;
        this.profession = profession;
        this.level = level;
        this.exp = exp;
        this.money = money;
        this.strength = strength;
        this.intelligence = intelligence;
        this.agile = agile;
        this.introduction = introduction;
        this.isAccepted = isAccepted;
        this.isInvite = isInvite;

    }

}
