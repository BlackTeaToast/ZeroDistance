package com.yoyoyee.zerodistance.helper;

import com.yoyoyee.zerodistance.app.AppController;

/**
 * Created by p1235 on 2016/4/8.
 */
public class SessionFunctions {

    private static SessionManager session = AppController.getSession();

    public static void setCardlayoutWay(int CardlayoutWay) {
        session.setCardlayoutWay(CardlayoutWay);
    }
    public static int getCardlayoutWay(){
        return session.getCardlayoutWay();
    }
    public static void setSortWay(int setSortWay) {
        session.setSortWay(setSortWay);
    }
    public static int getSortWay(){
        return session.getSortWay();
    }

    public static void setIsTeacher(boolean isTeacher) {
        session.setIsTeacher(isTeacher);
    }
    public static String getUserschoolName() {
        return session.getUserSchoolName();
    }
    public static void setUserschoolName(String schoolName) {
        session.setUserSchoolName(schoolName);
    }

    public static boolean isTeacher() {
        return session.isTeacher();
    }

    public static void setUserEmail(String email) {

        session.setUserEmail(email);

    }

    public static String getUserEmail() {
        return session.getUserEmail();
    }

    public static void setUserPassword(String password) {

        session.setUserPassword(password);

    }

    public static String getUserPassword() {
        return session.getUserPassword();
    }

    public static void setUserUid(String uid) {
        session.setUserUid(uid);
    }

    public static String getUserUid() {
        return  session.getUserUid();
    }

    public static void setUserAccessKey(String accessKey) {
        session.setUserAccessKey(accessKey);
    }

    public static String getUserAccessKey() {
        return getUserAccessKey();
    }

    public static void setUserName(String name) {
        session.setUserName(name);
    }

    public static String getUserName() {
        return session.getUserName();
    }

    public static void setUserNickName(String nickName) {
        session.setUserNickName(nickName);
    }

    public static String getUserNickName() {
        return session.getUserNickName();
    }

    public static void setUserSchoolID(int schoolID) {
        session.setUserSchoolID(schoolID);
    }

    public static int getUserSchoolID() {
        return session.getUserSchoolID();
    }

    public static void setUserStudentID(String studentID) {
        session.setUserStudentID(studentID);
    }

    public static String getStudentID() {
        return session.getStudentID();
    }
    public static void setUserTextSize(float UserTextSize){
        session.setUserTextSize(UserTextSize);
    }
    public static float getUserTextSize(){
        return session.getUserTextSize();
    }
    public static void setbecontext(boolean setbecontext){
        session.setbecontext(setbecontext);
    }
    public static boolean getbecontext(){
        return session.getbecontext();
    }

    public void setUserSchoolName(String schoolName) {
        session.setUserSchoolName(schoolName);
    }

    public String getUserSchoolName() {
        return session.getUserSchoolName();
    }
}
