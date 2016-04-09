package com.yoyoyee.zerodistance.helper;

import com.yoyoyee.zerodistance.app.AppController;

/**
 * Created by p1235 on 2016/4/8.
 */
public class SessionFunctions {

    private static SessionManager session = AppController.getSession();

    public void setIsTeacher(boolean isTeacher) {
        session.setIsTeacher(isTeacher);;
    }

    public boolean isTeacher() {
        return session.isTeacher();
    }

    public void setUserEmail(String email) {

        session.setUserEmail(email);

    }

    public String getUserEmail() {
        return session.getUserEmail();
    }

    public void setUserPassword(String password) {

        session.setUserPassword(password);

    }

    public String getUserPassword() {
        return session.getUserPassword();
    }

    public void setUserUid(String uid) {
        session.setUserUid(uid);
    }

    public String getUserUid() {
        return  session.getUserUid();
    }

    public void setUserAccessKey(String accessKey) {
        session.setUserAccessKey(accessKey);
    }

    public String getUserAccessKey() {
        return getUserAccessKey();
    }

    public void setUserName(String name) {
        session.setUserName(name);
    }

    public String getUserName() {
        return session.getUserName();
    }

    public void setUserNickName(String nickName) {
        session.setUserNickName(nickName);
    }

    public String getUserNickName() {
        return session.getUserNickName();
    }

    public void setUserSchoolID(int schoolID) {
        session.setUserSchoolID(schoolID);
    }

    public int getUserSchoolID() {
        return session.getUserSchoolID();
    }

    public void setUserStudentID(String studentID) {
        session.setUserStudentID(studentID);
    }

    public String getStudentID() {
        return session.getStudentID();
    }
    public void setUserTextSize(float UserTextSize){
        session.setUserTextSize(UserTextSize);
    }
    public float getUserTextSize(){
        return session.getUserTextSize();
    }
    public void setbecontext(boolean setbecontext){
        session.setbecontext(setbecontext);
    }
    public boolean getbecontext(){
        return session.getbecontext();
    }
}
