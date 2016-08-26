package com.yoyoyee.zerodistance.helper.datatype;

/**
 * Created by red on 2016/8/26.
 */
public class User {

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

    public User() {

    }

    public User(String uid, boolean isTeacher, String name, String nickName, int schoolID,
                  String studentID, String email, int profession, int level, int exp, int money,
                  int strength, int intelligence, int agile,String introduction) {

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

    }

}
